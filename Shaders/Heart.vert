uniform mat4 uProjection;
uniform mat4 uView;

varying vec4 position;
uniform double ration;

uniform float uTime;

//#define M_PI 3.1415926535897932384626433832795

vec2 grad( ivec2 z )  // replace this anything that returns a random vector
{
    // 2D to 1D  (feel free to replace by some other)
    int n = z.x+z.y*11111;

    // Hugo Elias hash (feel free to replace by another one)
    n = (n<<13)^n;
    n = (n*(n*n*15731+789221)+1376312589)>>16;

    #if 1

    // simple random vectors
    return vec2(cos(float(n)),sin(float(n)));

    #else

    // Perlin style vectors
    n &= 7;
    vec2 gr = vec2(n&1,n>>1)*2.0-1.0;
    return ( n>=6 ) ? vec2(0.0,gr.x) :
    ( n>=4 ) ? vec2(gr.x,0.0) :
    gr;
    #endif
}

float noise( in vec2 p )
{
    ivec2 i = ivec2(floor( p ));
    vec2 f =       fract( p );

    vec2 u = f*f*(3.0-2.0*f); // feel free to replace by a quintic smoothstep instead

    return mix( mix( dot( grad( i+ivec2(0,0) ), f-vec2(0.0,0.0) ),
                     dot( grad( i+ivec2(1,0) ), f-vec2(1.0,0.0) ), u.x),
                mix( dot( grad( i+ivec2(0,1) ), f-vec2(0.0,1.0) ),
                     dot( grad( i+ivec2(1,1) ), f-vec2(1.0,1.0) ), u.x), u.y);
}


void main(){
    position = uProjection * uView * gl_Vertex;
    position.y-=0.5f;
    position.y *= 2.f;

    vec2 p = position.xy;
    vec2 uv = p*vec2(ration,1.0) + uTime*0.25;
    float f = noise( 32.0*uv );
    position.y += 0.05*f;

    gl_Position = position;
}