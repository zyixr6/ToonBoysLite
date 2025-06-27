package com.zachrollins.toonboyslite;

import com.badlogic.gdx.audio.Music;

// manages mute state for music playback
public class MuteManager {
    // current mute status
    private static boolean isMuted = false;

    // returns whether sound is muted
    public static boolean isMuted() {
        return isMuted;
    }

    // toggles mute and adjusts music volume
    public static void toggleMute(Music music) {
        isMuted = !isMuted;
        if (music != null) {
            music.setVolume(isMuted ? 0 : 1);
        }
    }
}
