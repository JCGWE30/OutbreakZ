package org.lepigslayer.outbreakZ.Utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UtilEvents implements Listener {

    @EventHandler
    public void permissionStrip(PlayerJoinEvent e){
        PermissionsManager.setPermission(e.getPlayer(),PermissionsManager.VOICE_CHAT_GROUP,false);
    }
}
