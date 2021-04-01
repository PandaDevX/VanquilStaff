package com.vanquil.staff.chat.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class ChatListener implements Listener {

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

    @EventHandler
    public void onFilter(AsyncPlayerChatEvent e) {
        // on chat,

        // check permission
        if(e.getPlayer().hasPermission("staff.chat.admin")) return;

        // sensor words
        if(!Utility.sensor(Arrays.asList(e.getMessage()))) return;

        e.setCancelled(true);

        // alert player
        e.getPlayer().sendMessage(Utility.colorize("&c&lHey &fyou cannot swear"));

        // alert staffs
        for(Player player : Bukkit.getOnlinePlayers()) {

            // check if player is staff
            if(player.hasPermission("staff.chat.admin")) {

                // send message

                // components
                TextComponent textComponent = new TextComponent(Utility.colorize("&6&lmessage"));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(e.getMessage()).create()));

                TextComponent textComponent1 = new TextComponent(Utility.colorize("&c&lCensor: &fPlayer's "));
                textComponent1.addExtra(textComponent);

                TextComponent textComponent2 = new TextComponent(Utility.colorize("&f contains unwanted words"));
                textComponent1.addExtra(textComponent2);

                // send the message
                player.spigot().sendMessage(textComponent1);
                textComponent = null;
                textComponent1 = null;
                textComponent2 = null;
            }
        }
    }
}
