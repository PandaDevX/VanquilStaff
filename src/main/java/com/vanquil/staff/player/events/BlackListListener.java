package com.vanquil.staff.player.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class BlackListListener implements Listener {

    public BlackListListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        // check if not blacklisted
        if(!Storage.blackListPlayers.containsKey(e.getPlayer().getUniqueId().toString())) return;

        // do not allow to join the server
        e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, Storage.blackListPlayers.get(e.getPlayer().getUniqueId().toString()));
    }
}
