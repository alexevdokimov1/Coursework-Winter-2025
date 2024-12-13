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

float radialSin(in float x, in float height, in float up){
    return (sin(x+3*PI/2)+1)*height/2+up;
}

float sdStar(in vec2 p, in float r, in float n, in float w)
{
    // these 5 lines can be precomputed for a given shape
    //float m = n*(1.0-w) + w*2.0;
    float m = n + w*(2.0-n);

    float an = 3.1415927/n;
    float en = 3.1415927/m;
    vec2  racs = r*vec2(cos(an),sin(an));
    vec2   ecs =   vec2(cos(en),sin(en)); // ecs=vec2(0,1) and simplify, for regular polygon,

    // symmetry (optional)
    p.x = abs(p.x);

    // reduce to first sector
    float bn = mod(atan(p.x,p.y),2.0*an) - an;
    p = length(p)*vec2(cos(bn),abs(sin(bn)));

    // line sdf
    p -= racs;
    p += ecs*clamp( -dot(p,ecs), 0.0, racs.y/ecs.y);
    return length(p)*sign(p.x);
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    float x = uv.x*20+uTime*2;
    float volumeValue = pow(bassFrVolume, 2.0);

    float d = sdStar(uv,10,0.7)*pow(radialSin(x, volumeValue, 0.8f), 2.f);

    vec3 col;
     // coloring
    if(colorTemplate == 0 ) { col = (d>0.0) ? vec3(0.2f) : vec3(1.0,0.0,0.0)*pow(bassFrVolume, 1.5f)*2.0;
    col *= 1.0 - exp(-20.0*abs(d));
    col = mix( col, vec3(0.1, 0.1, 0.1)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));
    }
    else if(colorTemplate == 1 ) {
        vec3 noiseMix = mix(vec3(0.0,0.0,1.0),vec3(0),smoothstep(0,0.5f,ov(uv*15.0)));
        d += exp(-100.0*abs(d));
        col = (d>0.0) ? vec3(0) : noiseMix*pow(bassFrVolume, 1.5f)*2.0;
        col *= 1.0 - exp(-2.0*abs(d));
        col = mix( col, vec3(0.2f)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));
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
                col = mix( col, vec3(0.2, 0.2, 0.2)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
    }
    else col = (d>0.0) ? vec3(0) : vec3(1);

    outColor = vec4(col, 1.f);
}