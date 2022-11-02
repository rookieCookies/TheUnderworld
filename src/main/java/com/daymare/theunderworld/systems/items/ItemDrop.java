package com.daymare.theunderworld.systems.items;

import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.utils.Range;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemDrop {
    final String item;
    final Range amount;
    final double chance;


    public ItemDrop(String item, Range amount, double chance) {
        this.item = item;
        this.amount = amount;
        this.chance = chance;
    }

    public ItemStack get() {
        return Underworld.getInstance().getRandom().nextDouble() <= chance ? ItemUtils.getItem(item, amount.random()) : new ItemStack(Material.AIR);
    }
}
