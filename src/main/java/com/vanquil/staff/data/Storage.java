package com.vanquil.staff.data;

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

}
