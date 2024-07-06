package Drawable;

import Engine.Time;
import Engine.Window;
import Render.Shader;

import static java.lang.Math.*;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;

public class Heart extends Drawable {


    public Heart(){
        shader = new Shader("Heart", "Shader");
        shader.compile();
    }

    private double func1(double t){
        return (double) 4 /9*sin(2*t)+ (double) 1 /3*pow(sin(t),8)*cos(3*t)+ (double) 1 /8*sin(2*t)*pow(cos(247*t),4);
    }

    private double func2(double t){
        return sin(t)+ (double) 1 /3*pow(sin(t),8)*sin(3*t)+ (double) 1 /8*sin(2*t)*pow(sin(247*t),4);
    }

    private final int SAMPLES = 1000;

    @Override
    public void draw(){
        shader.use();
        shader.uploadProjectionMatrix("uProjection");
        shader.uploadViewMatrix("uView");
        shader.upload1f("uTime", Time.getTime());
        shader.upload1d("ration", Window.get().getRation());
        glBegin(GL_LINE_STRIP);
        for(double i = 0; i<=1; i+= (double) 1 /SAMPLES){
            glVertex3d(func1(i*PI), func2(i*PI),0);
        }
        glEnd();
        shader.detach();
    }
}