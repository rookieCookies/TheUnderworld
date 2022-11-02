package com.daymare.theunderworld.systems.crafting;

import com.daymare.library.filemanager.Configuration;
import com.daymare.library.filemanager.FileID;
import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.systems.items.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CraftingTable implements Listener {
    private HashMap<String[], Recipe> recipes;

    public CraftingTable() {
        Configuration config = Underworld.getInstance().getFileManager().getFile(FileID.RECIPES).getConfiguration();
    }

    public ItemStack process(ItemStack[] items) {
        String[] key = convertToKey(items);
        Recipe recipe = recipes.get(key);
        if (recipe == null) {
            return new ItemStack(Material.AIR);
        }
        RecipeItem[] recipeItems = RecipeItem.itemStackToRecipeItem(items);
        RecipeItem result = recipe.validate(recipeItems);
        recipe.removeItems(recipeItems);
        for (int i = 0; i < items.length; i++) {
            items[i].setAmount(recipeItems[i].amount);
        }
        return ItemUtils.getItem(result.item, result.amount);
    }

    private String[] convertToKey(ItemStack[] items) {
        String[] key = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            key[i] = ItemUtils.getItemID(items[i]);
        }
        return key;
    }

    @EventHandler
    public void playerRightClickOnCraftingTable(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getPlayer().isSneaking() || e.getClickedBlock().getType() != Material.CRAFTING_TABLE) {
            return;
        }
        e.setCancelled(true);
        new CraftingTableGUI().openInventory(e.getPlayer());
    }
}
