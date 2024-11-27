package org.lepigslayer.outbreakZ.Commands.CommandFiles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccess;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccessLevel;
import org.lepigslayer.outbreakZ.Commands.DoubleCommand;
import org.lepigslayer.outbreakZ.Commands.Annotations.RegisterCommand;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.util.List;

@RegisterCommand("session")
@CommandAccess(CommandAccessLevel.OP)
public class SessionCommand extends DoubleCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        OutbreakZ.startNewSession(Integer.parseInt(strings[0]));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
