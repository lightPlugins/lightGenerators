package de.lightplugins.pin.util;
import de.lightplugins.pin.Pin;
import de.lightplugins.pin.enums.MessagePath;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

public class Util {


    /*  Send Message with Prefix to player  */

    public void sendMessage(Player player, String message) {
        String prefix = MessagePath.Prefix.getPath();
        message = PlaceholderAPI.setPlaceholders(player, message);
        player.sendMessage(Pin.colorTranslation.hexTranslation(prefix + message));
    }

    public String translateColors(String message) {
        return Pin.colorTranslation.hexTranslation(message);
    }
    /*  Send a message List to player without Prefix  */

    public void sendMessageList(Player player, List<String> list) {
        for(String s : list) {
            s = PlaceholderAPI.setPlaceholders(player, s);
            player.sendMessage(Pin.colorTranslation.hexTranslation(s));
        }
    }

    public double fixDouble(double numberToFix) {
        BigDecimal bd = new BigDecimal(numberToFix).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isNumber(String number) {
        try {
            Double dummy = Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String formatDouble(double numberToFormat) {
        return String.format("%,.2f", numberToFormat);
    }

    public String finalFormatDouble(double numberToRound) { return formatDouble(fixDouble(numberToRound));
    }


    public boolean isInventoryEmpty(Player player) { return player.getInventory().firstEmpty() != -1; }

    public double subtractPercentage(double originalValue, double percentage) {

        Bukkit.getLogger().log(Level.WARNING, "TEST 1 " + originalValue + " - " + percentage);

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }

        return (percentage / 100) * originalValue;
    }

    public boolean checkPercentage(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Percent value must be between 0 and 100");
        }
        Random random = new Random();
        double randomPercent = random.nextDouble() * 100;
        return randomPercent <= percent;
    }

    public int getTime() {

        FileConfiguration settings = Pin.settings.getConfig();
        int timerMin = settings.getInt("settings.timer.min");
        int timerMax = settings.getInt("settings.timer.max");

        if (timerMin > timerMax) {
            throw new IllegalArgumentException("Min must be less than or equal to max");
        }

        if (timerMin == timerMax) {
            return timerMin; // Wenn min und max gleich sind, gebe nur den Mindestwert zurück
        }

        Random random = new Random();
        return random.nextInt(timerMax - timerMin + 1) + timerMin;
    }

    public Integer getRandomPin() {
        return generateRandomPin();
    }

    private static Integer generateRandomPin() {

        FileConfiguration settings = Pin.settings.getConfig();
        int digits = settings.getInt("settings.pinFormat.numberOfDigits");

        if (digits <= 0) {
            throw new IllegalArgumentException("The number of digits for the pin format must be greater than 0!");
        }

        Random random = new Random();
        int pin = 0;

        for (int i = 0; i < digits; i++) {
            int digit = random.nextInt(10);
            pin = pin * 10 + digit;
        }

        return pin;
    }

    public void sendDebug(String message) {
        FileConfiguration fileConfiguration = Pin.settings.getConfig();
        boolean debugMode = fileConfiguration.getBoolean("settings.debug");

        if(debugMode) {
            Bukkit.getConsoleSender().sendMessage(Pin.consolePrefix + "[DEBUG] "  + message);
        }
    }
}
