package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.lepigslayer.outbreakZ.Infection.Util.PlayerDiesEvent;
import org.lepigslayer.outbreakZ.OutbreakZ;
import org.lepigslayer.outbreakZ.Session.Session;
import org.lepigslayer.outbreakZ.Utils.TaskRunner;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.lepigslayer.outbreakZ.Infection.InfectionSystem.*;

public class InfectionEvents implements Listener {
    private static InfectionEvents instance;

    private BukkitRunnable scanner;

    public InfectionEvents(Plugin plugin) {
        instance = this;
        InfectionSystem.init(plugin);
    }

    @EventHandler
    public void sessionEnd(Session.SessionStateChangeEvent e){
        System.out.println(e.isEnded());
        if(e.isEnded()){
            InfectionSystem.saveStates();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void infectingPunch(EntityDamageByEntityEvent e) {
        if (!OutbreakZ.getSession().isOngoing()) return;

        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;

        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        boolean isVictimInfected = isInfected(victim);
        boolean isAttackerInfected = isInfected(attacker);

        if (isVictimInfected) {
            e.setDamage(e.getDamage() * INFECTION_DEFENSE_DEBUFF);
        }

        if (isAttackerInfected) {
            e.setDamage(e.getDamage() * INFECTION_DAMAGE_DEBUFF);

            if (!isVictimInfected) {
                InfectionState state = getInfectionState(victim);

                if (state.getLastHit() + HIT_COOLDOWN < System.currentTimeMillis()) {
                    state.hit();
                    state.changeInfection(HIT_AMOUNT);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void executionDeath(PlayerDiesEvent e){
        if (!OutbreakZ.getSession().isOngoing()){
            e.setCancelled(true);
            return;
        };
        processExecution(e);
        if(e.isCancelled()) return;
        processDeath(e);
    }

    private void processExecution(PlayerDiesEvent e){
        if(!e.isPlayerCaused()) return;

        Player victim = e.getPlayer();
        Player killer = e.getKiller();

        if(!isInfected(victim)) return;

        e.setCancelled(true);
        victim.setGameMode(GameMode.SPECTATOR);
        victim.sendTitle("§4§lEXECUTED!!!","§aYour game is over!");
        victim.getWorld().strikeLightningEffect(victim.getLocation());
    }

    private void processDeath(PlayerDiesEvent e){
        Player victim = e.getPlayer();
        Player killer = e.getKiller();

        long ressistanceRemoved = killer == null ? DEATH_LOSS : PLAYER_DEATH_LOSS;
        getInfectionState(victim).changeRessistance(-ressistanceRemoved);

        e.setCancelled(true);
        Location loc = Optional.ofNullable(victim.getRespawnLocation()).orElse(OutbreakZ.getWorld().getSpawnLocation());

        TaskRunner.runNextTick(()->victim.teleport(loc));

        victim.getActivePotionEffects().forEach(pt->victim.removePotionEffect(pt.getType()));
        victim.sendMessage("§7You Died");
        victim.setHealth(victim.getMaxHealth());
        victim.setFoodLevel(20);
        victim.setFireTicks(0);
        victim.setRemainingAir(victim.getMaximumAir());
        victim.setFreezeTicks(0);
    }

    public static void scanPlayers(){
        for(Player p:InfectionSystem.getInfectedPlayers()){
            List<Player> uninfected = p.getNearbyEntities(5,5,5).stream()
                    .filter(e->e instanceof Player)
                    .map(e->(Player) e)
                    .filter(e->!InfectionSystem.isInfected(e))
                    .toList();

            for(Player p2:uninfected){
                InfectionState state = getInfectionState(p2);
                state.changeInfection(PASSIVE_SPREAD_AMOUNT);
            }
        }
    }
}
