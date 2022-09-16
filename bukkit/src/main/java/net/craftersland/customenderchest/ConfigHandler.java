package net.craftersland.customenderchest;

import jss.customenderchest.EnderChest;
import jss.customenderchest.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private final EnderChest enderchest;

    public ConfigHandler(EnderChest enderchest) {
        this.enderchest = enderchest;
        loadConfig();
    }

    public void loadConfig() {
        File pluginFolder = new File("plugins" + System.getProperty("file.separator") + EnderChest.pluginName);
        if (!pluginFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            pluginFolder.mkdir();
        }
        File configFile = new File("plugins" + System.getProperty("file.separator") + EnderChest.pluginName + System.getProperty("file.separator") + "config.yml");
        if (!configFile.exists()) {
            EnderChest.log.info("No config file found! Creating new one...");
            enderchest.saveDefaultConfig();
        }
        try {
            EnderChest.log.info("Loading the config file...");
            enderchest.getConfig().load(configFile);
            EnderChest.log.info("Config loaded successfully!");
        } catch (Exception e) {
            EnderChest.log.severe("Could not load the config file! You need to regenerate the config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getStringWithColor(String key) {
        if (!enderchest.getConfig().contains(key)) {
            enderchest.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + EnderChest.pluginName + " folder! (Try generating a new one by deleting the current)");
            return "errorCouldNotLocateInConfigYml:" + key;
        } else {
            return Utils.colorized(enderchest.getConfig().getString(key));
        }
    }

    public String getString(String key) {
        if (!enderchest.getConfig().contains(key)) {
            enderchest.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + EnderChest.pluginName + " folder! (Try generating a new one by deleting the current)");
            return "errorCouldNotLocateInConfigYml:" + key;
        } else {
            return enderchest.getConfig().getString(key);
        }
    }

    public List<String> getStringList(String key) {
        if (!enderchest.getConfig().contains(key)) {
            enderchest.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + EnderChest.pluginName + " folder! (Try generating a new one by deleting the current)");
            return null;
        } else {
            return enderchest.getConfig().getStringList(key);
        }
    }

    public boolean getBoolean(String key) {
        if (!enderchest.getConfig().contains(key)) {
            enderchest.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + EnderChest.pluginName + " folder! (Try generating a new one by deleting the current)");
            return false;
        } else {
            return enderchest.getConfig().getBoolean(key);
        }
    }

    @SuppressWarnings("unused")
    public double getDouble(String key) {
        if (!enderchest.getConfig().contains(key)) {
            enderchest.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + EnderChest.pluginName + " folder! (Try generating a new one by deleting the current)");
            return 0.0;
        } else {
            return enderchest.getConfig().getDouble(key);
        }
    }

    @SuppressWarnings("unused")
    public int getInteger(String key) {
        if (!enderchest.getConfig().contains(key)) {
            enderchest.getLogger().severe("Could not locate " + key + " in the config.yml inside of the " + EnderChest.pluginName + " folder! (Try generating a new one by deleting the current)");
            return 0;
        } else {
            return enderchest.getConfig().getInt(key);
        }
    }

    //TODO remove this \/
    //Send chat messages from config
    public void printMessage(Player p, String messageKey) {
        if (enderchest.getConfig().contains(messageKey)) {
            List<String> message = new ArrayList<>();
            String configMsg = enderchest.getConfig().getString(messageKey);

            assert configMsg != null;
            if (configMsg.matches("")) return;

            message.add(configMsg);

            if (p != null) {
                //Message format
                Utils.sendColorMessage(p, getString("chatMessages.prefix") + message.get(0));
            }

        } else {
            enderchest.getLogger().severe("Could not locate '" + messageKey + "' in the config.yml inside of the CustomEnderChest folder!");
            p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED + "Could not locate '" + messageKey + "' in the config.yml inside of the CustomEnderChest folder!");
        }

    }

}
