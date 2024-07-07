package Engine;

import Drawable.*;
import Input.KeyListener;
import Input.MouseListener;
import Input.SizeListener;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void setRation(double newRation){
        this.ration = newRation;
    }

    public void init(){

        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
        glfwWindowHint(GLFW_STENCIL_BITS, 4);
        glfwWindowHint(GLFW_SAMPLES, 4);

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(videoMode==null) return;

        int width;
        int height;

        try {
            width = Integer.parseInt(Settings.getProperty("Width"));
            height = Integer.parseInt(Settings.getProperty("Height"));

            System.out.println("Resolution set to " + width + "x" + height);
        } catch (Exception e) {
            width = videoMode.width();
            height = videoMode.height();

            System.out.println("Width and Height set to screen resolution");

            System.out.println("Resolution set to " + width + "x" + height);
        }

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

        } catch (Exception e) {
            glfwWindow = glfwCreateWindow(width, height, "Window", NULL, NULL);
            System.err.println("No Fullscreen found");
        }

        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create window");
        }

        GLFWErrorCallback.createPrint(System.err).set();
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, SizeListener::resizeCallback);

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

        } catch (Exception e) {
            glfwSwapInterval(1);
            System.err.println("No VSync found");
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glClearDepth(1.0);
        glDepthFunc(GL_LESS);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc (GL_GREATER, 0.1f);

        glOrtho(-1, 1, -1, 1, 0, 1);
        glViewport(0,0, width, height);

        glfwShowWindow(glfwWindow);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void loop(){

        float lastTime = Time.getTime();
        float dt=0;

        ArrayList<Drawable> test = new ArrayList<>();
        test.add(new Circle(new Vector2f(), 0.5f, 0.05f, false, false));

        while(!glfwWindowShouldClose(glfwWindow)){

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if(dt >= 0) {
                for(Drawable each : test){
                    each.draw();
                }
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