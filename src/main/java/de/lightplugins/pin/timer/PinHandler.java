package de.lightplugins.pin.timer;

import de.lightplugins.pin.Pin;
import de.lightplugins.pin.enums.MessagePath;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PinHandler {

    public void startGlobalTimer() {
        Pin.util.sendDebug("Global timer starting");

        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar currentTime = Calendar.getInstance();
                List<Player> playersToRemove = new ArrayList<>();

                for (Player player : Pin.getInstance.pinEntry.keySet()) {
                    Calendar playersTimer = Pin.getInstance.pinEntry.get(player);

                    if (currentTime.compareTo(playersTimer) >= 0) {
                        Pin.util.sendDebug("Waiting time expired. Sending activity check");

                        if (player.isOnline()) {
                            int randomPin = Pin.util.getRandomPin();
                            sendTitle(player, randomPin);
                            startEntryTime(player, randomPin);
                            Pin.util.sendDebug("Title successfully sent");
                        }

                        playersToRemove.add(player);
                        Pin.util.sendDebug("Player marked for removal due to activity check");
                    }
                }

                for (Player player : playersToRemove) {
                    Pin.getInstance.pinEntry.remove(player);
                    Pin.util.sendDebug("Player was removed from pinEntry due to activity check");
                }

            }
        }.runTaskTimerAsynchronously(Pin.getInstance, 20 * 10L, 20);
    }

    public void sendTitle(Player player, int pin) {

        FileConfiguration settings = Pin.settings.getConfig();
        String upperLine = Objects.requireNonNull(settings.getString("settings.titles.upperLine"))
                .replace("#pin#", String.valueOf(pin));
        String lowerLine = Objects.requireNonNull(settings.getString("settings.titles.lowerLine"))
                .replace("#pin#", String.valueOf(pin));


        player.sendTitle(
                Pin.util.translateColors(upperLine),
                Pin.util.translateColors(lowerLine),
                35, (getTimeToEntry() * 20) - 60, 60);

        Pin.util.sendMessage(player, MessagePath.onAsking.getPath());
        Pin.sounds.soundOnAsking(player);

    }

    public void startEntryTime(Player player, int pin) {

        Pin.util.sendDebug("Player " + player.getName() + " pin entry countdown starting");
        Pin.getInstance.pinEntry.remove(player);
        Pin.getInstance.playersPin.put(player, pin);
        Pin.util.sendDebug("Generating for player "
                + player.getName() + " this pin: " + Pin.getInstance.playersPin.get(player));
        Pin.util.sendDebug("Removed player from pinEntry due entry timer start");

        new BukkitRunnable() {

            int count = 0;

            @Override
            public void run() {
                count ++;

                if(!Pin.getInstance.playersPin.containsKey(player)) {
                    Pin.util.sendDebug("entryCountdown stopped due player was not in playersPin");
                    cancel();
                }

                if(count >= getTimeToEntry()) {
                    Pin.util.sendDebug("Time expired. Do now actions due inactivity");
                    processAction(player);
                    Pin.util.sendDebug("Removed player from pinEntry due timer expire");
                    cancel();
                    return;
                }
            }
        }.runTaskTimerAsynchronously(Pin.getInstance, 0, 20);
    }

    private int getTimeToEntry() {
        FileConfiguration settings = Pin.settings.getConfig();
        return settings.getInt("settings.timeToEntry");
    }

    private void processAction(Player player) {
        FileConfiguration settings = Pin.settings.getConfig();

        String type = settings.getString("settings.action.type");
        String kickMessage = settings.getString("settings.action.kickMessage");
        String command = settings.getString("settings.action.command");

        if(type == null || command == null) {
            Pin.util.sendDebug("type or command was null");
            return;
        }

        switch (type) {

            case "KICK" :
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(Pin.util.translateColors(kickMessage));
                        Pin.util.sendDebug("Player " + player.getName() + " was kicked");
                    }
                }.runTask(Pin.getInstance);

            case "COMMAND" :
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                        Pin.util.sendDebug("Console command dispatched: " + command);
                    }
                }.runTask(Pin.getInstance);

                Calendar currentTime = Calendar.getInstance();
                int time = Pin.util.getTime();
                currentTime.add(Calendar.MINUTE, time);
                Pin.getInstance.pinEntry.put(player, currentTime);

                Pin.util.sendDebug("Successfully readded player "
                        + player.getName() + " to the timer with value " + time + " due command dispatch");
        }
    }

}
