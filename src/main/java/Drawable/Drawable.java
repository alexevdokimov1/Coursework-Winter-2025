package Drawable;

import Render.Shader;

public abstract class Drawable {
    protected Shader shader = new Shader();
    public abstract void draw();
}