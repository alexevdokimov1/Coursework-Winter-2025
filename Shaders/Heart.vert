uniform mat4 uProjection;
uniform mat4 uView;

varying vec4 position;

uniform float uTime;

float func1(float t){
    return 4/9*sin(2*t)+1/3*pow(sin(t),8)*cos(3*t) + 1/8*sin(2*t)*pow(cos(247*t),4);
}

void main(){
    position = uProjection * uView * gl_Vertex;
    position.y-=0.5f;
    position.y *= 2.f;
    gl_Position = position;
}