#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
in vec4 position;
uniform float ration;
uniform float dt;

uniform float bassFrVolume;
uniform float middleFrVolume;
uniform float highFrVolume;

out vec4 outColor;

void main() {
    vec2 uv = position.xy;
    vec3 color = mix(vec3(0.56, 0.0, 0.16), vec3(0.0, 0.0, 1.0), bassFrVolume );
    outColor = vec4(color,1);
}