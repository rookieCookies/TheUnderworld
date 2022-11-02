package com.daymare.theunderworld.commands;

import com.daymare.theunderworld.systems.items.ItemUtils;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveItem implements CommandExecutor {
    private final String commandUsage = "/giveitem [player] [item-id]";

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

        targetPlayer.getInventory().addItem(ItemUtils.getItem(args[1]));
        return true;
    }
}