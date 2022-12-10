package org.emeraldcraft.mcfrc.rapidreact.entities;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.frc.entities.Robot;
import org.emeraldcraft.mcfrc.rapidreact.RapidReact;
import org.emeraldcraft.mcfrc.rapidreact.climb.Climb;
import org.emeraldcraft.mcfrc.rapidreact.climb.ClimbListener;
import org.emeraldcraft.mcfrc.rapidreact.climb.ClimbState;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;
import org.emeraldcraft.mcfrc.rapidreact.fms.GameState;
import org.intellij.lang.annotations.Subst;

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
    @Getter
    @Setter
    private int shooterSpeed = 0;

    @Getter
    private GameState gameState = GameState.NONE;

    private final ClimbListener climbListener = new ClimbListener(JavaPlugin.getProvidingPlugin(RapidReact.class), this);
    @Getter
    private final ClimbState climbState = new ClimbState(this, climbListener);

    @Getter
    private final Climb climb = new Climb(this, () -> player.sendMessage("Climb Success"), () -> player.sendMessage("Climb Failed"));

    public RRRobot(Alliance team, Player player) {
        super(team, player);
    }

    @Override
    public void updateGameState() {
        GameState prev = this.gameState;
        gameState = FRCGame.getRapidReact().getFms().getGameState();

        if (prev != gameState) {
            @Subst("") String name = gameState.name().toLowerCase();
            Sound sound = Sound.sound(Key.key("rapidreact:gamestate." + name), Sound.Source.MUSIC, 1F, 1F);
            player.playSound(sound, Sound.Emitter.self());
            Bukkit.getLogger().log(Level.INFO, "Game state changed to " + name);
        }
    }

    public void addCargo(Cargo cargo) {
        cargo.despawnCargo();
        this.cargoAmount++;
        this.cargo.add(cargo);
        Inventory inv = player.getInventory();
        for (int i = 0; i < 35; i++) {
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
        ((RRCargo) cargo).launchCargo(this);
        this.cargo.remove(cargo);
        cargoAmount--;
        for (int i = 0; i < 35; i++) {
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
