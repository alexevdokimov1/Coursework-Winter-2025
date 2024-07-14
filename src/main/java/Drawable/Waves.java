package Drawable;

import org.joml.Vector3f;

public class Waves extends Plane{

    private final float lineWidth;
    private final Vector3f lineColor;
    private final boolean soft;
    private final float speed;

    public Waves(float lineWidth, Vector3f lineColor, boolean soft, float speed){
        super("Waves");
        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
        this.soft = soft;
        this.speed = speed;
    }

    public Waves(float lineWidth, Vector3f lineColor){
        this(lineWidth, lineColor, true, 1.f);
    }

    public Waves(){
        this(0.2f, new Vector3f(0,0,1), true, 1.f);
    }

    @Override
    protected void sendShaderData(){
        shader.upload1f("lineWidth", lineWidth);
        shader.upload3f("lineColor", lineColor);
        shader.uploadBoolean("soft", soft);
        shader.upload1f("speed", speed);
    }
}