package me.tinchx.levelrank;

import lombok.Getter;
import me.tinchx.levelrank.commands.ReputationCommands;
import me.tinchx.levelrank.config.BasicConfigurationFile;
import me.tinchx.levelrank.database.MongoDB;
import me.tinchx.levelrank.listener.ReputationListener;
import me.tinchx.levelrank.profile.Profile;
import me.tinchx.levelrank.profile.ProfileListener;
import me.tinchx.levelrank.utils.command.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class LevelPlugin extends JavaPlugin {

    private BasicConfigurationFile settings, messages;
    private MongoDB mongoDB;

    @Override
    public void onEnable() {
        load();
    }

    @Override
    public void onDisable() {
        mongoDB.close();
    }

    private void load() {
        settings = new BasicConfigurationFile(this, "settings");
        messages = new BasicConfigurationFile(this, "messages");

        (mongoDB = new MongoDB()).connect();

        Bukkit.getOnlinePlayers().forEach(player -> new Profile(player.getUniqueId()));

        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new ProfileListener(), this);
        manager.registerEvents(new ReputationListener(), this);

        CommandFramework framework = new CommandFramework(this);
        framework.registerCommands(new ReputationCommands());
        framework.registerHelp();
    }

    public static LevelPlugin getInstance() {
        return getPlugin(LevelPlugin.class);
    }
}