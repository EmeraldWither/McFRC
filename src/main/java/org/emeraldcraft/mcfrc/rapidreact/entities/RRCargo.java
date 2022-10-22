package org.emeraldcraft.mcfrc.rapidreact.entities;

import lombok.Getter;
import lombok.Setter;
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

public class RRCargo implements Cargo{
    @Getter
    private final AllianceColor color;
    @Getter
    private ArmorStand armorStand;
    /**
     * The armorstand which is carrying the armorstand entity which represents the cargo, which allows the cargo armor stand to be moved
     * May be null
     */
    @Getter
    private ArmorStand groundStand;
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

        //Ground entity stand
        groundStand = loc.getWorld().spawn(armorStand.getLocation().subtract(0, 3.3, 0), ArmorStand.class);
        groundStand.addPassenger(armorStand);
        groundStand.setGravity(false);
        groundStand.setInvisible(true);
        groundStand.getPersistentDataContainer().set(
                FRCGame.getRapidReact().getKey(),
                PersistentDataType.STRING,
                uuid.toString()
        );

        armorStand.getBoundingBox().expand(10);

        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getProvidingPlugin(MCFRCPlugin.class), () -> {
            if(color == AllianceColor.BLUE) RapidUtils.changeSkin(ball, AllianceColor.BLUE_CARGO_HEAD);
            else RapidUtils.changeSkin(ball, AllianceColor.RED_CARGO_HEAD);
            armorStand.getEquipment().setHelmet(ball);
        });
    }
    public void launchCargo(Player player, boolean randomize){
        if(armorStand == null || !armorStand.isValid()) throw new IllegalStateException("Cargo is not spawned");
        groundStand.removePassenger(armorStand);
        groundStand.remove();
        groundStand = null;

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
        armorStand.setVelocity(vel);
        isOnGround = false;
    }
    public void land(Location loc){
        if(armorStand != null && armorStand.isValid()){
            armorStand.teleport(loc.subtract(0, 1.45, 0));
            isOnGround = true;

            groundStand = loc.getWorld().spawn(armorStand.getLocation(), ArmorStand.class);
            groundStand.addPassenger(armorStand);
        }
    }

    public void despawnCargo() {
        armorStand.remove();
        armorStand = null;
        if(groundStand != null && groundStand.isValid()){
            groundStand.remove();
            groundStand = null;
        }

    }

}
