package org.emeraldcraft.mcfrc.rapidreact.fms.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.frc.entities.Robot;

public class GameStateTasks extends BukkitRunnable {
    @Override
    public void run() {
        for(Robot robot : FRCGame.getRapidReact().getRobots()){
            long time = FRCGame.getRapidReact().getFms().getTime();
            Component actionBar;
            actionBar = Component.text(
                    "RED: %s | %s | BLUE: %s"
                            .formatted(
                                    FRCGame.getRapidReact().getFms().getScore().getRedAllianceScore(),
                                    robot.isDisabled() ? "DISABLED" : time,
                                    FRCGame.getRapidReact().getFms().getScore().getBlueAllianceScore()
                            )
            );

            robot.getPlayer().sendActionBar(actionBar);
        }
        switch (FRCGame.getRapidReact().getFms().getGameState()) {
            case START -> {
                //Robots are in AUTO mode
                for (Robot robot : FRCGame.getRapidReact().getRobots()) {
                    Player player = robot.getPlayer();
                    player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(Integer.MAX_VALUE, 1));
                }
            }
            case TELEOP -> {
                //Robots are in TELEOP mode
                for (Robot robot : FRCGame.getRapidReact().getRobots()) {
                    Player player = robot.getPlayer();
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                }
            }
            case END, ABORT -> {
                //Robots are in ENDGAME mode
                for (Robot robot : FRCGame.getRapidReact().getRobots()) {
                    robot.setDisabled(true);
                }
            }
        }
    }

}
