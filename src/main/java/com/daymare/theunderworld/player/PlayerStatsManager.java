package com.daymare.theunderworld.player;

import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.systems.items.ItemNamespacedKey;
import com.daymare.theunderworld.systems.potions.PotionEffect;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;

public class PlayerStatsManager {
    public static void updateData(Player p) {
        PlayerData data = Utils.getPlayerData(p);

        ItemStats tool = itemToItemStats(p.getInventory().getItemInMainHand(), "tool");
        ItemStats helmet = itemToItemStats(p.getInventory().getHelmet(), "armor");
        ItemStats chestplate = itemToItemStats(p.getInventory().getChestplate(), "armor");
        ItemStats leggings = itemToItemStats(p.getInventory().getLeggings(), "armor");
        ItemStats boots = itemToItemStats(p.getInventory().getBoots(), "armor");

        ItemStats finalStat = ItemStats.fuse(new ItemStats[] {tool, helmet, chestplate, leggings, boots});

        applyPotionEffects(finalStat, data.potionEffects);

        data.miningSpeed = finalStat.miningSpeed + 20;
        data.miningPower = finalStat.miningPower;
        data.miningFortune = finalStat.miningFortune;
        data.attackDamage = finalStat.attackDamage;
        data.attackSpeed = finalStat.attackSpeed;
    }

    private static void applyPotionEffects(ItemStats stats, HashMap<PotionEffect.PotionEffectType, PotionEffect> effectList) {
        if (effectList.containsKey(PotionEffect.PotionEffectType.HASTE)) {
            stats.miningSpeed += effectList.get(PotionEffect.PotionEffectType.HASTE).power * Underworld.getInstance().config().getInt("effects.haste");
        }
        if (effectList.containsKey(PotionEffect.PotionEffectType.PRISTINE_MINER)) {
            stats.miningFortune += effectList.get(PotionEffect.PotionEffectType.PRISTINE_MINER).power * Underworld.getInstance().config().getInt("effects.pristine_miner");
        }
    }

    private static class ItemStats {
        int miningSpeed;
        int miningPower;
        int miningFortune;
        int attackDamage;
        int attackSpeed;

        public static ItemStats fuse(ItemStats[] itemStats) {
            ItemStats newItem = new ItemStats();

            for (ItemStats i : itemStats) {
                newItem.miningSpeed += i.miningSpeed;
                newItem.miningFortune += i.miningFortune;
                newItem.miningPower += i.miningPower;
                newItem.attackDamage += i.attackDamage;
                newItem.attackSpeed += i.attackSpeed;
            }
            return newItem;
        }
    }

    private static ItemStats itemToItemStats(ItemStack itemStack, String expected) {
        ItemStats itemStats = new ItemStats();
        if (itemStack == null || itemStack.getItemMeta() == null) {
            return itemStats;
        }
        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
        if (!expected.equals(pdc.get(ItemNamespacedKey.INTERNAL_CATEGORY.nsk, PersistentDataType.STRING))) {
            return itemStats;
        }
        itemStats.miningPower = pdc.getOrDefault(ItemNamespacedKey.MINING_POWER.nsk, PersistentDataType.INTEGER, 0);
        itemStats.miningFortune = pdc.getOrDefault(ItemNamespacedKey.MINING_FORTUNE.nsk, PersistentDataType.INTEGER, 0);
        itemStats.miningSpeed = pdc.getOrDefault(ItemNamespacedKey.MINING_SPEED.nsk, PersistentDataType.INTEGER, 0);
        itemStats.attackDamage = pdc.getOrDefault(ItemNamespacedKey.ATTACK_DAMAGE.nsk, PersistentDataType.INTEGER, 0);
        itemStats.attackSpeed = pdc.getOrDefault(ItemNamespacedKey.ATTACK_SPEED.nsk, PersistentDataType.INTEGER, 0);
        return itemStats;
    }
}
