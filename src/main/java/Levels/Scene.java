package Levels;

import Drawable.Drawable;

import java.util.ArrayList;

public abstract class Scene {

    protected ArrayList<Drawable> actors = new ArrayList<>();

    public abstract void init();

    public abstract void update(float dt);

    public Scene(){
    }
}
