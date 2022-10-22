package org.emeraldcraft.mcfrc.rapidreact.fms;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.emeraldcraft.mcfrc.frc.entities.Robot;
import org.emeraldcraft.mcfrc.rapidreact.RapidReact;
import org.emeraldcraft.mcfrc.rapidreact.entities.Hub;
import org.emeraldcraft.mcfrc.rapidreact.fms.listeners.CargoScoreListener;
import org.emeraldcraft.mcfrc.rapidreact.fms.listeners.GameStateTasks;

import static java.util.logging.Level.INFO;

public class RRFMS {
    private final JavaPlugin plugin;
    @Getter
    private GameState gameState = GameState.NONE;
    @Getter
    private final RapidReactScore score = new RapidReactScore();
    private final RapidReact rapidReact;
    @Getter
    private Hub[] hubs;
    //Game Tasks
    private BukkitTask gameTimer;
    private GameStateTasks gameStateTask;
    @Getter
    private boolean isRunning = false;
    @Getter
    private int time = 150;
    public RRFMS(JavaPlugin plugin, RapidReact rapidReact) {
        this.plugin = plugin;
        this.rapidReact = rapidReact;
        Bukkit.getPluginManager().registerEvents(new CargoScoreListener(), plugin);
    }
    public void start(){
        rapidReact.getRobots().forEach(robot -> {
            robot.getPlayer().getInventory().clear();
            for (int i = 0; i < robot.getPlayer().getInventory().getSize(); i++) {
                if(i == 37) continue;
                ItemStack itemStack = new ItemStack(Material.CARROT_ON_A_STICK);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.displayName(Component.text(""));
                itemStack.setItemMeta(itemMeta);
                robot.getPlayer().getInventory().setItem(i, itemStack);
            }
        });
        hubs = new Hub[]{new Hub(Hub.HubLevel.UPPER), new Hub(Hub.HubLevel.LOWER)};
        setGameState(GameState.START);
        startGameTimer();
        startGameStateTask();
        isRunning = true;
    }
    private void startGameTimer(){
        gameTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(time == 135){
                setGameState(GameState.TELEOP);
            }
            if(time == 30){
                setGameState(GameState.WARNING);
            }
            if(time == 0){
                endGame(false);
            }
            time--;
        }, 0, 20);
    }
    private void startGameStateTask(){
        gameStateTask = new GameStateTasks();
        gameStateTask.runTaskTimer(plugin, 0, 5);
    }
    private void endGame(boolean abort){
        if(abort) {
            setGameState(GameState.ABORT);
        } else {
            setGameState(GameState.END);
        }
        for (Hub hub : hubs) {
            hub.remove();
        }
        isRunning = false;
        gameTimer.cancel();
        gameStateTask.cancel();
        Bukkit.getScheduler().runTaskLater(plugin,() -> {
            setGameState(GameState.NONE);
            for(Robot robot: rapidReact.getRobots()){
                robot.setDisabled(false);
                robot.getPlayer().getInventory().clear();
            }
            Bukkit.getLogger().log(INFO, "Fully ended");
            System.gc();
        }, 20 * 5);
    }
    public void setGameState(GameState state){
        gameState = state;
        rapidReact.getRobots().forEach(
                Robot::updateGameState
        );
    }
}
