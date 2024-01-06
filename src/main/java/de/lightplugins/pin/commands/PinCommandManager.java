package de.lightplugins.pin.commands;

import de.lightplugins.pin.Pin;
import de.lightplugins.pin.enums.MessagePath;
import de.lightplugins.pin.timer.PinHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class PinCommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        Player player = (Player) commandSender;

        if(args.length == 1) {

            if(args[0].equals("reload")) {
                if(!player.hasPermission("lightpin.admin.reload")) {
                    Pin.util.sendMessage(player, MessagePath.NoPermission.getPath());
                    return false;
                }

                Pin.messages.reloadConfig("messages.yml");
                Pin.settings.reloadConfig("settings.yml");
                Pin.util.sendMessage(player, MessagePath.Reload.getPath());
                Pin.util.sendDebug("Reloaded the config files");
                return false;
            }

            if(!Pin.getInstance.playersPin.containsKey(player)) {
                Pin.util.sendMessage(player, MessagePath.notChecked.getPath());
                Pin.util.sendDebug("Player are not int the playersPin HashMap");
                return false;
            }

            try {

                int enteredPin = Integer.parseInt(args[0]);
                int givenPin = Pin.getInstance.playersPin.get(player);

                if(enteredPin != givenPin) {
                    Pin.util.sendMessage(player, MessagePath.onFailing.getPath());
                    Pin.sounds.soundOnFailed(player);
                    Pin.util.sendDebug("enteredPin and givenPin are not the same " + enteredPin + " - " + givenPin);
                    return false;
                }

                Pin.util.sendMessage(player, MessagePath.onSuccess.getPath());
                Pin.sounds.soundOnSuccess(player);
                player.resetTitle();
                Pin.getInstance.pinEntry.remove(player);
                Pin.util.sendDebug("Removed player from pinEntry");
                Pin.getInstance.playersPin.remove(player);
                Pin.util.sendDebug("Player are not int the playersPin HashMap");
                Pin.util.sendDebug("Removed player from playersPin");

                Calendar currentTime = Calendar.getInstance();
                int time = Pin.util.getTime();
                currentTime.add(Calendar.MINUTE, time);
                Pin.getInstance.pinEntry.put(player, currentTime);
                Pin.util.sendDebug("Successfully readded player "
                        + player.getName() + " to the timer with value " + time);

            } catch (NumberFormatException e) {
                Pin.util.sendMessage(player, MessagePath.onFailing.getPath());
                Pin.sounds.soundOnFailed(player);
                Pin.util.sendDebug("Player used chars instead of numbers");
                return false;
            }

        } else if(args.length == 0) {

            if(!Pin.getInstance.playersPin.containsKey(player)) {
                Pin.util.sendMessage(player, MessagePath.notChecked.getPath());
                Pin.util.sendDebug("Player are not int the playersPin HashMap");
                return false;
            }

            PinHandler pinHandler = new PinHandler();
            pinHandler.sendTitle(player, Pin.getInstance.playersPin.get(player));
            Pin.util.sendDebug("Send the title to the player again");
            return false;
        }

        return false;
    }
}
