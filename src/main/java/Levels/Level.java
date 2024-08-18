package Levels;

import Drawable.*;
import Engine.MusicPlayer;
import Engine.Time;
import Engine.Window;
import org.joml.Vector2f;

import static Engine.Interpolator.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final Circle circle;
    private final MusicPlayer player;

    private float lastVolumeValue;
    private float currentVolumeAlpha;
    float start = Time.getTime();

    public Level(){
        circle = new Circle(new Vector2f(), 0, 0.03f, true , true);
        player = new MusicPlayer("song.wav");
        lastVolumeValue = 0;
        currentVolumeAlpha = 0;
    }

    @Override
    public void update(float dt) {

        float timeElapsed = Time.getTime() - start;
        start = Time.getTime();

        if(currentVolumeAlpha<1) currentVolumeAlpha += timeElapsed;
        else currentVolumeAlpha = 0;

        circle.draw();
        circle.setRadius(fastInterpolateFloatByAlpha(lastVolumeValue, player.getVolume(), currentVolumeAlpha)/100.f);
        lastVolumeValue = fastInterpolateFloatByAlpha(lastVolumeValue, player.getVolume(), currentVolumeAlpha);

        for(Drawable each : actors){
            each.draw();
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
    }
}