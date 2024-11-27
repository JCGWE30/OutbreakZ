package org.lepigslayer.outbreakZ.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.lepigslayer.outbreakZ.Commands.Annotations.RegisterCommand;
import org.lepigslayer.outbreakZ.OutbreakZ;
import org.reflections.Reflections;

import java.util.Set;

public class CommandRegisterer {
    public static void loadCommands(OutbreakZ plugin) {
        Reflections reflections = new Reflections("org.lepigslayer.outbreakZ.Commands.CommandFiles");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RegisterCommand.class);

        for(Class<?> clazz:classes){
            try {
                PluginCommand command = plugin.getCommand(clazz.getAnnotation(RegisterCommand.class).value());

                if (DoubleCommand.class.isInstance(clazz)) {
                    Class<? extends DoubleCommand> executor = (Class<? extends DoubleCommand>) clazz;
                    DoubleCommand executorInstance = executor.getDeclaredConstructor().newInstance();

                    command.setExecutor(executorInstance);
                    command.setTabCompleter(executorInstance);
                } else {
                    Class<? extends CommandExecutor> executor = (Class<? extends CommandExecutor>) clazz;
                    CommandExecutor executorInstance = executor.getDeclaredConstructor().newInstance();

                    command.setExecutor(new ElevatedExecutor(executorInstance));
                }
            }catch(Exception ignored){}
        }
    }
}
