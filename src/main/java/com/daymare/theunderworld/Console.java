package com.daymare.theunderworld;

public class Console {
    public static void error(String s) {
        Underworld.getInstance().getLogger().severe(s);
    }
    public static void info(String s) {
        Underworld.getInstance().getLogger().info(s);
    }
    public static void info(int s) {
        info(String.valueOf(s));
    }
}
