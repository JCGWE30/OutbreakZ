package org.lepigslayer.outbreakZ.Infection.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDiesEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final Player player;
    private final Player killer;

    public PlayerDiesEvent(Player player, Player killer) {
        this.player = player;
        this.killer = killer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public boolean isPlayerCaused(){
        return killer!=null;
    }

    public Player getPlayer(){
        return player;
    }

    public Player getKiller(){
        return killer;
    }
}
