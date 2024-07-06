uniform float uTime;
varying vec4 position;

float rand2(vec2 co)
{
    float a = 12.9898;
    float b = 78.233;
    float c = 43758.5453;
    float dt= dot(co.xy ,vec2(a,b));
    float sn= mod(dt,3.14);
    return fract(sin(sn) * c);
}

void main() {
    float b = rand2(position.xy*uTime);
    gl_FragColor = vec4(1.f-b,b,1.f,1.f);
}