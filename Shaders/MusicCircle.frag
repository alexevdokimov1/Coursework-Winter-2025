uniform float uTime;
varying vec4 position;
uniform double ration;

uniform float volume;
uniform float maxVolume;
uniform float sumVolume;
uniform float maxRadius = 0.5f;
uniform float circleThickness = 0.1f;

float sdCircle( in vec2 p, in float radius )
{
    return length(p) - radius;
}

float opOnion( in vec2 p, in float radius, in float r )
{
    return abs(sdCircle(p, radius)) - r;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;

    volume /= clamp(maxVolume, 1.f, 100.f);
    sumVolume /= 1000.f;

    float d = opOnion(uv, volume*maxRadius, circleThickness);

    vec3 col = (d>0.0) ? vec3(0) : vec3(1.0,0.0,0.0);
    col *= 1.0 - exp(-6.0*abs(d));

    gl_FragColor = vec4(col, 1.f);
}