package org.emeraldcraft.mcfrc.rapidreact.entities;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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

public class RRCargo implements Cargo{
    @Getter
    private final AllianceColor color;
    @Getter
    private ArmorStand armorStand;
    @Getter
    private final ItemStack ball;

    @Getter
    private boolean isOnGround = true;

    @Getter
    private final UUID uuid = UUID.randomUUID();

    @Getter
    @Setter
    private boolean moving = false;

    public RRCargo(AllianceColor color) {
        this.color = color;
        ball = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = ball.getItemMeta();
        meta.setDisplayName("%s Cargo".formatted(color.name()));
        ball.setItemMeta(meta);
    }


    public void spawnCargo(Location loc) {
        armorStand = (ArmorStand) loc.getWorld().spawnEntity(
                loc.add(0, 0.3, 0),
                EntityType.ARMOR_STAND
        );
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setCollidable(true);
        armorStand.setGravity(true);
        armorStand.getPersistentDataContainer().set(
                FRCGame.getRapidReact().getKey(),
                PersistentDataType.STRING,
                uuid.toString()
        );

        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(MCFRCPlugin.class), () -> {
            if(color == AllianceColor.BLUE) RapidUtils.changeSkin(ball, AllianceColor.BLUE_CARGO_HEAD);
            else RapidUtils.changeSkin(ball, AllianceColor.RED_CARGO_HEAD);
            armorStand.getEquipment().setHelmet(ball);
        });
    }
    public void launchCargo(RRRobot robot, boolean randomize){
        if(armorStand == null || !armorStand.isValid()) throw new IllegalStateException("Cargo is not spawned");

        Vector vel = armorStand.getEyeLocation().getDirection();
        vel.setY(0);
        if(randomize) {
            Random gen = new Random();
            vel.setX(vel.getX() + gen.nextDouble() * 0.5 - 0.15);
            vel.setY(vel.getY() + gen.nextDouble() * 0.5 - 0.15);
            vel.setZ(vel.getZ() + gen.nextDouble() * 0.5 - 0.15);
        }
        else{
            vel.add(new Vector(0, (robot.getShooterSpeed() / 20.0) + 0.5 , 0));
        }
        armorStand.setVelocity(vel);
        isOnGround = false;
    }
    public void land(Location loc){
        if(armorStand != null && armorStand.isValid()){
            loc = loc.subtract(0, 1.45, 0);
            if(loc.getBlock().getType().toString().contains("quartz")){
                return; // Don't land on quartz
            }
            armorStand.teleport(loc);
            isOnGround = true;
        }
    }

    public void despawnCargo() {
        armorStand.remove();
        armorStand = null;
    }

}
