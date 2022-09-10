package org.emeraldcraft.mcfrc.rapidreact.fms;

import lombok.Data;

@Data
public class RapidReactScore {
    private int redAllianceScore = 0;
    private int redAllianceRP = 0;
    private int blueAllianceScore = 0;
    private int blueAllianceRP = 0;

    public void setBlueAllianceScore(int blueAllianceScore) {
        this.blueAllianceScore = blueAllianceScore;
        if(blueAllianceScore >= 20){
            this.blueAllianceRP++;
        }
    }

    public void setRedAllianceScore(int redAllianceScore) {
        this.redAllianceScore = redAllianceScore;
        if(redAllianceScore >= 20){
            this.redAllianceRP++;
        }
    }
}
