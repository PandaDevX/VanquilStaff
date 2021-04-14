package com.vanquil.staff;

import com.vanquil.staff.chat.command.FilterAlertCommand;
import com.vanquil.staff.chat.command.FilterCommand;
import com.vanquil.staff.chat.command.SlowChatCommand;
import com.vanquil.staff.chat.command.UnSlowChatCommand;
import com.vanquil.staff.chat.events.ChatListener;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.DatabaseManager;
import com.vanquil.staff.database.PinDatabase;
import com.vanquil.staff.player.command.*;
import com.vanquil.staff.player.events.*;
import com.vanquil.staff.player.staffs.*;
import com.vanquil.staff.player.staffs.admin.AdminModeCommand;
import com.vanquil.staff.player.staffs.admin.AdminModeListener;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.Utility;
import com.vanquil.staff.vanquilstaff.MainCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Staff extends JavaPlugin {
    private static Staff instance = null;

    @Override
    public void onEnable() {

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
        new ReportCommand(this);
        new ReportsCommand(this);
        new MainCommand(this);
        new StaffModeCommand(this);
        new AdminModeCommand(this);
        new StaffLogsCommand(this);
        new StaffHideCommand(this);

        // listeners
        new StaffLogsListener(this);
        new AdminModeListener(this);
        new StaffExamineListener(this);
        new StaffPlayerSelectionListener(this);
        new StaffModeListener(this);
        new OpenListener(this);
        new CloseListener(this);
        new HomeListener(this);
        new CasesListener(this);
        new FreezeListener(this);
        new BlackListListener(this);
        new DeathListener(this);
        new StaffsListener(this);
        new CPSListener(this);
        new StaffJoin(this);
        new StaffQuit(this);
        new StaffAuth(this);
        new ReportCreationListener(this);


        getLogger().info("Commands and Listeners loaded");

        // connect
        try {
            DatabaseManager.connect(getConfig());
            getLogger().info("Successfully connected to database");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info("No database found");
        }



        // cps counter
        CPSCounter counter = new CPSCounter();
        counter.runTaskTimerAsynchronously(this, 0, 1);

        // auth staffs
        if(DatabaseManager.getConnection() != null && !Bukkit.getOnlinePlayers().isEmpty()) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(Utility.getStaffNames().contains(player.getName())) {
                    PinDatabase pinDatabase = new PinDatabase(player);
                    if(!pinDatabase.isLoggedIn()) {
                        Utility.auth(player);
                    }
                    pinDatabase = null;
                }
            }
        }
    }

    @Override
    public void onDisable() {

        // close all storages
        Storage.staffAttempt.clear();
        Storage.playerIndexPin.clear();
        Storage.playerInventory.clear();
        Storage.playerExtras.clear();
        Storage.playerArmors.clear();
        Storage.staffMode.clear();
        Storage.staffArmors.clear();
        Storage.staffExtras.clear();
        Storage.staffInventoryContents.clear();
        Storage.staffLogger.clear();
        Storage.hideStaffs.clear();
        Storage.cpsListeners.clear();
        Storage.clicksCount.clear();
        Storage.clicksInterval.clear();
        Storage.coolDown.clear();
        Storage.cpsTaskID.clear();
        Storage.defaultCD.clear();
        Storage.blackListPlayers.clear();
        Storage.vanishedPlayers.clear();
        Storage.frozenPlayers.clear();
        Storage.filterAlerts.clear();
        Storage.playerSelection.clear();
        Storage.playerReport.clear();
        Storage.playerReportCoolDown.clear();
        Storage.playerReportEditing.clear();
        Storage.staffTool.clear();
        getLogger().info("All storage are closed");

        DatabaseManager.disconnect();
    }


    public static Staff getInstance() {
        return instance;
    }
}
