package de.lightplugins.pin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class PinTabcompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;

        if(args.length == 1) {
            List<String> arguments = new ArrayList<>();

            if(player.hasPermission("lightpin.admin.reload")) {
                arguments.add("reload");
            }

            return arguments;
        }
        return null;
    }
}
