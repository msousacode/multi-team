package com.multiteam.util;

import java.util.UUID;

public class ProvisinalPasswordUtils {

    public static String generate() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}
