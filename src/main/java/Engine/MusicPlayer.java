package Engine;

import javax.sound.sampled.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import ControlPanel.SoundControlInterface;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.complex.Complex;

public class MusicPlayer implements SoundControlInterface {

    private AudioInputStream audioInputStream;
    private SourceDataLine line;
    private final byte[] buffer = new byte[1024];
    private int bytesRead = 0;
    private final AtomicBoolean isPaused = new AtomicBoolean(false);
    private File file;
    private float volume = 100;

    public static final float MIN_BASS_FREQ = 80.0f;
    public static final float MAX_BASS_FREQ = 300.0f;
    public static final float MIN_MIDDLE_FREQ = 1000.0f;
    public static final float MAX_MIDDLE_FREQ = 3000.0f;
    public static final float MIN_HIGH_FREQ = 5000.0f;
    public static final float MAX_HIGH_FREQ = 8000.0f;

    public MusicPlayer(String filename){
        openFile(filename);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (Window.get().isRunning()) {
                        while(isPaused.get() || -1 == (bytesRead = audioInputStream.read(buffer, 0, buffer.length) ));
                        line.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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

        if(isPaused.get() || bytesRead <= 0) return 0.1f;

        int len = bytesRead / 2;
        double[] samples = new double[len];
        for (int i = 0; i < len; i++) {
            samples[i] = (buffer[i * 2] & 0xFF) | ((buffer[i * 2 + 1] << 8) & 0xFF);
            samples[i] = samples[i] / 32768.0;
        }

        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] fftResult = transformer.transform(samples, TransformType.FORWARD);

        double sumOfEnergy = 0.0;
        int numFreqBins = len / 2; // Frequency bins
        for (int i = 1; i < numFreqBins; i++) {
            float freq = i * audioInputStream.getFormat().getSampleRate() / numFreqBins;
            if (freq >= minFreq && freq <= maxFreq) {
                sumOfEnergy += Math.pow(fftResult[i].getReal(), 2) + Math.pow(fftResult[i].getImaginary(), 2);
            }
        }

        double meanEnergy = sumOfEnergy / numFreqBins;
        double spectrumVolume = 10 * Math.log10(meanEnergy);

        double minDB = -150.0;
        double maxDB = 0.0;

        if (spectrumVolume < minDB) spectrumVolume = minDB;
        if (spectrumVolume > maxDB) spectrumVolume = maxDB;

        return (float) ((spectrumVolume - minDB) / (maxDB - minDB));
    }

    @Override
    public void pause() {
        isPaused.set(true);
    }

    @Override
    public void resume() {
        isPaused.set(false);
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public boolean isPaused(){
        return this.isPaused.get();
    }

    public void openFile(String filename){
        try {
            file = new File(filename);
            audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            // Play the audio
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioInputStream.getFormat());
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioInputStream.getFormat());
            line.start();

            ((FloatControl)line.getControl( FloatControl.Type.MASTER_GAIN )).setValue( 20.0f * (float) Math.log10( this.volume / 100.0 ) );
        } catch (Exception e) {
           System.out.println(e);
        }
        resume();
    }

    @Override
    public void setVolume(float value) {
        this.volume = Math.clamp(value, 0, 100);
        final FloatControl volumeControl = (FloatControl) line.getControl( FloatControl.Type.MASTER_GAIN );
        volumeControl.setValue( 20.0f * (float) Math.log10( this.volume / 100.0 ) );
    }

    @Override
    public float getVolume() {
        return this.volume;
    }

    @Override
    public float getPlaybackPosition(){
        return (float) (line.getMicrosecondPosition()*1e-6);
    }

    @Override
    public float getDuration(){
        AudioFormat format = audioInputStream.getFormat();
        long audioFileLength = file.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        return ((audioFileLength / (frameSize * frameRate)));
    }

    public float getDurationAlpha(){
        return getPlaybackPosition()/getDuration();
    }
}