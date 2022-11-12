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

    @Override
    public void run() {
        try {
            while (MinecraftClient.getInstance().isRunning()) {
                if (CinemaModClient.getInstance().getVideoSettings().isMuteWhenAltTabbed()) {
                    if (MinecraftClient.getInstance().isWindowFocused() && !previousState) {
                        // if currently focused and was previously not focused
                        for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
                            screen.setVideoVolume(CinemaModClient.getInstance().getVideoSettings().getVolume());
                        }
                    } else if (!MinecraftClient.getInstance().isWindowFocused() && previousState) {
                        // if not focused and was previous focused
                        for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
                            screen.setVideoVolume(0f);
                        }
                    } else {
                        for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {

                            float distance = 1f;
                            Vec3d playerPos = MinecraftClient.getInstance().player.getPos();
                            BlockPos screenPos = screen.getPos();
                            float distD = (float) Math.sqrt(Math.pow(playerPos.x - screenPos.getX(), 2) + Math.pow(playerPos.z - screenPos.getZ(), 2));
                            float distanceVolume = 1f - (distD / 30f);
                            distanceVolume = Math.max(0f, distanceVolume);
                            screen.setVideoVolume(CinemaModClient.getInstance().getVideoSettings().getVolume() * distanceVolume);
                        }
                    }

                    previousState = MinecraftClient.getInstance().isWindowFocused();
                }

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            MinecraftClient.getInstance().player.sendChatMessage(e.toString());
        }
    }

}
