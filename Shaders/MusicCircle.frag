#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
varying vec4 position;
uniform float ration;

uniform float volume;
uniform float maxVolume;
uniform float sumVolume;
uniform int colorTemplate;

const float maxRadius = 0.5f;
const float circleThickness = 0.1f;

vec2 uv = position.xy * vec2(ration, 1.f);

#define PI 3.1415926535897932384626433832795

float radialSin(in float x, in float height, in float up){
    return (sin(x+3*PI/2)+1)*height/2+up;
}

float modValue(in float x, in float height, in float up){
    return mod(x, height)+up;
}

float sdCircle( in vec2 p, in float radius )
{
    return length(p) - radius;
}

float opOnion( in vec2 p, in float radius, in float r )
{
    float x = p.x*20+sumVolume/800.f;
    float volumeValue = pow(volume/100, 2.0);
    return abs(sdCircle(p, radius)) - r*radialSin(x, volumeValue, 1.f)*modValue(x, volumeValue, 1.f);
}

void main() {
    float d = opOnion(uv, maxRadius, circleThickness);
    vec3 color;

    if(colorTemplate==0) color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));
    else if(colorTemplate==1) color = vec3(0,0,0.9) + vec3(0,0.7,0.5) * cos(uTime+uv.xyx+vec3(2,1,4));
    else color = vec3(1);

    vec3 col = (d>0.0) ? vec3(0) : color;
    col *= 1.0 - exp(-6.0*abs(d));

    gl_FragColor = vec4(col, 1.f);
}