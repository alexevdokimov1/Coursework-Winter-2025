uniform float uTime;
varying vec4 position;
uniform double ration;

uniform float lineWidth;
uniform vec3 lineColor;
uniform bool soft;
uniform float speed;

#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.1415926535897932384626433832795

float plot(vec2 uv, float func){
    if(soft)
        return smoothstep( func-lineWidth/2, func, uv.y) - smoothstep( func, func+lineWidth/2, uv.y);
    else
        return step( func-lineWidth/2,  uv.y) - step( func+lineWidth/2, uv.y);
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
        temp *= sin(247.f*t+speed*uTime);
    result += 1.f/8.f*sin(2*t+speed*uTime) * temp;
    return result;
}

float func2(float t){

    t /= PI/2; //scaling function

    float result = sin(t);
    float temp = 1.f;
    for(int i = 0; i<8; i++)
        temp *= sin(t);
    result += 1/3*temp*sin(3*t);

    temp = 1.f;
    for(int i = 0; i<4; i++)
        temp *= sin(247.f*t);

    result += 1/8*sin(2.f*t)*temp;
    return result;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    float y1 = func(uv.x+speed*uTime);
    float y2 = func2(uv.x+speed*uTime);

    float pct1 = plot(uv,y1);
    float pct2 = plot(uv,y2);

    vec3 color;
    color += pct1*lineColor;
    color += pct2 *vec3(1,0,0);

    gl_FragColor = vec4(color,smoothstep(0.5,1.f,pct1+pct2));
}