package org.emeraldcraft.mcfrc.rapidreact.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CargoLaunchListener implements Listener {
    @EventHandler
    public void onLeftClick(PlayerInteractEvent event){
        //Check to see if the player is holding down right click
        if(event.getAction().isRightClick()){
            event.getPlayer().sendRawMessage("You are holding down right click!");
            event.getPlayer().giveExp(7);
        }
    }
}
