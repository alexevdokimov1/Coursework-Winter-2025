package Engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {

    private AudioInputStream audioInputStream = null;
    private SourceDataLine line = null;

    public MusicPlayer(String filename) {
        try {
        // Specify the WAV file to play
        File file = new File(filename);

        // Create an AudioInputStream from the file
        audioInputStream = AudioSystem.getAudioInputStream(file);

        // Get the audio format
        AudioFormat audioFormat = audioInputStream.getFormat();

        // Play the audio
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(audioInputStream.getFormat());
        line.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getVolume() {
        try {
            int bytesRead;
            byte[] buffer = new byte[1024];
            float volume = 0.f;
            if ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);

                // Calculate the volume as the average of the absolute values of the audio samples
                float sum = 0;
                for (int i = 0; i < bytesRead; i += 2) {
                    short sample = (short) ((buffer[i] & 0xff) | (buffer[i + 1] << 8));
                    sum += Math.abs(sample);
                }
                if(sum!=0) volume = 20 * (float) Math.log10(sum / (bytesRead / 2));
            }
            return volume;
        } catch (IOException e){
            return 0;
        }
    }
}