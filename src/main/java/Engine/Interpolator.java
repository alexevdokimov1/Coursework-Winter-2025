package Engine;

public class Interpolator {

    private float lastKnownVolume = 0.f;
    private float alpha = 0.f;
    private final float speed;
    private boolean isOn = true;

    public Interpolator(float interpolationSpeed){
        this.speed=interpolationSpeed;
    }

    public Interpolator(){
        this(1.f);
    }

    public static float linearInterpolate(float a, float b, float alpha) {
        return a + alpha * (b - a);
    }

    public float interpolate(float newValue){
        if(!isOn) return newValue;

        lastKnownVolume = Interpolator.linearInterpolate(newValue, lastKnownVolume, alpha);
        return lastKnownVolume;
    }

    public void update(float dt){
        if(alpha < 1) alpha+=dt*speed;
        else alpha = 0.f;
    }

    public void on(){
        this.isOn = true;
    }

    public void off(){
        this.isOn = false;
    }

    public boolean isOn(){
        return isOn;
    }
}