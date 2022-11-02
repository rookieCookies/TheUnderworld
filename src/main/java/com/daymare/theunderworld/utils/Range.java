package com.daymare.theunderworld.utils;

import com.daymare.theunderworld.Underworld;

public class Range {
    public int min;
    public int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int random() {
        return Underworld.getInstance().getRandom().nextInt(max - min) + min;
    }
}
