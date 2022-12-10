package org.emeraldcraft.mcfrc.rapidreact.entities;

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
        hubEntity = (ArmorStand) Bukkit.getWorld("RapidReact").spawnEntity(
                level == HubLevel.LOWER ?
                        new Location(Bukkit.getWorld("RapidReact"), 13.406, 0.5, -16.118) :
                        new Location(Bukkit.getWorld("RapidReact"), 13.016, 6.3, -16.483)
                , EntityType.ARMOR_STAND);
        hubEntity.setGravity(false);
        hubEntity.setInvulnerable(true);
        hubEntity.setVisible(false);
        hubEntity.setCollidable(false);
        if(level == HubLevel.LOWER){
            //Apply the lowe hub texture to the armor stand
            hubEntity.setItem(EquipmentSlot.HEAD, new ItemStack(Material.GLASS));
        }
        else{
            //TODO Make upper hub texture
            hubEntity.setItem(EquipmentSlot.HEAD, new ItemStack(Material.GRAY_STAINED_GLASS));
        }
    }
    public void remove(){
        hubEntity.remove();
        hubEntity = null;
    }
}
