package Render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgram;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;

    private String vertexFilename, fragmentFilename;

    public Shader(String vertexFilepath, String fragmentFilepath){
        this.vertexFilename = vertexFilepath;
        this.fragmentFilename = fragmentFilepath;
        try{
            String filepath_vert =  "Shaders/"+vertexFilepath+".vert";
            vertexSource = new String(Files.readAllBytes(Paths.get(filepath_vert)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            String filepath_frag =  "Shaders/"+fragmentFilepath+".frag";
            fragmentSource = new String(Files.readAllBytes(Paths.get(filepath_frag)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Shader(){}

    public void compile(){
        long start = System.currentTimeMillis();

        shaderProgram = glCreateProgram();
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.printf("Error during vertex shader compilation: %s\n", this.vertexFilename);
            System.err.print(glGetShaderInfoLog(vertexShader));
            return;
        }

        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.printf("Error during fragment shader compilation: %s\n", this.fragmentFilename);
            System.err.print(glGetShaderInfoLog(fragmentShader));
            return;
        }

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.printf("Shader %s compiled for %d ms\n", vertexFilename, timeElapsed);
    }

    public void use(){
        if(!beingUsed) {
            glUseProgram(shaderProgram);
            beingUsed = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void upload1f(String varName, float value){
        glUniform1f(glGetUniformLocation(shaderProgram, varName), value);
    }

    public void upload3f(String varName, Vector3f vec){
        glUniform3f(glGetUniformLocation(shaderProgram, varName), vec.x, vec.y, vec.z);
    }

    public void uploadMat4(String varName, Matrix4f mat4){
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, varName), false, matBuffer);
    }

    public Matrix4f getModelViewMatrix() {
        return getMatrix( GL11.GL_MODELVIEW_MATRIX );
    }

    public Matrix4f getProjectionMatrix() {
        return getMatrix( GL11.GL_PROJECTION_MATRIX );
    }

    private Matrix4f getMatrix( int name ) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer( 16 );
        GL11.glGetFloatv( name, buffer );
        return new Matrix4f( buffer );
    }

}
