package org.lepigslayer.outbreakZ.Infection;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;
import org.lepigslayer.outbreakZ.OutbreakZ;

import java.io.*;
import java.util.*;
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
    private Plugin plugin;

    static InfectionState getInfectionState(Player attacker) {
        return instance.states.get(attacker.getUniqueId());
    }

    public static boolean isInfected(Player p) {
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

    static void execute(Player p){
        instance.turnDates.remove(p.getUniqueId());
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
        reloadNames();
    }

    public static void addMissingPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID id = player.getUniqueId();
            instance.states.putIfAbsent(id, new InfectionState(id));
        }
    }

    public static void init(Plugin plugin) {
        instance = new InfectionSystem();
        instance.plugin = plugin;
        instance.turnDates = InfectionLoader.loadTurnDates(plugin);
        instance.states = InfectionLoader.loadInfectionStates(plugin);
    }

    public static void reloadNames(){
        for(Player p:Bukkit.getOnlinePlayers()){
            p.getScoreboard().getTeams().forEach(Team::unregister);
            p.getScoreboard().registerNewTeam("infected").setColor(ChatColor.RED);
        }

        PacketContainer addPacket = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        addPacket.getIntegers().write(0, 3);

        addPacket.getStrings().write(0,"infected");

        addPacket.getSpecificModifier(Collection.class)
                .write(0,Bukkit.getOnlinePlayers().stream()
                        .filter(InfectionSystem::isInfected)
                        .map(Player::getName).toList()
                );

        ProtocolLibrary.getProtocolManager().broadcastServerPacket(addPacket,getInfectedPlayers());
    }

    public static void applyCure(Player player) {
        InfectionState state = getInfectionState(player);
        state.changeInfection(-100);
        state.changeRessistance(10);
    }

    public static void saveStates(){
        InfectionLoader.saveInfectionStates(instance.plugin,instance.states);
        InfectionLoader.saveTurnDates(instance.plugin,instance.turnDates);
    }
}
