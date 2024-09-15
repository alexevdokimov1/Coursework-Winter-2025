uniform float uTime;
varying vec4 position;
uniform double ration;

uniform float volume;
uniform float maxVolume;
uniform float sumVolume;
uniform float maxRadius = 0.5f;
uniform float circleThickness = 0.1f;

vec2 uv = position.xy * vec2(ration, 1.f);

#define PI 3.1415926535897932384626433832795

float sdCircle( in vec2 p, in float radius )
{
    return length(p) - radius;
}

float testSin(in float x, in float height, in float up){
    return (sin(x+3*PI/2)+1)*height/2+up;
}

float opOnion( in vec2 p, in float radius, in float r )
{
    return abs(sdCircle(p, radius)) - r*testSin(p.x*20+sumVolume/800.f, pow(volume, 2.0), 1.f);
}

void main() {
    volume /= clamp(maxVolume, 1.f, 100.f);

    float d = opOnion(uv, volume*maxRadius, circleThickness);

    vec3 color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));

    vec3 col = (d>0.0) ? vec3(0) : color;
    col *= 1.0 - exp(-6.0*abs(d));

    gl_FragColor = vec4(col, 1.f);
}