package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InfectionState {
    private transient Player owner;
    private long lastHit = 0;
    private float infectionPercentage = 0;
    private float infectionRessitance = 100;

    public InfectionState(UUID uid) {
        owner= Bukkit.getPlayer(uid);
    }

    long getLastHit() {
        return lastHit;
    }

    void hit(){
        lastHit = System.currentTimeMillis();
    }

    void changeRessistance(float amount){
        infectionRessitance += amount;
        InfectionSystem.tryInfect(owner);
    }

    void changeInfection(float amount){
        if(isInfected())
            return;
        infectionPercentage += amount;
        InfectionSystem.tryInfect(owner);
    }

    boolean isInfected(){
        return infectionPercentage >= infectionRessitance;
    }
}
