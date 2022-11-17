package com.cinemamod.fabric;

import com.cinemamod.fabric.screen.Screen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WindowFocusMuteThread extends Thread {

    private boolean previousState;

    public WindowFocusMuteThread() {
        setDaemon(true);
        setName("window-focus-cef-mute-thread");
    }

    public void setDynamicVolumeForAllScreens(){
        for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
            Vec3d playerPos = MinecraftClient.getInstance().player.getPos();
            BlockPos screenPos = screen.getPos();
            float distD = (float) Math.sqrt(Math.pow(playerPos.x - screenPos.getX(), 2) + Math.pow(playerPos.z - screenPos.getZ(), 2));
            float scale = 8; //How steep should the volume curve be? Lower->steeper (Rasmus boring explanation is distance for half volume)
            float distanceVolume = scale/(scale+distD);
            /*
            float distanceVolume = 1f - (distD / 30f);
            distanceVolume = Math.max(0f, distanceVolume);
            */
            screen.setVideoVolume(CinemaModClient.getInstance().getVideoSettings().getVolume() * distanceVolume);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (CinemaModClient.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
                    if (MinecraftClient.getInstance().isWindowFocused()) {
                        setDynamicVolumeForAllScreens();
                    } else if (!MinecraftClient.getInstance().isWindowFocused() && previousState) {
                        // if not focused and was previous focused
                        for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
                            screen.setVideoVolume(0f);
                        }
                    }
                } else {
                    setDynamicVolumeForAllScreens();
                }
                previousState = MinecraftClient.getInstance().isWindowFocused();
            } catch (Exception ignored) { }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
