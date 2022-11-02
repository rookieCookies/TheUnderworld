package com.daymare.theunderworld.systems.mining;

import com.daymare.library.filemanager.Configuration;
import com.daymare.library.filemanager.FileID;
import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.player.PlayerData;
import com.daymare.theunderworld.systems.items.ItemDrop;
import com.daymare.theunderworld.systems.items.ItemUtils;
import com.daymare.theunderworld.utils.BetterSound;
import com.daymare.theunderworld.utils.Utils;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;

public class Mining implements Listener {
    private HashMap<String, MiningBlock> blocks = new HashMap<>();

    public Mining() {
        Configuration configuration = Underworld.getInstance().getFileManager().getFile(FileID.BLOCKS).getConfiguration().getConfiguration("material");
        for (String i : configuration.getKeys()) {
            Configuration blockConfig = configuration.getConfiguration(i);
            MiningBlock miningBlock = new MiningBlock();
            miningBlock.powerRequired = blockConfig.getInt("power");
            miningBlock.hardness = blockConfig.getInt("hardness");
            miningBlock.maxDamagePerSwing = blockConfig.getInt("max-swing-damage");
            miningBlock.drop = blockConfig.getString("drop");
            BetterSound sound = new BetterSound();
            sound.soundID = Sound.valueOf(blockConfig.getString("sound").toUpperCase(Locale.ROOT));
            blockConfig.setLog(false);
            sound.volume = blockConfig.getInt("volume", 1);
            sound.pitch = blockConfig.getInt("pitch", 1);
            miningBlock.sound = sound;
            blocks.put(i, miningBlock);
        }
    }

    @EventHandler
    public void blockBreakStart(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        stopMining(e.getPlayer());
        PlayerData data = Utils.getPlayerData(e.getPlayer());
        String id = e.getClickedBlock().getType().toString().toLowerCase();
        if (!blocks.containsKey(id)) {
            return;
        }
        if (data.miningPower < blocks.get(id).powerRequired) {
            e.getPlayer().sendMessage("Your pickaxe is not powerful enough to mine this block");
            return;
        }
        data.miningTargetBlock = e.getClickedBlock().getLocation();
        data.miningTargetBlockID = id;
        data.miningWorker = Bukkit.getScheduler().runTaskTimer(Underworld.getInstance(), () -> incrementAndUpdateMining(e.getPlayer()), 0L, 2L);
    }

    @EventHandler
    public void blockDamageEvent(BlockDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void blockBreakAbort(BlockDamageAbortEvent e) {
        stopMining(e.getPlayer());
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        e.setDropItems(false);
    }

    private void stopMining(Player p) {
        PlayerData data = Utils.getPlayerData(p);
        if (data.miningTargetBlock == null) {
            return;
        }
        data.miningCurrentProgress = 0;
        updateMining(p);
        data.miningTargetBlock = null;
        data.miningWorker.cancel();
    }

    public void incrementAndUpdateMining(Player p) {
        PlayerData data = Utils.getPlayerData(p);
        MiningBlock targetBlock = blocks.get(data.miningTargetBlockID);
        data.miningCurrentProgress = Math.min(data.miningCurrentProgress + Math.min(data.miningSpeed, targetBlock.maxDamagePerSwing == -1 ? Integer.MAX_VALUE : targetBlock.maxDamagePerSwing), targetBlock.hardness);
        updateMining(p);
    }

    public void updateMining(Player p) {
        PlayerData data = Utils.getPlayerData(p);
        MiningBlock targetBlock = blocks.get(data.miningTargetBlockID);
        sendBlockDamage(p, data.miningCurrentProgress == 0 ? -1 : (float) data.miningCurrentProgress / targetBlock.hardness, data.miningTargetBlock);
        if (data.miningCurrentProgress != 0 && data.miningCurrentProgress == targetBlock.hardness) {
            p.breakBlock(p.getWorld().getBlockAt(data.miningTargetBlock));
            targetBlock.sound.playSound(p, data.miningTargetBlock);
            if (targetBlock.drop != null) {
                int amount = data.miningFortune / 100 + 1;
                for (int i = 0; i < amount; i++) {
                    p.getWorld().dropItem(data.miningTargetBlock.add(0, 0.5, 0.5), ItemUtils.getItemDrop(targetBlock.drop).get());
                }
                if (Underworld.getInstance().getRandom().nextDouble() <= data.miningFortune % 100 * 0.01) {
                    p.getWorld().dropItem(data.miningTargetBlock.add(0, 0.5, 0.5), ItemUtils.getItemDrop(targetBlock.drop).get());
                }
            }
            stopMining(p);
        }
    }

    private void sendBlockDamage(Player p, float progress, Location loc) {
        int stage = (int)(9.0F * progress);
        BlockPosition blockLoc = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(blockLoc.hashCode(), blockLoc, stage);
        ((CraftPlayer)p).getHandle().b.a(packet);
    }
}
