package org.emeraldcraft.mcfrc.frc.entities;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.emeraldcraft.mcfrc.frc.MCFRCPlugin;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;

/**
 * Represents a base FRC robot.
 *
 * @see org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot
 */
public abstract class Robot {
    @Getter
    protected final Alliance allianceColor;
    @Getter
    protected final Player player;
    @Getter
    @Setter
    private boolean disabled = false;

    @Getter
    private ArmorStand entity;
    /**
     * Ranking point.
     */
    @Getter
    @Setter
    private int rankingPoints = 0;

    public Robot(Alliance team, Player player) {
        allianceColor = team;
        this.player = player;
    }

    /**
     * Updates the game state of the robot.
     * You probably shouldn't be calling this on your own.
     * <p>
     * It will play any sounds if the game state was updated
     */
    public abstract void updateGameState();

    public void spawnRobot(Location loc) {
        if(loc == null){
            loc = player.getLocation();
        }
        ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
        stand.setInvisible(true);
        stand.getEquipment().setHelmet(new ItemStack(Material.BLUE_STAINED_GLASS));
        stand.addPassenger(player);
        player.setInvisible(false);
        player.setGameMode(GameMode.ADVENTURE);
        entity = stand;
    }

    public void despawnRobot() {
        entity.remove();
        entity = null;
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void teleport(Location loc) {
        Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(MCFRCPlugin.class), () -> {
            //FIRST, remove passenger
            entity.removePassenger(this.player);
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(MCFRCPlugin.class), () -> {
                //Then teleport armorstand
                entity.teleport(loc);
                //Then remount player
                Bukkit.getScheduler().runTaskLater(JavaPlugin.getProvidingPlugin(MCFRCPlugin.class), () -> entity.addPassenger(this.player), 2);
            });

        });
    }

    /**
     * @return A formatted name with the color of the alliance of the player
     */
    public Component getFormattedName() {
        return Component.text(player.getName()).
                //If color is blue, then set the color to blue
                //else set the color to red
                        color(allianceColor == Alliance.BLUE ? TextColor.color(0, 0, 255) : TextColor.color(255, 0, 0));
    }

}
