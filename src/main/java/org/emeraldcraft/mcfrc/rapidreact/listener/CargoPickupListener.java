package org.emeraldcraft.mcfrc.rapidreact.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.utils.RapidUtils;

public class CargoPickupListener implements Listener {
    @EventHandler
    public void onPlayerMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            if (armorStand.getPassengers().isEmpty()) return;
            Entity entity = armorStand.getPassengers().get(0);
            if (entity instanceof Player player) {
                player.getLocation().getNearbyLivingEntities(2.5).forEach(livingEntity -> {
                    if (livingEntity instanceof ArmorStand stand) {
                        Cargo cargo = RapidUtils.isCargo(stand);
                        if (cargo == null) return;
                        if (!cargo.isOnGround()) return;
                        cargo.pickupCargo(
                                FRCGame.getInstance().getRobot(player)
                        );
                    }
                });
            }
        }
    }
}
