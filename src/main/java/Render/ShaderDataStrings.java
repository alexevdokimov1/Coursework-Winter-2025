package Render;

public class ShaderDataStrings {
    public final static String DEFAULT_FRAG_SHADER = "#version 330\n" +
            "#ifdef GL_ES\n" +
            "    precision mediump float;\n" +
            "#endif\n" +
            "uniform float uTime;\n" +
            "in vec4 position;\n" +
            "uniform float ration;\n" +
            "uniform float dt;\n" +
            "uniform float bassFrVolume;\n" +
            "uniform float middleFrVolume;\n" +
            "uniform float highFrVolume;\n" +
            "out vec4 outColor;\n" +
            "void main() {\n" +
            "    vec2 uv = position.xy;\n" +
            "    vec3 color = mix(vec3(0.56, 0.0, 0.16), vec3(0.0, 0.0, 1.0), bassFrVolume );\n" +
            "    outColor = vec4(color,1);\n" +
            "}";
    public final static String DEFAULT_VERT_SHADER = "#version 330\n" +
            "uniform mat4 uProjection;\n" +
            "uniform mat4 uView;\n" +
            "attribute vec4  mcVertex;\n" +
            "out vec4 position;\n" +
            "void main(){\n" +
            "    position = uProjection * uView * mcVertex;\n" +
            "    gl_Position = position;\n" +
            "}";
    public final static String MUSIC_CIRCLE_FRAG_SHADER = "#version 330\n" +
            "#ifdef GL_ES\n" +
            "    precision mediump float;\n" +
            "#endif\n" +
            "uniform float uTime;\n" +
            "in vec4 position;\n" +
            "uniform float ration;\n" +
            "uniform float dt;\n" +
            "\n" +
            "uniform float bassFrVolume;\n" +
            "uniform float middleFrVolume;\n" +
            "uniform float highFrVolume;\n" +
            "\n" +
            "uniform int colorTemplate;\n" +
            "\n" +
            "out vec4 outColor;\n" +
            "\n" +
            "const float maxRadius = 0.7f;\n" +
            "const float circleThickness = 0.1f;\n" +
            "\n" +
            "vec2 uv = position.xy * vec2(ration, 1.f);\n" +
            "\n" +
            "#define PI 3.1415926535897932384626433832795\n" +
            "\n" +
            "vec2 hash2(vec2 p ) {\n" +
            "   return fract(sin(vec2(dot(p, vec2(123.4, 748.6)), dot(p, vec2(547.3, 659.3))))*5232.85324);\n" +
            "}\n" +
            "float hash(vec2 p) {\n" +
            "  return fract(sin(dot(p, vec2(43.232, 75.876)))*4526.3257);\n" +
            "}\n" +
            "\n" +
            "float voronoi(vec2 p) {\n" +
            "    vec2 n = floor(p);\n" +
            "    vec2 f = fract(p);\n" +
            "    float md = 5.0;\n" +
            "    vec2 m = vec2(0.0);\n" +
            "    for (int i = -1;i<=1;i++) {\n" +
            "        for (int j = -1;j<=1;j++) {\n" +
            "            vec2 g = vec2(i, j);\n" +
            "            vec2 o = hash2(n+g);\n" +
            "            o = 0.5+0.5*sin(uTime+5.038*o);\n" +
            "            vec2 r = g + o - f;\n" +
            "            float d = dot(r, r);\n" +
            "            if (d<md) {\n" +
            "              md = d;\n" +
            "              m = n+g+o;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "    return md*bassFrVolume;\n" +
            "}\n" +
            "\n" +
            "float ov(vec2 p) {\n" +
            "    float v = 0.0;\n" +
            "    float a = 0.4;\n" +
            "    for (int i = 0;i<3;i++) {\n" +
            "        v+= voronoi(p)*a;\n" +
            "        p*=2.0;\n" +
            "        a*=0.5;\n" +
            "    }\n" +
            "    return v;\n" +
            "}\n" +
            "\n" +
            "float radialSin(in float x, in float height, in float up){\n" +
            "    return (sin(x+3*PI/2)+1)*height/2+up;\n" +
            "}\n" +
            "\n" +
            "float modValue(in float x, in float height, in float up){\n" +
            "    return abs(mod(x, height)-height/2)+up;\n" +
            "}\n" +
            "\n" +
            "float sdCircle( in vec2 p, in float radius )\n" +
            "{\n" +
            "    return length(p) - radius*pow((0.8+bassFrVolume*0.2),2);\n" +
            "}\n" +
            "\n" +
            "float opOnion( in vec2 p, in float radius, in float r, bool applySin, bool applyMod)\n" +
            "{\n" +
            "    float x = p.x*200;\n" +
            "    float finalRadius = r;\n" +
            "    if(applySin) finalRadius *= radialSin(x, pow(bassFrVolume, 2.0)*2, 0.7f);\n" +
            "    if(applyMod) finalRadius *= modValue(x+uTime, pow(highFrVolume,2), 1.f);\n" +
            "    return abs(sdCircle(p, radius)) - finalRadius;\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    uv.y += 0.1;\n" +
            "    float d = opOnion(uv, maxRadius, circleThickness, true, true);\n" +
            "    vec3 color;\n" +
            "\n" +
            "    if(colorTemplate==0) color = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));\n" +
            "    else if(colorTemplate==1) {\n" +
            "         color = mix(vec3(0.1568627450980392, 0.29411764705882354, 0.45098039215686275)*6, vec3(0.9019607843137255,0.2235294117647059,0.5294117647058824)*6, smoothstep(0.0, 0.5, ov(uv*5.0)));\n" +
            "         d *= mix(0.2, 1, voronoi(uv*5.0)) * bassFrVolume;\n" +
            "    }\n" +
            "    else if(colorTemplate==2) {\n" +
            "        color = mix(vec3(1,0,0.8)*2, vec3(2,0.0,0)*2, smoothstep(0.0, 0.5, ov(uv*5.0)));\n" +
            "        d *= mix(0.2, 1, voronoi(uv*5.0)) * bassFrVolume;\n" +
            "    }\n" +
            "    else if(colorTemplate==3) {\n" +
            "        color = mix(vec3(0,0,1)*2, vec3(1,0,0)*2, pow(smoothstep(0.0, 0.5, ov(uv*1.0)),0.5));\n" +
            "        d *= mix(1, 0, smoothstep(0.0, 0.5, ov(uv*5.0)));\n" +
            "    }\n" +
            "    else if(colorTemplate==4) {\n" +
            "        d = opOnion(uv, maxRadius, (circleThickness+0.1)*bassFrVolume, false, false);\n" +
            "        color = mix(vec3(0, 0.5156862745098039, 0.7), vec3(2,2,2)*3*bassFrVolume, smoothstep(0.0, 0.5, 1.0 - exp(-3.0*abs(ov(uv)))));\n" +
            "        d *= mix(1, 0, smoothstep(0.0, 0.5, ov(uv)));\n" +
            "    }\n" +
            "    else color = vec3(1);\n" +
            "\n" +
            "    vec3 col = (d>0.0) ? vec3(0) : color;\n" +
            "    col *= 1.0 - exp(-6.0*abs(d));\n" +
            "\n" +
            "    outColor = vec4(col, (d<=0.0));\n" +
            "}";

    public final static String MUSIC_HEART_FRAG_SHADER ="#version 330\n" +
            "#ifdef GL_ES\n" +
            "    precision mediump float;\n" +
            "#endif\n" +
            "uniform float uTime;\n" +
            "in vec4 position;\n" +
            "uniform float ration;\n" +
            "uniform float dt;\n" +
            "\n" +
            "uniform float bassFrVolume;\n" +
            "uniform float middleFrVolume;\n" +
            "uniform float highFrVolume;\n" +
            "\n" +
            "uniform int colorTemplate;\n" +
            "\n" +
            "out vec4 outColor;\n" +
            "\n" +
            "float dot2( in vec2 v ) { return dot(v,v); }\n" +
            "\n" +
            "#define PI 3.1415926535897932384626433832795\n" +
            "\n" +
            "\n" +
            "vec2 hash2(vec2 p ) {\n" +
            "   return fract(sin(vec2(dot(p, vec2(123.4, 748.6)), dot(p, vec2(547.3, 659.3))))*5232.85324);\n" +
            "}\n" +
            "float hash(vec2 p) {\n" +
            "  return fract(sin(dot(p, vec2(43.232, 75.876)))*4526.3257);\n" +
            "}\n" +
            "\n" +
            "float voronoi(vec2 p) {\n" +
            "    vec2 n = floor(p);\n" +
            "    vec2 f = fract(p);\n" +
            "    float md = 5.0;\n" +
            "    vec2 m = vec2(0.0);\n" +
            "    for (int i = -1;i<=1;i++) {\n" +
            "        for (int j = -1;j<=1;j++) {\n" +
            "            vec2 g = vec2(i, j);\n" +
            "            vec2 o = hash2(n+g);\n" +
            "            o = 0.5+0.5*sin(uTime+5.038*o);\n" +
            "            vec2 r = g + o - f;\n" +
            "            float d = dot(r, r);\n" +
            "            if (d<md) {\n" +
            "              md = d;\n" +
            "              m = n+g+o;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "    return md;\n" +
            "}\n" +
            "\n" +
            "float ov(vec2 p) {\n" +
            "    float v = 0.0;\n" +
            "    float a = 0.4;\n" +
            "    for (int i = 0;i<3;i++) {\n" +
            "        v+= voronoi(p)*a;\n" +
            "        p*=2.0;\n" +
            "        a*=0.5;\n" +
            "    }\n" +
            "    return v;\n" +
            "}\n" +
            "\n" +
            "float radialSin(in float x, in float height, in float up){\n" +
            "    return (sin(x+3*PI/2)+1)*height/2+up;\n" +
            "}\n" +
            "\n" +
            "float sdHeart( in vec2 p )\n" +
            "{\n" +
            "    p.x = abs(p.x);\n" +
            "\n" +
            "    if( p.y+p.x>1.0 )\n" +
            "        return sqrt(dot2(p-vec2(0.25,0.75))) - sqrt(2.0)/4.0;\n" +
            "    return sqrt(min(dot2(p-vec2(0.00,1.00)),\n" +
            "                    dot2(p-0.5*max(p.x+p.y,0.0)))) * sign(p.x-p.y);\n" +
            "}\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 uv = position.xy;\n" +
            "    uv.x *= ration;\n" +
            "\n" +
            "    uv /= 1.2;\n" +
            "\n" +
            "    uv /= bassFrVolume * 1.2;\n" +
            "    uv.y += 0.7;\n" +
            "\n" +
            "    float x = uv.x*20+uTime*2;\n" +
            "    float volumeValue = pow(bassFrVolume, 2.0);\n" +
            "\n" +
            "    float d = sdHeart(uv)*pow(radialSin(x, volumeValue, 0.8f), 2.f);\n" +
            "\n" +
            "    vec3 col;\n" +
            "    if(colorTemplate == 0 ) { col = (d>0.0) ? vec3(0.2f) : vec3(1.0,0.0,0.0)*pow(bassFrVolume, 1.5f)*2.0;\n" +
            "    col *= 1.0 - exp(-20.0*abs(d));\n" +
            "    col = mix( col, vec3(0.1, 0.1, 0.1)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));\n" +
            "    }\n" +
            "    else if(colorTemplate == 1 ) {\n" +
            "        vec3 colors = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4)+ov(uv*10.0));\n" +
            "        vec3 noiseMix = mix(colors,vec3(0),smoothstep(0,0.5f,ov(uv*15.0)));\n" +
            "        d += exp(-100.0*abs(d));\n" +
            "        col = (d>0.0) ? vec3(0) : noiseMix*pow(bassFrVolume, 1.5f)*2.0;\n" +
            "        col *= 1.0 - exp(-2.0*abs(d));\n" +
            "        col = mix( col, vec3(0.2f)*bassFrVolume, 1.0-smoothstep(0.0,0.05,abs(d)));\n" +
            "    }\n" +
            "    else if(colorTemplate == 2 ) {\n" +
            "    vec3 gradient = 0.5 + 0.5 * cos(uTime+uv.xyx+vec3(0,2,4));\n" +
            "    col = (d>0.0) ? vec3(0) : vec3(0);\n" +
            "            col *= 1.0 - exp(-5.0*abs(d));\n" +
            "            col = mix( col, gradient*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));\n" +
            "    }\n" +
            "    else if(colorTemplate == 3 ) {\n" +
            "        col = (d>0.0) ? vec3(0) : mix(vec3(1,0,0),vec3(0),smoothstep(0,0.5f,ov(uv*5.0)))*pow(bassFrVolume, 1.5f)*2.0;\n" +
            "                col *= 1.0 - exp(-5.0*abs(d));\n" +
            "                col = mix( col, vec3(0.2, 0.2, 0.2)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));\n" +
            "    }\n" +
            "    else if(colorTemplate==4) {\n" +
            "            col = (d>0.0) ? vec3(0) : mix(vec3(1, 0.8156862745098039, 0),\n" +
            "                        vec3(1.3,1.3,1.3),smoothstep(0,0.5f,ov(uv*5.0)))*pow(bassFrVolume, 1.5f)*2.0;\n" +
            "                        col *= 1.0 - exp(-15.0*abs(d));\n" +
            "                        col = mix( col, vec3(1, 0.8980392156862745, 0.45098039215686275)*bassFrVolume, 1.0-smoothstep(0.0,0.1,abs(d)));\n" +
            "    }\n" +
            "    else col = (d>0.0) ? vec3(0) : vec3(1);\n" +
            "\n" +
            "    outColor = vec4(col, 1.f);\n" +
            "}";
}
