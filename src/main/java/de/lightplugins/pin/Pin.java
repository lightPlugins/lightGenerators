package de.lightplugins.pin;

import de.lightplugins.pin.commands.PinCommandManager;
import de.lightplugins.pin.commands.PinTabcompletion;
import de.lightplugins.pin.events.PinEventHandler;
import de.lightplugins.pin.timer.PinHandler;
import de.lightplugins.pin.util.ColorTranslation;
import de.lightplugins.pin.util.FileManager;
import de.lightplugins.pin.util.Sounds;
import de.lightplugins.pin.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class Pin extends JavaPlugin {

    public static Pin getInstance;

    public static final String consolePrefix = "§r[light§5Pin§r] ";

    public static Util util;
    public static ColorTranslation colorTranslation;
    public static Sounds sounds;

    public static FileManager settings;
    public static FileManager messages;

    public HashMap<Player, Calendar> pinEntry = new HashMap<>();
    public HashMap<Player, Integer> playersPin = new HashMap<>();


    public void onLoad() {
        getInstance = this;
    }

    public void onEnable() {


        colorTranslation = new ColorTranslation();
        util = new Util();
        sounds = new Sounds();

        messages = new FileManager(this, "messages.yml");
        settings = new FileManager(this, "settings.yml");

        Objects.requireNonNull(this.getCommand("pin")).setExecutor(new PinCommandManager());
        Objects.requireNonNull(this.getCommand("pin")).setTabCompleter(new PinTabcompletion());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PinEventHandler(), this);


        PinHandler pinHandler = new PinHandler();
        pinHandler.startGlobalTimer();

    }

    public void onDisable() {

    }

}