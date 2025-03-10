package Render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL40.*;

public class Shader {

    private int shaderProgram;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;

    public Shader(){}

    public void addSource(String vertexSource, String fragmentSource){
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
    }

    public void compile(){
        shaderProgram = glCreateProgram();
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Error during vertex shader compilation");
            System.err.print(glGetShaderInfoLog(vertexShader));
            return;
        }

        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Error during fragment shader compilation");
            System.err.print(glGetShaderInfoLog(fragmentShader));
            return;
        }

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
    }

    public void use(){
        if(!beingUsed) {
            glUseProgram(shaderProgram);
            beingUsed = true;
        }
    }

    public void detach(){
        if(beingUsed){
            glUseProgram(0);
            beingUsed = false;
        }
    }

    public void upload1i(String varName, int value){
        glUniform1i(glGetUniformLocation(shaderProgram, varName), value);
    }

    public void upload1f(String varName, float value){
        glUniform1f(glGetUniformLocation(shaderProgram, varName), value);
    }

    public void uploadViewMatrix(String viewMatrixName) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer( 16 );
        GL11.glGetFloatv( GL11.GL_MODELVIEW_MATRIX, buffer );
        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, viewMatrixName), false, buffer);
    }

    public void uploadProjectionMatrix(String projectionMatrixName) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer( 16 );
        GL11.glGetFloatv( GL11.GL_PROJECTION_MATRIX, buffer );
        glUniformMatrix4fv(glGetUniformLocation(shaderProgram, projectionMatrixName), false, buffer);
    }
}