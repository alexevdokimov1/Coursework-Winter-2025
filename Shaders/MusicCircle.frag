uniform float uTime;
varying vec4 position;
uniform double ration;

uniform vec2 circlePosition = vec2(0,0);
uniform float volume;
uniform float sumVolume;
uniform float circleThickness = 0.02f;

float sdCircle( vec2 p, float r )
{
    return length(p) - r;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    volume /= 100.f;
    sumVolume /= 1000.f;

    float d = sdCircle(uv, volume);

    d = abs(d);

    d = smoothstep(0, circleThickness, d);

    vec3 color = 0.5 + 0.5 * cos(sumVolume+uv.xyx+vec3(0,2,4));

    color *= volume;

    gl_FragColor = (vec4(1)-vec4(d)) * vec4(color, 1.f);
}