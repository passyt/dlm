package com.derbysoft.nuke.dlm.utils;

/**
 * Created by passyt on 16-9-4.
 */
public class ProtoBufUtils {

    public static String safeValueOf(String value) {
        return value == null ? "" : value;
    }

    public static int safeValueOf(Integer value) {
        return value == null ? 0 : value;
    }

    public static long safeValueOf(Long value) {
        return value == null ? 0 : value;
    }

    public static float safeValueOf(Float value) {
        return value == null ? 0 : value;
    }

    public static double safeValueOf(Double value) {
        return value == null ? 0 : value;
    }

    public static <T extends Enum> T safeValueOf(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
