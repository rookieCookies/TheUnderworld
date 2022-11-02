package com.daymare.theunderworld.player;

import com.daymare.theunderworld.systems.potions.PotionEffect;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class PlayerData {
    // Stats

    public int attackDamage = 0;
    public int attackSpeed = 0;
    public int armor = 0;
    public int health = 0;
    public int miningSpeed = 0;
    public int miningPower = 0;
    public int miningFortune = 0;


    // Mining
    public Location miningTargetBlock = null;
    public int miningCurrentProgress = 0;
    public String miningTargetBlockID = null;
    public BukkitTask miningWorker = null;


    public BukkitTask updateWorker = null;

    // Other
    public HashMap<PotionEffect.PotionEffectType, PotionEffect> potionEffects = new HashMap<>(0);
}
