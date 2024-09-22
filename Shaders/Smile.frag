#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
in vec4 position;
uniform float ration;

out vec4 outColor;

float sdArc( in vec2 p, in vec2 sc, in float ra, float rb )
{
    p.x = abs(p.x);
    return ((sc.y*p.x<sc.x*p.y) ? length(p-sc*ra) :
                                  abs(length(p)-ra)) - rb;
}

void main() {
     vec2 uv = position.xy;
        uv.x *= ration;
        uv *= 1.2;

        // animation
        float time = uTime;
        float tb = 3.14*(0.55+0.15*cos(time*0.31+2.0));
        vec2  sc = vec2(sin(tb),cos(tb));

        // distance
        float d = sdArc(uv,sc, 0.7, 0.1);

        // coloring
        vec3 col = (d>0.0) ? vec3(0) : vec3(1.0,0,0);
    	//col *= 1.0 - exp(-8.0*abs(d));
    	//col *= 0.8 + 0.2*cos(128.0*abs(d));
    	col = mix( col, vec3(0.9, 0.5, 0.5), 1.0-smoothstep(0.0,0.015,abs(d)) );

    	outColor = vec4(col, 1.0);
}