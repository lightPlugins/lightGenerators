package de.lightplugins.pin.enums;

import de.lightplugins.pin.Pin;
import org.bukkit.configuration.file.FileConfiguration;


public enum MessagePath {

    Prefix("prefix"),
    NoPermission("noPermission"),
    Reload("reload"),
    notChecked("notChecked"),
    onAsking("onAsking"),
    onFailing("onFailing"),
    onSuccess("onSuccess"),
    notNumber("notNumber"),
    ;

    private final String path;

    MessagePath(String path) { this.path = path; }
    public String getPath() {
        FileConfiguration paths = Pin.messages.getConfig();
        try {
            return paths.getString(this.path);
        } catch (Exception e) {
            throw new RuntimeException("Error occured on Message creation", e);
        }
    }
}
