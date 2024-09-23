#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
in vec4 position;
uniform float ration;

uniform vec2 circlePosition;
uniform float circleRadius;
uniform float circleThickness;
uniform bool hollow;

out vec4 outColor;

float sdCircle( in vec2 p, in float r )
{
    return length(p) - r;
}

float opOnion( in vec2 p, in float radius, in float thickness )
{
    return abs(sdCircle(p, radius)) - thickness;
}

void main() {
    vec2 uv = position.xy;
    uv += circlePosition;
    uv.x *= ration;
    float d = sdCircle(uv, circleRadius);
    if (hollow) d = opOnion( uv, circleRadius, circleThickness );

     d = step(circleThickness, d);

    outColor = vec4(1)-vec4(d);
}