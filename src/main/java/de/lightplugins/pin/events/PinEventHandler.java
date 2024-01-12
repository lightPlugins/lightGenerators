package de.lightplugins.pin.events;

import de.lightplugins.pin.Pin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Calendar;
import java.util.List;

public class PinEventHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        insertList(e.getPlayer());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        insertList(e.getPlayer());

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Pin.getInstance.pinEntry.remove(player);
        Pin.util.sendDebug("Removed player " + player.getName() + " from pinEntry due PlayerQuitEvent");
        Pin.getInstance.playersPin.remove(player);
        Pin.util.sendDebug("Removed player " + player.getName() + " from playersPin due PlayerQuitEvent");
    }

    private void insertList(Player player) {

        String currentWorld = player.getWorld().getName();

        if(!configWorlds().contains(currentWorld)) {
            Pin.util.sendDebug("Was trying to insert player into pinEntry but the world "
                    + currentWorld + " was not defined in the settings.yml");
            return;
        }

        Calendar currentTime = Calendar.getInstance();
        int time = Pin.util.getTime();

        currentTime.add(Calendar.MINUTE, time);

        if(!Pin.getInstance.pinEntry.containsKey(player)) {
            Pin.getInstance.pinEntry.put(player, currentTime);
            Pin.util.sendDebug("Successfully put player into pinEntry " +
                    "due Join/Leave/WorldChange event with timer value " + time);
            return;
        }

        Pin.util.sendDebug("This player is already in pinEntry. Maybe he just switched the world");
    }

    private List<String> configWorlds() {
        FileConfiguration settings = Pin.settings.getConfig();
        return settings.getStringList("settings.worlds");
    }

}
