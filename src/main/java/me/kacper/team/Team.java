package me.kacper.team;

import java.util.List;

public class Team {

    private final String leader;
    private final List<String> members;
    private final String name;

    public Team(String leader, String name, List<String> members){
        this.leader = leader;
        this.name = name;
        this.members = members;
    }

    public String getLeader() {
        return leader;
    }

    public String getName() {
        return name;
    }

    public List<String> getMembers() {
        return members;
    }
}
