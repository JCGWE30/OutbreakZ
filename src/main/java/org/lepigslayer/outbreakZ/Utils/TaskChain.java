package org.lepigslayer.outbreakZ.Utils;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaskChain {
    private final Map<Runnable,Long> tasks = new LinkedHashMap<>();
    private final long defaultDelay;
    public TaskChain(long defaultDelay) {
        this.defaultDelay = defaultDelay;
    }
    public TaskChain() {
        this.defaultDelay = 0;
    }

    public TaskChain addTask(Runnable runnable){
        tasks.put(runnable,defaultDelay);
        return this;
    }

    public TaskChain addTask(Runnable runnable, long delay){
        tasks.put(runnable,delay);
        return this;
    }

    public void execute(){
        long offset = 0;
        for(Map.Entry<Runnable,Long> entry : tasks.entrySet()){
            Runnable task = entry.getKey();
            long delay = entry.getValue();

            TaskRunner.runTickTask(offset,task);
            offset += delay;
        }
    }
}
