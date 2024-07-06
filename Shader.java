package Render;

import org.joml.Vector3f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgram;

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
    }

    public void use(){
        glUseProgram(shaderProgram);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void upload1f(String varName, float value){
        glUniform1f(glGetUniformLocation(shaderProgram, varName), value);
    }

    public void upload3f(String varName, Vector3f vec){
        glUniform3f(glGetUniformLocation(shaderProgram, varName), vec.x, vec.y, vec.z);
    }

    public void uploadTexture(String varName, int slot){
        glUniform1i(glGetUniformLocation(shaderProgram, varName), slot);
    }
}
