package com.vanquil.staff;

import com.vanquil.staff.chat.command.SlowChatCommand;
import com.vanquil.staff.chat.command.UnSlowChatCommand;
import com.vanquil.staff.chat.events.ChatListener;
import com.vanquil.staff.player.command.FreezeCommand;
import com.vanquil.staff.player.events.FreezeListener;
import com.vanquil.staff.utility.FileUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class Staff extends JavaPlugin {
    private static Staff instance = null;

    @Override
    public void onEnable() {

        // logger
        getLogger().info("Has been enabled");

        // instance of plugin
        instance = this;

        // save configurations

        // config.yml
        saveDefaultConfig();
        // words.yml
        FileUtil fileUtil = new FileUtil(this, "words.yml", true);
        fileUtil.createFile();
        getLogger().info("Storage loaded");
        fileUtil = null;

        // chat handler

        // commands
        new SlowChatCommand(this);
        new UnSlowChatCommand(this);
        // listener
        new ChatListener(this);

        // player handlers

        // commands
        new FreezeCommand(this);

        // listeners
        new FreezeListener(this);


        getLogger().info("Commands and Listeners loaded");


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
