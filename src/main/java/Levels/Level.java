package Levels;

import Drawable.*;
import Engine.*;

import static Input.KeyListener.isKeyPressed;
import static org.lwjgl.glfw.GLFW.*;

public class Level extends Scene {

    private final MusicPlayer player;
    private final Interpolator bassInterpolator = new Interpolator();
    private final Interpolator middleInterpolator = new Interpolator();
    private final Interpolator highInterpolator = new Interpolator();

    public Level(){
        actors.add(new MusicCircle());
        player = new MusicPlayer();
        player.openFile("song.wav");
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

        if (isKeyPressed(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(Window.get().getWindow(), true);

        if (isKeyPressed(GLFW_KEY_SPACE))
            if(player.isPaused()) player.resume();
            else player.pause();

        if (isKeyPressed(GLFW_KEY_2))
            System.out.printf("Playback Position: %02d:%02d\n", (int)player.getPlaybackPosition()/60,
                    (int)player.getPlaybackPosition()%60);

        if (isKeyPressed(GLFW_KEY_4))
            player.openFile("song2.wav");

        if (isKeyPressed(GLFW_KEY_UP))
            player.setVolume(player.getVolume()+1);
        
        if (isKeyPressed(GLFW_KEY_DOWN))
            player.setVolume(player.getVolume()-1);
    }
}