package Engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmoothBalancerTest {
    @Test
    public void check(){

        SmoothBalancer test = new SmoothBalancer();
        test.addValue(5.f);
        test.addValue(8.f);
        test.addValue(13.f);
        test.addValue(9.f);
        test.addValue(6.f);
        assertEquals(test.getAvg(), 8.2f);
    }
}