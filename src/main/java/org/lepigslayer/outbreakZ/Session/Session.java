package org.lepigslayer.outbreakZ.Session;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
import org.lepigslayer.outbreakZ.Utils.TaskChain;
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

        public static HandlerList getHandlerList() {
            return HANDLERS;
        }

        @Override
        public HandlerList getHandlers() {
            return HANDLERS;
        }
    }
    public static final long SESSION_LENGTH = TimeUnit.SECONDS.toMillis(10);

    private int sessionNumber;
    private long sessionStart;
    private boolean isStarted;

    private BossBar bossBar;

    public void startSession(int number){
        new TaskChain(TimeUnit.SECONDS.toMillis(1))
                .addTask(()->broadcastInfo("§e3",null,Sound.BLOCK_NOTE_BLOCK_BASS))
                .addTask(()->broadcastInfo("§e2",null,Sound.BLOCK_NOTE_BLOCK_BASS))
                .addTask(()->broadcastInfo("§e1",null,Sound.BLOCK_NOTE_BLOCK_BASS))
                .addTask(()->{
                    broadcastInfo("§e§lBEGIN!!","§6The session has begun",Sound.ENTITY_ELDER_GUARDIAN_CURSE);
                    finalizeStart(number);
                }).execute();
    }

    private void finalizeStart(int number){
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

    private void endSession(){
        OutbreakZ.callEvent(new SessionStateChangeEvent(false));
        broadcastInfo("§e§lEnd Of Session!","§6The session has ended",Sound.ENTITY_ELDER_GUARDIAN_CURSE);
    }

    private void broadcastInfo(String title, String sub, Sound sound){
        for(Player p:Bukkit.getOnlinePlayers()){
            p.sendTitle(title,sub,10,70,20);
            if(sound!=null){
                p.playSound(p.getLocation(),sound,1,1);
            }
        }
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
        SimpleDateFormat formatter;
        if(TimeUnit.MILLISECONDS.toHours(getTimeLeft())>0){
            formatter = new SimpleDateFormat("hh:mm:ss");
        }else {
            formatter = new SimpleDateFormat("mm:ss");
        }
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
                    endSession();
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
