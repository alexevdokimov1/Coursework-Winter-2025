package Drawable;

import Engine.Time;
import Engine.Window;
import Render.Shader;
import static org.lwjgl.opengl.GL11.*;

public class Plane extends Drawable {

    public Plane(String vertexFilepath, String fragmentFilepath){
        shader = new Shader(vertexFilepath, fragmentFilepath);
        shader.compile();
    }

    public Plane(String fragmentFilepath){
        this("Default", fragmentFilepath);
    }

    public Plane(){
        this("Default");
    }

    @Override
    public void draw(float dt){
        shader.use();
        //load basic data
        shader.uploadProjectionMatrix("uProjection");
        shader.uploadViewMatrix("uView");
        shader.upload1f("uTime", Time.getTime());
        shader.upload1f("ration", Window.get().getRation());
        shader.upload1f("dt", dt);
        sendShaderData();
        drawPlane();
        shader.detach();
    }

    protected void sendShaderData(){
    }

    protected void drawPlane(){
        glBegin(GL_POLYGON);
            glVertex3d(-1,-1,0);
            glVertex3d(-1,1,0);
            glVertex3d(1,1,0);
            glVertex3d(1,-1,0);
        glEnd();
    }
}
