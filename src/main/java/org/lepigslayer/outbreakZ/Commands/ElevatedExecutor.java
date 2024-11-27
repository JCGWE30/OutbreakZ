package org.lepigslayer.outbreakZ.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccess;
import org.lepigslayer.outbreakZ.Commands.Annotations.CommandAccessLevel;

public class ElevatedExecutor implements CommandExecutor {
    private CommandExecutor executor;
    private CommandAccessLevel access = CommandAccessLevel.ANYONE;

    ElevatedExecutor(CommandExecutor executor) {
        this.executor = executor;
        if(executor.getClass().isAnnotationPresent(CommandAccess.class)){
            access = executor.getClass().getAnnotation(CommandAccess.class).value();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        switch(access){
            case OP:
                if(!commandSender.isOp())
                    return true;
                break;
            case CONSOLE:
                if(commandSender instanceof Player)
                    return true;
                break;
        }
        return executor.onCommand(commandSender, command, s, strings);
    }
}
