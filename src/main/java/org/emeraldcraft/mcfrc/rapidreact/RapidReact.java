package org.emeraldcraft.mcfrc.rapidreact;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.MCFRCPlugin;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;
import org.emeraldcraft.mcfrc.rapidreact.fms.FMS;

import java.util.ArrayList;

public class RapidReact {
    @Getter
    private final ArrayList<RRRobot> robots = new ArrayList<>();
    @Getter
    private final ArrayList<Cargo> cargos = new ArrayList<>();
    @Getter
    private final NamespacedKey key;

    @Getter
    private final FMS fms;


    public RapidReact(MCFRCPlugin plugin) {
        this.key = new NamespacedKey(plugin, "rapidreact");
        fms = new FMS(plugin, this);
    }

    public RRRobot getRobot(Player player){
        return robots.stream()
                .filter(robot -> robot.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }
    public Cargo createCargo(AllianceColor color){
        Cargo cargo = new Cargo(color);
        cargos.add(cargo);
        return cargo;
    }
    public void createRobot(AllianceColor color, Player player){
        robots.add(new RRRobot(color, player));
    }
}
