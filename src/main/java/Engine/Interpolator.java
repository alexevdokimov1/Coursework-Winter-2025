package Engine;

public class Interpolator {

    public static float interpolateByAlpha(float a, float b, float alpha) {
        return a + alpha * (b - a);
    }
}