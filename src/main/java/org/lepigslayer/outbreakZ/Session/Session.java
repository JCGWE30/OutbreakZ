package org.lepigslayer.outbreakZ.Session;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.lepigslayer.outbreakZ.Infection.InfectionEvents;
import org.lepigslayer.outbreakZ.Infection.InfectionSystem;
import org.lepigslayer.outbreakZ.OutbreakZ;
import org.lepigslayer.outbreakZ.Utils.TaskRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Session implements Listener {
    public static class SessionStateChangeEvent extends Event{
        private static final HandlerList HANDLERS = new HandlerList();
        private boolean isStarting;

        public SessionStateChangeEvent(boolean starting) {
            this.isStarting = starting;
        }

        public boolean isEnded() {
            return !isStarting;
        }

        @Override
        public HandlerList getHandlers() {
            return HANDLERS;
        }
    }
    public static final long SESSION_LENGTH = TimeUnit.MINUTES.toMillis(1);

    private int sessionNumber;
    private long sessionStart;
    private boolean isStarted;

    private BossBar bossBar;

    public void startSession(int number){
        this.sessionNumber = number;
        this.sessionStart = System.currentTimeMillis();
        this.isStarted = true;

        this.bossBar = Bukkit.createBossBar("§eTime Remaining: "+getFormattedTime(), BarColor.YELLOW, BarStyle.SOLID);

        InfectionSystem.addMissingPlayers();
        InfectionSystem.reloadNames();

        for(Player p: Bukkit.getOnlinePlayers()){
            bossBar.addPlayer(p);
            InfectionSystem.removeSessionInfection(p);
        }

        startSessionTicker();
        startInfectionTicker();
    }

    public boolean isOngoing(){
        return isStarted;
    }

    public int getSessionNumber(){
        return this.sessionNumber;
    }

    private long getTimeElapsed(){
        return System.currentTimeMillis() - sessionStart;
    }

    private long getTimeLeft(){
        return SESSION_LENGTH - getTimeElapsed();
    }

    private String getFormattedTime(){
        Date date = new Date(getTimeLeft());
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

    private void startSessionTicker(){
        TaskRunner.runTickTimer(TimeUnit.SECONDS.toMillis(1),new BukkitRunnable() {
            @Override
            public void run() {
                if(!isStarted){
                    this.cancel();
                    return;
                }

                float progress = (float) getTimeLeft() /SESSION_LENGTH;

                if(progress<=0){
                    isStarted = false;
                    OutbreakZ.callEvent(new SessionStateChangeEvent(true));
                }

                bossBar.setProgress(Math.max(progress,0));
                bossBar.setTitle("§eTime: "+getFormattedTime());
            }
        });
    }

    private void startInfectionTicker(){
        TaskRunner.runTickTimer(InfectionSystem.PASSIVE_SPREAD_TIMER,new BukkitRunnable() {
            @Override
            public void run() {
                if(!isStarted){
                    this.cancel();
                    return;
                }

                InfectionEvents.scanPlayers();
            }
        });
    }
}
