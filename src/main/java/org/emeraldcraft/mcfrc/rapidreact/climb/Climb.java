package org.emeraldcraft.mcfrc.rapidreact.climb;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.emeraldcraft.mcfrc.frc.MCFRCPlugin;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;

import java.util.HashMap;

import static org.bukkit.Sound.UI_BUTTON_CLICK;

public class Climb {
    public static final HashMap<Integer, Integer> climbTime = new HashMap<>();

    static {
        climbTime.put(ClimbLevel.LOW, 7);
        climbTime.put(ClimbLevel.MID, 5);
        climbTime.put(ClimbLevel.HIGH, 3);
        climbTime.put(ClimbLevel.TRAVERSAL, 2);
    }

    private boolean success = false;

    @Getter
    private int climbLevel = ClimbLevel.LOW;

    private int counter = 0;

    private int taskID = 0;

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
        counter = 0;
        status = ClimbStatus.NONE;

        Player player = robot.getPlayer();
        isClimbing = true;

        int climbTime = Climb.climbTime.get(this.climbLevel);

        final int climbCounter = climbTime * 2;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                JavaPlugin.getProvidingPlugin(MCFRCPlugin.class),
                () -> {
                    if (counter > 100) {
                        onClimbSuccess.run();
                        Bukkit.getScheduler().cancelTask(taskID);
                        isClimbing = false;
                        status = ClimbStatus.SUCCESS;
                        if (climbLevel != ClimbLevel.TRAVERSAL) climbLevel++;

                        return;
                    }
                    if (counter % climbCounter == 0) {
                        player.playSound(Sound.sound(UI_BUTTON_CLICK, Sound.Source.MASTER, 1, 1));
                        success = false;
                    }
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
                        onClimbFailure.run();
                        Bukkit.getScheduler().cancelTask(taskID);
                        isClimbing = false;
                        status = ClimbStatus.FAILED;
                        climbLevel = ClimbLevel.LOW;

                        player.setExp(0);
                        return;
                    }


                    player.setLevel(0);
//                    player.sendMessage("Coutner % 10: " + counter % climbTime);
//                    player.sendMessage("Climb Time: " + climbTime);
//                    player.sendMessage("EXP: " + ((counter % 10) / (float)climbTime));
                    float xp = ((counter % climbCounter) / (float)climbCounter);
                    if(xp > 1) xp = 1;
                    player.setExp(xp);
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
