package com.daymare.theunderworld.utils;

import com.daymare.library.filemanager.Configuration;
import com.daymare.library.filemanager.FileID;
import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.player.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.TreeMap;

public class Utils {
    private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();

    static {
        ROMAN_NUMERALS.put(1000, "M");
        ROMAN_NUMERALS.put(900, "CM");
        ROMAN_NUMERALS.put(500, "D");
        ROMAN_NUMERALS.put(400, "CD");
        ROMAN_NUMERALS.put(100, "C");
        ROMAN_NUMERALS.put(90, "XC");
        ROMAN_NUMERALS.put(50, "L");
        ROMAN_NUMERALS.put(40, "XL");
        ROMAN_NUMERALS.put(10, "X");
        ROMAN_NUMERALS.put(9, "IX");
        ROMAN_NUMERALS.put(5, "V");
        ROMAN_NUMERALS.put(4, "IV");
        ROMAN_NUMERALS.put(1, "I");
    }

    public static String coloured(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> coloured(List<String> l) {
        return l.stream().map(Utils::coloured).toList();
    }

    public static String toRoman(int number) {
        int l = ROMAN_NUMERALS.floorKey(number);
        if (number == l) {
            return ROMAN_NUMERALS.get(number);
        }
        return ROMAN_NUMERALS.get(l) + toRoman(number - l);
    }

    public static String toTime(int baseSecond) {
        int second = baseSecond;
        int minute = second / 60;
        second -= minute * 60;

        if (baseSecond > 3600) {
            int hour = minute / 60;
            minute -= hour * 60;
            return hour + " hours " + minute + " minutes " + second + " seconds";
        } else if (baseSecond > 60) {
            return minute + " minutes " + second + " seconds";
        } else {
            return second + " seconds";
        }
    }

    public static PlayerData getPlayerData(Player p) {
        return Underworld.getInstance().getPlayerDataHashMap().get(p.getUniqueId());
    }

    public static Configuration getLang() {
        return Underworld.getInstance().getFileManager().getFile(FileID.LANG).getConfiguration();
    }
}
