package Engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {

    private final AudioInputStream audioInputStream;
    private final SourceDataLine line;

    public MusicPlayer(String filename) {
        try {
        // Create an AudioInputStream from the file
        audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());

        // Play the audio
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                audioInputStream.getFormat());
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(audioInputStream.getFormat());
        line.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getVolume() {
        float volume = 0.f;
        try {
            int bytesRead;
            byte[] buffer = new byte[1024];
            if ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);

                // Calculate the volume as the average of the absolute values of the audio samples
                float sum = 0;
                for (int i = 0; i < bytesRead; i += 2) {
                    short sample = (short) ( (buffer[i] & 0xff) | (buffer[i + 1] << 8));
                    sum += Math.abs(sample);
                }
                if(sum!=0) volume = 20 * (float) Math.log10( sum / bytesRead );
            }
        } catch (IOException _){}
        return volume;
    }
}