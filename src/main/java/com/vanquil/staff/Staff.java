package com.vanquil.staff;

import com.vanquil.staff.chat.command.FilterAlertCommand;
import com.vanquil.staff.chat.command.FilterCommand;
import com.vanquil.staff.chat.command.SlowChatCommand;
import com.vanquil.staff.chat.command.UnSlowChatCommand;
import com.vanquil.staff.chat.events.ChatListener;
import com.vanquil.staff.database.DatabaseManager;
import com.vanquil.staff.player.command.*;
import com.vanquil.staff.player.events.*;
import com.vanquil.staff.player.staffs.StaffJoin;
import com.vanquil.staff.player.staffs.StaffQuit;
import com.vanquil.staff.player.staffs.StaffsListener;
import com.vanquil.staff.utility.FileUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

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
        fileUtil = new FileUtil(this, "staffs.yml", true);
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
        new StaffsCommand(this);
        new CPSCommand(this);

        // listeners
        new FreezeListener(this);
        new BlackListListener(this);
        new DeathListener(this);
        new StaffsListener(this);
        new CPSListener(this);
        new StaffJoin(this);
        new StaffQuit(this);


        getLogger().info("Commands and Listeners loaded");

        // connect
        try {
            DatabaseManager.connect(getConfig());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        getLogger().info("Successfully connected to database");


    }

    @Override
    public void onDisable() {

        //logger
        getLogger().info("Has been disabled");

        DatabaseManager.disconnect();
    }


    public static Staff getInstance() {
        return instance;
    }
}
