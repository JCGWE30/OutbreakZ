package org.lepigslayer.outbreakZ.Commands.CommandFiles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccess;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccessLevel;
import org.lepigslayer.outbreakZ.Commands.DoubleCommand;
import org.lepigslayer.outbreakZ.Commands.Annotations.RegisterCommand;
import org.lepigslayer.outbreakZ.Infection.InfectionEvents;
import org.lepigslayer.outbreakZ.Infection.InfectionSystem;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.util.List;


@RegisterCommand("infection")
@CommandAccess(CommandAccessLevel.OP)
public class InfectedCommand extends DoubleCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        InfectionSystem.infect((Player) commandSender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
