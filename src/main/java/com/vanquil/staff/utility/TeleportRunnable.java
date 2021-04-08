package com.vanquil.staff.utility;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportRunnable implements Runnable {

    private Player player;
    private World world;
    private int maxX;
    private int maxZ;
    public TeleportRunnable(Player player, World world, int maxX, int maxZ) {
        this.player = player;
        this.world = world;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    @Override
    public void run() {
        TeleportHandler teleportHandler = new TeleportHandler(player, world, maxX, maxZ);
        teleportHandler.teleport();
        teleportHandler = null;
    }
}
