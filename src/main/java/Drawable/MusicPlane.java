package Drawable;

public class MusicPlane extends Plane{
    private float volume;
    private float sumVolume = 0.f;
    private float maxVolume;
    private int colorTemplate = 0;

    public MusicPlane(String fragmentFilePath){
        super(fragmentFilePath);
    }

    public MusicPlane(String fragmentFilePath, int colorTemplate){
        this(fragmentFilePath);
        this.colorTemplate = colorTemplate;
    }

    public MusicPlane(){
        this("Shader");
    }

    @Override
    protected void sendShaderData(){
        shader.upload1f("sumVolume", sumVolume);
        shader.upload1f("volume", volume);
        shader.upload1f("maxVolume", maxVolume);
        shader.upload1i("colorTemplate", colorTemplate);
    }

    public void setVolume(float volume) {
        this.sumVolume += volume;
        this.volume = volume;
    }

    public void setMaxVolume(float volume) {
       this.maxVolume = volume;
    }

    public void setColorTemplate(int number){
        this.colorTemplate=number;
    }

    public void switchColorTemplate() {
        if(colorTemplate<2) colorTemplate++;
        else colorTemplate=0;
    }
}