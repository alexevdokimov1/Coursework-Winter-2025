package Engine;

public class Interpolator {

    private float lastKnownVolume = 0.f;
    private float alpha = 0.f;

    public Interpolator(){
    }

    public static float linearInterpolate(float a, float b, float alpha) {
        return a + alpha * (b - a);
    }

    public float interpolate(float newValue){
        lastKnownVolume = Interpolator.linearInterpolate(newValue, lastKnownVolume, alpha);
        return lastKnownVolume;
    }

    public void update(float dt){
        if(alpha < 1) alpha+=dt;
        else alpha = 0.f;
    }
}