package com.daymare.theunderworld.systems.crafting;

import com.daymare.theunderworld.Underworld;
import net.minecraft.world.inventory.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingTableGUI implements Listener {
    private static List<Integer> craftingSlots = List.of(1, 2, 3, 10, 11, 12, 19, 20, 21);
    private final Inventory inv;

    public CraftingTableGUI() {
        inv = Bukkit.createInventory(null, 27, "§f\uF000\uF000\uF000\uF000\uF000\uF000\uF000\uF000\uF001");
        Underworld.getInstance().getServer().getPluginManager().registerEvents(this, Underworld.getInstance());
        initializeItems();
    }

    public void initializeItems() {

    }

    public void openInventory(final Player p) {
        p.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getClickedInventory() != inv)
            return;
        e.setCancelled(true);
        if (craftingSlots.contains(e.getRawSlot())) {
            e.setCancelled(false);
            e.getInventory().setItem(15, process(e.getInventory()));
        } else if (e.getRawSlot() == 15 && (e.getClick() == ClickType.LEFT && e.getCursor() == null)) {
            for (int i : craftingSlots) {
                e.getInventory().setItem(i, new ItemStack(Material.AIR));
            }
            e.setCancelled(false);
        }
        e.getWhoClicked().sendMessage(e.getRawSlot() + " " + e.getClick() + " " + e.getCursor().getType());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (!e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    private static ItemStack process(Inventory inventory) {
        ItemStack[] itemStacks = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            itemStacks[i] = inventory.getItem(craftingSlots.get(i));
        }

        return Underworld.getInstance().getCraftingTableManager().process(itemStacks);
    }
}
