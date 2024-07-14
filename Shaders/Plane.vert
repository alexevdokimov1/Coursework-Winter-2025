uniform mat4 uProjection;
uniform mat4 uView;

varying vec4 position;

void main(){
    position = uProjection * uView * gl_Vertex;
    gl_Position = position;
}