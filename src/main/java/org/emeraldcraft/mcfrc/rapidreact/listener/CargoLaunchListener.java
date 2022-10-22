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
        if(event.getAction().isRightClick()){
            RRRobot robot = FRCGame.getRapidReact().getRobot(event.getPlayer());
            if(robot == null) return;
            robot.useCargo();
            event.getPlayer().sendMessage("Cargo launched!");
        }
    }
}
