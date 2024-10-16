#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
in vec4 position;
uniform float ration;

uniform float volume;
uniform float sumVolume;
uniform int colorTemplate;

out vec4 outColor;

float sdArc( in vec2 p, in vec2 sc, in float ra, float rb )
{
    p.x = abs(p.x);
    return ((sc.y*p.x<sc.x*p.y) ? length(p-sc*ra) :
                                  abs(length(p)-ra)) - rb;
}

float sdCircle( in vec2 p, in float radius )
{
    return length(p) - radius;
}

void main() {
     vec2 uv = position.xy;
        uv.x *= ration;

        // animation
        float time = sumVolume/500;
        float tb = 3.14*(0.55+0.1*cos(time*0.31+2.0));
        vec2  sc = vec2(sin(tb),cos(tb));

        // distance
        float d = sdArc(uv*1.5,sc, 0.7, 0.1);

        d *= sdCircle( uv + vec2(0.3, 0.-0.5) + vec2(0.05*sin(time), 0) , 0.12 );
        d *= sdCircle( uv + vec2(-0.3, 0.-0.5) + vec2(-0.05*sin(time), 0), 0.12 );

        // coloring
        vec3 col = (d>0.0) ? vec3(0) : vec3(10.0*volume,0,0);
    	col *= 1.0 - exp(-8.0*abs(d));
    	col *= 0.8 + 0.2*cos(128.0*abs(d));
    	col = mix( col, vec3(0.9, 0.5, 0.5), 1.0-smoothstep(0.0,0.015,abs(d)) );

    	outColor = vec4(col, 1.0);
}