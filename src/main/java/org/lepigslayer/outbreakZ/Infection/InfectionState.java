package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.UUID;

public class InfectionState implements Serializable {
    private transient Player owner;
    private long lastHit = 0;
    private float infectionPercentage = 0;
    private float infectionRessitance = 100;

    public InfectionState(UUID uid) {
        owner= Bukkit.getPlayer(uid);
    }

    void setPlayer(Player p){
        owner = p;
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
        InfectionSystem.tryInfect(owner);
    }

    void changeInfection(float amount){
        if(isInfected())
            return;
        infectionPercentage += amount;
        infectionRessitance = Math.max(0, infectionPercentage);
        InfectionSystem.tryInfect(owner);
    }

    boolean isInfected(){
        return infectionPercentage >= infectionRessitance;
    }
}
