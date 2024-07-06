uniform mat4 uProjection;
uniform mat4 uView;

varying vec4 position;

//#define M_PI 3.1415926535897932384626433832795

void main(){
    position = uProjection * uView * gl_Vertex;
    position.xy *= 2.f;
    position.xy-=1.f;
    gl_Position = position;
}