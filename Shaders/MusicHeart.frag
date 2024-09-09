uniform float uTime;
varying vec4 position;
uniform double ration;

uniform float volume;
uniform float sumVolume;

float dot2( in vec2 v ) { return dot(v,v); }

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

    volume /= 100.f;
    sumVolume /= 1000.f;

    float d = sdHeart(uv);

     // coloring
    vec3 col = (d>0.0) ? vec3(0) : vec3(1.0,0.0,0.0)*volume;
    col *= 1.0 - exp(-6.0*abs(d));
    col = mix( col, vec3(0), 1.0-smoothstep(0.0,0.01,abs(d)) );

    gl_FragColor = vec4(col, 1.f);
}