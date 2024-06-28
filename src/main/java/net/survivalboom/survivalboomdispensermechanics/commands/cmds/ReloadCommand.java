package net.survivalboom.survivalboomdispensermechanics.commands.cmds;

import net.survivalboom.survivalboomdispensermechanics.SurvivalBoomDispenserMechanics;
import net.survivalboom.survivalboomdispensermechanics.configuration.PluginMessages;
import net.survivalboom.survivalboomdispensermechanics.dispenser.DispenserListener;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ReloadCommand {

    public static void command(@NotNull CommandSender sender) throws IOException, InvalidConfigurationException {

        if (!sender.hasPermission("sbdm.reload")) {
            PluginMessages.sendMessage(sender, PluginMessages.getMessage("no-permission").replace("{PERMISSION}", "sbdm.reload"));
            return;
        }

        File dataFolder = SurvivalBoomDispenserMechanics.getPlugin().getDataFolder();
        PluginMessages.reload(new File(dataFolder, "messages.yml"));
        SurvivalBoomDispenserMechanics.getPlugin().getConfig().load(new File(dataFolder, "config.yml"));

        DispenserListener.reload();

        PluginMessages.sendMessage(sender, PluginMessages.getMessage("reload-success"));

    }

}
