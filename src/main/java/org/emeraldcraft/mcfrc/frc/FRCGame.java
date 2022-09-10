package org.emeraldcraft.mcfrc.frc;

import lombok.Getter;
import org.emeraldcraft.mcfrc.rapidreact.RapidReact;

public class FRCGame {
    @Getter
    private static RapidReact instance;

    public static void setInstance(RapidReact instance) {
        if(FRCGame.instance == null){
            FRCGame.instance = instance;
        }
    }

}
