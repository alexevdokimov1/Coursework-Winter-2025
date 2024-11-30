package Levels;

import Drawable.*;
import Engine.Interpolator;
import Engine.MusicPlayer;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Level extends Scene {

    private final MusicPlayer player;
    private final Interpolator bassInterpolator = new Interpolator();
    private final Interpolator middleInterpolator = new Interpolator();
    private final Interpolator highInterpolator = new Interpolator();

    public Level(){
        actors.add(new MusicCircle());
        player = new MusicPlayer("song.wav");
    }

    @Override
    public void update(float dt) {

        bassInterpolator.update(dt);
        middleInterpolator.update(dt);
        highInterpolator.update(dt);

        for(Drawable each : actors){
            each.draw(dt);

            float interpolatedBassVolume = bassInterpolator.interpolate(player.getBass());
            float interpolatedMiddleVolume = middleInterpolator.interpolate(player.getMiddle());
            float interpolatedHighVolume = highInterpolator.interpolate(player.getHigh());

            if(each instanceof MusicPlane){
                ((MusicPlane) each).setBassVolume(interpolatedBassVolume);
                ((MusicPlane) each).setMiddleVolume(interpolatedMiddleVolume);
                ((MusicPlane) each).setHighVolume(interpolatedHighVolume);
            }
        }

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
            glfwSetWindowShouldClose(Window.get().getWindow(), true);

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_1) == GLFW_PRESS)
            player.pause();

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_2) == GLFW_PRESS)
            player.resume();

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_4) == GLFW_PRESS)
            player.openFile("song_1.wav");

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_UP) == GLFW_PRESS)
            player.setVolume(player.getVolume()+10);

        if (glfwGetKey(Window.get().getWindow(), GLFW_KEY_DOWN) == GLFW_PRESS)
            player.setVolume(player.getVolume()-10);
    }
}