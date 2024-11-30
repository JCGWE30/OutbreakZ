package org.lepigslayer.outbreakZ.Utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.lepigslayer.outbreakZ.OutbreakZ;

public class TaskRunner {
    private static TaskRunner instance;
    private OutbreakZ plugin;

    public static void initTaskRunner(OutbreakZ pluigin) {
        instance = new TaskRunner();
        instance.plugin = pluigin;
    }

    public static void runTimer(long delay, BukkitRunnable runnable){
        runnable.runTaskTimer(instance.plugin, 0,delay);
    }

    public static void runTickTimer(long delay, BukkitRunnable runnable){
        runTimer(delay/50,runnable);
    }

    public static void runTask(long delay, Runnable runnable){
        convert(runnable).runTaskLater(instance.plugin, delay);
    }

    public static void runTickTask(long delay, Runnable runnable){
        runTask(delay/50, runnable);
    }

    public static void runNextTick(Runnable runnable){
        convert(runnable).runTaskLater(instance.plugin, 1);
    }

    private static BukkitRunnable convert(Runnable runnable){
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }
}
