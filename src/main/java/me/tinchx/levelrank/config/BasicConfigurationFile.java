package me.tinchx.levelrank.config;

import lombok.Getter;
import me.tinchx.levelrank.utils.ColorText;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class BasicConfigurationFile {
    private File file;
    private YamlConfiguration configuration;

    public BasicConfigurationFile(JavaPlugin plugin, String name, boolean overwrite) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        plugin.saveResource(name + ".yml", overwrite);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public BasicConfigurationFile(JavaPlugin plugin, String name) {
        this(plugin, name, false);
    }

    public boolean contains(String path) {
        return configuration.contains(path);
    }

    public String getString(String path) {
        if (this.configuration.contains(path)) {
            return ColorText.translate(configuration.getString(path));
        }
        return null;
    }

    public String getStringOrDefault(String path, String or) {
        String toReturn = this.getString(path);
        return (toReturn == null) ? or : toReturn;
    }

    public int getInteger(String path) {
        if (contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }

    public boolean getBoolean(String path) {
        return contains(path) && this.configuration.getBoolean(path);
    }

    public double getDouble(String path) {
        if (contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }

    public Object get(String path) {
        if (contains(path)) {
            return this.configuration.get(path);
        }
        return null;
    }

    public List<String> getStringList(String path) {
        if (contains(path)) {
            return this.configuration.getStringList(path);
        }
        return null;
    }

    public void save() {
        try {
            getConfiguration().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
