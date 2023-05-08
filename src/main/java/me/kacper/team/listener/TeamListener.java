package me.kacper.team.listener;

import me.kacper.TeamCore;
import me.kacper.team.manager.TeamManager;
import me.kacper.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class TeamListener implements Listener {

    private final TeamCore plugin;

    public TeamListener(TeamCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!TeamManager.teamChat.contains(event.getPlayer().getUniqueId())) return;
        List<String> members = this.plugin.getTeamManager().getTeamMembers(this.plugin.getTeamManager().getTeamByPlayer(event.getPlayer()));
        members.add(this.plugin.getTeamManager().getLeaderByTeam(this.plugin.getTeamManager().getTeamByPlayer(event.getPlayer())));

        for (final String member : members){
            if (member != null) {
                Bukkit.getPlayer(UUID.fromString(member)).sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.team-chat-prefix") + " " + event.getMessage()));
                Bukkit.getPlayer(UUID.fromString(member)).sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.chat-format")
                        .replace("%message%", event.getMessage())
                        .replace("%player%", event.getPlayer().getName())));
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (this.plugin.getTeamManager().getTeamByPlayer(online) != null
            && !this.plugin.getTeamManager().getTeamByPlayer(online).equalsIgnoreCase(
                    this.plugin.getTeamManager().getTeamByPlayer(event.getPlayer())
            )) {
                online.hidePlayer(event.getPlayer());
            }
        }
    }
}
