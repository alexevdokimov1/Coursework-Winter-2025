package Drawable;

import Render.ShaderDataStrings;

public class MusicCircle extends MusicPlane{

    public MusicCircle(){
        super(ShaderDataStrings.MUSIC_CIRCLE_FRAG_SHADER);
    }
}