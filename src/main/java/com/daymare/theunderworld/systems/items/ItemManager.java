package com.daymare.theunderworld.systems.items;

import com.daymare.library.filemanager.Configuration;
import com.daymare.library.filemanager.FileID;
import com.daymare.library.filemanager.ManagedFile;
import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.utils.Range;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ItemManager {
    public HashMap<String, ItemStack> itemsInMemory = new HashMap<>();
    public HashMap<String, ItemDrop> itemDropsInMemory = new HashMap<>();
    private Configuration itemConfiguration;
    private Configuration dropsConfiguration;
    private Configuration langConfiguration;

    public void reload() {
        itemsInMemory.clear();
        itemDropsInMemory.clear();
        itemConfiguration = Underworld.getInstance().getFileManager().getFile(FileID.ITEMS).getConfiguration();
        dropsConfiguration = Underworld.getInstance().getFileManager().getFile(FileID.DROPS).getConfiguration();
        langConfiguration = Underworld.getInstance().getFileManager().getFile(FileID.LANG).getConfiguration();
    }

    public void loadItemDrop(String id) {
        Configuration config = dropsConfiguration.getConfiguration(id);

        Range range = new Range(config.getInt("min"), config.getInt("max"));
        double chance = config.getDouble("chance");
        String drop = config.getString("item");

        itemDropsInMemory.put(id, new ItemDrop(drop, range, chance));
    }

    public void loadItem(String id) {
        Configuration config = itemConfiguration.getConfiguration(id);

        // Grab all values from the config of the item
        Material material = Material.valueOf(config.getString("material", "STONE"));
        String itemType = config.getString("item-type", "item").toLowerCase(Locale.UK);
        String internalCategory = config.getString("internal-category", "item").toLowerCase(Locale.UK);
        String displayName = config.getString("display-name");
        config.setLog(false);
        List<String> abilities = config.getStringList("abilities");
        boolean hasUuid = config.getBoolean("has-uuid");
        String rarity = config.getString("rarity").toLowerCase(Locale.UK);
        int customModelData = config.getInt("cmd", config.getInt("custom-model-data", 0));
        List<String> configLore = config.getStringList("lore");

        // Construct the item
        ItemStack item = new ItemStack(material);

        // Apply rarity colour
        displayName = langConfiguration.getString("rarities."+rarity+".name-colour") + displayName;

        List<String> itemLore = new LinkedList<>();
        itemLore.add(langConfiguration.getString("item-type."+itemType));

        ItemMeta meta = item.getItemMeta();

        List<String> stats = compileStats(meta, config);
        if (!stats.isEmpty()) {
            itemLore.add("");
            itemLore.addAll(stats);
        }
        if (!configLore.isEmpty()) {
            itemLore.add("");
            itemLore.addAll(configLore);
        }
        itemLore.add("");
        itemLore.add(langConfiguration.getString("rarities."+rarity+".lore-text"));

        meta.setDisplayName(Utils.coloured(displayName));
        itemLore = Utils.coloured(itemLore);
        meta.setLore(itemLore);
        meta.setCustomModelData(customModelData);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (hasUuid) {
            pdc.set(ItemNamespacedKey.UUID.nsk, PersistentDataType.STRING, "undefined");
        }
        pdc.set(ItemNamespacedKey.ITEM_ID.nsk, PersistentDataType.STRING, id);
        pdc.set(ItemNamespacedKey.ITEM_TYPE.nsk, PersistentDataType.STRING, itemType);
        pdc.set(ItemNamespacedKey.INTERNAL_CATEGORY.nsk, PersistentDataType.STRING, internalCategory);

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        itemsInMemory.put(id, item);
    }

    private List<String> compileStats(ItemMeta meta, Configuration config) {
        List<String> lore = new LinkedList<>();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        config = config.getConfiguration("stats");

        List<String> sectionTwo = new LinkedList<>();

        /*
         We can just put the base lore in here since it is the first one
         This way we don't need to have a separate variable for section one
        */
        addStat(config, ItemNamespacedKey.ATTACK_DAMAGE.nsk, pdc, lore, "attack-damage");
        addStat(config, ItemNamespacedKey.ATTACK_SPEED.nsk, pdc, lore, "attack-speed");

        addStat(config, ItemNamespacedKey.MINING_POWER.nsk, pdc, sectionTwo, "mining-power");
        addStat(config, ItemNamespacedKey.MINING_SPEED.nsk, pdc, sectionTwo, "mining-speed");
        addStat(config, ItemNamespacedKey.MINING_FORTUNE.nsk, pdc, sectionTwo, "mining-fortune");

        if (!lore.isEmpty() && !sectionTwo.isEmpty()) {
            lore.add("");
            lore.addAll(sectionTwo);
        } else if (!sectionTwo.isEmpty()) {
            lore.addAll(sectionTwo);
        }

        return lore;
    }

    private void addStat(Configuration config, NamespacedKey key, PersistentDataContainer pdc, List<String> list, String id) {
        if (config.self().contains(id)) {
            list.add(langConfiguration.getString("stats." + id) + Underworld.getInstance().getNumberFormat().format(config.self().getInt(id)));
            pdc.set(key, PersistentDataType.INTEGER, config.self().getInt(id));
        }
    }

}
