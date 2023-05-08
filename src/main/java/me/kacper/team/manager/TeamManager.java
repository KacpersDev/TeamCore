package me.kacper.team.manager;

import me.kacper.TeamCore;
import me.kacper.team.Team;
import me.kacper.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeamManager {

    private final TeamCore plugin;
    public static List<UUID> teamChat = new ArrayList<>();
    public static HashMap<UUID, String> invitation = new HashMap<>();

    public TeamManager(TeamCore plugin) {
        this.plugin = plugin;
    }

    public void createTeam(Team team){
        this.plugin.getTeams().set("team." + team.getName() + ".name", team.getName());
        this.plugin.getTeams().set("team." + team.getName() + ".leader", team.getLeader());
        this.plugin.getTeams().set("team." + team.getName() + ".members", team.getMembers());
        new Config(this.plugin, this.plugin.getTeamsFile(), this.plugin.getTeams(), "teams.yml").save();
    }

    public void removeTeam(String team) {
        this.plugin.getTeams().set("team." + team, null);
        new Config(this.plugin, this.plugin.getTeamsFile(), this.plugin.getTeams(), "teams.yml").save();
    }

    public boolean exists(String name) {
        return this.plugin.getTeams().contains("team." + name);
    }

    public boolean isInTeam(Player player) {
        if (this.plugin.getTeams().getConfigurationSection("team") == null) return false;
        for (final String teams : this.plugin.getTeams().getConfigurationSection("team").getKeys(false)) {
            List<String> members = this.plugin.getTeams().getStringList("team." + teams + ".members");
            if (this.plugin.getTeams().getString("team." + teams + ".leader").equalsIgnoreCase(player.getUniqueId().toString())
            || members.contains(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public String getTeamByPlayer(Player player){
        if (this.plugin.getTeams().getConfigurationSection("team") == null) return null;
        for (final String teams : this.plugin.getTeams().getConfigurationSection("team").getKeys(false)) {
            List<String> members = this.plugin.getTeams().getStringList("team." + teams + ".members");
            if (this.plugin.getTeams().getString("team." + teams + ".leader").equalsIgnoreCase(player.getUniqueId().toString())
                    || members.contains(player.getUniqueId().toString())) {
                return this.plugin.getTeams().getString("team." + teams + ".name");
            }
        }
        return null;
    }

    public String getLeaderByTeam(String team) {
        return this.plugin.getTeams().getString("team." + team + ".leader");
    }

    public boolean validate(String arg) {
        return arg.length() >= 1 && arg.length() <= 3;
    }

    public List<String> getTeamMembers(String teamByPlayer) {
        return this.plugin.getTeams().getStringList("team." + teamByPlayer + ".members");
    }

    public void leaveTeam(Player player, String teamByPlayer) {
        List<String> members = this.getTeamMembers(teamByPlayer);
        members.remove(player.getUniqueId().toString());
        this.plugin.getTeams().set("team." + teamByPlayer + ".members", members);
        new Config(this.plugin, this.plugin.getTeamsFile(), this.plugin.getTeams(), "teams.yml");

        List<String> total = this.getTeamMembers(teamByPlayer);
        total.add(this.plugin.getTeamManager().getLeaderByTeam(teamByPlayer));
        for (String string : total) {
            Bukkit.getPlayer(UUID.fromString(string)).hidePlayer(player);
        }
    }

    public void putMember(Player member, String team){
        List<String> members = this.getTeamMembers(team);
        members.add(member.getUniqueId().toString());
        this.plugin.getTeams().set("team." + team + ".members", members);
        new Config(this.plugin, this.plugin.getTeamsFile(), this.plugin.getTeams(), "teams.yml");

        List<String> total = this.getTeamMembers(team);
        total.add(this.plugin.getTeamManager().getLeaderByTeam(team));
        for (String string : total) {
            Bukkit.getPlayer(UUID.fromString(string)).showPlayer(member);
        }
    }
}
