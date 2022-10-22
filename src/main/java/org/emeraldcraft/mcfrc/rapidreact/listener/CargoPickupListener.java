package org.emeraldcraft.mcfrc.rapidreact.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.utils.RapidUtils;

import java.util.Collection;

public class CargoPickupListener implements Listener {
    @EventHandler
    public void onPlayerMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            if (armorStand.getPassengers().isEmpty()) return;
            Entity entity = armorStand.getPassengers().get(0);
            if (entity instanceof Player player) {
                player.sendMessage("Found player on entity.");
                //Check to see if there are any entities near the armorstand
                Collection<Entity> entities = armorStand.getLocation().getNearbyEntities(2, 0, 2);
                int amount = entities.size();
                if(amount == 0) return;
                player.sendMessage("Found " + amount + " entities near the player.");

                /*
                    Attempt to raytrace from the front of the robot (armor stand), and see if it collides with anything.
                    Since its intake is at the front, it will give a realistic representation of what the robot can pick up.
                 */
                //Start by ray tracing from the robot and see what entities collide with it
                armorStand.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, armorStand.getLocation(), 30);
                RayTraceResult rayTraceResult = armorStand.getWorld().rayTraceEntities(armorStand.getLocation().subtract(0, -0.5, 0), player.getLocation().getDirection(), 0.5, 0.5, entity1 -> entity1 instanceof ArmorStand);

                if(rayTraceResult == null) return;
                player.sendMessage("Ray trace result: " + rayTraceResult);
                Entity entityCargo = rayTraceResult.getHitEntity();

                if(entityCargo == null) return;

                player.sendMessage("Found entity: " + entityCargo);
                player.sendMessage("Has passengers? " + entityCargo.getPassengers().isEmpty());
                player.sendMessage("Possible data: " + entityCargo.getPersistentDataContainer().get(FRCGame.getRapidReact().getKey(), PersistentDataType.STRING));
                entityCargo.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, rayTraceResult.getHitPosition().toLocation(entityCargo.getWorld()), 30);
                //Check to see if the entity is actually a cargo
                if(entityCargo instanceof ArmorStand stand){
                    //Check to see if the cargo is actually a cargo
                    if(RapidUtils.isCargo(stand) == null){
                        return;
                    }

                    Cargo cargo = RapidUtils.isCargo(stand);
                    player.sendMessage("Cargo has been found: " + cargo);
                    //Check to see if the cargo is on the ground
                    assert cargo != null;
                    if(!cargo.isOnGround()) return;
                    //Pick up the cargo
                    FRCGame.getRapidReact().getRobot(player).addCargo(cargo);
                    player.sendMessage("Picked up the cargo");
                }
                entities.remove(entityCargo);
                //Iterate over the list of entities, check to see if they are cargo, and if they are, push them away
                for (Entity  cargoEntity: entities) {
                    if(!(cargoEntity instanceof ArmorStand)) return;
                    Cargo cargo = RapidUtils.isCargo((ArmorStand) cargoEntity);
                    if(cargo == null) return;
                    player.sendMessage("Found cargo: " + cargo);
                    //Push le cargo away
                    if(!cargo.isOnGround()) return;
                    ArmorStand groundStand = cargo.getGroundStand();
                    Location loc = groundStand.getLocation();
                    final Vector v = groundStand.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(1).setY(0.1);
                    //groundStand.setVelocity(v);
                    player.sendMessage("Pushed the cargo away");

                }
            }
        }
    }
}
