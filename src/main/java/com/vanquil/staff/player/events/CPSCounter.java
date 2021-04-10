package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class CPSCounter extends BukkitRunnable {

    public void run() {

        if(Storage.cpsListeners.isEmpty()) {
            Storage.clicksInterval.clear();
            Storage.clicksCount.clear();
            return;
        }

        Set<OfflinePlayer> listeners = new HashSet<>();
        for(String uuid : Storage.cpsListeners) {
            listeners.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
        }

        for(OfflinePlayer listener : listeners) {
            if(listener.isOnline()) {
                Set<OfflinePlayer> players = new HashSet<>();
                for(String uuid : Storage.clicksCountRaw.keySet()) {
                    players.add(Bukkit.getOfflinePlayer(UUID.fromString(uuid)));
                }
                Iterator<OfflinePlayer> iterator = players.iterator();
                while(iterator.hasNext()) {
                    OfflinePlayer player = iterator.next();
                    if(Storage.clicksInterval.containsKey(player.getUniqueId().toString()) && Storage.clicksCount.containsKey(player.getUniqueId().toString())
                    && Storage.clicksCount.get(player.getUniqueId().toString()) > 0) {
                        if(Storage.clicksInterval.get(player.getUniqueId().toString()) <= System.currentTimeMillis()) {
                            Storage.clicksInterval.remove(player.getUniqueId().toString());
                            int count = 0;
                            if(Storage.clicksCountRaw.get(player.getUniqueId().toString()).contains(",")) {
                                for(String split : Storage.clicksCountRaw.get(player.getUniqueId().toString()).split(",")) {
                                    count += Integer.parseInt(split);
                                }
                                count /= Storage.clicksCountRaw.get(player.getUniqueId().toString()).split(",").length;
                            } else {
                                count += Integer.parseInt(Storage.clicksCountRaw.get(player.getUniqueId().toString()));
                            }
                            Storage.clicksCountRaw.remove(player.getUniqueId().toString());
                            Storage.clicksCount.remove(player.getUniqueId().toString());
                            String color = count >= Staff.getInstance().getConfig().getInt("CPS.warning") ? Staff.getInstance().getConfig().getString("CPS.warning_prefix") : "&e";
                            if(count > 0) {
                                listener.getPlayer().sendMessage(Utility.colorize("&c&lCPS>> &fPlayer &6" + player.getName() + " &c>> " + color + count + " click/second over 10 seconds"));
                            }
                            Storage.clicksCount.remove(player.getUniqueId().toString());
                        }
                    }
                }
                players = null;
                iterator = null;
            }else {
                listeners.remove(listener);
                Storage.cpsListeners.remove(listener.getUniqueId().toString());
                Bukkit.getScheduler().cancelTask(Storage.cpsTaskID.get(listener.getUniqueId().toString()));
                Storage.cpsTaskID.remove(listener.getUniqueId().toString());
            }

        }
        listeners = null;
    }
}
