package com.kumo0621.github.materialcollection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MaterialCollection extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // プラグインが有効になった時の処理
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig(); // config.ymlがない場合はデフォルトのconfigを保存
    }
    Boolean set = true;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if(!set){
            return;
        }
        // ブロックが壊された時のイベント処理
        Location location = event.getBlock().getLocation();

        Material originalMaterial = event.getBlock().getType();
        FileConfiguration config = this.getConfig();

        // 設定ファイルからリセット時間を取得（デフォルトは10秒）
        int resetTime = config.getInt("reset-time", 10);

        new BukkitRunnable() {
            @Override
            public void run() {
                // 指定された時間後に実行される処理
                if (location.getWorld() != null) {
                    // ワールドがまだロードされているか確認
                    Block blockToReset = location.getWorld().getBlockAt(location);
                    // ブロックを元の材質に戻す
                    blockToReset.setType(originalMaterial);
                }
            }
        }.runTaskLater(this, resetTime * 20); // 20 ticks = 1 second
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみが使用できます。");
            return true;
        }

        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(5); // プレイヤーが見ているブロックを取得

        if (block == null) {
            player.sendMessage("対象のブロックが見つかりません。");
            return true;
        }

        if (command.getName().equalsIgnoreCase("setresetblock")) {
            set = true;
            player.sendMessage("ブロック復元を有効化しました。");
        } else if (command.getName().equalsIgnoreCase("resetblockeffect")) {
            set = false;
            player.sendMessage("ブロックの復元を無効化しました。");
        }

        return true;
    }
}
