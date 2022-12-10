package org.emeraldcraft.mcfrc.rapidreact.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;

public class CargoLaunchListener implements Listener {
    @EventHandler
    public void onCargoChargeUp(PlayerInteractEvent event){
        //Check to see if the player is holding down right click
        RRRobot robot = FRCGame.getRapidReact().getRobot(event.getPlayer());
        if (robot == null) return;
        if (robot.isDisabled()) {
            event.setCancelled(true);
            return;
        }
        if(event.getAction().isRightClick()){
            if(!event.hasItem()) return;
            if(event.hasBlock()) return;
            int speed = robot.getShooterSpeed() < 5 ? robot.getShooterSpeed() + 1 : 5;
            robot.setShooterSpeed(speed);
            robot.getPlayer().setLevel(speed);
        }
        if(event.getAction().isLeftClick()){
            robot.useCargo();
            robot.setShooterSpeed(0);

            robot.getPlayer().setExp(0);
            robot.getPlayer().setLevel(0);
        }
        event.setCancelled(true);
    }
}
