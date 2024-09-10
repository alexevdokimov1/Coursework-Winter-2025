package Levels;

import Drawable.*;
import Engine.MusicPlayer;
import Engine.SmoothBalancer;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final MusicPlayer player;
    private final SmoothBalancer balancer = new SmoothBalancer(100);

    public Level(){
        actors.add(new MusicHeart());
        player = new MusicPlayer("song.wav");
    }

    @Override
    public void update(float dt) {

        for(Drawable each : actors){
            each.draw();

            float currentVolume = player.getVolume();
            balancer.addValue(currentVolume);

            ((MusicPlane) each).setVolume(currentVolume);
            ((MusicPlane) each).setMaxVolume(balancer.getMax());
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
    }
}