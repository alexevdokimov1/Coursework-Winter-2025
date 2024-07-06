uniform float uTime;
varying vec4 position;

uniform double ration;
uniform vec2 circlePosition;
uniform float circleRadius;
uniform float circleThickness;

float circleShape(float radius, vec2 position, vec2 center) {
    float value = distance(position, center);
    return step(radius, value);
}

void main() {
    vec2 pixelCoord = position;
    pixelCoord.x*=ration;
    circlePosition.x*=ration;

    float outer = clamp(1-circleShape(circleRadius, pixelCoord, circlePosition), 0, 1);
    float innter= clamp(1-circleShape(circleRadius-circleThickness, pixelCoord, circlePosition), 0, 1);
    vec3 color = vec3(outer-innter);
    gl_FragColor = vec4(color, 1.0);
}