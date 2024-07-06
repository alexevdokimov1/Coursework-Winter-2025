package Drawable;

import static java.lang.Math.*;
import static java.lang.Math.PI;
import static org.lwjgl.opengl.GL11.*;

import Engine.Window;
import org.joml.Vector2d;

import java.util.ArrayList;

public class Circle {

    private final static int SAMPLES = 1000;

    private final ArrayList<Vector2d> coords = new ArrayList<>();

    public Circle(double x, double y, double radius){
        for(int i = 0; i<=SAMPLES; i++){
            double xValue = x+radius*sin((double) i /SAMPLES*2*PI);
            double yValue = y+radius*cos((double) i /SAMPLES*2*PI);
            coords.add(new Vector2d(xValue,yValue));
        }
    }

    public void drawCircle(){
        glPushMatrix();
            glScaled(1/ Window.get().getRation(),1,1);
            glBegin(GL_LINE_STRIP);
            for(Vector2d each : coords){
                glVertex3d(each.x,each.y,0);
            }
            glEnd();
        glPopMatrix();
    }
}
