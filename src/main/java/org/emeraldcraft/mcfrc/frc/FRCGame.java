package org.emeraldcraft.mcfrc.frc;

import lombok.Getter;
import org.emeraldcraft.mcfrc.rapidreact.RapidReact;

public class FRCGame {
    @Getter
    private static RapidReact rapidReact;

    public static void setRapidReact(RapidReact rapidReact) {
        if(FRCGame.rapidReact == null){
            FRCGame.rapidReact = rapidReact;
        }
    }

}
