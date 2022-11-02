package com.daymare.theunderworld;

import com.daymare.library.filemanager.Configuration;
import com.daymare.library.filemanager.FileID;
import com.daymare.library.filemanager.FileManager;
import com.daymare.theunderworld.commands.CommandApplyEffect;
import com.daymare.theunderworld.commands.CommandGiveItem;
import com.daymare.theunderworld.commands.CommandSetPlayerData;
import com.daymare.theunderworld.player.PlayerData;
import com.daymare.theunderworld.player.PlayerEvents;
import com.daymare.theunderworld.systems.crafting.CraftingTable;
import com.daymare.theunderworld.systems.mining.Mining;
import com.daymare.theunderworld.systems.items.ItemManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.NumberFormat;
import java.util.*;

public final class Underworld extends JavaPlugin {
    private static Underworld instance;
    private final Map<UUID, PlayerData> playerDataHashMap = new HashMap<>();
    private final FileManager fileManager = new FileManager(this);
    private final ItemManager itemManager = new ItemManager();
    private final CraftingTable craftingTableManager = new CraftingTable();
    private final Random random = new Random();
    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);

    @Override
    public void onEnable() {
        instance = this;
        registerFiles();
        registerEvents();
        registerCommands();
        itemManager.reload();
    }

    private void registerFiles() {
        fileManager.addFile(FileID.CONFIG, fileManager.create(null, "config.yml"));
        fileManager.addFile(FileID.ITEMS, fileManager.create(null, "items.yml"));
        fileManager.addFile(FileID.DROPS, fileManager.create(null, "drops.yml"));
        fileManager.addFile(FileID.BLOCKS, fileManager.create(null, "blocks.yml"));
        fileManager.addFile(FileID.LANG, fileManager.create(null, "language.yml"));
        fileManager.addFile(FileID.RECIPES, fileManager.create(null, "recipes.yml"));
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new CraftingTable(), this);
        pluginManager.registerEvents(new PlayerEvents(), this);
        pluginManager.registerEvents(new Mining(), this);
    }

    private void registerCommands() {
        getCommand("setdata").setExecutor(new CommandSetPlayerData());
        getCommand("giveitem").setExecutor(new CommandGiveItem());
        getCommand("potion").setExecutor(new CommandApplyEffect());
        getCommand("potion").setTabCompleter(new CommandApplyEffect());
    }

    public FileManager getFileManager() {
        return fileManager;
    }
    public static Underworld getInstance() {
        return instance;
    }
    public Map<UUID, PlayerData> getPlayerDataHashMap() {
        return playerDataHashMap;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public Random getRandom() {
        return random;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public CraftingTable getCraftingTableManager() {
        return craftingTableManager;
    }

    public Configuration config() {
        return fileManager.getFile(FileID.CONFIG).getConfiguration();
    }
}
