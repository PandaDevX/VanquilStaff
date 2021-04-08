package com.vanquil.staff.utility;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FollowRoster {
    private static final FollowRoster instance = new FollowRoster();

    private static HashMap<String, Stalker> ROSTER = new HashMap<>();

    public static FollowRoster getInstance() {
        return instance;
    }

    public Set<Stalker> getStalkersForSuspect(Player suspect) {
        Iterator<String> iterator = ROSTER.keySet().iterator();
        Set<Stalker> stalkers = new HashSet<Stalker>();
        while (iterator.hasNext()) {
            Stalker s = ROSTER.get(iterator.next());
            if (suspect.getName().equalsIgnoreCase(s.getSuspectName()))
                stalkers.add(s);
        }
        return stalkers;
    }

    public Stalker getStalker(String stalkerName) {
        Iterator<String> iterator = ROSTER.keySet().iterator();
        Stalker stalker = null;
        while (iterator.hasNext() && stalker == null) {
            Stalker s = ROSTER.get(iterator.next());
            if (stalkerName.equalsIgnoreCase(s.getName()))
                stalker = s;
        }
        return stalker;
    }

    public void follow(Player stalker, Player suspect, int distance) {
        ROSTER.put(stalker.getName(), new Stalker(stalker.getName(), suspect.getName(), distance));
    }

    public Stalker unfollow(Player stalker) {
        return ROSTER.remove(stalker.getName());
    }

    public int getSize() {
        return ROSTER.size();
    }

    public String[] toStringArray() {
        String[] result = new String[ROSTER.size() + 2];
        Iterator<String> iterator = ROSTER.keySet().iterator();
        int i = 0;
        result[i++] = ChatColor.GOLD + "===== Follow List ======";
        while (iterator.hasNext()) {
            String key = iterator.next();
            Stalker value = ROSTER.get(key);
            result[i++] = ChatColor.GOLD + "" +
                    ChatColor.RED + value.getName() +
                    ChatColor.GOLD + " is following " +
                    ChatColor.WHITE + value.getSuspectName() +
                    ChatColor.GOLD + " at distance " +
                    ChatColor.AQUA + value.getDistance();
        }
        result[i++] = ChatColor.GOLD + "===== End Of List =====";
        return result;
    }

    public void remove(Player player) {
        unfollow(player);
        removeStalkersForSuspect(player);
    }

    private void removeStalkersForSuspect(Player suspect) {
        Iterator<String> iterator = ROSTER.keySet().iterator();
        while (iterator.hasNext()) {
            Stalker s = ROSTER.get(iterator.next());
            if (suspect.getName().equalsIgnoreCase(s.getSuspectName()))
                ROSTER.remove(s.getName());
        }
    }

    public boolean isSuspect(String suspectName) {
        Iterator<String> iterator = ROSTER.keySet().iterator();
        while (iterator.hasNext()) {
            Stalker s = ROSTER.get(iterator.next());
            if (suspectName == s.getSuspectName())
                return true;
        }
        return false;
    }
}
