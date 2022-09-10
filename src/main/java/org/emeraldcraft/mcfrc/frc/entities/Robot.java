package org.emeraldcraft.mcfrc.frc.entities;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.emeraldcraft.mcfrc.frc.FRCGame;
import org.emeraldcraft.mcfrc.rapidreact.fms.AllianceColor;
import org.emeraldcraft.mcfrc.rapidreact.fms.GameState;
import org.intellij.lang.annotations.Subst;

import java.util.logging.Level;

/**
 * Represents a base FRC robot.
 *
 * @see org.emeraldcraft.mcfrc.rapidreact.entities.RRRobot
 */
public abstract class Robot {
    @Getter
    protected final AllianceColor allianceColor;
    @Getter
    protected final Player player;
    @Getter
    @Setter
    private boolean disabled;

    @Getter
    private GameState gameState = GameState.NONE;

    public Robot(AllianceColor team, Player player) {
        allianceColor = team;
        this.player = player;
    }

    public void updateGameState() {
        GameState prev = gameState;
        gameState = FRCGame.getInstance().getFms().getGameState();

        if (prev != gameState) {
            @Subst("") String name = gameState.name().toLowerCase();
            Sound sound = Sound.sound(Key.key("rapidreact:gamestate." + name), Sound.Source.MUSIC, 1F, 1F);
            player.playSound(sound, Sound.Emitter.self());
            Bukkit.getLogger().log(Level.INFO, "Game state changed to " + name);
        }

    }

    public Component getFormattedName() {
        return Component.text(player.getName()).
                //If color is blue, then set the color to blue
                //else set the color to red
                        color(allianceColor == AllianceColor.BLUE ? TextColor.color(0, 0, 255) : TextColor.color(255, 0, 0));
    }

}
