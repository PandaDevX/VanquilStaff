package com.vanquil.staff.utility;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportHandler {

    private final Player player;

    private final World world;

    private int xCoord;

    private int zCoord;

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
            player.sendMessage(Utility.colorize("&6&lRandomTP &cFailed to find a safe place"));
            return;
        }
        if(player.isFlying())
            player.setFlying(false);
        player.sendMessage(Utility.colorize("&6&lRandomTP &aLooking for a safe place..."));
        if(!world.getChunkAt((int)location.getX(), (int) location.getZ()).isLoaded()) {
            world.getChunkAt((int)location.getX(), (int) location.getZ()).load();
        }
        player.teleport(location);
        player.sendMessage(Utility.colorize("&6&lRandomTP &aSuccessfully teleported to a safe place"));
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