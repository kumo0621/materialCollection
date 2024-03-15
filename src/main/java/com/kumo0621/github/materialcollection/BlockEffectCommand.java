package com.kumo0621.github.materialcollection;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;

public class BlockEffectCommand implements CommandExecutor {
    private BlockHandler blockHandler;

    public BlockEffectCommand(BlockHandler blockHandler) {
        this.blockHandler = blockHandler;
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

        Location location = block.getLocation();

        if (command.getName().equalsIgnoreCase("setresetblock")) {
            blockHandler.activateEffectForLocation(location);
            player.sendMessage("ブロックに特別な効果を適用しました。");
        } else if (command.getName().equalsIgnoreCase("resetblockeffect")) {
            blockHandler.deactivateEffectForLocation(location);
            player.sendMessage("ブロックの特別な効果を解除しました。");
        }

        return true;
    }
}


