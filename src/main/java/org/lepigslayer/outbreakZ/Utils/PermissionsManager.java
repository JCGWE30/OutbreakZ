package org.lepigslayer.outbreakZ.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.security.Permission;
import java.util.HashMap;
import java.util.UUID;

public class PermissionsManager {
    public static final String VOICE_CHAT_LISTEN = "voicechat.listen";
    public static final String VOICE_CHAT_SPEAK = "voicechat.speak";
    public static final String VOICE_CHAT_GROUP = "voicechat.groups";
    public static final String VOICE_CHAT_ADMIN = "voicechat.admin";

    private static HashMap<UUID, PermissionAttachment> permissions = new HashMap<>();

    public static void setPermission(Player p,String perm,boolean state){
        PermissionAttachment attachment = permissions.computeIfAbsent(p.getUniqueId(),(u)-> OutbreakZ.newAttachment(p));
        attachment.setPermission(perm,state);
    }
}
