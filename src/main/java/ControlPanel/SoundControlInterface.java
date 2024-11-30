package ControlPanel;

public interface SoundControlInterface {

    void resume();
    void pause();
    void stop();

    void openFile(String filename);

    void setVolume(float value);
    float getVolume();
}
