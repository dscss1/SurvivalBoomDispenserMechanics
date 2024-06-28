package net.survivalboom.survivalboomdispensermechanics.configuration;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.survivalboom.survivalboomdispensermechanics.SurvivalBoomDispenserMechanics;
import net.survivalboom.survivalboomdispensermechanics.placeholders.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginMessages {

    private static final Map<String, String> messages = new HashMap<>();


    public static void reload(File file) throws IOException, InvalidConfigurationException {

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(file);

        messages.clear();
        for (String s : configuration.getKeys(false)) messages.put(s, configuration.getString(s));

    }

    @NotNull
    public static String getMessage(@NotNull String key) {
        if (messages.containsKey(key)) return messages.get(key).replace("{VERSION}", SurvivalBoomDispenserMechanics.getVersion());
        else return key;
    }

    public static boolean contains(@NotNull String key) {
        return messages.containsKey(key);
    }

    public static void consoleSend(@NotNull String message) {
        if (message.equals("")) return;
        Bukkit.getConsoleSender().sendMessage(parse(message));
    }

    public static void sendMessage(@NotNull Audience target, @Nullable String message) {
        if (message == null) return;
        if (message.equals("")) return;
        if (target instanceof Player player) target.sendMessage(parse(message, player));
        else target.sendMessage(parse(message));
    }

    public static void sendAdmins(@Nullable String permission, @Nullable String message) {
        if (message == null || permission == null) return;
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.removeIf(p -> !p.hasPermission(permission));
        players.forEach(p -> PluginMessages.sendMessage(p, message));
    }

    @NotNull
    public static Component parse(@NotNull String text, @Nullable Player target) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(Placeholders.papi(text, target)).decoration(TextDecoration.ITALIC, false);
    }

    @NotNull
    public static Component parse(@NotNull String text) {
        return parse(text, null);
    }

    @NotNull
    public static Component parse(@NotNull String text, @Nullable Player target, @Nullable Placeholders placeholders) {
        return parse(Placeholders.parseFull(text, target, placeholders));
    }

    @NotNull
    public static Component parseOnlyColors(@NotNull String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

}
