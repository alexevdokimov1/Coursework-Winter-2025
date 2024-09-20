package Engine;

import javax.sound.sampled.*;
import java.io.File;

public class MusicPlayer {

    private final AudioInputStream audioInputStream;
    private final SourceDataLine line;
    private final byte[] buffer = new byte[1024];
    private int bytesRead = 0;

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
        Thread playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (-1 != (bytesRead = audioInputStream.read(buffer, 0, buffer.length))
                            && Window.get().isRunning()) {
                        line.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        playThread.start();
    }

    public float getVolume() {
        float volume = 0.f;
        if (bytesRead > 0) {
            // Calculate the volume as the average of the absolute values of the audio samples
            float sum = 0;
            for (int i = 0; i < bytesRead; i += 2) {
                short sample = (short) ( (buffer[i] & 0xff) | (buffer[i + 1] << 8));
                sum += Math.abs(sample);
            }
            if(sum!=0) volume = 20 * (float) Math.log10( sum / bytesRead );
        }
        return volume;
    }

    public float getBassVolume(){
    float bassVolume = 0.f;
    if (bytesRead > 0) {
        // Apply a low-pass filter to the audio samples
        float[] filteredSamples = new float[bytesRead / 2];
        float filterCoefficient = 0.1f; // Adjust this value to change the filter cutoff frequency
        float previousSample = 0.f;
        for (int i = 0; i < bytesRead; i += 2) {
            short sample = (short) ((buffer[i] & 0xff) | (buffer[i + 1] << 8));
            float filteredSample = previousSample + filterCoefficient * (sample - previousSample);
            filteredSamples[i / 2] = filteredSample;
            previousSample = filteredSample;
        }

        // Calculate the bass volume as the average of the absolute values of the filtered samples
        float sum = 0;
        for (float sample : filteredSamples) {
            sum += Math.abs(sample);
        }
        if (sum != 0) bassVolume = 20 * (float) Math.log10(sum / filteredSamples.length);
    }
    return bassVolume;
    }
}