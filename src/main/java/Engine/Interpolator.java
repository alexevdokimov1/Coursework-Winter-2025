package Engine;

public class Interpolator {

    public static float fastInterpolateFloatByAlpha(float a, float b, float alpha) {
        return a + alpha * (b - a);
    }

    public static float calculatePeriodByHz(float frequency){
        return 1.f/frequency;
    }
}