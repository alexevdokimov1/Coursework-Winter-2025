uniform float uTime;
varying vec4 position;

uniform double ration;
uniform vec2 circlePosition;
uniform float circleRadius;
uniform float circleThickness;
uniform bool soft;
uniform bool hollow;

float sdCircle( vec2 p, float r )
{
    return length(p) - r;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;
    float d = sdCircle(uv, circleRadius);
    if(hollow)
        d = abs(d);
    if(soft)
        d = smoothstep(0.0, circleThickness, d);
    else
        d = step(circleThickness, d);

    gl_FragColor = vec4(1-d,1-d,1-d,1-d);
}