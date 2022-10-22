package org.emeraldcraft.mcfrc.frc.controllers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.frc.entities.Robot;

public class RobotController extends PacketAdapter {
    public RobotController(JavaPlugin plugin){
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer pc = event.getPacket();
        Player player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        assert vehicle != null;

        Robot robot = FRCGame.getRapidReact().getRobot(player);
        if(robot == null){
            return;
        }
        if(robot.isDisabled()) {
            vehicle.setVelocity(new Vector(0,0,0));
            return;
        }

        //Tank Drive
        float side = 0.0F;
        //side = pc.getFloat().read(0);
        float forw = pc.getFloat().read(1);
        Vector vel = getVelocityVector(vehicle.getVelocity(), player, side, forw);
        vehicle.setVelocity(vel);
        vehicle.setRotation(player.getEyeLocation().getYaw(), 0);
    }
    //Def not mine
    private Vector getVelocityVector(Vector vector, Player player, float side, float forw) {
        vector.setX(0.0);
        vector.setZ(0.0);
        Vector mot = new Vector(forw * -1.0, 0, side);
        if (mot.length() > 0.0) {
            mot.rotateAroundY(Math.toRadians(player.getLocation().getYaw() * -1.0F + 90.0F));
            mot.normalize().multiply(0.5F);
        }
        return mot;
    }

}
