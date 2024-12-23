package Drawable;

public class MusicPlane extends Plane{
    private float bassFrVolume;
    private float middleFrVolume;
    private float highFrVolume;
    private int colorTemplate = 0;

    public MusicPlane(String fragmentSource){
        super(fragmentSource);
    }

    @Override
    protected void sendShaderData(){
        shader.upload1f("highFrVolume", highFrVolume);
        shader.upload1f("middleFrVolume", middleFrVolume);
        shader.upload1f("bassFrVolume", bassFrVolume);
        shader.upload1i("colorTemplate", colorTemplate);
    }

    public void setBassVolume(float volume) {
        this.bassFrVolume = volume;
    }

    public void setMiddleVolume(float volume) {
        this.middleFrVolume = volume;
    }

    public void setHighVolume(float volume) {
        this.highFrVolume = volume;
    }

    public void setColorTemplate(int number){
        this.colorTemplate=number;
    }
}