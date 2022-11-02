package com.daymare.theunderworld.player;

import com.daymare.theunderworld.systems.potions.PotionEffect;
import org.bukkit.entity.Player;

public class PlayerUpdate {
    public static void update(Player p) {
        PlayerStatsManager.updateData(p);
        PotionEffect.tick(p);
    }
}
