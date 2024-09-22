#version 330

uniform mat4 uProjection;
uniform mat4 uView;

attribute vec4  mcVertex;
varying vec4 position;

void main(){
    position = uProjection * uView * mcVertex;
    gl_Position = position;
}