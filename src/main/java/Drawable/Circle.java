package Drawable;

import org.joml.Vector2f;

public class Circle extends Plane{
    private final Vector2f position;
    private final float radius;
    private final float borderThickness;
    private final boolean soft;
    private final boolean hollow;

    public Circle(Vector2f position, float radius,  float borderThickness, boolean soft, boolean hollow){
        super("CirclePlane");
        this.position = position;
        this.radius = radius;
        this.borderThickness = borderThickness;
        this.soft = soft;
        this.hollow = hollow;
    }

    public Circle(Vector2f position, float radius,  float borderThickness){
        super("CirclePlane");
        this.position = position;
        this.radius = radius;
        this.borderThickness = borderThickness;
        this.soft = true;
        this.hollow = true;
    }

    @Override
    protected void sendShaderData(){
        shader.upload2f("circlePosition", position);
        shader.upload1f("circleRadius", radius);
        shader.upload1f("circleThickness", borderThickness);
        shader.uploadBoolean("soft", soft);
        shader.uploadBoolean("hollow", hollow);
    }
}