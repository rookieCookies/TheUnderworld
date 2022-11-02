package com.daymare.theunderworld.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BetterSound {
    public Sound soundID;
    public int volume = 1;
    public int pitch = 1;

    public void playSound(Player p, Location at) {
        p.playSound(at, soundID, volume, pitch);
    }
}
