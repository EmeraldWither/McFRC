package org.emeraldcraft.mcfrc.rapidreact.entities;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;

import java.util.UUID;

public interface Cargo {
    void spawnCargo(Location loc);
    void land(Location loc);
    void despawnCargo();
    AllianceColor getColor();

    ArmorStand getArmorStand();
    ArmorStand getGroundStand();

    boolean isOnGround();
    boolean isMoving();

    ItemStack getBall();
    UUID getUuid();

}
