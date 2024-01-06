package de.lightplugins.pin.util;

import de.lightplugins.pin.Pin;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Sounds {

    public void soundOnAsking(Player player) {
        playSound(player, "onAsking");
    }

    public void soundOnFailed(Player player) {
        playSound(player, "onFailed");
    }

    public void soundOnSuccess(Player player) {
        playSound(player, "onSuccess");
    }

    private void playSound(Player player, String type) {

        FileConfiguration settings = Pin.settings.getConfig();
        boolean enable = settings.getBoolean("settings.sounds.enable");

        if(!enable) {
            return;
        }

        Sound sound = Sound.valueOf(Objects.requireNonNull(
                settings.getString("settings.sounds." + type + ".sound")).toUpperCase());

        float volume = (float) settings.getDouble("settings.sounds." + type + ".volume");
        float pitch = (float) settings.getDouble("settings.sounds." + type + ".pitch");

        player.playSound(player, sound, volume,pitch);
    }
}
