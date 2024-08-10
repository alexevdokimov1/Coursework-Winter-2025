package Levels;

import Drawable.*;
import Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    public Level(){
        actors.add(new Circle(new Vector2f(), 0.8f, 0.1f));
    }

    @Override
    public void update(float dt) {
        for(Drawable each : actors){
            each.draw();
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);
    }
}