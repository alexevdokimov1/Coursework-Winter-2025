package Drawable;

import Engine.Time;
import Engine.Window;
import Render.Shader;
import static org.lwjgl.opengl.GL11.*;

public class Plane extends Drawable {

    public Plane(String fragmentFilepath){
        shader = new Shader("Plane", fragmentFilepath);
        shader.compile();
    }

    @Override
    public void draw(){
        shader.use();
        //load basic data
        shader.uploadProjectionMatrix("uProjection");
        shader.uploadViewMatrix("uView");
        shader.upload1f("uTime", Time.getTime());
        sendShaderData();
        drawPlane();
        shader.detach();
    }

    protected void sendShaderData(){
    }

    protected void drawPlane(){
        glBegin(GL_POLYGON);
            glVertex3d(0,0,0);
            glVertex3d(0,1,0);
            glVertex3d(1,1,0);
            glVertex3d(1,0,0);
        glEnd();
    }
}
