package com.kumo0621.github.materialcollection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockHandler implements Listener {
    private JavaPlugin plugin;
    private Set<Location> affectedBlocks;

    public BlockHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.affectedBlocks = new HashSet<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void loadAffectedBlocks() {
        FileConfiguration config = plugin.getConfig();
        List<String> locationStrings = config.getStringList("affectedBlocks");
        this.affectedBlocks = locationStrings.stream()
                .map(this::stringToLocation)
                .collect(Collectors.toSet());
    }

    public void saveAffectedBlocks() {
        List<String> locationStrings = affectedBlocks.stream()
                .map(this::locationToString)
                .collect(Collectors.toList());
        plugin.getConfig().set("affectedBlocks", locationStrings);
        plugin.saveConfig();
    }

    public void activateEffectForLocation(Location location) {
        affectedBlocks.add(location);
        saveAffectedBlocks();
    }

    public void deactivateEffectForLocation(Location location) {
        affectedBlocks.remove(location);
        saveAffectedBlocks();
    }

    private String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    private Location stringToLocation(String str) {
        String[] parts = str.split(",");
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (affectedBlocks.contains(location)) {
            int resetTime = plugin.getConfig().getInt("reset-time", 10); // Default to 10 seconds if not specified
            Material originalMaterial = event.getBlock().getType();
            loadAffectedBlocks();
            // Temporarily set to bedrock
            event.getBlock().setType(Material.BEDROCK);

            new BukkitRunnable() {
                @Override
                public void run() {
                    // Ensure the world is still loaded
                    if (location.getWorld() != null) {
                        Block blockToReset = location.getWorld().getBlockAt(location);
                        blockToReset.setType(originalMaterial);
                    }
                }
            }.runTaskLater(plugin, resetTime * 20); // 20 ticks = 1 second

            // Prevent the block from breaking
            event.setCancelled(true);
        }
    }
}

