package Drawable;

public class MusicCircle extends Plane{
    private float volume;

    public MusicCircle(){
        super("MusicCircle");
    }

    @Override
    protected void sendShaderData(){
        shader.upload1f("volume", volume);
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}