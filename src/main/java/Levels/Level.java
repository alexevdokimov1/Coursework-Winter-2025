package Levels;

import Drawable.*;
import Engine.MusicPlayer;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final MusicPlayer player;

    public Level(){
        actors.add(new MusicHeart());
        player = new MusicPlayer("song.wav");
    }

    @Override
    public void update(float dt) {

        for(Drawable each : actors){
            each.draw();

            float currentVolume = player.getVolume();

            ((MusicPlane) each).setVolume(currentVolume);
            ((MusicPlane) each).setMaxVolume(100.f);
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
    }
}