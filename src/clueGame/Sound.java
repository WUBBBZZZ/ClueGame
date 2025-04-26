package clueGame;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public final class Sound {

    private Sound() {}

    /** Load a Clip from /audio/XYZ.wav; fully decodes into memory. */
    public static Clip loadClip(String file) {
        try (InputStream in = Sound.class.getResourceAsStream("/audio/" + file);
             AudioInputStream ais = AudioSystem.getAudioInputStream(in)) {

            Clip clip = AudioSystem.getClip();
            clip.open(ais);            // load entire file into the clip
            return clip;

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException("Cannot load audio: " + file, e);
        }
    }

    /** Fire-and-forget playback of a short clip (non-looping). */
    public static void playSFX(Clip clip) {
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
