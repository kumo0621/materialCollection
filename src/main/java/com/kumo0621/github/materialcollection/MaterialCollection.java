package com.kumo0621.github.materialcollection;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
public final class MaterialCollection extends JavaPlugin {
    private FileConfiguration config = getConfig();
    private BlockHandler blockHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
// コンフィグの初期設定
        config.addDefault("reset-time", 10);
        config.options().copyDefaults(true);
        saveConfig();

        // イベントリスナーとコマンドの登録
        blockHandler = new BlockHandler(this);
        getServer().getPluginManager().registerEvents(blockHandler, this);
        this.getCommand("setresetblock").setExecutor(new BlockEffectCommand(new BlockHandler(this)));
        this.getCommand("resetblockeffect").setExecutor(new BlockEffectCommand(new BlockHandler(this)));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public FileConfiguration getPluginConfig() {
        return config;
    }
}
