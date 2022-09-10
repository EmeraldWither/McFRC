package org.emeraldcraft.mcfrc.frc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.emeraldcraft.mcfrc.frc.command.FRCCommand;
import org.emeraldcraft.mcfrc.frc.command.SpawnRobotCommand;
import org.emeraldcraft.mcfrc.frc.controllers.RobotController;
import org.emeraldcraft.mcfrc.rapidreact.RapidReact;
import org.emeraldcraft.mcfrc.rapidreact.listener.CargoLandListener;
import org.emeraldcraft.mcfrc.rapidreact.listener.CargoLaunchListener;
import org.emeraldcraft.mcfrc.rapidreact.listener.CargoPickupListener;

public class MCFRCPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null){
            getLogger().severe("ProtocolLib is not installed! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("frc").setExecutor(new FRCCommand());
        getCommand("spawnrobot").setExecutor(new SpawnRobotCommand());
        getServer().getPluginManager().registerEvents(new CargoLandListener(), this);
        getServer().getPluginManager().registerEvents(new CargoPickupListener(), this);
        getServer().getPluginManager().registerEvents(new CargoLaunchListener(), this);
        RapidReact game = new RapidReact(this);
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(new RobotController(this));
        FRCGame.setInstance(game);
    }
}
