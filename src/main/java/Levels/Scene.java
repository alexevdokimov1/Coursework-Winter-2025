package Levels;

import ControlPanel.ControlPanel;
import Drawable.Drawable;

import java.util.ArrayList;

public abstract class Scene {

    protected ControlPanel panel = new ControlPanel();
    protected ArrayList<Drawable> actors = new ArrayList<>();

    public abstract void update(float dt);

    public Scene(){
    }
}
