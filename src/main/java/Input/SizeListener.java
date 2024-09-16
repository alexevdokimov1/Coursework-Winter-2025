package Input;

import Engine.Window;

import static org.lwjgl.opengl.GL11C.glViewport;

public class SizeListener {
    public static void resizeCallback(long window, int width, int height)
    {
        glViewport(0, 0, width, height);
        Window.get().setRation((float) width /height);
    }
}
