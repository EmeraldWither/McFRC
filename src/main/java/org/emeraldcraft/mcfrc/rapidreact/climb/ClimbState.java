package org.emeraldcraft.mcfrc.rapidreact.climb;

import lombok.Getter;
import org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot;

public class ClimbState {
    @Getter
    private final RRRobot robot;
    private final ClimbListener climbListener;

    public ClimbState(RRRobot robot, ClimbListener climbListener) {
        this.robot = robot;
        this.climbListener = climbListener;
    }
    public boolean isJumping() {
        return climbListener.isJumping();
    }
    public int getClimbLevel() {
        return robot.getClimb().getClimbLevel();
    }

    public boolean isClimbing() {
        return robot.getClimb().isClimbing();
    }
}
