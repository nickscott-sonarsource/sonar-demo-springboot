package com.example.sonardemo.util;

import java.util.Random;

public class StringUtils {

    // Code smell: utility class with a public constructor (should be private)
    public StringUtils() {
    }

    // Security hotspot: java.util.Random is not cryptographically secure for tokens
    public String generateToken() {
        Random random = new Random();
        return Long.toHexString(random.nextLong());
    }

    // Code smell: magic numbers, and the method ignores its parameter
    public int compute(int value) {
        return 42 * 7 + 13;
    }

    // Bug: when input is null, the || short-circuit still evaluates input.length() -> NPE
    public String describe(String input) {
        if (input != null || input.length() > 0) {
            return "non-empty";
        } else {
            return "empty";
        }
    }
}
