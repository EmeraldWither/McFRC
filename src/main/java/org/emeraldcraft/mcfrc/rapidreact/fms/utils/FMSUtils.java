package org.emeraldcraft.mcfrc.rapidreact.fms.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.entities.Hub;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;
import org.emeraldcraft.mcfrc.rapidreact.fms.RapidReactScore;

import java.util.logging.Level;

public class FMSUtils {
    public static Hub getHub(Cargo cargo){
        Entity armorStand = cargo.getArmorStand();
        //Compare to armor stands for the hub
        for (Hub hub : FRCGame.getRapidReact().getFms().getHubs()) {
            Bukkit.getLogger().log(Level.INFO, "Checking hub: " + hub.getLevel().toString());
            double yDistance = Math.abs(armorStand.getLocation().getY() - hub.getHubEntity().getLocation().getY());
            if(yDistance <= 1){
                double distance = hub.getHubEntity().getLocation().distance(armorStand.getLocation());
                Bukkit.getLogger().log(Level.INFO, """
                        Distance from hub %s: %s""".formatted(hub.getLevel().toString(), distance));
                if(distance <= 2.5){
                    return hub;
                }
            }
        }
        return null;
    }
    public static void incrementScore(Alliance color, int score){
        RapidReactScore gamePoints = FRCGame.getRapidReact().getFms().getScore();
        if(color == Alliance.BLUE){
            gamePoints.setBlueAllianceScore(gamePoints.getBlueAllianceScore() + score);
        } else {
            gamePoints.setRedAllianceScore(gamePoints.getRedAllianceScore() + score);
        }
    }
}
