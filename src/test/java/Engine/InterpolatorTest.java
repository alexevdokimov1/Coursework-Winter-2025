package Engine;

import static org.junit.jupiter.api.Assertions.*;
import static Engine.Interpolator.*;
import org.junit.jupiter.api.Test;


public class InterpolatorTest {
    @Test
    public void checkFunctions(){

        assertEquals(13.f, linearInterpolate(5,21,0.5f));

        assertEquals(43.5f, linearInterpolate(56,31,0.5f));
    }
}