package org.emeraldcraft.mcfrc.frc.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.climb.Climb;
import org.jetbrains.annotations.NotNull;

public class TestClimbCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage("Starting climb testing");
            Climb test = new Climb(FRCGame.getRapidReact().getRobot(player),
                    () -> player.sendMessage("Climb finished"),
                    () -> player.sendMessage("Climb failed")
            );
            test.startClimb();
        }
        return false;
    }
}
