package ControlPanel;

public interface SoundControlInterface {

    void resume();
    void pause();

    boolean openFile(String filename);

    void setVolume(int value);
    float getVolume();

    boolean isPaused();

    int getDuration();
    int getPlaybackPosition();
}
