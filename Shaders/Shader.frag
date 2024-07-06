uniform float uTime;
varying vec4 position;
uniform double ration;

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;
    vec3 color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));
    gl_FragColor = vec4(color,1);
}