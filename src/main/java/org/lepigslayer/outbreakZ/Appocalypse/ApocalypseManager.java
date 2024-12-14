package org.lepigslayer.outbreakZ.Appocalypse;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.WrappedLevelChunkData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.util.HashMap;
import java.util.Map;

public class ApocalypseManager {
    private static ApocalypseManager instance;

    public static void init(){
        instance = new ApocalypseManager();
    }

    public static void startApocalypse(){
        Player p = Bukkit.getOnlinePlayers().stream().findFirst().get();
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHUNKS_BIOMES);
        packet.getChunkCoordIntPairs().write(0, new ChunkCoordIntPair(p.getLocation().getChunk().getX(),p.getLocation().getChunk().getZ()));
    }
}
