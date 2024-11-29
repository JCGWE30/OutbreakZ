package org.lepigslayer.outbreakZ.Commands.CommandFiles;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccess;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccessLevel;
import org.lepigslayer.outbreakZ.Commands.Annotations.RegisterCommand;

import java.util.*;

@RegisterCommand("testpacket")
@CommandAccess(CommandAccessLevel.OP)
public class TestPacketCommand implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        for(Player p: Bukkit.getOnlinePlayers()){
            PacketContainer pc = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

            pc.getIntegers().write(0, 3);

            pc.getStrings().write(0,"infected");

            pc.getSpecificModifier(Collection.class)
                            .write(0,Collections.singletonList(p.getName()));

            ProtocolLibrary.getProtocolManager().broadcastServerPacket(pc);
        }
        return true;
    }
}