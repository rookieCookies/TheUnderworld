package com.daymare.theunderworld.systems.potions;

import com.daymare.library.filemanager.FileID;
import com.daymare.theunderworld.Underworld;
import com.daymare.theunderworld.player.PlayerData;
import com.daymare.theunderworld.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PotionEffect {
    public PotionEffectType type;
    public int duration;
    public int power;

    public PotionEffect(PotionEffectType type, int duration, int power) {
        this.type = type;
        this.duration = duration;
        this.power = power;
    }

    public enum PotionEffectType {
        HASTE,
        PRISTINE_MINER;

        public String getTitle() {
            return Utils.getLang().getString("potions."+this.toString().toLowerCase(Locale.ROOT));
        }
    }

    public static void tick(Player p) {
        PlayerData data = Utils.getPlayerData(p);
        for (PotionEffect effect : data.potionEffects.values()) {
            effect.duration -= 1;
            if (effect.duration <= 0) {
                data.potionEffects.remove(effect.type);
                p.sendMessage(Utils.coloured(
                        Utils.getLang().getString("messages.potion-effect-expired")
                                .replace("{potion_effect}", effect.toString())
                ));
            }
        }
    }

    @Override
    public String toString() {
        return this.type.getTitle() + " " + Utils.toRoman(this.power);
    }
}
