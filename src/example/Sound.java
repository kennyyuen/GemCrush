/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import java.io.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.*;

public class Sound {

    private String soundPath;

    Sound(String soundPath) {
        this.soundPath = soundPath;
    }

    public void playSound() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(soundPath);
            AudioStream audioStream = new AudioStream(inputStream);
            AudioPlayer.player.start(audioStream);
        } catch (Exception e) {
        }
    }


}
