package org.emeraldcraft.mcfrc.frc.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;
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
                            FRCGame.getRapidReact().createRobot(Alliance.RED, player);
                        }
                        if (args[1].equalsIgnoreCase("blue")) {
                            FRCGame.getRapidReact().createRobot(Alliance.BLUE, player);
                        }
                    }
                }

//                if (args[0].equalsIgnoreCase("spawncargo")) {
//                    FRCGame.getRapidReact().createCargo(Alliance.BLUE).spawnCargo(player.getLocation());
//                }
                if(args[0].equalsIgnoreCase("gamestate")){
                    if(args.length > 1){
                        GameState gameState = GameState.valueOf(args[1].toUpperCase());
                        FRCGame.getRapidReact().getFms().setGameState(gameState);
                        sender.sendMessage("Game state set to " + gameState.name());
                    }
                }
                if(args[0].equalsIgnoreCase("start")){
                    FRCGame.getRapidReact().getFms().start();
                }
            }
        }
        return false;
    }
}
