package me.tinchx.levelrank.utils;

import me.tinchx.levelrank.LevelPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void runTaskAsync(Runnable runnable) {
        LevelPlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(LevelPlugin.getInstance(), runnable);
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        LevelPlugin.getInstance().getServer().getScheduler().runTaskLater(LevelPlugin.getInstance(), runnable, delay);
    }

    public static void runTaskTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(LevelPlugin.getInstance(), delay, timer);
    }

    public static void runTaskTimer(Runnable runnable, long delay, long timer) {
        LevelPlugin.getInstance().getServer().getScheduler().runTaskTimer(LevelPlugin.getInstance(), runnable, delay, timer);
    }

    public static void runTask(Runnable runnable) {
        LevelPlugin.getInstance().getServer().getScheduler().runTask(LevelPlugin.getInstance(), runnable);
    }
}