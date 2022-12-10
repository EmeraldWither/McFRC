package org.emeraldcraft.mcfrc.rapidreact.climb;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;

public class AllianceHanger {
    @Getter
    private final Alliance alliance;

    @Getter
    private final Location corner1;
    @Getter
    private final Location corner2;

    public AllianceHanger(Alliance alliance) {
        this.alliance = alliance;
        World world = Bukkit.getWorld("rapidreact");
        if(alliance == Alliance.RED) {
            corner1 = new Location(world, 15, 0, -24);
            corner2 = new Location(world, 20, 6, -33);
        } else {
            corner1 = new Location(world, 11, 0, -6);
            corner2 = new Location(world, 6, 6, 1);
        }
    }
    public boolean isInside(Location l) {
        double x1 = Math.min(corner1.getX(), corner2.getX());
        double y1 = Math.min(corner1.getY(), corner2.getY());
        double z1 = Math.min(corner1.getZ(), corner2.getZ());

        double x2 = Math.max(corner1.getX(), corner2.getX());
        double y2 = Math.max(corner1.getY(), corner2.getY());
        double z2 = Math.max(corner1.getZ(), corner2.getZ());

        if(l.getX() >= x1 && l.getX() <= x2 &&
                l.getY() >= y1 && l.getY() <= y2 &&
                l.getZ() >= z1 && l.getZ() <= z2){
            return true;
        }
        return false;
    }
}
