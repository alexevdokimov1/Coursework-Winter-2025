uniform float uTime;
varying vec4 position;
uniform double ration;

#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.1415926535897932384626433832795

const float lineWidth=0.1f;

float plot(vec2 uv, float func){
    return smoothstep( func-lineWidth, func, uv.y) - smoothstep( func, func+lineWidth, uv.y);
    //step( func-lineWidth,  uv.y) - step( func+lineWidth, uv.y)
}

float func(float t){

    t /= PI/2; //scaling function

    float result = sin(t);
    float temp = 1.f;
    for(int i = 0; i<8; i++)
            temp *= sin(t);
    result += 1.f/3.f*temp * sin(3.f*t);

    temp = 1.f;
    for(int i = 0; i<4; i++)
        temp *= sin(247.f*t+uTime);
    result += 1.f/8.f*sin(2*t+uTime) * temp;
    return result;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    float y = func(uv.x+uTime);

    float pct = plot(uv,y);
    vec3 color  = pct*vec3(0,0,1);

    gl_FragColor = vec4(color,1.0);
}