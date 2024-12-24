package Render;

public class ShaderDataStrings {
    public final static String DEFAULT_FRAG_SHADER = """
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
            """;
    public final static String DEFAULT_VERT_SHADER = """
            #version 330
            
            uniform mat4 uProjection;
            uniform mat4 uView;
            
            attribute vec4  mcVertex;
            out vec4 position;
            
            void main(){
                position = uProjection * uView * mcVertex;
                gl_Position = position;
            }
            """;
    public final static String MUSIC_CIRCLE_FRAG_SHADER = """
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
            
            uniform int colorTemplate;
            
            out vec4 outColor;
            
            const float maxRadius = 0.7f;
            const float circleThickness = 0.1f;
            
            vec2 uv = position.xy * vec2(ration, 1.f);
            
            #define PI 3.1415926535897932384626433832795
            
            vec2 hash2(vec2 p ) {
               return fract(sin(vec2(dot(p, vec2(123.4, 748.6)), dot(p, vec2(547.3, 659.3))))*5232.85324);
            }
            float hash(vec2 p) {
              return fract(sin(dot(p, vec2(43.232, 75.876)))*4526.3257);
            }
            
            float voronoi(vec2 p, bool bass = false) {
                vec2 n = floor(p);
                vec2 f = fract(p);
                float md = 5.0;
                vec2 m = vec2(0.0);
                for (int i = -1;i<=1;i++) {
                    for (int j = -1;j<=1;j++) {
                        vec2 g = vec2(i, j);
                        vec2 o = hash2(n+g);
                        o = 0.5+0.5*sin(uTime+5.038*o);
                        vec2 r = g + o - f;
                        float d = dot(r, r);
                        if (d<md) {
                          md = d;
                          m = n+g+o;
                        }
                    }
                }
                return md* (bass ? bassFrVolume:1);
            }
            
            float ov(vec2 p, bool bass = false) {
                float v = 0.0;
                float a = 0.4;
                for (int i = 0;i<3;i++) {
                    v+= voronoi(p, bass)*a;
                    p*=2.0;
                    a*=0.5;
                }
                return v;
            }
            
            float radialSin(in float x, in float height, in float up){
                return (sin(x+3*PI/2)+1)*height/2+up;
            }
            
            float modValue(in float x, in float height, in float up){
                return abs(mod(x, height)-height/2)+up;
            }
            
            float sdCircle( in vec2 p, in float radius )
            {
                return length(p) - radius*pow((0.8+bassFrVolume*0.2),2);
            }
            
            float opOnion( in vec2 p, in float radius, in float r, bool applySin, bool applyMod)
            {
                float x = p.x*200;
                float finalRadius = r;
                if(applySin) finalRadius *= radialSin(x, pow(bassFrVolume, 2.0)*2, 0.7f);
                if(applyMod) finalRadius *= modValue(x+uTime, pow(highFrVolume,2), 1.f);
                return abs(sdCircle(p, radius)) - finalRadius;
            }
            
            void main() {
                uv.y += 0.1;
                float d = opOnion(uv, maxRadius, circleThickness, true, true);
                vec3 color;
            
                if(colorTemplate==0) color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));
                else if(colorTemplate==1) {
                     color = mix(vec3(0.1568627450980392, 0.29411764705882354, 0.45098039215686275)*6, vec3(0.9019607843137255,0.2235294117647059,0.5294117647058824)*6, smoothstep(0.0, 0.5, ov(uv*5.0)));
                     d *= mix(0.2, 1, voronoi(uv*5.0)) * bassFrVolume;
                }
                else if(colorTemplate==2) {
                    color = mix(vec3(1,0,0.8)*2, vec3(2,0.0,0)*2, smoothstep(0.0, 0.5, ov(uv*5.0)));
                    d *= mix(0.2, 1, voronoi(uv*5.0)) * bassFrVolume;
                }
                else if(colorTemplate==3) {
                    color = mix(vec3(0,0,1)*2, vec3(1,0,0)*2, pow(smoothstep(0.0, 0.5, ov(uv)),0.5));
                    d *= mix(1, 0, smoothstep(0.0, 0.5, ov(uv*5.0)));
                }
                else if(colorTemplate==4) {
                    float noise = ov(uv, true);
                    d = opOnion(uv, maxRadius, (circleThickness+0.1)*bassFrVolume, false, true);
                    color = mix(vec3(0.2, 0.2, 0.7)*1.2, vec3(1.5,1.5,2)*3*bassFrVolume, smoothstep(0.0, 0.5, 1.0 - exp(-3.0*abs(noise))));
                    d *= mix(1, 0, smoothstep(0.0, 0.5, noise));
                    if (abs(bassFrVolume)>0.3) d -= 0.3*ov(uv*3, true);
                    else d -= 0.5*ov(uv*2);
                }
                else color = vec3(1);
            
                vec3 col = (d>0.0) ? vec3(0) : color;
                col *= 1.0 - exp(-6.0*abs(d));
            
                outColor = vec4(col, (d<=0.0));
            }
            """;

    public final static String MUSIC_HEART_FRAG_SHADER = """
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
            
            uniform int colorTemplate;
            
            out vec4 outColor;
            
            float dot2( in vec2 v ) { return dot(v,v); }
            
            #define PI 3.1415926535897932384626433832795
            
            vec2 hash2(vec2 p ) {
               return fract(sin(vec2(dot(p, vec2(123.4, 748.6)), dot(p, vec2(547.3, 659.3))))*5232.85324);
            }
            float hash(vec2 p) {
              return fract(sin(dot(p, vec2(43.232, 75.876)))*4526.3257);
            }
            
            float voronoi(vec2 p) {
                vec2 n = floor(p);
                vec2 f = fract(p);
                float md = 5.0;
                vec2 m = vec2(0.0);
                for (int i = -1;i<=1;i++) {
                    for (int j = -1;j<=1;j++) {
                        vec2 g = vec2(i, j);
                        vec2 o = hash2(n+g);
                        o = 0.5+0.5*sin(uTime+5.038*o);
                        vec2 r = g + o - f;
                        float d = dot(r, r);
                        if (d<md) {
                          md = d;
                          m = n+g+o;
                        }
                    }
                }
                return md;
            }
            
            float ov(vec2 p) {
                float v = 0.0;
                float a = 0.4;
                for (int i = 0;i<3;i++) {
                    v+= voronoi(p)*a;
                    p*=2.0;
                    a*=0.5;
                }
                return v;
            }
            
            float radialSin(in float x, in float height, in float up){
                return (sin(x+3*PI/2)+1)*height/2+up;
            }
            
            float sdHeart( in vec2 p )
            {
                p.x = abs(p.x);
            
                if( p.y+p.x>1.0 )
                    return sqrt(dot2(p-vec2(0.25,0.75))) - sqrt(2.0)/4.0;
                return sqrt(min(dot2(p-vec2(0.00,1.00)),
                                dot2(p-0.5*max(p.x+p.y,0.0)))) * sign(p.x-p.y);
            }
            
            void main() {
                vec2 uv = position.xy;
                uv.x *= ration;
            
                uv /= 1.2;
            
                uv /= bassFrVolume * 1.2;
                uv.y += 0.7;
            
                float x = uv.x*20+uTime*2;
                float volumeValue = pow(bassFrVolume, 2.0);
            
                float d = sdHeart(uv)*pow(radialSin(x, volumeValue, 0.8f), 2.f);
            
                vec3 col;
                if(colorTemplate == 0 ) { col = (d>0.0) ? vec3(0.2f) : vec3(1.0,0.0,0.0)*pow(bassFrVolume, 1.5f)*2.0;
                col *= 1.0 - exp(-20.0*abs(d));
                col = mix( col, vec3(0.1, 0.1, 0.1)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));
                }
                else if(colorTemplate == 1 ) {
                    vec3 colors = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4)+ov(uv*10.0));
                    vec3 noiseMix = mix(colors,vec3(0),smoothstep(0,0.5f,ov(uv*15.0)));
                    d += exp(-100.0*abs(d));
                    col = (d>0.0) ? vec3(0) : noiseMix*pow(bassFrVolume, 1.5f)*2.0;
                    col *= 1.0 - exp(-2.0*abs(d));
                    col = mix( col, vec3(0.2f)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));
                }
                else if(colorTemplate == 2 ) {
                vec3 gradient = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));
                col = (d>0.0) ? vec3(0) : vec3(0);
                        col *= 1.0 - exp(-5.0*abs(d));
                        col = mix( col, gradient*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
                }
                else if(colorTemplate == 3 ) {
                    col = (d>0.0) ? vec3(0) : mix(vec3(1,0,0),vec3(0),smoothstep(0,0.5f,ov(uv*5.0)))*pow(bassFrVolume, 1.5f)*2.0;
                            col *= 1.0 - exp(-5.0*abs(d));
                            col = mix( col, vec3(0.2, 0.2, 0.2)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
                }
                else if(colorTemplate==4) {
                        col = (d>0.0) ? vec3(0) : mix(vec3(1, 0.8156862745098039, 0),
                                    vec3(1.3,1.3,1.3),smoothstep(0,0.5f,ov(uv*5.0)))*pow(bassFrVolume, 1.5f)*2.0;
                                    col *= 1.0 - exp(-15.0*abs(d));
                                    col = mix( col, vec3(1, 0.8980392156862745, 0.45098039215686275)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));
                }
                else col = (d>0.0) ? vec3(0) : vec3(1);
            
                outColor = vec4(col, 1.f);
            }
            """;
}
