package Levels;

import Drawable.*;
import Engine.MusicPlayer;
import Engine.Window;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final Circle circle;
    private final MusicPlayer player;

    public Level(){
        circle = new Circle(new Vector2f(), 0.8f, 0.003f, false , true);
        player = new MusicPlayer("song.wav");
    }

    @Override
    public void update(float dt) {

        circle.draw();
        circle.setRadius(player.getVolume()/100.f);

        for(Drawable each : actors){
            each.draw();
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
    }
}