package org.lepigslayer.outbreakZ;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.lepigslayer.outbreakZ.Commands.CommandRegisterer;
import org.lepigslayer.outbreakZ.Infection.InfectionEvents;
import org.lepigslayer.outbreakZ.Infection.Util.EventWrapper;
import org.lepigslayer.outbreakZ.Session.Session;
import org.lepigslayer.outbreakZ.Utils.TaskRunner;
import org.lepigslayer.outbreakZ.Utils.UtilEvents;

public final class OutbreakZ extends JavaPlugin {
    private static Session currentSession;
    private static OutbreakZ instance;
    private static World overworld;

    @Override
    public void onEnable() {
        instance = this;
        currentSession = new Session();
        overworld = Bukkit.getWorld("world");

        TaskRunner.initTaskRunner(this);

        getServer().getPluginManager().registerEvents(new EventWrapper(),this);
        getServer().getPluginManager().registerEvents(new InfectionEvents(this),this);
        getServer().getPluginManager().registerEvents(new UtilEvents(),this);
        getServer().getPluginManager().registerEvents(currentSession,this);

        CommandRegisterer.loadCommands(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void startNewSession(int sessionNumber){
        currentSession.startSession(sessionNumber);
    }

    public static Session getSession(){
        return currentSession;
    }

    public static void callEvent(Event event){
        instance.getServer().getPluginManager().callEvent(event);
    }

    public static PermissionAttachment newAttachment(Player player){
        return player.addAttachment(instance);
    }

    public static World getWorld(){
        return overworld;
    }

    public static void sendEvent(Event event){
        instance.getServer().getPluginManager().callEvent(event);
    }
}
