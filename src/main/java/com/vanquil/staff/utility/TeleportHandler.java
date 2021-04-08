package com.vanquil.staff.utility;

import com.vanquil.staff.Staff;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportHandler {

    private final Player player;

    private final World world;

    private int xCoord = -1;

    private int zCoord = -1;

    private int xF;

    private int yF;

    private int zF;

    public TeleportHandler(Player player, World world, int xCoord, int zCoord) {
        this.player = player;
        this.world = world;
        this.xCoord = xCoord;
        this.zCoord = zCoord;
    }

    public void teleport() {
        final Location location = getLocation();
        if (location == null) {
            this.player.sendTitle(Utility.colorize("&6&lRandom Teleport"), Utility.colorize("&cFailed to find a safe location"), 10, 70, 20);
            return;
        }
        if(!world.getChunkAt((int)location.getX(), (int) location.getZ()).isLoaded()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Staff.getInstance(), () -> world.getChunkAt((int)location.getX(), (int) location.getZ()).load());
        }
        player.sendTitle(Utility.colorize("&6&lRandom Teleport"), Utility.colorize("&aLooking for a safe place..."), 10, 70, 20);
        new BukkitRunnable() {
            public void run() {
                player.teleport(location);
                player.sendTitle(Utility.colorize("&6&lRandom Teleport"), Utility.colorize("&aSuccessfully found a safe place for you"), 10, 70, 20);
            }
        }.runTaskLater(Staff.getInstance(), 100L);
    }

    public int getX() {
        return this.xF;
    }

    public int getY() {
        return this.yF;
    }

    public int getZ() {
        return this.zF;
    }

    private void set(double x, double y, double z) {
        this.xF = (int)x;
        this.yF = (int)y;
        this.zF = (int)z;
    }

    protected Location getLocation() {
        int x = Utility.getRandom().nextInt(this.xCoord);
        int z = Utility.getRandom().nextInt(this.zCoord);
        x = randomizeType(x);
        z = randomizeType(z);
        int y = 63;
        Location location = TeleportUtils.safeizeLocation(new Location(this.world, x, y, z));
        if (location == null)
            return null;
        String block = location.getBlock().getType().name().toLowerCase().replace("_", " ");
        String biome = location.getBlock().getBiome().name().toLowerCase().replace("_", " ");
        if (Utility.getBlackListBiomes().contains(biome))
            location = TeleportUtils.safeizeLocation(getLocation());
        if (Utility.getBlackListBlocks().contains(block))
            location = TeleportUtils.safeizeLocation(getLocation());
        if (location == null)
            return null;
        set(location.getX(), location.getY(), location.getZ());

        block = null;
        biome = null;
        return location;
    }

    protected int randomizeType(int i) {
        int j = Utility.getRandom().nextInt(2);
        switch (j) {
            case 0:
                return -i;
            case 1:
                return i;
        }
        return -1;
    }
}