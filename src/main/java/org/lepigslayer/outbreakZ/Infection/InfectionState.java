package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class InfectionState implements Serializable {
    private UUID owner;
    private long lastHit = 0;
    private float infectionPercentage = 0;
    private float infectionRessitance = 100;

    public InfectionState(UUID uid) {
        owner=uid;
    }

    long getLastHit() {
        return lastHit;
    }

    void hit(){
        lastHit = System.currentTimeMillis();
    }

    void changeRessistance(float amount){
        infectionRessitance += amount;
        infectionRessitance = Math.min(100,infectionRessitance);
        InfectionSystem.tryInfect(Bukkit.getPlayer(owner));
    }

    void changeInfection(float amount){
        if(isInfected())
            return;
        infectionPercentage += amount;
        infectionPercentage = Math.max(0, infectionPercentage);
        InfectionSystem.tryInfect(Bukkit.getPlayer(owner));
    }

    boolean isInfected(){
        return infectionPercentage >= infectionRessitance;
    }

    public float getDebugInfo(String key){
        switch (key){
            case "infection":
                return infectionPercentage;
            case "resistance":
                return infectionRessitance;
            default:
                throw new IllegalArgumentException();
        }
    }
}
