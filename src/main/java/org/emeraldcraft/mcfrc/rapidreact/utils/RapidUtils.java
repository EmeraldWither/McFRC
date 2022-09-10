package org.emeraldcraft.mcfrc.rapidreact.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;

import java.util.UUID;

public class RapidUtils {
    public static void changeSkin(ItemStack item, String base64Str) {
        if (item.getType() != Material.PLAYER_HEAD) return;
        final SkullMeta skull = (SkullMeta)item.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "Blue Cargo");
        profile.setProperty(
                new ProfileProperty("textures", base64Str)
        );
        profile.complete(true);
        skull.setPlayerProfile(profile);
        item.setItemMeta(skull);
    }
    public static Cargo isCargo(ArmorStand stand){
        if(stand.getPersistentDataContainer().has(FRCGame.getInstance().getKey(), PersistentDataType.STRING)){
            String uuid = stand.getPersistentDataContainer().get(FRCGame.getInstance().getKey(), PersistentDataType.STRING);
            for(Cargo cargo : FRCGame.getInstance().getCargos()){
                assert uuid != null;
                if(UUID.fromString(uuid).equals(cargo.getUuid())){
                    return cargo;
                }
            }
        }
        return null;
    }
}
