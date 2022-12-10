package org.emeraldcraft.mcfrc.rapidreact.fms.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.entities.Hub;
import org.emeraldcraft.mcfrc.rapidreact.fms.GameState;
import org.emeraldcraft.mcfrc.rapidreact.fms.utils.FMSUtils;
import org.emeraldcraft.mcfrc.rapidreact.utils.RapidUtils;

import java.util.Random;

public class CargoScoreListener implements Listener {
    @EventHandler
    public void onEntityMove(EntityMoveEvent event){
        if(FRCGame.getRapidReact().getFms().getGameState() == GameState.NONE) return;
        if(event.getEntity() instanceof ArmorStand armorStand) {
            Cargo cargo = RapidUtils.isCargo(armorStand);
            if(cargo == null) return;
            Hub hub = FMSUtils.getHub(cargo);
            if(hub == null) return;
            //Despawn cargo piece
            cargo.despawnCargo();
            //Check to see if it is the upper or lower hub
            if(hub.getLevel() == Hub.HubLevel.UPPER){
                FMSUtils.incrementScore(cargo.getColor(), 2);
            } else {
                FMSUtils.incrementScore(cargo.getColor(), 1);
            }

            Location randomCargoLoc = FRCGame.getRapidReact().getFms().getRedAllianceCargoLocations()[new Random().nextInt(FRCGame.getRapidReact().getFms().getRedAllianceCargoLocations().length)];
//            Randomize the location of the cargo by a little bit

            Random ran = new Random();
            randomCargoLoc.add(ran.nextInt(2) + 0.5, 0, ran.nextInt(2) + 0.5);
            cargo.spawnCargo(randomCargoLoc);
        }
    }
}
