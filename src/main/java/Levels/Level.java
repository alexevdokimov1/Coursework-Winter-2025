package Levels;

import Drawable.*;
import Engine.MusicPlayer;
import Engine.SmoothBalancer;
import Engine.Window;
import Input.KeyListener;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final MusicPlayer player;
    private final SmoothBalancer balancer = new SmoothBalancer(5);

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

            if(each instanceof MusicPlane){
                ((MusicPlane) each).setVolume(currentVolume);
                ((MusicPlane) each).setMaxVolume(balancer.getMax());
            }
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);

        for(Drawable each : actors){
            if(each instanceof MusicPlane){
                if (KeyListener.isKeyPressed(GLFW_KEY_1)) {
                    ((MusicPlane) each).setColorTemplate(0);
                }
                else if(KeyListener.isKeyPressed(GLFW_KEY_2)) {
                    ((MusicPlane) each).setColorTemplate(1);
                }
            }
        }

    }
}