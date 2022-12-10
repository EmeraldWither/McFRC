package org.emeraldcraft.mcfrc.frc.entities;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    @Getter @Setter
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
    public void spawnRobot(){
        ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        stand.setInvisible(true);
        stand.getEquipment().setHelmet(new ItemStack(Material.BLUE_STAINED_GLASS));
        stand.addPassenger(player);
        player.setInvisible(false);
        player.setGameMode(GameMode.ADVENTURE);
        entity = stand;
    }
    public void despawnRobot(){
        entity.remove();
        entity = null;
        player.setGameMode(GameMode.SURVIVAL);
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
