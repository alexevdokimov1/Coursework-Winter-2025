package Engine;

public class Interpolator {

    public static float fastInterpolateFloatByAlpha(float a, float b, float alpha) {
        return a + alpha * (b - a);
    }
}