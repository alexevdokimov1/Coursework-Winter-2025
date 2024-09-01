uniform float uTime;
varying vec4 position;
uniform double ration;

uniform vec2 circlePosition = vec2(0,0);
uniform float volume;
uniform float circleThickness = 0.03f;

float sdCircle( vec2 p, float r )
{
    return length(p) - r;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    if(abs(volume)<0.03) {
        gl_FragColor = vec4(0);
        return;
    }

    float d = sdCircle(uv, volume);

    d = abs(d);

    d = smoothstep(0, circleThickness, d);

    vec3 color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));

    gl_FragColor = (vec4(1)-vec4(d)*1/circleThickness) * vec4(color, 1.f);
}