package org.emeraldcraft.mcfrc.rapidreact.climb;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.emeraldcraft.mcfrc.frc.MCFRCPlugin;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;

import java.util.HashMap;

import static org.bukkit.Sound.UI_BUTTON_CLICK;

public class Climb {
    public static final HashMap<Integer, Integer> CLIMB_TIMES = new HashMap<>();
    public static final HashMap<Integer, Location> BLUE_CLIMB_LOCATIONS = new HashMap<>();

    static {
        CLIMB_TIMES.put(ClimbLevel.LOW, 10);
        CLIMB_TIMES.put(ClimbLevel.MID, 7);
        CLIMB_TIMES.put(ClimbLevel.HIGH, 5);
        CLIMB_TIMES.put(ClimbLevel.TRAVERSAL, 3);
        World world = Bukkit.getWorld("rapidreact");
        BLUE_CLIMB_LOCATIONS.put(ClimbLevel.LOW, new Location(world, 8, 1.3, -4.9));
        BLUE_CLIMB_LOCATIONS.put(ClimbLevel.MID, new Location(world, 8, 2.3, -2.95));
        BLUE_CLIMB_LOCATIONS.put(ClimbLevel.HIGH, new Location(world, 8, 3.1, -0.95));
        BLUE_CLIMB_LOCATIONS.put(ClimbLevel.TRAVERSAL, new Location(world, 8, 4.1, 1.95));


    }

    private boolean success = false;

    @Getter
    private int climbLevel = ClimbLevel.LOW;

    private int counter = 0;
    private int taskID = 0;
    private boolean first = true;
    private final RRRobot robot;

    private final Runnable onClimbSuccess;
    private final Runnable onClimbFailure;

    @Getter
    private boolean isClimbing = false;

    @Getter
    @Setter
    private ClimbStatus status = ClimbStatus.NONE;

    public Climb(RRRobot robot, Runnable onClimbSuccess, Runnable onClimbFailure) {
        this.robot = robot;
        this.onClimbSuccess = onClimbSuccess;
        this.onClimbFailure = onClimbFailure;
    }

    public void startClimb() {
        if(climbLevel == ClimbLevel.TRAVERSAL){
            return;
        }

        counter = 0;
        status = ClimbStatus.NONE;

        Player player = robot.getPlayer();
        isClimbing = true;

        int climbTime = Climb.CLIMB_TIMES.get(this.climbLevel);

        final int climbCounter = climbTime * 2;
        counter = 1;
        first = true;

        //Disable the robot while it is climbing
        robot.setDisabled(true);
        robot.getEntity().setGravity(false);
        robot.teleport(BLUE_CLIMB_LOCATIONS.get(this.climbLevel));
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                JavaPlugin.getProvidingPlugin(MCFRCPlugin.class),
                () -> {
                    if (counter > 100) {
                        onClimbSuccess.run();
                        Bukkit.getScheduler().cancelTask(taskID);
                        isClimbing = false;
                        status = ClimbStatus.SUCCESS;

                        player.setExp(0);
                        player.setLevel(0);
                        if (climbLevel != ClimbLevel.TRAVERSAL) climbLevel++;
                        robot.teleport(BLUE_CLIMB_LOCATIONS.get(this.climbLevel));
                        return;
                    }
                    if (counter % climbCounter == 0) {
                        player.playSound(Sound.sound(UI_BUTTON_CLICK, Sound.Source.MASTER, 1, 1));
                        success = false;
                        first = false;
                    }
                    //Check to see if it is NOT the first time, if so, then run checks to see if they are jumping
                    if(!first){
                        //Now check to see if the player is jumping
                        for (int i = 1; i <= climbTime; i++) {
                            if (counter % climbCounter == i) {
                                //Now check to see if they are jumping at the right time
                                if (robot.getClimbState().isJumping()) {
                                    success = true;
                                    break; //they are jumping at the right time
                                }
                            }
                        }
                        //Check to see if they are successful
                        if (counter % climbCounter > climbTime && robot.getClimbState().isJumping() || (counter % climbCounter > climbTime && !success)) {
                            //Climb Failure
                            onClimbFailure.run();
                            isClimbing = false;
                            status = ClimbStatus.FAILED;
                            climbLevel = ClimbLevel.LOW;
                            player.setExp(0);

                            Bukkit.getScheduler().cancelTask(taskID);
                            robot.setDisabled(false);

                            Location loc = robot.getEntity().getLocation();
                            loc.setY(1.0);
                            robot.despawnRobot();
                            robot.spawnRobot(loc);
                            return;
                        }
                    }


                    player.setLevel(0);
                    float xp = ((counter % climbCounter) / (float)climbCounter);
                    if(xp > 1) xp = 1;
                    player.setExp((float) (1.0 - xp));
                    counter++;
                },
                20,
                1
        );
    }

    public enum ClimbStatus {
        NONE,
        SUCCESS,
        FAILED
    }

    public static class ClimbLevel {
        public static int LOW = 0;
        public static int MID = 1;
        public static int HIGH = 2;
        public static int TRAVERSAL = 3;
    }
}
