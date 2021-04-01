package com.vanquil.staff;

import com.vanquil.staff.chat.command.SlowChatCommand;
import com.vanquil.staff.chat.command.UnSlowChatCommand;
import com.vanquil.staff.chat.events.ChatListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Staff extends JavaPlugin {
    private static Staff instance = null;

    @Override
    public void onEnable() {

        // logger
        getLogger().info("Has been enabled");

        // instance of plugin
        instance = this;

        // save configuration
        saveDefaultConfig();

        // chat handler

        // commands
        new SlowChatCommand(this);
        new UnSlowChatCommand(this);
        // listener
        new ChatListener(this);


    }

    @Override
    public void onDisable() {

        //logger
        getLogger().info("Has been disabled");
    }


    public static Staff getInstance() {
        return instance;
    }
}
