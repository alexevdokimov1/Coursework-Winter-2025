package Drawable;

import Engine.Time;

import static java.lang.Math.*;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

public class Waves {

    private static double func1(double t){
        return (double) 4 /9*sin(2*t)+ (double) 1 /3*pow(sin(t),8)*cos(3*t)+ (double) 1 /8*sin(2*t)*pow(cos(247*t),4);
    }

    private static double func2(double t){
        return sin(t)+ (double) 1 /3*pow(sin(t),8)*sin(3*t)+ (double) 1 /8*sin(2*t)*pow(sin(247*t),4);
    }

    private static final int SAMPLES = 1000;

    public static void drawWaves(){
        glLoadIdentity();
        glBegin(GL_LINE_STRIP);
        for(double i = -1; i<=1; i+= (double) 1 /SAMPLES){
            glColor3d(i, func2(i+ Time.getTime()), 1- func2(i+ Time.getTime()));
            glVertex3d(i, func2(i+ Time.getTime()),0);
        }
        glEnd();

        glBegin(GL_LINE_STRIP);
        for(double i = -1; i<=1; i+= (double) 1 /SAMPLES){
            glColor3d(i, func1(i+ Time.getTime()), 1-func2(i+ Time.getTime()));
            glVertex3d(i, func1(i+ Time.getTime()),0);
        }
        glEnd();
    }
}
