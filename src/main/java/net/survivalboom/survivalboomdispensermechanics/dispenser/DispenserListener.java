package net.survivalboom.survivalboomdispensermechanics.dispenser;

import net.survivalboom.survivalboomdispensermechanics.SurvivalBoomDispenserMechanics;
import net.survivalboom.survivalboomdispensermechanics.configuration.PluginMessages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DispenserListener implements Listener {

    private static final Map<Material, Integer> weapons = new HashMap<>();
    private static final List<Material> tools = new ArrayList<>();

    public static void init() {
        PluginMessages.consoleSend("&b>> &fLoading dispenser listener...");
        Bukkit.getPluginManager().registerEvents(new DispenserListener(), SurvivalBoomDispenserMechanics.getPlugin());
        reload();
    }

    public static void reload() {

        FileConfiguration configuration = SurvivalBoomDispenserMechanics.getPlugin().getConfig();

        weapons.clear();

        List<String> www = configuration.getStringList("weapons");
        for (String s : www) {

            String[] args = s.split(":");
            if (args.length != 2) continue;

            int damage;
            try { damage = Integer.parseInt(args[1]); } catch (NumberFormatException e) { continue; }

            Material material = Material.getMaterial(args[0]);
            if (material == null) continue;

            weapons.put(material, damage);

        }

        tools.clear();

        List<String> ttt = configuration.getStringList("tools");
        for (String s : ttt) {
            Material material = Material.getMaterial(s);
            if (material == null) continue;
            tools.add(material);
        }

    }

    @EventHandler
    public void mainListener(BlockDispenseEvent event) {

        ItemStack stack = event.getItem();
        Block block = event.getBlock();

        Dispenser dispenser = (Dispenser) block.getBlockData();
        Block targetBlock = block.getRelative(dispenser.getFacing());
        Material material = stack.getType();

        if (material.isBlock()) blockPlacement(event, block, targetBlock, stack);
        else if (weapons.containsKey(material)) entityKiller(event, block, targetBlock, stack);
        else if (tools.contains(material)) blockDestroy(event, block, dispenser, targetBlock, stack);

    }

    public void blockPlacement(@NotNull BlockDispenseEvent event, @NotNull Block dispenserBlock, @NotNull Block targetBlock, @NotNull ItemStack stack) {

        if (!targetBlock.getType().isAir()) return;

        Location location = targetBlock.getLocation();
        World world = location.getWorld();

        org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) dispenserBlock.getState();

        world.spawnParticle(Particle.BLOCK_CRACK, location.toCenterLocation(), 100, Bukkit.createBlockData(stack.getType()));
        world.playSound(location, Sound.BLOCK_STONE_PLACE, 10, 1);

        targetBlock.setType(stack.getType());

        event.setCancelled(true);

        Inventory inventory = dispenser.getInventory();

        Bukkit.getScheduler().runTaskLater(SurvivalBoomDispenserMechanics.getPlugin(), () -> inventory.removeItemAnySlot(stack), 1L);

    }

    public void entityKiller(@NotNull BlockDispenseEvent event, @NotNull Block dispenserBlock, @NotNull Block targetBlock, @NotNull ItemStack stack) {

        Location location = targetBlock.getLocation();
        Material material = stack.getType();

        if (!weapons.containsKey(material)) return;

        int damage = weapons.get(material);

        Collection<Entity> entities = location.getNearbyEntities(1, 1, 1);
        entities.forEach(e -> {
            if (!e.isValid()) return;
            LivingEntity livingEntity = (LivingEntity) e;
            livingEntity.damage(damage);
        });

        event.setCancelled(true);

    }

    public void blockDestroy(@NotNull BlockDispenseEvent event, @NotNull Block dispenserBlock, @NotNull Dispenser dispenser, @NotNull Block targetBlock, @NotNull ItemStack stack) {

        if (targetBlock.getType().isAir()) return;

        Material material = stack.getType();
        Material targetMaterial = targetBlock.getType();

        if (!tools.contains(material)) return;

        Location location = targetBlock.getLocation();
        World world = location.getWorld();

        long time = (long) targetBlock.getDestroySpeed(stack);
        time = time * 20;

        Location l = dispenserBlock.getLocation().add(0, 1, 0).toCenterLocation();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(SurvivalBoomDispenserMechanics.getPlugin(), () -> {

            try {

                while (!atomicBoolean.get()) {
                    Thread.sleep(100);
                    Bukkit.getScheduler().runTask(SurvivalBoomDispenserMechanics.getPlugin(), () -> {
                        world.spawnParticle(Particle.BLOCK_CRACK, l, 10, Bukkit.createBlockData(targetMaterial));
                        world.playSound(l, Sound.BLOCK_STONE_BREAK, 10, 1);
                    });
                }

            } catch (Exception ignored) {}
        });

        Bukkit.getScheduler().runTaskLater(SurvivalBoomDispenserMechanics.getPlugin(), () -> {
            atomicBoolean.set(true);
            task.cancel();
            targetBlock.breakNaturally(stack, true);
            world.playSound(l, Sound.BLOCK_STONE_BREAK, 10, 0);
            world.spawnParticle(Particle.BLOCK_CRACK, location, 10, Bukkit.createBlockData(targetMaterial));
        }, time);

//        targetBlock.setType(Material.AIR);
        event.setCancelled(true);

        stack.setDurability((short) (stack.getDurability() - 1));

    }



}
