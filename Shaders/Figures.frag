uniform float uTime;
varying vec4 position;
uniform float ration;

float sdEquilateralTriangle( in vec2 p, in float r )
{
    const float k = sqrt(3.0);
    p.x = abs(p.x) - r;
    p.y = p.y + r/k;
    if( p.x+k*p.y>0.0 ) p = vec2(p.x-k*p.y,-k*p.x-p.y)/2.0;
    p.x -= clamp( p.x, -2.0*r, 0.0 );
    return -length(p)*sign(p.y);
}

float sdBox( in vec2 p, in vec2 b )
{
    vec2 d = abs(p)-b;
    return length(max(d,0.0)) + min(max(d.x,d.y),0.0);
}

float sdCircle( vec2 p, float r )
{
    return length(p) - r;
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;
    float d = sdBox(uv, 0.5);
    d = tan(d*8.+uTime)/8.;
    d = abs(d);
    d = smoothstep(0.0, 0.1, d);

    //set background color
    vec3 color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));

    gl_FragColor = (vec4(1)-vec4(d))*vec4(color, 1);
}