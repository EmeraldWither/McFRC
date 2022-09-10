package org.emeraldcraft.mcfrc.rapidreact.entities.hub;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Hub {
    @Getter
    private final HubLevel level;
    @Getter
    private ArmorStand hubEntity;

    public enum HubLevel {
        LOWER,
        UPPER
    }

    public Hub(HubLevel level) {
        this.level = level;
        hubEntity = (ArmorStand) Bukkit.getWorld("rapidreact").spawnEntity(
                level == HubLevel.LOWER ?
                        new Location(Bukkit.getWorld("rapidreact"), 0, 0, 0) :
                        new Location(Bukkit.getWorld("rapidreact"), 0, 10, 0)
                , EntityType.ARMOR_STAND);
        if(level == HubLevel.LOWER){
            //Apply the lowe hub texture to the armor stand
            hubEntity.setItem(EquipmentSlot.HEAD, new ItemStack(Material.GLASS));
        }
        else{
            //TODO Make upper hub texture
            hubEntity.setItem(EquipmentSlot.HEAD, new ItemStack(Material.HOPPER));
        }
    }
    public void remove(){
        hubEntity.remove();
        hubEntity = null;
    }
}
