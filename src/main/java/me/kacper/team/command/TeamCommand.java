package me.kacper.team.command;

import me.kacper.TeamCore;
import me.kacper.team.Team;
import me.kacper.team.manager.TeamManager;
import me.kacper.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    private final TeamCore plugin;

    public TeamCommand(TeamCore plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length == 0) {
            usage(player);
            return false;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.invalid-name")));
                return false;
            } else {
                if (this.plugin.getTeamManager().isInTeam(player)) {
                    player.sendMessage(Color.translate(this.plugin.getConfiguration()
                            .getString("language.in-team")));
                    return false;
                }

                if (this.plugin.getTeamManager().exists(args[1])) {
                    player.sendMessage(Color.translate(this.plugin.getConfiguration()
                            .getString("language.exists")
                            .replace("%team%", args[1])));
                    return false;
                }

                if (!this.plugin.getTeamManager().validate(args[1])) {
                    player.sendMessage(Color.translate(this.plugin.getConfiguration()
                            .getString("language.validation-error")));
                    return false;
                }

                this.plugin.getTeamManager().createTeam(new Team(player.getUniqueId().toString(), args[1], null));
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.created")
                        .replace("%team%", args[1])));
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!this.plugin.getTeamManager().isInTeam(player)) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.not-in-team")));
                return false;
            }
            String leader = this.plugin.getTeamManager().getLeaderByTeam(this.plugin.getTeamManager().getTeamByPlayer(player));
            if (!(leader.equalsIgnoreCase(player.getUniqueId().toString()))) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.leader")));
                return false;
            }

            this.plugin.getTeamManager().removeTeam(this.plugin.getTeamManager().getTeamByPlayer(player));
            player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.removed")));
        } else if (args[0].equalsIgnoreCase("chat")) {
            if (!this.plugin.getTeamManager().isInTeam(player)) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.not-in-team")));
                return false;
            }

            if (TeamManager.teamChat.contains(player.getUniqueId())) {
                TeamManager.teamChat.remove(player.getUniqueId());
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.chat-disabled")));
            } else {
                TeamManager.teamChat.add(player.getUniqueId());
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.chat-enabled")));
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (!this.plugin.getTeamManager().isInTeam(player)) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.not-in-team")));
                return false;
            }

            if (this.plugin.getTeamManager().getLeaderByTeam(this.plugin.getTeamManager().getTeamByPlayer(player)).equalsIgnoreCase(player.getUniqueId().toString())) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.leave-own-team")));
                return false;
            }

            this.plugin.getTeamManager().leaveTeam(player, this.plugin.getTeamManager().getTeamByPlayer(player));
            player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.left-team")));
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (!this.plugin.getTeamManager().isInTeam(player)) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.not-in-team")));
                return false;
            }

            String leader = this.plugin.getTeamManager().getLeaderByTeam(this.plugin.getTeamManager().getTeamByPlayer(player));
            if (!(leader.equalsIgnoreCase(player.getUniqueId().toString()))) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.leader")));
                return false;
            }

            if (args.length == 1) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.null-player")));
                return false;
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.player-offline")));
                    return false;
                }

                if (TeamManager.invitation.get(target.getUniqueId()) == null) {
                    TeamManager.invitation.put(target.getUniqueId(), this.plugin.getTeamManager().getTeamByPlayer(player));
                } else {
                    TeamManager.invitation.replace(target.getUniqueId(), TeamManager.invitation.get(target.getUniqueId()),
                            this.plugin.getTeamManager().getTeamByPlayer(player));
                }
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.player-invited")
                        .replace("%player%", target.getName())));
            }
        } else if (args[0].equalsIgnoreCase("uninvite")) {
            if (!this.plugin.getTeamManager().isInTeam(player)) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.not-in-team")));
                return false;
            }

            String leader = this.plugin.getTeamManager().getLeaderByTeam(this.plugin.getTeamManager().getTeamByPlayer(player));
            if (!(leader.equalsIgnoreCase(player.getUniqueId().toString()))) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.leader")));
                return false;
            }
            if (args.length == 1) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.null-player")));
                return false;
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                if (!target.isOnline()) {
                    player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.player-offline")));
                    return false;
                }

                if (TeamManager.invitation.get(target.getUniqueId()).equalsIgnoreCase(this.plugin.getTeamManager()
                        .getTeamByPlayer(player))) {
                    TeamManager.invitation.remove(target.getUniqueId());
                }
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.player-uninvited")
                        .replace("%player%", target.getName())));
            }
        } else if (args[0].equalsIgnoreCase("accept")) {
            if (this.plugin.getTeamManager().isInTeam(player)) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration()
                        .getString("language.in-team")));
                return false;
            }

            if (!TeamManager.invitation.containsKey(player.getUniqueId())) {
                player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.no-invitation")));
                return false;
            }

            this.plugin.getTeamManager().putMember(player, TeamManager.invitation.get(player.getUniqueId()));
            player.sendMessage(Color.translate(this.plugin.getConfiguration().getString("language.accepted")
                    .replace("%team%", TeamManager.invitation.get(player.getUniqueId()))));
        }

        return true;
    }

    private void usage(Player player){
        for (final String lines : this.plugin.getConfiguration().getStringList("language.usage")) {
            player.sendMessage(Color.translate(lines));
        }
    }
}
