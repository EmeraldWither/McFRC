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
        for(Robot robot : FRCGame.getInstance().getRobots()){
            long time = FRCGame.getInstance().getFms().getTime();
            Component actionBar = Component.text(
                    "RED: %s | %s | BLUE: %s"
                            .formatted(
                                    FRCGame.getInstance().getFms().getScore().getRedAllianceScore(),
                                    time,
                                    FRCGame.getInstance().getFms().getScore().getBlueAllianceScore()
                            )
            );
            robot.getPlayer().sendActionBar(actionBar);
        }
        switch(FRCGame.getInstance().getFms().getGameState()){
            case START:
                //Robots are in AUTO mode
                for(Robot robot : FRCGame.getInstance().getRobots()){
                    Player player = robot.getPlayer();
                    player.setWalkSpeed(0.5F);
                    player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(Integer.MAX_VALUE, 1));
                }
                break;
            case TELEOP:
                //Robots are in TELEOP mode
                for(Robot robot : FRCGame.getInstance().getRobots()){
                    Player player = robot.getPlayer();
                    player.setWalkSpeed(1F);
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                }
                break;
            case END:
                //Robots are in END mode
                for(Robot robot : FRCGame.getInstance().getRobots()){
                    robot.setDisabled(true);
                }
                break;
        }
    }

}
