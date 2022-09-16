package jss.customenderchest;

import jss.customenderchest.commands.CommandHandler;
import net.craftersland.customenderchest.*;
import net.craftersland.customenderchest.commands.FileToMysqlCmd;
import net.craftersland.customenderchest.storage.FlatFileStorage;
import net.craftersland.customenderchest.storage.MysqlSetup;
import net.craftersland.customenderchest.storage.MysqlStorage;
import net.craftersland.customenderchest.storage.StorageInterface;
import net.craftersland.customenderchest.utils.EnderChestUtils;
import net.craftersland.customenderchest.utils.ModdedSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class EnderChest extends CustomEnderChest {

    public static Logger log;
    public static boolean is19Server = true;
    public static boolean is13Server = false;
    public static String pluginName = "CustomEnderChest";
    private static ConfigHandler configHandler;
    private static StorageInterface storageInterface;
    private static EnderChestUtils enderchestUtils;
    private static DataHandler dH;
    private static MysqlSetup mysqlSetup;
    private static SoundHandler sH;
    private static ModdedSerializer ms;
    private static FileToMysqlCmd ftMc;
    public Map<Inventory, UUID> admin = new HashMap<>();

    public void onEnable() {
        log = getLogger();
        getMcVersion();
        configHandler = new ConfigHandler(this);
        checkForModdedNBTSupport();
        enderchestUtils = new EnderChestUtils(this);
        if (configHandler.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
            log.info("Using MySQL database for data.");
            mysqlSetup = new MysqlSetup(this);
            storageInterface = new MysqlStorage(this);
        } else {
            log.info("Using FlatFile system for data. IMPORTANT! We recommend MySQL.");
            File pluginFolder = new File("plugins" + System.getProperty("file.separator") + pluginName + System.getProperty("file.separator") + "PlayerData");
            if (!pluginFolder.exists()) {
                //noinspection ResultOfMethodCallIgnored
                pluginFolder.mkdir();
            }
            storageInterface = new FlatFileStorage(this);
        }
        dH = new DataHandler(this);
        sH = new SoundHandler(this);
        ftMc = new FileToMysqlCmd(this);

        //listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerHandler(this), this);
        //command
        CommandHandler cH = new CommandHandler(this);
        //noinspection ConstantConditions
        getCommand("customenderchest").setExecutor(cH);
        //noinspection ConstantConditions
        getCommand("customenderchest").setTabCompleter(cH);
        log.info(pluginName + " loaded successfully!");
    }

    //Disabling plugin
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        if (configHandler.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
            if (mysqlSetup.getConnection() != null) {
                log.info("Closing database connection...");
                mysqlSetup.closeDatabase();
            }
        }
        log.info("Cleaning internal data...");
        dH.clearLiveData();
        HandlerList.unregisterAll(this);
        log.info(pluginName + " is disabled!");
    }

    private void getMcVersion() {
        String[] serverVersion = Bukkit.getBukkitVersion().split("-");
        String version = serverVersion[0];

        if (version.matches("1.7.10") || version.matches("1.7.9") || version.matches("1.7.5") || version.matches("1.7.2") || version.matches("1.8.8") || version.matches("1.8.3") || version.matches("1.8.4") || version.matches("1.8")) {
            is19Server = false;
            is13Server = false;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.9") || version.matches("1.9.1") || version.matches("1.9.2") || version.matches("1.9.3") || version.matches("1.9.4")) {
            is19Server = true;
            is13Server = false;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.10") || version.matches("1.10.1") || version.matches("1.10.2")) {
            is19Server = true;
            is13Server = false;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.11") || version.matches("1.11.1") || version.matches("1.11.2")) {
            is19Server = true;
            is13Server = false;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.12") || version.matches("1.12.1") || version.matches("1.12.2")) {
            is19Server = true;
            is13Server = false;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.13") || version.matches("1.13.1") || version.matches("1.13.2")) {
            is19Server = true;
            is13Server = true;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.14") || version.matches("1.14.1") || version.matches("1.14.2") || version.matches("1.14.3") || version.matches("1.14.4")) {
            is19Server = true;
            is13Server = true;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.15") || version.matches("1.15.1") || version.matches("1.15.2")) {
            is19Server = true;
            is13Server = true;
            log.info("Compatible server version detected: " + version);
        } else if (version.matches("1.16") || version.matches("1.16.1") || version.matches("1.16.2") || version.matches("1.16.3")) {
            is19Server = true;
            is13Server = true;
            log.info("Compatible server version detected: " + version);
        } else {
            //Default fallback to 1.15 API
            is19Server = true;
            is13Server = true;
            log.info("Incompatible server version detected: " + version + " . Running into 1.16 API mode.");
        }
    }

    private void checkForModdedNBTSupport() {
        if (configHandler.getBoolean("settings.modded-NBT-data-support")) {
            if (configHandler.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
                if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
                    ms = new ModdedSerializer(this);
                    log.info("ProtocolLib dependency found. Modded NBT data support is enabled!");
                } else {
                    log.warning("ProtocolLib dependency not found!!! Modded NBT data support is disabled!");
                }
            } else {
                log.warning("NBT Modded data support only works for MySQL storage. Modded NBT data support is disabled!");
            }
        }
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public StorageInterface getStorageInterface() {
        return storageInterface;
    }

    public EnderChestUtils getEnderChestUtils() {
        return enderchestUtils;
    }

    public MysqlSetup getMysqlSetup() {
        return mysqlSetup;
    }

    public SoundHandler getSoundHandler() {
        return sH;
    }

    public DataHandler getDataHandler() {
        return dH;
    }

    public ModdedSerializer getModdedSerializer() {
        return ms;
    }

    public FileToMysqlCmd getFileToMysqlCmd() {
        return ftMc;
    }

}
