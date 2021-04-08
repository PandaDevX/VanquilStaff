package com.vanquil.staff.utility;

import org.bukkit.Bukkit;

public class Stalker {
    private int _maxDistance = Bukkit.getServer().getViewDistance() * 16 / 2;

    private String _name;

    private String _suspect;

    private int _distance;

    private final long beginTime = System.currentTimeMillis();

    public Stalker(String name, String suspect, int distance) {
        this._name = name;
        this._suspect = suspect;
        this._distance = (distance > 0) ? (Math.min(distance, this._maxDistance)) : 1;
    }

    public String getName() {
        return this._name;
    }

    public String getSuspectName() {
        return this._suspect;
    }

    public int getDistance() {
        return this._distance;
    }
}
