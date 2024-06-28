package net.survivalboom.survivalboomdispensermechanics.commands;

import net.survivalboom.survivalboomdispensermechanics.configuration.PluginMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteHandler implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> out = new ArrayList<>();
        addPossibleCompletions(null, List.of("help", "reload"), args, out, 0, false);
        return out;
    }

    public static boolean addPossibleCompletions(@Nullable List<String> hints, @Nullable List<String> arguments, @NotNull String[] tabcompleteArgs, @NotNull List<String> list, int index, boolean onlySuggest) {

        if (tabcompleteArgs.length > index + 1) return false;
        if (tabcompleteArgs.length - 1 < index) return true;

        if (hints != null) list.addAll(hints);

        if (arguments == null) return true;

        String userInput;
        try { userInput = tabcompleteArgs[index]; } catch (IndexOutOfBoundsException e) { userInput = null; }
        if (userInput.equals("")) userInput = null;

        if (arguments.size() < 1 && !onlySuggest) {
            list.add(replaceColorCodes(PluginMessages.getMessage("not-found")));
            return true;
        }

        if (userInput == null) {
            list.addAll(arguments);
            return true;
        }

        List<String> out = new ArrayList<>();
        for (String arg : arguments) {
            if (!arg.contains(userInput)) continue;
            out.add(arg);
        }

        if (out.size() < 1 && !onlySuggest) {
            list.add(replaceColorCodes(PluginMessages.getMessage("argument-not-found").replace("{ARGUMENT}", userInput)));
            return true;
        }

        list.addAll(out);

        return true;

    }

    private static String replaceColorCodes(@NotNull String in) {
        return in.replace("&", "§");
    }
}
