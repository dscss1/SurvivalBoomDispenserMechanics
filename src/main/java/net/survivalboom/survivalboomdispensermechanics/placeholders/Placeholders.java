package net.survivalboom.survivalboomdispensermechanics.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Placeholders {

    private final Map<String, String> placeholders = new HashMap<>();
    private static final boolean isPlaceholderApiInstalled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

    public Placeholders(Placeholders oldPlaceholders) {
        if (oldPlaceholders != null) placeholders.putAll(oldPlaceholders.placeholders);
    }

    public Placeholders() {}

    public void add(@NotNull String placeholder, @NotNull String value) {
        placeholders.put(placeholder, value);
    }

    public void add(@NotNull String placeholder, int value) {
        placeholders.put(placeholder, String.valueOf(value));
    }

    @Nullable
    public String get(@NotNull String key) {
        return placeholders.get(key);
    }

    @NotNull
    public List<String> get() {
        return new ArrayList<>(placeholders.keySet());
    }

    @NotNull
    public String parse(@NotNull String in) {

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            in = in.replace(entry.getKey(), entry.getValue());
        }

        return in;

    }

    @NotNull
    public static String papi(@NotNull String input, @Nullable Player player) {
        if (!isPlaceholderApiInstalled) return input;
        return PlaceholderAPI.setPlaceholders(player, input);
    }

    @NotNull
    public static List<String> papi(@NotNull List<String> input, @Nullable Player player) {
        if (!isPlaceholderApiInstalled) return input;
        List<String> out = new ArrayList<>();
        input.forEach(string -> out.add(PlaceholderAPI.setPlaceholders(player, string)));
        return out;
    }

    @NotNull
    public static String papi(@NotNull String input) {
        return papi(input, null);
    }

    @NotNull
    public static String parseNull(@NotNull String text, @Nullable Placeholders placeholders) {
        if (placeholders == null) return text;
        return placeholders.parse(text);
    }

    @NotNull
    public static String parseFull(@NotNull String text, @Nullable Player target, @Nullable Placeholders placeholders) {
        String out;
        if (placeholders == null) out = text;
        else out = placeholders.parse(text);
        return papi(out, target);
    }

    @NotNull
    public static List<String> parseFull(@NotNull List<String> text, @Nullable Player target, @Nullable Placeholders placeholders) {
        List<String> out = new ArrayList<>();
        if (placeholders == null) out.addAll(text);
        else text.forEach(string -> out.add(placeholders.parse(string)));
        return papi(out, target);
    }

    @NotNull
    public Placeholders copy() {
        return new Placeholders(this);
    }

    public boolean contains(@NotNull String key) {
        return placeholders.containsKey(key);
    }

    public static boolean isPlaceholderApiInstalled() {
        return isPlaceholderApiInstalled;
    }

}
