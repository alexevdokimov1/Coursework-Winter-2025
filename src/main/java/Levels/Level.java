package Levels;

import Drawable.*;
import Engine.MusicPlayer;
import Engine.Time;
import Engine.Window;

import static Engine.Interpolator.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final MusicPlayer player;

    private float lastVolumeValue;
    private float currentVolumeAlpha;
    private float start;

    public Level(){
        actors.add(new MusicHeart());
        player = new MusicPlayer("song.wav");
        lastVolumeValue = 0;
        currentVolumeAlpha = 0;
        start = Time.getTime();
    }

    @Override
    public void update(float dt) {

        float timeElapsed = Time.getTime() - start;
        start = Time.getTime();

        if(currentVolumeAlpha<1) currentVolumeAlpha += timeElapsed;
        else currentVolumeAlpha = 0;

        for(Drawable each : actors){
            each.draw();
            ((MusicPlane) each).setVolume(interpolateFloatByAlpha(lastVolumeValue, player.getVolume(), currentVolumeAlpha));
        }

        lastVolumeValue = interpolateFloatByAlpha(lastVolumeValue, player.getVolume(), currentVolumeAlpha);

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
    }
}