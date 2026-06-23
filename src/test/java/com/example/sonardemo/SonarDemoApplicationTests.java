package com.example.sonardemo;

import com.example.sonardemo.util.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SonarDemoApplicationTests {

    @Test
    void computeReturnsFixedValue() {
        StringUtils utils = new StringUtils();
        assertEquals(307, utils.compute(0));
    }

    @Test
    void describeReturnsNonEmpty() {
        StringUtils utils = new StringUtils();
        assertEquals("non-empty", utils.describe("hello"));
    }
}
