package org.emeraldcraft.mcfrc.rapidreact.fms.utils;

import org.bukkit.entity.Entity;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.entities.Hub;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;
import org.emeraldcraft.mcfrc.rapidreact.fms.RapidReactScore;

public class FMSUtils {
    public static Hub getHub(Cargo cargo){
        Entity armorStand = cargo.getArmorStand();
        //Compare to armor stands for the hub
        for (Hub hub : FRCGame.getRapidReact().getFms().getHubs()) {
            if(armorStand.getLocation().getY() == hub.getHubEntity().getLocation().getY()){
                double distance = hub.getHubEntity().getLocation().distanceSquared(armorStand.getLocation());
                if(distance <= 1.5){
                    return hub;
                }
            }
        }
        return null;
    }
    public static void incrementScore(AllianceColor color, int score){
        RapidReactScore gamePoints = FRCGame.getRapidReact().getFms().getScore();
        if(color == AllianceColor.BLUE){
            gamePoints.setBlueAllianceScore(gamePoints.getBlueAllianceScore() + score);
        } else {
            gamePoints.setRedAllianceScore(gamePoints.getRedAllianceScore() + score);
        }
    }
}
