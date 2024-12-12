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

float dot2( in vec2 v ) { return dot(v,v); }

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

    uv /= 1.2;

    uv /= bassFrVolume * 1.2;
    uv.y += 0.5;

    float x = uv.x*20+uTime*2;
    float volumeValue = pow(bassFrVolume, 2.0);

    float d = sdHeart(uv)*pow(radialSin(x, volumeValue, 0.8f), 2.f);

    vec3 col;
     // coloring
    if(colorTemplate == 0 ) { col = (d>0.0) ? vec3(0.2f) : vec3(1.0,0.0,0.0)*pow(bassFrVolume, 1.5f)*2.0;
    col *= 1.0 - exp(-20.0*abs(d));
    col = mix( col, vec3(0.1, 0.1, 0.1)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));
    }
    else if(colorTemplate == 1 ) { col = (d>0.0) ? vec3(0) : vec3(0.0,0.0,1.0)*pow(bassFrVolume, 1.5f)*2.0;
        col *= 1.0 - exp(-2.0*abs(d));
        col = mix( col, vec3(1.f)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
    }
    else if(colorTemplate == 2 ) {
    vec3 gradient = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));
    col = (d>0.0) ? vec3(0) : vec3(0);
            col *= 1.0 - exp(-5.0*abs(d));
            col = mix( col, gradient*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
    }
    else if(colorTemplate == 3 ) {
        col = (d>0.0) ? vec3(0) : mix(vec3(1,0,0),vec3(0),smoothstep(0,0.5f,ov(uv*5.0)))*pow(bassFrVolume, 1.5f)*2.0;
                col *= 1.0 - exp(-5.0*abs(d));
                col = mix( col, vec3(0.1, 0.1, 0.1)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
    }
    else col = (d>0.0) ? vec3(0) : vec3(1);

    outColor = vec4(col, 1.f);
}