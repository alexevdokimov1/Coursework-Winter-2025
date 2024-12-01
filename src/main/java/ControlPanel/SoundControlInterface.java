package ControlPanel;

public interface SoundControlInterface {

    void resume();
    void pause();

    void openFile(String filename);

    void setVolume(float value);
    float getVolume();

    boolean isPaused();

    float getDuration();
    float getPlaybackPosition();
}
