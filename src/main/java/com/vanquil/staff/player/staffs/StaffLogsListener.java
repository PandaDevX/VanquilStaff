package com.vanquil.staff.player.staffs;

import com.vanquil.staff.data.Storage;
import com.vanquil.staff.utility.Utility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffLogsListener implements Listener {

    public StaffLogsListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogCmd(PlayerCommandPreprocessEvent e) {
        if(Utility.getStaffNames().contains(e.getPlayer().getName())) {


            for(String staff : Storage.staffLogger) {
                Player player = Bukkit.getPlayer(staff);
                if(player == null) {
                    Storage.staffLogger.remove(staff);
                    continue;
                }
                player.sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &3" + e.getPlayer().getName() + " &7used command: &a" + e.getMessage()));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerTeleportEvent e) {
        if(Utility.getStaffNames().contains(e.getPlayer().getName())) {


            for(String staff : Storage.staffLogger) {
                Player player = Bukkit.getPlayer(staff);
                if(player == null) {
                    Storage.staffLogger.remove(staff);
                    continue;
                }
                player.sendMessage(Utility.colorize("&a&lVanquil Logger &8>> &3" + e.getPlayer().getName() + " &7teleports to a new location"));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        if(!Utility.isFrozen(e.getPlayer())) return;

        for(String staff : Utility.getStaffNames()) {
            Player player = Bukkit.getPlayer(staff);
            if(player == null)
                continue;
            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&cban")));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ban " + e.getPlayer().getName()));

            TextComponent textComponent1 = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&a&lVanquil Staff &8>> &7Player &f" + e.getPlayer().getName() + " &7has gone offline being frozen. ")));

            player.spigot().sendMessage(textComponent1, textComponent);
            textComponent = null;
            textComponent1 = null;
        }
    }

}
