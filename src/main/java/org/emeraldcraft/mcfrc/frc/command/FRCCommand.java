package org.emeraldcraft.mcfrc.frc.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;
import org.emeraldcraft.mcfrc.rapidreact.fms.GameState;
import org.jetbrains.annotations.NotNull;

public class FRCCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("register")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("red")) {
                            FRCGame.getInstance().createRobot(AllianceColor.RED, player);
                        }
                        if (args[1].equalsIgnoreCase("blue")) {
                            FRCGame.getInstance().createRobot(AllianceColor.BLUE, player);
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("spawncargo")) {
                    FRCGame.getInstance().createCargo(AllianceColor.BLUE).spawnCargo(player.getLocation());
                }
                if(args[0].equalsIgnoreCase("gamestate")){
                    if(args.length > 1){
                        GameState gameState = GameState.valueOf(args[1].toUpperCase());
                        FRCGame.getInstance().getFms().setGameState(gameState);
                        sender.sendMessage("Game state set to " + gameState.name());
                    }
                }
                if(args[0].equalsIgnoreCase("start")){
                    FRCGame.getInstance().getFms().start();
                }
            }
        }
        return false;
    }
}