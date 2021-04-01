package com.vanquil.staff.data;

import com.vanquil.staff.Staff;

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

}
