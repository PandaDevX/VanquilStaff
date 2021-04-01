package com.vanquil.staff.chat.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private Staff plugin;

    public ChatListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }



    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // on chat,

        // check permission
        if(e.getPlayer().hasPermission("staff.chat.admin")) return;

        // if the player still has cool down, if default is not 0
        if(Storage.slow > 0) {
            if(Storage.coolDown.containsKey(e.getPlayer().getUniqueId().toString())) {

                // prevent player from chatting
                e.setCancelled(true);

                // seconds before ability to chat again
                int seconds = (int) (Storage.coolDown.get(e.getPlayer().getUniqueId().toString()) - System.currentTimeMillis()) / 1000;

                // remove if already 0 second
                if(seconds == 0) {
                    Storage.coolDown.remove(e.getPlayer().getUniqueId().toString());
                }

                // alert player
                e.getPlayer().sendMessage(Utility.colorize("&cYou can chat again in &6" + seconds + " second(s)"));
                return;
            }
        }

        // if the player has no permission add cool down
        Storage.coolDown.put(e.getPlayer().getUniqueId().toString(), System.currentTimeMillis() + (Storage.slow * 1000));
    }
}
