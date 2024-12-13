#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
in vec4 position;
uniform float ration;
uniform float dt;

uniform float bassFrVolume;
uniform float middleFrVolume;
uniform float highFrVolume;

uniform int colorTemplate;

out vec4 outColor;

const float maxRadius = 0.7f;
const float circleThickness = 0.1f;

vec2 uv = position.xy * vec2(ration, 1.f);

#define PI 3.1415926535897932384626433832795

vec2 hash2(vec2 p ) {
   return fract(sin(vec2(dot(p, vec2(123.4, 748.6)), dot(p, vec2(547.3, 659.3))))*5232.85324);
}
float hash(vec2 p) {
  return fract(sin(dot(p, vec2(43.232, 75.876)))*4526.3257);
}

float voronoi(vec2 p) {
    vec2 n = floor(p);
    vec2 f = fract(p);
    float md = 5.0;
    vec2 m = vec2(0.0);
    for (int i = -1;i<=1;i++) {
        for (int j = -1;j<=1;j++) {
            vec2 g = vec2(i, j);
            vec2 o = hash2(n+g);
            o = 0.5+0.5*sin(uTime+5.038*o);
            vec2 r = g + o - f;
            float d = dot(r, r);
            if (d<md) {
              md = d;
              m = n+g+o;
            }
        }
    }
    return md;
}

float ov(vec2 p) {
    float v = 0.0;
    float a = 0.4;
    for (int i = 0;i<3;i++) {
        v+= voronoi(p)*a;
        p*=2.0;
        a*=0.5;
    }
    return v;
}

float radialSin(in float x, in float height, in float up){
    return (sin(x+3*PI/2)+1)*height/2+up;
}

float modValue(in float x, in float height, in float up){
    return abs(mod(x, height)-height/2)+up;
}

float sdCircle( in vec2 p, in float radius )
{
    return length(p) - radius*pow((0.8+bassFrVolume*0.2),2);
}

float opOnion( in vec2 p, in float radius, in float r )
{
    float x = p.x*200;
    return abs(sdCircle(p, radius)) - r*radialSin(x, pow(bassFrVolume, 2.0)*2, 0.7f)*
    modValue(x+uTime, pow(highFrVolume,2), 1.f);
}

void main() {
    float d = opOnion(uv, maxRadius, circleThickness);
    vec3 color;

    if(colorTemplate==0) color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));
    else if(colorTemplate==1) color = vec3(0,0,0.9) + vec3(0,0.7,0.5) * cos(uTime+uv.xyx+vec3(2,1,4));
    else if(colorTemplate==2) {
        color = mix(vec3(1,0,0.8), vec3(2,0.0,0), smoothstep(0.0, 0.5, ov(uv*5.0)));
        d *= mix(0.2, 1, voronoi(uv*5.0)) * bassFrVolume;
    }
    else if(colorTemplate==3) {
        color = mix(vec3(0,0,1), vec3(1,0,0), pow(smoothstep(0.0, 0.5, ov(uv*1.0)),0.5));
        d *= mix(1, 0, smoothstep(0.0, 0.5, ov(uv*5.0)));
    }
    else color = vec3(1);

    vec3 col = (d>0.0) ? vec3(0) : color;
    col *= 1.0 - exp(-6.0*abs(d));

    outColor = vec4(col, (d<=0.0));
}