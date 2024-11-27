package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InfectionSystem{
    public static final long DEATH_LOSS = 5;
    public static final long PLAYER_DEATH_LOSS = 10;

    public static final long HIT_COOLDOWN = TimeUnit.MINUTES.toMillis(5);
    public static final float HIT_AMOUNT = 10;

    public static final float PASSIVE_DECAY_AMOUNT = 1;
    public static final long PASSIVE_DECAY_TIMER = TimeUnit.MINUTES.toMillis(10);

    public static final float PASSIVE_SPREAD_AMOUNT = 20;
    public static final long PASSIVE_SPREAD_TIMER = TimeUnit.SECONDS.toMillis(1);

    public static final float SESSION_LOSS = -10;

    public static final float INFECTION_DAMAGE_DEBUFF = 0.85f;
    public static final float INFECTION_DEFENSE_DEBUFF = 1.25f;

    private static InfectionSystem instance;

    private HashMap<UUID, Integer> turnDates;
    private HashMap<UUID, InfectionState> states;

    static InfectionState getInfectionState(Player attacker) {
        return instance.states.get(attacker.getUniqueId());
    }

    static boolean isInfected(Player p) {
        return getInfectedPlayers().contains(p);
    }

    public static List<Player> getInfectedPlayers() {
        return instance.turnDates.keySet().stream().map(Bukkit::getPlayer).toList();
    }

    public static void tryInfect(Player p){
        InfectionState state = instance.states.get(p.getUniqueId());

        if(state.isInfected())
            infect(p);
    }

    public static void removeSessionInfection(Player p){
        getInfectionState(p).changeInfection(SESSION_LOSS);
    }

    public static void infect(Player p){
        p.playSound(p, Sound.ENTITY_ENDERMAN_STARE,1f,0.5f);
        p.sendMessage("§c§lYou feel a cursed influence throughout your body, it wont be long before you become one of them. Your fate has been sealed");
        if(!getInfectedPlayers().isEmpty()){
            p.sendMessage("§c§lYou sense a familiar scent in the air. You are not the first. Your fellow infected are:");
            for(Player pl:getInfectedPlayers()){
                p.sendMessage("§a "+pl.getName());
                pl.playSound(p, Sound.ENTITY_ENDERMAN_SCREAM,1f,0.5f);
                pl.sendMessage("§c§lA sharp sense radiates throughout your mind, another victim has fallen to the plague. Your numbers continue to grow.");
                pl.sendMessage("§a"+p.getName()+" is now infected");
            }
        }else{
            p.sendMessage("§c§lYou soon realize that you are alone in this cursed world, spread your influence, and hunt down humanity.");
        }
        instance.turnDates.put(p.getUniqueId(), OutbreakZ.getSession().getSessionNumber()+2);
    }

    public static void addMissingPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID id = player.getUniqueId();
            instance.states.putIfAbsent(id, new InfectionState(id));
        }
    }

    public static void init(Plugin plugin) {
        instance = new InfectionSystem();
        instance.turnDates = InfectionLoader.loadTurnDates(plugin);
        instance.states = InfectionLoader.loadInfectionStates(plugin);
    }
}
