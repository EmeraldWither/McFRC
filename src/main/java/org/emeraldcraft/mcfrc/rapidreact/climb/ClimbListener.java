package org.emeraldcraft.mcfrc.rapidreact.climb;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;

public class ClimbListener extends PacketAdapter{
    private final RRRobot robot;
    private boolean jumping = false;

    @Getter
    private Alliance climbZone = null;

    public ClimbListener(JavaPlugin plugin, RRRobot robot) {
        super(plugin, PacketType.Play.Client.STEER_VEHICLE);

        this.robot = robot;
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(this);
    }
    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer pc = event.getPacket();
        Player player = event.getPlayer();
        RRRobot robot = FRCGame.getRapidReact().getRobot(player);

        if(robot != this.robot) return;

        //Check to see if it inside the climb zone
        boolean red = FRCGame.getRapidReact().getRedHanger().isInside(player.getLocation());
        boolean blue = FRCGame.getRapidReact().getBlueHanger().isInside(player.getLocation());

        if(red) climbZone = Alliance.RED;
        else if(blue) climbZone = Alliance.BLUE;
        else climbZone = null;

        //Check to see if the player is jumping
        jumping = pc.getBooleans().read(0);


        if(robot.getClimbState().isClimbing()) return;

        //Start their climb process
        if(climbZone != null && jumping && robot.getClimb().getStatus().equals(Climb.ClimbStatus.NONE)) {
            player.sendMessage("Prepare to climb! You will be climbing on level " + robot.getClimb().getClimbLevel());
            robot.getClimb().startClimb();
        }
        if(climbZone == null){
            robot.getClimb().setStatus(Climb.ClimbStatus.NONE);
        }

    }
    public boolean isJumping() {
        return this.jumping;
    }
}
