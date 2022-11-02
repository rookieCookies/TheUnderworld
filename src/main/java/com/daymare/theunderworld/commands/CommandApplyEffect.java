package com.daymare.theunderworld.commands;

import com.daymare.theunderworld.Console;
import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.player.PlayerData;
import com.daymare.theunderworld.systems.items.ItemUtils;
import com.daymare.theunderworld.systems.potions.PotionEffect;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class CommandApplyEffect implements CommandExecutor, TabCompleter {
    private final String commandUsage = "/potion [player] [potion-effect] (duration = 30) (power = 1)";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.insufficient-arguments")
                    .replace("{arg_count}", "2")
                    .replace("{command_usage}", commandUsage)
            ));
            return true;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.target-player-not-online")));
            return true;
        }
        PotionEffect.PotionEffectType type;
        int duration = 30;
        int power = 1;

        try {
            type = PotionEffect.PotionEffectType.valueOf(args[1].toUpperCase(Locale.UK));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.invalid-potion-effect")));
            return true;
        }
        try {
            if (args.length >= 3) {
                duration = Integer.parseInt(args[2]);
                if (args.length >= 4) {
                    power = Integer.parseInt(args[3]);
                }
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.not-an-integer")));
        }

        PlayerData data = Utils.getPlayerData(targetPlayer);

        PotionEffect effect = new PotionEffect(type, duration, power);
        data.potionEffects.put(type, effect);

        targetPlayer.sendMessage(Utils.coloured(Utils.getLang().getString("messages.potion-effect-applied")
                .replace("{potion_effect}", effect.toString())
                .replace("{potion_duration}", Utils.toTime(effect.duration))
        ));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new LinkedList<>();
        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
        } else if (args.length == 2) {
            for (PotionEffect.PotionEffectType p : PotionEffect.PotionEffectType.values()) {
                list.add(p.toString().toLowerCase(Locale.UK));
            }
        }
        Console.info(args.length);
        return list;
    }
}