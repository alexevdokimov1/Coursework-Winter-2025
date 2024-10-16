package Engine;

import javax.sound.sampled.*;
import java.io.File;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;

public class MusicPlayer {

    private final AudioInputStream audioInputStream;
    private final SourceDataLine line;
    private final byte[] buffer = new byte[1024];
    private int bytesRead = 0;
    private Thread playThread;

    public static final float MIN_BASS_FREQ = 80.0f;      // Adjust as needed
    public static final float MAX_BASS_FREQ = 300.0f;     // Adjust as needed
    public static final float MIN_MIDDLE_FREQ = 1000.0f;  // Adjust as needed
    public static final float MAX_MIDDLE_FREQ = 3000.0f;  // Adjust as needed
    public static final float MIN_HIGH_FREQ = 5000.0f;    // Adjust as needed
    public static final float MAX_HIGH_FREQ = 8000.0f;    // Adjust as needed

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
        playThread = new Thread(new Runnable() {
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

    public float getBass(){
        return calculateSpectrumValue(MIN_BASS_FREQ, MAX_BASS_FREQ);
    }

    public float getMiddle(){
        return calculateSpectrumValue(MIN_MIDDLE_FREQ, MAX_MIDDLE_FREQ);
    }

    public float getHigh(){
        return calculateSpectrumValue(MIN_HIGH_FREQ, MAX_HIGH_FREQ);
    }

    private float calculateSpectrumValue(float minFreq, float maxFreq) {
        // Convert byte buffer to double array for FFT
        int len = bytesRead / 2;
        double[] samples = new double[len];
        for (int i = 0; i < len; i++) {
            samples[i] = (buffer[i * 2] & 0xFF) | ((buffer[i * 2 + 1] << 8) & 0xFF);
            samples[i] = samples[i] / 32768.0;
        }

        // Perform unnormalized FFT
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fftResult = transformer.transform(samples, TransformType.FORWARD);

        // Calculate spectrum volume (mean energy in the range)
        double sumOfEnergy = 0.0;
        int numFreqBins = len / 2; // Frequency bins
        for (int i = 1; i < numFreqBins; i++) {
            float freq = i * audioInputStream.getFormat().getSampleRate() / numFreqBins;
            if (freq >= minFreq && freq <= maxFreq) {
                sumOfEnergy += Math.pow(fftResult[i].getReal(), 2) + Math.pow(fftResult[i].getImaginary(), 2);
            }
        }

        // Convert to decibels (optional)
        double meanEnergy = sumOfEnergy / numFreqBins;
        double spectrumVolume = 10 * Math.log10(meanEnergy);

        // Normalize spectrum volume from -∞ to 0 dB to range [0, 1]
        double minDB = -150.0;  // Adjust this value based on your requirements
        double maxDB = 0.0;     // Adjust this value based on your requirements

        if (spectrumVolume < minDB) spectrumVolume = minDB;
        if (spectrumVolume > maxDB) spectrumVolume = maxDB;

        return (float) ((spectrumVolume - minDB) / (maxDB - minDB));
    }
}