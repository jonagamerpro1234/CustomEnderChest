package jss.customenderchest.utils;

import jss.customenderchest.libs.iridium.IridiumColorAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static @NotNull String colorized(String text) {
        return IridiumColorAPI.process(text);
    }

    @Contract(pure = true)
    public static @NotNull String unColorized(String text) {
        return IridiumColorAPI.stripColorFormatting(text);
    }

    public static void sendColorMessage(@NotNull Player player, String text) {
        player.sendMessage(colorized(text));
    }

    public static void sendColorMessage(@NotNull CommandSender sender, String text) {
        sender.sendMessage(colorized(text));
    }

    public static @NotNull List<String> setLimitTab(@NotNull List<String> options, String lastArgs) {
        List<String> returned = new ArrayList<>();
        options.forEach(s -> {
            if (s != null && s.toLowerCase().startsWith(lastArgs.toLowerCase())) {
                returned.add(s);
            }
        });
        return returned;
    }

}
