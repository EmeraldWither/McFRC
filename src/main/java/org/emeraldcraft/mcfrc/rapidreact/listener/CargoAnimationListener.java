package org.emeraldcraft.mcfrc.rapidreact.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.EulerAngle;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.utils.RapidUtils;

public class CargoAnimationListener implements Listener {
    @EventHandler
    public void onEntityMove(EntityMoveEvent event){
        if(event.getEntity() instanceof ArmorStand stand){
            if(RapidUtils.isCargo(stand) == null) return;
            Cargo cargo = RapidUtils.isCargo(stand);
            assert cargo != null;
            if(stand.isOnGround() && !cargo.isMoving()){
                stand.setGravity(false);
                stand.setHeadPose(new EulerAngle(0, 0, 0));
                cargo.land(stand.getLocation());
                return;
            }
            //Stand is in the air
            stand.setHeadPose(stand.getHeadPose().add(0.5, 0.0, 0.0));

        }
    }
}
