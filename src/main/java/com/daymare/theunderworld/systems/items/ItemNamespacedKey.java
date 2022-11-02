package com.daymare.theunderworld.systems.items;

import com.daymare.theunderworld.Underworld;
import org.bukkit.NamespacedKey;

public enum ItemNamespacedKey {
    UUID("uuid"),
    ITEM_ID("item_id"),
    ITEM_TYPE("item_type"),
    INTERNAL_CATEGORY("category"),

    ATTACK_DAMAGE("item_stat.attack_damage"),
    ATTACK_SPEED("item_stat.attack_speed"),

    MINING_POWER("item_stat.mining_power"),
    MINING_FORTUNE("item_stat.mining_fortune"),
    MINING_SPEED("item_stat.mining_speed");

    public final NamespacedKey nsk;
    ItemNamespacedKey(String s) {
        nsk = new NamespacedKey(Underworld.getInstance(), s);
    }
}
