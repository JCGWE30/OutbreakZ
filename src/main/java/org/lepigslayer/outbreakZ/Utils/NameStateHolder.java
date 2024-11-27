package org.lepigslayer.outbreakZ.Utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.lepigslayer.outbreakZ.Infection.InfectionSystem;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.util.Collections;
import java.util.List;

public class NameStateHolder {
    public static void initalizeHolder(OutbreakZ plugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(!OutbreakZ.getSession().isOngoing()) return;
                PlayerInfoData data = event.getPacket().getPlayerInfoDataLists().read(1).get(0);
                WrappedGameProfile profile = data.getProfile().withName("§c"+data.getProfile().getName());
                profile.getProperties().putAll(data.getProfile().getProperties());
                PlayerInfoData newData = new PlayerInfoData(profile,data.getLatency(),data.getGameMode(),data.getDisplayName());
                event.getPacket().getPlayerInfoDataLists().write(1,Collections.singletonList(newData));
            }
        });
    }
}
