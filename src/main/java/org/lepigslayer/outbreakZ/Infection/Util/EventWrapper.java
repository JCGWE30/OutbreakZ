package org.lepigslayer.outbreakZ.Infection.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.lepigslayer.outbreakZ.OutbreakZ;

public class EventWrapper implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if(p.getHealth()-e.getDamage()<=0){
            PlayerDiesEvent death = new PlayerDiesEvent(p,p.getKiller());
            OutbreakZ.sendEvent(death);
            e.setCancelled(death.isCancelled());
        }
    }
}
