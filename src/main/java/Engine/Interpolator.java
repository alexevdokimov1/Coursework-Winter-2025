package Engine;

public class Interpolator {

    private float lastKnownVolume = 0.f;
    private float alpha = 0.f;
    private final float speed;

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
        lastKnownVolume = Interpolator.linearInterpolate(newValue, lastKnownVolume, alpha);
        return lastKnownVolume;
    }

    public void update(float dt){
        if(alpha < 1) alpha+=dt*speed;
        else alpha = 0.f;
    }
}