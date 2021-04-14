package com.vanquil.staff.chat.events;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.FileUtil;
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

    private Staff plugin;

    public ChatListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;
    }



    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // on chat,

        // check permission
        if(e.getPlayer().hasPermission("staff.chat.admin")) return;

        // if the player still has cool down, if default is not 0
        if(Storage.defaultCD.getOrDefault("vanquil", plugin.getConfig().getInt("ChatControl.default")) > 0) {
            if(Storage.coolDown.containsKey(e.getPlayer().getUniqueId().toString())) {

                // seconds before ability to chat again
                int seconds = (int)(Storage.coolDown.get(e.getPlayer().getUniqueId().toString()) - System.currentTimeMillis()) / 1000;

                // remove if already 0 second
                if(seconds > 0) {
                    // prevent player from chatting
                    e.setCancelled(true);

                    // alert player
                    e.getPlayer().sendMessage(Utility.colorize(plugin.getConfig().getString("ChatControl.message")
                            .replace("{cooldown}", String.valueOf(seconds))));
                    return;
                }
                Storage.coolDown.remove(e.getPlayer().getUniqueId().toString());
            }
        }

        // if the player has no permission add cool down
        Storage.coolDown.put(e.getPlayer().getUniqueId().toString(), System.currentTimeMillis() + (Storage.defaultCD.getOrDefault("vanquil", plugin.getConfig().getInt("ChatControl.default")) * 1000));
    }

    @EventHandler
    public void onFilter(AsyncPlayerChatEvent e) {
        // check if enabled
        FileUtil storage = new FileUtil(plugin, "words.yml", true);
        if(!storage.get().getBoolean("Enable")) return;
        storage = null;

        // on chat,

        // check permission
        if(e.getPlayer().hasPermission("staff.chat.admin")) return;

        // sensor words
        if(!Utility.sensor(Arrays.asList(e.getMessage()))) return;

        e.setCancelled(true);

        // alert player
        // open file
        FileUtil fileUtil = new FileUtil(plugin, "words.yml", true);
        e.getPlayer().sendMessage(Utility.colorize(fileUtil.get().getString("Message")));
        // close file
        fileUtil = null;

        // alert staffs
        for(Player player : Bukkit.getOnlinePlayers()) {

            // performance stuff
            if(!Storage.filterAlerts.isEmpty())
                break;

            // check if player is receiving alerts
            if(Storage.filterAlerts.contains(player.getUniqueId().toString())) {

                // send message

                // components
                TextComponent textComponent = new TextComponent(Utility.colorize("&6&lmessage"));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(e.getMessage()).create()));

                TextComponent textComponent1 = new TextComponent(Utility.colorize("&a&lVanquil &8>> &f" + e.getPlayer().getDisplayName() + "'s "));
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
