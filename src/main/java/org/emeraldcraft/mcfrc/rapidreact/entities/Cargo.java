package org.emeraldcraft.mcfrc.rapidreact.entities;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.frc.MCFRCPlugin;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;
import org.emeraldcraft.mcfrc.rapidreact.utils.RapidUtils;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class Cargo {
    @Getter
    private final AllianceColor color;
    @Getter
    private ArmorStand entity;
    @Getter
    private final ItemStack ball;
    @Getter
    private final UUID uuid = UUID.randomUUID();

    @Getter
    private boolean isOnGround = false;

    public Cargo(AllianceColor color) {
        this.color = color;
        ball = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = ball.getItemMeta();
        meta.setDisplayName("%s Cargo".formatted(color.name()));
        ball.setItemMeta(meta);
    }


    public void spawnCargo(Location loc) {
        entity = (ArmorStand) loc.getWorld().spawnEntity(
                loc.add(0, 0.3, 0),
                EntityType.ARMOR_STAND
        );
        entity.setInvulnerable(true);
        entity.setVisible(false);
        entity.setCollidable(true);
        entity.setGravity(true);
        entity.getPersistentDataContainer().set(
                FRCGame.getInstance().getKey(),
                PersistentDataType.STRING,
                uuid.toString()
        );
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(MCFRCPlugin.class), () -> {
            if(color == AllianceColor.BLUE) RapidUtils.changeSkin(ball, AllianceColor.BLUE_CARGO_HEAD);
            else RapidUtils.changeSkin(ball, AllianceColor.RED_CARGO_HEAD);
            entity.getEquipment().setHelmet(ball);
        });
    }
    public void launchCargo(Player player, boolean randomize){
        if(entity == null || !entity.isValid()) throw new IllegalStateException("Cargo is not spawned");
        Vector vel = player.getEyeLocation().getDirection();
        if(randomize) {
            Random gen = new Random();
            vel.setX(vel.getX() + gen.nextDouble() * 0.5 - 0.15);
            vel.setY(vel.getY() + gen.nextDouble() * 0.5 - 0.15);
            vel.setZ(vel.getZ() + gen.nextDouble() * 0.5 - 0.15);
        }
        else{
            vel.add(new Vector(0, 1.3, 0));
        }
        vel.multiply(1.5);
        entity.setVelocity(vel);
        isOnGround = false;
    }
    public void pickupCargo(RRRobot robot){
        if(entity != null && entity.isValid()){
            despawnCargo();
            robot.addCargo(this);
            Bukkit.getLogger().log(Level.INFO, "Cargo picked up by " + robot.getPlayer().getName() + ". Amount of cargo %s. Total cargo registered %s".formatted(robot.getCargoAmount(), FRCGame.getInstance().getCargos().size()));
            robot.getPlayer().sendMessage("Picked up the cargo");
        }
    }
    public void land(Location loc){
        if(entity != null && entity.isValid()){
            entity.teleport(loc.subtract(0, 1.45, 0));
            isOnGround = true;
        }
    }

    public void despawnCargo() {
        entity.remove();
        entity = null;
    }

}
