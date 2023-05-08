package me.kacper.utils;

import me.kacper.TeamCore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private final TeamCore plugin;
    private final File file;
    private final FileConfiguration configuration;
    private final String directory;

    public Config(TeamCore plugin, File file, FileConfiguration configuration, String directory){
        this.plugin = plugin;
        this.file = file;
        this.configuration = configuration;
        this.directory = directory;
    }

    public void create(){
        if (!(this.file.exists())) {
            this.file.getParentFile().mkdir();
            this.plugin.saveResource(this.directory, false);
        }

        try {
            this.configuration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.configuration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
