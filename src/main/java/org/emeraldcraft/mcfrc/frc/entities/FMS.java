package org.emeraldcraft.mcfrc.frc.entities;

import org.emeraldcraft.mcfrc.rapidreact.fms.GameState;

public interface FMS {
    GameState getGameState();
    int getTime();
    boolean isRunning();
    void start();

    void stop(boolean abort);
}
