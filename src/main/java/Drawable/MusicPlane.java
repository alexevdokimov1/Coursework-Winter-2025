package Drawable;

public class MusicPlane extends Plane{
    private float volume;
    private float sumVolume = 0.f;
    private float maxVolume;

    public MusicPlane(String fragmentFilePath){
        super(fragmentFilePath);
    }

    @Override
    protected void sendShaderData(){
        shader.upload1f("sumVolume", sumVolume);
        shader.upload1f("volume", volume);
        shader.upload1f("maxVolume", maxVolume);
    }

    public void setVolume(float volume) {
        this.sumVolume += volume;
        this.volume = volume;
    }

    public void setMaxVolume(float volume) {
       this.maxVolume = volume;
    }
}