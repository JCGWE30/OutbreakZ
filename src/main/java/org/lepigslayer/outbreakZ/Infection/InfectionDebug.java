package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InfectionDebug {
    public static void runDebug(){
        for(int i=0;i<20;i++){Bukkit.broadcastMessage(" ");}
        for(Player p: Bukkit.getOnlinePlayers()){
            InfectionState state = InfectionSystem.getInfectionState(p);
            Bukkit.broadcastMessage("§6"+p.getName()+": §3 "+String.format("Infection: %s%%, Ressistance %s%%"
                    ,state.getDebugInfo("infection"),
                    state.getDebugInfo("resistance")));
        }
    }
}
