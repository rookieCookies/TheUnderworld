package com.daymare.theunderworld.commands;

import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.player.PlayerData;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class  CommandSetPlayerData implements CommandExecutor {
    private static final String commandUsage = "/setdata [player] [data type] [value]";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.insufficient-arguments")
                    .replace("{arg_count}", "3")
                    .replace("{command_usage}", commandUsage)
            ));
            return true;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.target-player-not-online")));
            return true;
        }
        PlayerData data = Utils.getPlayerData(targetPlayer);
        assert data != null;
        switch (args[1]) {
            case "mining_speed" -> {
                int miningSpeed = data.miningSpeed;
                try { miningSpeed = Integer.parseInt(args[2]); }
                catch (NumberFormatException e) { sender.sendMessage(Utils.coloured(Utils.getLang().getString("messages.not-an-integer"))); }
                data.miningSpeed = miningSpeed;
            }
            default -> sender.sendMessage("The data type is not valid");
        }
        return true;
    }
}