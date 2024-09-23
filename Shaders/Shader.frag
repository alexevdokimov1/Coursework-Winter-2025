#version 330
#ifdef GL_ES
    precision mediump float;
#endif
uniform float uTime;
in vec4 position;
uniform float ration;
uniform float dt;

uniform float sumVolume;

out vec4 outColor;

vec3 random3(vec3 c) {
	float j = 4096.0*sin(dot(c,vec3(17.0, 59.4, 15.0)));
	vec3 r;
	r.z = fract(512.0*j);
	j *= .125;
	r.x = fract(512.0*j);
	j *= .125;
	r.y = fract(512.0*j);
	return r-0.5;
}

/* skew constants for 3d simplex functions */
const float F3 =  0.3333333;
const float G3 =  0.1666667;

/* 3d simplex noise */
float simplex3d(vec3 p) {
	 /* 1. find current tetrahedron T and it's four vertices */
	 /* s, s+i1, s+i2, s+1.0 - absolute skewed (integer) coordinates of T vertices */
	 /* x, x1, x2, x3 - unskewed coordinates of p relative to each of T vertices*/

	 /* calculate s and x */
	 vec3 s = floor(p + dot(p, vec3(F3)));
	 vec3 x = p - s + dot(s, vec3(G3));

	 /* calculate i1 and i2 */
	 vec3 e = step(vec3(0.0), x - x.yzx);
	 vec3 i1 = e*(1.0 - e.zxy);
	 vec3 i2 = 1.0 - e.zxy*(1.0 - e);

	 /* x1, x2, x3 */
	 vec3 x1 = x - i1 + G3;
	 vec3 x2 = x - i2 + 2.0*G3;
	 vec3 x3 = x - 1.0 + 3.0*G3;

	 /* 2. find four surflets and store them in d */
	 vec4 w, d;

	 /* calculate surflet weights */
	 w.x = dot(x, x);
	 w.y = dot(x1, x1);
	 w.z = dot(x2, x2);
	 w.w = dot(x3, x3);

	 /* w fades from 0.6 at the center of the surflet to 0.0 at the margin */
	 w = max(0.6 - w, 0.0);

	 /* calculate surflet components */
	 d.x = dot(random3(s), x);
	 d.y = dot(random3(s + i1), x1);
	 d.z = dot(random3(s + i2), x2);
	 d.w = dot(random3(s + 1.0), x3);

	 /* multiply d by w^4 */
	 w *= w;
	 w *= w;
	 d *= w;

	 /* 3. return the sum of the four surflets */
	 return dot(d, vec4(52.0));
}
//</3d simplex noise by nikat https://www.shadertoy.com/view/XsX3zB>
float fbm(vec2 xy, float z, int octs){
    float f = 1.0;
    float a = 1.0;
    float t = 0.0;
    float a_bound = 0.0;
    for(int i=0;i<octs;i++){
        t += a*simplex3d(vec3(xy*f,z*f));
        f *= 2.0;
        a_bound += a;
        a *= 0.5;
    }
    return t/a_bound;
}
float noise_final_comp(vec2 xy, float z){
    float value = fbm(vec2(xy.x / 200.0+513.0, xy.y / 200.0+124.0), z, 3);
    value = 1.0-abs(value);
    value = value*value;
    return value*2.0-1.0;
}

float noise_final(vec2 xy, float z){
        float value = fbm(vec2((noise_final_comp(xy, z)*15.0+xy.x) / 100.0,(noise_final_comp(xy+300.0, z)*15.0+xy.y) / 100.0), z*1.5, 5);
        return max(0.0, min(1.0, (value*0.5+0.5)*1.3));
}

void main() {
    vec2 uv = position.xy;
    uv.x *= ration;
    uv *= 200;
    float time;
    if(sumVolume==0) time = uTime;
    else time = sumVolume/800.f;
    float t = sin(time/10);

    float p = noise_final(uv,time*0.025+0.05*sin(time*0.2+(uv.x*0.3*(sin(time/30.0)-0.3)+uv.y)/265.0));

    vec3 color = mix(vec3(0.56, 0.0, 0.16), vec3(0.0, 0.0, 1.0), (t + 1.0) / 2.0)*p;
    outColor = vec4(color,1);
}