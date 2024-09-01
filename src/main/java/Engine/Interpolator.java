package Engine;

public class Interpolator {

    public static float interpolateFloatByAlpha(float a, float b, float alpha) {
        return a + alpha * (b - a);
    }
}