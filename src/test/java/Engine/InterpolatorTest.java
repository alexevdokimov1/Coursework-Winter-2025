package Engine;

import static org.junit.jupiter.api.Assertions.*;
import static Engine.Interpolator.*;
import org.junit.jupiter.api.Test;


public class InterpolatorTest {
    @Test
    public void checkFunctions(){

        assertEquals(interpolateByAlpha(5,21,0.5f),13.f);

        assertEquals(interpolateByAlpha(56,31,0.5f),43.5f);
    }
}