package com.vanquil.staff;

import com.vanquil.staff.chat.command.FilterAlertCommand;
import com.vanquil.staff.chat.command.FilterCommand;
import com.vanquil.staff.chat.command.SlowChatCommand;
import com.vanquil.staff.chat.command.UnSlowChatCommand;
import com.vanquil.staff.chat.events.ChatListener;
import com.vanquil.staff.player.command.BlackListCommand;
import com.vanquil.staff.player.command.FreezeCommand;
import com.vanquil.staff.player.command.ReviveCommand;
import com.vanquil.staff.player.command.VanishCommand;
import com.vanquil.staff.player.events.BlackListListener;
import com.vanquil.staff.player.events.DeathListener;
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

        /*
        chat handlers
         */

        // commands
        new SlowChatCommand(this);
        new UnSlowChatCommand(this);
        new FilterAlertCommand(this);
        new FilterCommand(this);

        // listener
        new ChatListener(this);

        /*
        player handlers
         */

        // commands
        new FreezeCommand(this);
        new BlackListCommand(this);
        new ReviveCommand(this);
        new VanishCommand(this);

        // listeners
        new FreezeListener(this);
        new BlackListListener(this);
        new DeathListener(this);


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
