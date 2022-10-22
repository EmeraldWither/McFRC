package org.emeraldcraft.mcfrc.frc.command;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpawnRobotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            ArmorStand stand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            stand.setInvisible(true);
            stand.getEquipment().setHelmet(new ItemStack(Material.BLUE_STAINED_GLASS));
            stand.addPassenger(player);
            player.setInvisible(false);
            player.setGameMode(GameMode.ADVENTURE);
        }

        return false;
    }
}
