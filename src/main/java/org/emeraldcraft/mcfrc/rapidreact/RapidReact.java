package org.emeraldcraft.mcfrc.rapidreact;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.MCFRCPlugin;
import org.emeraldcraft.mcfrc.rapidreact.climb.AllianceHanger;
import org.emeraldcraft.mcfrc.rapidreact.entities.Cargo;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRCargo;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;
import org.emeraldcraft.mcfrc.rapidreact.fms.Alliance;
import org.emeraldcraft.mcfrc.rapidreact.fms.RRFMS;

import java.util.ArrayList;
import java.util.List;

public class RapidReact {
    private final ArrayList<RRRobot> robots = new ArrayList<>();
    private final ArrayList<Cargo> cargos = new ArrayList<>();
    @Getter
    private final NamespacedKey key;

    @Getter
    private final RRFMS fms;

    @Getter
    private final World world = Bukkit.getWorld("rapidreact");

    @Getter
    private final AllianceHanger redHanger = new AllianceHanger(Alliance.RED);
    @Getter
    private final AllianceHanger blueHanger = new AllianceHanger(Alliance.BLUE);


    public RapidReact(MCFRCPlugin plugin) {
        this.key = new NamespacedKey(plugin, "rapidreact");
        fms = new RRFMS(plugin, this);
    }

    public RRRobot getRobot(Player player){
        return robots.stream()
                .filter(robot -> robot.getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }
    public Cargo createCargo(Alliance color){
        Cargo cargo = new RRCargo(color);
        cargos.add(cargo);
        return cargo;
    }
    public void createRobot(Alliance color, Player player){
        robots.add(new RRRobot(color, player));
    }

    public void removeCargo(Cargo cargo) {
        cargo.despawnCargo();
        cargos.remove(cargo);
    }
    public List<RRRobot> getRobots(){return new ArrayList<>(robots);}
    public List<Cargo> getCargos(){
        return new ArrayList<>(cargos);
    }
}
