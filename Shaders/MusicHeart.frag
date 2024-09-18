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

float dot2( in vec2 v ) { return dot(v,v); }

#define PI 3.1415926535897932384626433832795

float radialSin(in float x, in float height, in float up){
    return (sin(x+3*PI/2)+1)*height/2+up;
}

float sdHeart( in vec2 p )
{
    p.x = abs(p.x);

    if( p.y+p.x>1.0 )
        return sqrt(dot2(p-vec2(0.25,0.75))) - sqrt(2.0)/4.0;
    return sqrt(min(dot2(p-vec2(0.00,1.00)),
                    dot2(p-0.5*max(p.x+p.y,0.0)))) * sign(p.x-p.y);
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    uv.y += 0.5;

    float x = uv.x*20+sumVolume/800.f;
    float volumeValue = pow(volume/100, 2.0);
    float d = sdHeart(uv)*radialSin(x, volumeValue, 1.f);

     // coloring
    vec3 col = (d>0.0) ? vec3(0) : vec3(1.0,0.0,0.0)*pow(volume/100, 1.5f)*2.0;
    col *= 1.0 - exp(-6.0*abs(d));
    col = mix( col, vec3(0.1, 0.1, 0.1)*smoothstep(0.f, 5.f, volume), 1.0-smoothstep(0.0,0.01,abs(d)*radialSin(x, volumeValue, 1.f)) );

    gl_FragColor = vec4(col, 1.f);
}