package com.daymare.theunderworld.systems.items;

import com.daymare.theunderworld.Underworld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ItemUtils {
    public static String getItemID(ItemStack item) {
        if (item.getItemMeta() == null) {
            return null;
        }
        return item.getItemMeta().getPersistentDataContainer().get(ItemNamespacedKey.ITEM_ID.nsk, PersistentDataType.STRING);
    }

    public static ItemStack getItem(String id, int amount) {
        if (id == null) {
            return null;
        }
        ItemManager itemManager = Underworld.getInstance().getItemManager();
        if (!itemManager.itemsInMemory.containsKey(id)) {
            itemManager.loadItem(id);
        }
        ItemStack item = itemManager.itemsInMemory.get(id);
        if (item.getItemMeta().getPersistentDataContainer().has(ItemNamespacedKey.UUID.nsk, PersistentDataType.STRING)) {
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(ItemNamespacedKey.UUID.nsk, PersistentDataType.STRING, UUID.randomUUID().toString());
            item.setItemMeta(meta);
        }
        item.setAmount(amount);
        return item;
    }

    public static ItemDrop getItemDrop(String id) {
        if (id == null) {
            return null;
        }
        ItemManager itemManager = Underworld.getInstance().getItemManager();
        if (!itemManager.itemDropsInMemory.containsKey(id)) {
            itemManager.loadItemDrop(id);
        }
        return itemManager.itemDropsInMemory.get(id);
    }

    public static ItemStack getItem(String id) {
        return getItem(id, 1);
    }

    static EquipmentSlot[] equipmentSlots = {
            EquipmentSlot.HAND,
            EquipmentSlot.OFF_HAND,
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    public static void updatePlayerInventory(Player p) {
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            p.getInventory().setItem(i, getUpdatedItem(p.getInventory().getItem(i)));
        }
        for (EquipmentSlot s : equipmentSlots) {
            p.getInventory().setItem(s, getUpdatedItem(p.getInventory().getItem(s)));
        }
    }

    private static ItemStack getUpdatedItem(ItemStack itemStack) {
        if (itemStack == null) {
            return new ItemStack(Material.AIR);
        }
        return getItem(getItemID(itemStack), itemStack.getAmount());
    }

}
