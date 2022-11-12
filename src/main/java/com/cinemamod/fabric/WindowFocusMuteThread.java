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
                }

                previousState = MinecraftClient.getInstance().isWindowFocused();
            }

            for (Screen screen : CinemaModClient.getInstance().getScreenManager().getScreens()) {
                Vec3d playerPos = MinecraftClient.getInstance().player.getPos();
                BlockPos screenPos = screen.getPos();
                double distD = Math.sqrt(Math.pow(playerPos.x - screenPos.getX(), 2) + Math.pow(playerPos.y - screenPos.getY(), 2) + Math.pow(playerPos.z - (screenPos.getZ()), 2));
                float dist = (float)distD;
                MinecraftClient.getInstance().player.sendChatMessage(String.valueOf(1f-(dist/30f)));
                screen.setVideoVolume((1f - (dist/30f)));
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
