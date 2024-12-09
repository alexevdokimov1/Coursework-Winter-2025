package ControlPanel;

public interface SoundControlInterface {

    void resume();
    void pause();

    void openFile(String filename);

    void setVolume(int value);
    float getVolume();

    boolean isPaused();

    int getDuration();
    int getPlaybackPosition();
}
