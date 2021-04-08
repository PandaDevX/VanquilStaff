package com.vanquil.staff.data;

import com.vanquil.staff.database.Report;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Storage {

    // chat control data
    public static HashMap<String, Integer> defaultCD = new HashMap<>();
    public static HashMap<String, Long> coolDown = new HashMap<>();

    // players
    public static Set<String> frozenPlayers = new HashSet<>();
    public static Set<String> filterAlerts = new HashSet<>();
    public static Set<String> vanishedPlayers = new HashSet<>();
    public static Set<String> cpsListeners = new HashSet<>();
    public static HashMap<String, Integer> cpsTaskID = new HashMap<>();
    public static HashMap<String, Integer> clicksCount = new HashMap<>();
    public static HashMap<String, Long> clicksInterval = new HashMap<>();
    public static HashMap<String, String> blackListPlayers = new HashMap<>();
    public static HashMap<String, ItemStack[]> playerInventory = new HashMap<>();
    public static HashMap<String, ItemStack[]> staffInventory = new HashMap<>();
    public static HashMap<String, Integer> playerIndexPin = new HashMap<>();
    public static HashMap<String, Integer> staffAttempt = new HashMap<>();
    public static HashMap<String, String> playerSelection = new HashMap<>();
    public static HashMap<String, Report> playerReport = new HashMap<>();
    public static HashMap<String, String> playerReportEditing = new HashMap<>();
    public static HashMap<String, Long> playerReportCoolDown = new HashMap<>();
    public static HashMap<String, String> staffTool = new HashMap<>();
}
