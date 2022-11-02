package com.daymare.theunderworld.player;

import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.systems.items.ItemNamespacedKey;
import com.daymare.theunderworld.systems.items.ItemUtils;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEvents implements Listener {
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        registerPlayer(e.getPlayer());
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, -1, false, false));
        ItemUtils.updatePlayerInventory(e.getPlayer());
        PlayerStatsManager.updateData(e.getPlayer());
    }

    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        unregisterPlayer(e.getPlayer());
    }

    @EventHandler
    public void playerBlockPlaceEvent(BlockPlaceEvent e) {
        if (!"block".equals(e.getItemInHand().getItemMeta().getPersistentDataContainer().get(ItemNamespacedKey.ITEM_TYPE.nsk, PersistentDataType.STRING)) && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerItemChange(PlayerItemHeldEvent e) {
        Bukkit.getScheduler().runTaskLater(Underworld.getInstance(), () -> PlayerStatsManager.updateData(e.getPlayer()), 1);
    }

    @EventHandler
    public void playerItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p) {
            PlayerStatsManager.updateData(p);
        }
    }

    @EventHandler
    public void playerInventoryEvent(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            PlayerStatsManager.updateData(p);
        }
    }

    private void registerPlayer(Player p) {
        Underworld.getInstance().getPlayerDataHashMap().put(p.getUniqueId(), new PlayerData());
        Utils.getPlayerData(p).updateWorker = Bukkit.getScheduler().runTaskTimer(Underworld.getInstance(), () -> PlayerUpdate.update(p), 0L, 20L);
        System.out.println("Registered player " + p.getName() + " there are now a total of " + Underworld.getInstance().getPlayerDataHashMap().size() + " players");
    }

    private void unregisterPlayer(Player p) {
        PlayerData data = Underworld.getInstance().getPlayerDataHashMap().remove(p.getUniqueId());
        data.miningWorker.cancel();
        data.updateWorker.cancel();
    }
}
