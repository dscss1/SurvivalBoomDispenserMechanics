package net.survivalboom.survivalboomdispensermechanics.utils;

import net.survivalboom.survivalboomdispensermechanics.SurvivalBoomDispenserMechanics;
import net.survivalboom.survivalboomdispensermechanics.configuration.PluginMessages;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class Utils {

    private static final JavaPlugin plugin = SurvivalBoomDispenserMechanics.getPlugin();

    public static boolean copyPluginFile(String plugin_file_path) {

        File file = new File(plugin.getDataFolder(), plugin_file_path);
        if (file.exists()) return false;

        String[] path_parts = plugin_file_path.split("/");

        if (!plugin_file_path.contains("/")) { plugin.saveResource(plugin_file_path, true); return true;}
        else plugin.saveResource(path_parts[path_parts.length - 1], true);

        String[] args = plugin_file_path.split("/");

        File origin = new File(plugin.getDataFolder(), args[args.length - 1]);
        origin.renameTo(file);

        return true;
    }

    public static boolean createPluginFolder(String path) {

        File file = new File(plugin.getDataFolder(), path);;

        if (!file.exists()) return file.mkdirs();
        else return false;

    }

    @Nullable
    public static String getArrayValue(@NotNull String[] array, int index) {
        try { return array[index]; } catch (Exception e) { return null; }
    }

    public static void sendPluginError(@NotNull String msg, @NotNull Exception e) {

        PluginMessages.consoleSend("&cSurvivalBoomDispenserMechanics &8&l► &f: " + msg);
        PluginMessages.consoleSend(String.format("&6>> &e%s", e));

        for (StackTraceElement element : e.getStackTrace()) {
            PluginMessages.consoleSend(String.format("&4>> &c%s", element));
        }

    }

    public static void sendStackTrace(@NotNull Exception e) {
        PluginMessages.consoleSend(String.format("&6>> &e%s", e));
        for (StackTraceElement element : e.getStackTrace()) PluginMessages.consoleSend(String.format("&4>> &c%s", element));
    }


}
