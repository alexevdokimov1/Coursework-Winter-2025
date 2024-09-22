#version 330

uniform float uTime;
in vec4 position;
uniform float ration;

uniform vec2 circlePosition;
uniform float circleRadius;
uniform float circleThickness;
uniform bool soft;
uniform bool hollow;

out vec4 outColor;

float sdCircle( vec2 p, float r )
{
    return length(p) - r;
}

void main() {
    vec2 uv = position.xy;
    uv += circlePosition;
    uv.x *= ration;
    float d = sdCircle(uv, circleRadius);
    if(hollow)
        d = abs(d);

    if(soft)
        d = smoothstep(0, circleThickness, d);
    else
        d = step(circleThickness, d);

    outColor = (vec4(1)-vec4(d)*1/circleThickness);
}