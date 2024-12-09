package Levels;

import ControlPanel.ControlPanel;
import Drawable.*;
import Engine.*;

import javax.swing.*;

import java.util.ArrayList;

import static Input.KeyListener.isKeyPressed;
import static org.lwjgl.glfw.GLFW.*;

public class Level {

    private final ArrayList<Drawable> actors = new ArrayList<>();

    private final MusicPlayer player = new MusicPlayer();
    private final ControlPanel panel;
    private final Interpolator bassInterpolator = new Interpolator();
    private final Interpolator middleInterpolator = new Interpolator();
    private final Interpolator highInterpolator = new Interpolator();

    public Level(){
        actors.add(new MusicCircle());
        player.openFile("song.wav");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Style is unavalable");
        }

        panel = new ControlPanel(this.player);
    }

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

        panel.updateProgressBar(dt);

        if (isKeyPressed(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(Window.get().getWindow(), true);

        if(!Window.get().isRunning()) panel.dispose();
    }
}