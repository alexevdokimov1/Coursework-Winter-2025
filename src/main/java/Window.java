import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;

import static java.lang.Math.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long glfwWindow;

    private static Window window = null;

    private Window(){
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private double ration;

    public double getRation(){
        return ration;
    }

    public void init(){

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        glfwWindowHint(GLFW_STENCIL_BITS, 4);
        glfwWindowHint(GLFW_SAMPLES, 4);

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(videoMode==null) return;

        int resolutionScale = 1;
        try {
            resolutionScale = Integer.parseInt(Settings.getProperty("ResolutionScale"));
            if(resolutionScale==0) {
                System.err.println("Resolution scale can not be " + resolutionScale);
                resolutionScale = 1;
            }
            System.out.println("Resolution scale set to " + resolutionScale);
        } catch (IOException e) {
           System.err.println("No Resolution Scale found");
        }

        int width = videoMode.width();
        int height = videoMode.height();

        width /= resolutionScale;
        height/= resolutionScale;
        ration = (double) width /height;

        try {
            if(Boolean.parseBoolean(Settings.getProperty("Fullscreen"))) {
                glfwWindow = glfwCreateWindow(width, height, "Window", glfwGetPrimaryMonitor(), NULL);
                System.out.println("Fullscreen mode is set");
            }
            else{
                glfwWindow = glfwCreateWindow(width, height, "Window", NULL, NULL);
                System.out.println("Windowed mode is set");
            }

        } catch (IOException e) {
            glfwWindow = glfwCreateWindow(width, height, "Window", NULL, NULL);
            System.err.println("No Fullscreen found");
        }

        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create window");
        }

        GLFWErrorCallback.createPrint(System.err).set();

        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        glfwMakeContextCurrent(glfwWindow);

        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        try {
            if(Boolean.parseBoolean(Settings.getProperty("VSync"))) {
                glfwSwapInterval(1);
                System.out.println("VSync is on");
            }
            else{
                glfwSwapInterval(0);
                System.out.println("VSync is off");
            }

        } catch (IOException e) {
            glfwSwapInterval(1);
            System.err.println("No VSync found");
        }

        glEnable(GL_DEPTH_TEST);

        glOrtho(-1, 1, -1, 1, 0, 1);
        glViewport(0,0, width, height);

        glfwShowWindow(glfwWindow);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    private final int SAMPLES = 1000;

    private void drawCircle(double x, double y, double radius){
        glBegin(GL_LINE_STRIP);
        for(int i = 0; i<=SAMPLES; i++){
                glVertex3d(x+radius*sin((double) i /SAMPLES*2*PI),y+radius*cos((double) i /SAMPLES*2*PI),0);
            }
        glEnd();
    }

    public void loop(){

        float lastTime = Time.getTime();
        float dt=0;

        while(!glfwWindowShouldClose(glfwWindow)){

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if(dt >= 0) {
                glColor3d(1,1,1);
                glPushMatrix();
                glScaled(1/ration,1,1);
                drawCircle(0, 0, 0.8);
                Waves.drawHeart();
                glPopMatrix();
            }

            if (glfwGetKey(glfwWindow, GLFW_KEY_ESCAPE) == GLFW_PRESS)
                glfwSetWindowShouldClose(glfwWindow, true);

            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();

            dt = Time.getTime() - lastTime;
            lastTime = Time.getTime();
        }
    }
}