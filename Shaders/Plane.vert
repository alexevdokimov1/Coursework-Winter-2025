uniform mat4 uProjection;
uniform mat4 uView;

varying vec4 position;

void main(){
    position = uProjection * uView * gl_Vertex;
    position.xy *= 2.f;
    position.xy-=1.f;
    gl_Position = position;
}