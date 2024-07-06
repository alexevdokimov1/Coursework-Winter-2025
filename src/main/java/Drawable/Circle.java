package Drawable;

import Engine.Time;
import Engine.Window;
import org.joml.Vector2f;

public class Circle extends Plane{
    private final Vector2f position;
    private final float radius;
    private final float borderThickness;

    public Circle(Vector2f position, float radius,  float borderThickness){
        super("Plane", "CirclePlane");
        this.position = position;
        this.radius = radius;
        this.borderThickness = borderThickness;
    }

    @Override
    protected void sendShaderData(){
        shader.uploadProjectionMatrix("uProjection");
        shader.uploadViewMatrix("uView");
        shader.upload1f("uTime", Time.getTime());
        shader.upload1d("ration", Window.get().getRation());
        shader.upload2f("circlePosition", position);
        shader.upload1f("circleRadius", radius);
        shader.upload1f("circleThickness", borderThickness);
    }
}
