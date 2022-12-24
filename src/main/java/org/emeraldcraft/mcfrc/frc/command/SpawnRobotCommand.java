package org.emeraldcraft.mcfrc.frc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.jetbrains.annotations.NotNull;

public class SpawnRobotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            FRCGame.getRapidReact().getRobot(player).spawnRobot(null);
        }

        return false;
    }
}
