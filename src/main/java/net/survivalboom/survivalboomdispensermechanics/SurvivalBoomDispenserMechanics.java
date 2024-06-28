package net.survivalboom.survivalboomdispensermechanics;

import net.survivalboom.survivalboomdispensermechanics.commands.CommandsHandler;
import net.survivalboom.survivalboomdispensermechanics.commands.TabCompleteHandler;
import net.survivalboom.survivalboomdispensermechanics.configuration.PluginMessages;
import net.survivalboom.survivalboomdispensermechanics.dispenser.DispenserListener;
import net.survivalboom.survivalboomdispensermechanics.utils.Utils;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class SurvivalBoomDispenserMechanics extends JavaPlugin {

    private static SurvivalBoomDispenserMechanics plugin = null;
    private static final String compiledFor = "SurvivalBoomChat Public Release. Compiled For Public Use";

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        try {

            sendSplash();

            PluginMessages.consoleSend("&b>> &fChecking files...");
            checkFiles(false);

            PluginMessages.consoleSend("&b>> &fLoading configuration...");
            getConfig().load(new File(getDataFolder(), "config.yml"));
            PluginMessages.reload(new File(getDataFolder(), "messages.yml"));

            DispenserListener.init();

            PluginMessages.consoleSend("&b>> &fRegistering plugin components...");
            PluginCommand command = getCommand("survivalboomdispensermechanics");
            command.setTabCompleter(new TabCompleteHandler());
            command.setExecutor(new CommandsHandler());

            PluginMessages.consoleSend("&a>> &fPlugin &aSurvivalBoomDispenserMechanics &fsuccessfully enabled!");

        }

        catch (Exception e) {
            PluginMessages.consoleSend("&c&l! &fLooks like &cSurvivalBoomDispenserMechanics &fjust crashed! &fSowwy >.<");
            Utils.sendStackTrace(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PluginMessages.consoleSend("&a>> &fPlugin &aSurvivalBoomDispenserMechanics &fsuccessfully disabled!");
    }

    @NotNull
    public static SurvivalBoomDispenserMechanics getPlugin() {
        return plugin;
    }

    @NotNull @SuppressWarnings("UnstableApiUsage")
    public static String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    public static void sendSplash() {

        PluginMessages.consoleSend("&b");
        PluginMessages.consoleSend("&b   _____                  _            _ &3____");
        PluginMessages.consoleSend("&b  / ____|                (_)          | &3|  _ \\                       ");
        PluginMessages.consoleSend("&b | (___  _   _ _ ____   _____   ____ _| &3| |_) | ___   ___  _ __ ___   ");
        PluginMessages.consoleSend("&b  \\___ \\| | | | '__\\ \\ / / \\ \\ / / _` | &3|  _ < / _ \\ / _ \\| '_ ` _ \\ ");
        PluginMessages.consoleSend("&b  ____) | |_| | |   \\ V /| |\\ V / (_| | &3| |_) | (_) | (_) | | | | | | ");
        PluginMessages.consoleSend("&b |_____/ \\__,_|_|    \\_/ |_| \\_/ \\__,_|_&3|____/ \\___/ \\___/|_| |_| |_|   ");
        PluginMessages.consoleSend("    &dSurvivalBoom Network &8| &fBy &bTIMURishche &8|  &dSurvivalBoomDispenserMechanics &fv&3{VERSION}".replace("{VERSION}", getVersion()));

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void checkFiles(boolean silent) {

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();

        String[] files = {"config.yml", "messages.yml"};
        for (String file : files) if (Utils.copyPluginFile(file) && !silent) PluginMessages.consoleSend(String.format("&3>> &fCreated &3%s", file));

    }

    @NotNull
    public static String getCompiledFor() {
        return compiledFor;
    }

}
