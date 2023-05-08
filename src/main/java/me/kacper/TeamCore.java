package me.kacper;

import me.kacper.team.command.TeamCommand;
import me.kacper.team.listener.TeamListener;
import me.kacper.team.manager.TeamManager;
import me.kacper.utils.Color;
import me.kacper.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class TeamCore extends JavaPlugin {

    private final TeamManager teamManager = new TeamManager(this);
    private final File configuration = new File(getDataFolder(), "configuration.yml");
    private final FileConfiguration configConfiguration = new YamlConfiguration();
    private final File teams = new File(getDataFolder(), "teams.yml");
    private final FileConfiguration teamsConfiguration = new YamlConfiguration();

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Bukkit.getConsoleSender().sendMessage(Color.translate("&7---&a---&7--- &fLOADING CONFIGURATIONS &7---&a---&7---"));
        this.configuration();
        Bukkit.getConsoleSender().sendMessage(Color.translate("&7---&a---&7--- &fLOADED CONFIGURATIONS &7---&a---&7---"));
        Bukkit.getConsoleSender().sendMessage(Color.translate("&7---&a---&7--- &fLOADING COMMANDS &7---&a---&7---"));
        this.command();
        Bukkit.getConsoleSender().sendMessage(Color.translate("&7---&a---&7--- &fLOADED COMMANDS &7---&a---&7---"));
        Bukkit.getConsoleSender().sendMessage(Color.translate("&7---&a---&7--- &fLOADING LISTENERS &7---&a---&7---"));
        this.listener();
        Bukkit.getConsoleSender().sendMessage(Color.translate("&7---&a---&7--- &fLOADED LISTENERS &7---&a---&7---"));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(Color.translate("&aPlugin enabled in " + (System.currentTimeMillis() - start)) + " ms");
    }

    @Override
    public void onDisable() {
    }

    private void configuration(){
        new Config(this, configuration, configConfiguration, "configuration.yml").create();
        new Config(this, teams, teamsConfiguration, "teams.yml").create();
    }

    private void listener(){
        Arrays.asList(
                new TeamListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void command(){
        this.getCommand("team").setExecutor(new TeamCommand(this));
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public FileConfiguration getTeams() {
        return teamsConfiguration;
    }

    public FileConfiguration getConfiguration(){
        return this.configConfiguration;
    }

    public File getTeamsFile(){ return this.teams; }

    public File getConfigurationFile(){ return this.configuration; }
}
