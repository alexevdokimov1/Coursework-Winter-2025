package Levels;

import ControlPanel.ControlPanel;
import Drawable.*;
import Engine.*;

import javax.swing.*;

import java.util.ArrayList;

public class Level {

    private final ArrayList<Drawable> actors = new ArrayList<>();

    private final MusicPlayer player = new MusicPlayer();
    private final ControlPanel panel;
    private final Interpolator bassInterpolator = new Interpolator();
    private final Interpolator middleInterpolator = new Interpolator();
    private final Interpolator highInterpolator = new Interpolator();

    public Level(){

        actors.add(new MusicCircle());
        actors.add(new MusicHeart());

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

            switch(panel.getDrawableShape()){
                case DrawableShape.HEART:
                    if(each instanceof MusicHeart) each.draw(dt);
                    break;
                case DrawableShape.CIRCLE:
                    if(each instanceof MusicCircle) each.draw(dt);
                    break;
            }

            float interpolatedBassVolume = bassInterpolator.interpolate(player.getBass());
            float interpolatedMiddleVolume = middleInterpolator.interpolate(player.getMiddle());
            float interpolatedHighVolume = highInterpolator.interpolate(player.getHigh());

            if(each instanceof MusicPlane){
                ((MusicPlane) each).setBassVolume(interpolatedBassVolume);
                ((MusicPlane) each).setMiddleVolume(interpolatedMiddleVolume);
                ((MusicPlane) each).setHighVolume(interpolatedHighVolume);
                ((MusicPlane) each).setColorTemplate(panel.getColorTemplate());
            }
        }

        panel.updateProgressBar(dt);
    }
}