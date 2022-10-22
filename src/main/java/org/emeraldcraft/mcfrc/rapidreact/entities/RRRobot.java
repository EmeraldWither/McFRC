package org.emeraldcraft.mcfrc.rapidreact.entities;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.emeraldcraft.mcfrc.frc.entities.Robot;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Represents a Rapid-React Robot
 */
public class RRRobot extends Robot {
    @Getter
    private int cargoAmount = 0;
    private final ArrayList<Cargo> cargo = new ArrayList<>(2);

    public RRRobot(AllianceColor team, Player player) {
        super(team, player);
    }

    public void addCargo(Cargo cargo) {
        cargo.despawnCargo();
        this.cargoAmount++;
        this.cargo.add(cargo);
        Inventory inv = player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (inv.getItem(i) == null || Objects.requireNonNull(inv.getItem(i)).getType() == Material.CARROT_ON_A_STICK) {
                inv.setItem(i, cargo.getBall());
                break;
            }
        }
        Bukkit.getLogger().log(Level.INFO, "Cargo added to " + player.getName());
    }

    public void useCargo() {
        if (cargo.stream()
                .findFirst()
                .isPresent()) useCargo(cargo.get(0));
    }

    public void useCargo(Cargo cargo) {
        if (cargo == null) return;
        cargo.spawnCargo(player.getLocation().add(0, 1.5, 0));
        ((RRCargo) cargo).launchCargo(player, false);
        this.cargo.remove(cargo);
        cargoAmount--;
        for (int i = 0; i < 36; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;
            if (item.getType() == Material.PLAYER_HEAD) {
                ItemStack replaceItem = new ItemStack(Material.CARROT_ON_A_STICK);
                ItemMeta itemMeta = replaceItem.getItemMeta();
                itemMeta.displayName(Component.text(""));
                replaceItem.setItemMeta(itemMeta);
                player.getInventory().setItem(i, replaceItem);
                return;
            }
        }
    }
}
