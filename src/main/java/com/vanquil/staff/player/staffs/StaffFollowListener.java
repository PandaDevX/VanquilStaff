package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FollowRoster;
import com.vanquil.staff.utility.Stalker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StaffFollowListener implements Listener {

    private Staff plugin;

    private static final List<Material> SAFE_TO_SHARE = new ArrayList<Material>();

    private static final List<Material> DONT_STAND_ON = new ArrayList<Material>();

    private static final List<Material> HALF_HEIGHT = new ArrayList<Material>();

    private static final List<Material> HEIGHT_AND_HALF = new ArrayList<Material>();

    public StaffFollowListener(Staff plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent e) {
        onMovement(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        onMovement(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogout(PlayerQuitEvent e) {
        FollowRoster.getInstance().remove(e.getPlayer());
    }

    private void onMovement(PlayerMoveEvent e) {
        FollowRoster roster = FollowRoster.getInstance();
        Player player = e.getPlayer();
        Set<Stalker> stalkers = null;
        stalkers = roster.getStalkersForSuspect(player);
        if (stalkers.isEmpty())
            return;
        Location from = e.getFrom();
        Location to = e.getTo();
        if (!from.getWorld().equals(to.getWorld()) || from.distance(to) > plugin.getConfig().getDouble("Follow.significantDistance"))
            updateStalkers(stalkers, player, to);
    }

    private void updateStalkers(Set<Stalker> stalkers, Player suspectPlayer, Location to) {
        Player stalkingPlayer = null;
        assert stalkers != null;
        Iterator<Stalker> iterator = stalkers.iterator();
        while (iterator.hasNext()) {
            Stalker s = iterator.next();
            stalkingPlayer = Bukkit.getPlayer(s.getName());
            if (stalkingPlayer == null) {
                s= null;
                continue;
            }
            if (plugin.getConfig().getBoolean("Follow.rotateHead")) {
                rotateStalker(stalkingPlayer, suspectPlayer, to);
            }
            s= null;
        }
        stalkingPlayer = null;
        iterator = null;
    }

    private boolean rotateStalker(Player stalkingPlayer, Player suspectPlayer, Location to) {
        Location stalkerLocation = stalkingPlayer.getLocation();
        Location suspectLocation = to;
        if (!stalkerLocation.getWorld().getName().equalsIgnoreCase(suspectLocation.getWorld().getName()))
            return false;
        double deltax = suspectLocation.getX() - stalkerLocation.getX();
        double deltay = suspectLocation.getY() - stalkerLocation.getY();
        double deltaz = suspectLocation.getZ() - stalkerLocation.getZ();
        stalkerLocation.setYaw((float)calculateYaw(deltax, deltaz));
        stalkerLocation.setPitch((float)calculatePitch(deltax, deltay, deltaz));
        return stalkingPlayer.teleport(stalkerLocation);
    }

    private boolean moveStalker(Player stalkingPlayer, Player suspectPlayer, int preferredDistance, Location newSuspectLocation) {
        Location stalkerLocation = stalkingPlayer.getLocation();
        World w = newSuspectLocation.getWorld();
        if (!stalkerLocation.getWorld().getName().equalsIgnoreCase(w.getName()))
            stalkerLocation.setWorld(w);
        int style = plugin.getConfig().getInt("Follow.followStyle");
        if (style == 2) {
        } else {
            double deltax = newSuspectLocation.getX() - stalkerLocation.getX();
            double deltaz = newSuspectLocation.getZ() - stalkerLocation.getZ();
            double actualDistance = Math.sqrt(deltax * deltax + deltaz * deltaz);
            double ratio = preferredDistance / actualDistance;
            double x = newSuspectLocation.getX() - deltax * ratio;
            double z = newSuspectLocation.getZ() - deltaz * ratio;
            double y = newSuspectLocation.getY();
            stalkingPlayer.setFlying(suspectPlayer.isFlying());
            stalkingPlayer.setGameMode(suspectPlayer.getGameMode());
            y = makeSafeY(w, x, y, z, stalkingPlayer.isFlying());
            if (y < 1.0D)
                y = w.getHighestBlockYAt((int)Math.round(Math.floor(x)), (int)Math.round(Math.floor(z)));
            double deltay = newSuspectLocation.getY() - y;
            setLocation(stalkerLocation, x, y, z, (float)calculateYaw(deltax, deltaz), (float)calculatePitch(deltax, deltay, deltaz));
        }
        return stalkingPlayer.teleport(stalkerLocation);
    }

    private void setLocation(Location stalkerLocation, double x, double y, double z, float yaw, float pitch) {
        stalkerLocation.setX(x);
        stalkerLocation.setY(y);
        stalkerLocation.setZ(z);
        stalkerLocation.setYaw(yaw);
        stalkerLocation.setPitch(pitch);
    }

    private double calculateYaw(double deltax, double deltaz) {
        double viewDirection = 90.0D;
        if (deltaz != 0.0D)
            viewDirection = Math.toDegrees(Math.atan(-deltax / deltaz));
        if (deltaz < 0.0D) {
            viewDirection += 180.0D;
        } else if (deltax > 0.0D && deltaz > 0.0D) {
            viewDirection += 360.0D;
        }
        return viewDirection;
    }

    private double calculatePitch(double x, double y, double z) {
        double pitch = 0.0D;
        double a = Math.sqrt(x * x + z * z);
        double theta = Math.atan(y / a);
        pitch = -Math.toDegrees(theta);
        return pitch;
    }

    private double makeSafeY(World w, double dx, double dy, double dz, boolean flying) {
        int x = (int)Math.floor(dx);
        int y = (int)Math.floor(dy);
        int z = (int)Math.floor(dz);
        Double newy = 0.0D;
        while (!safe(w, x, y, z) && y <= w.getHighestBlockYAt(x, z))
            y++;
        do {
            y--;
        } while (safe(w, x, y, z) && y > 1 && !flying);
        if (y < w.getMaxHeight())
            if (DONT_STAND_ON.contains(w.getBlockAt(x, y, z).getType())) {
                newy = 0.0D;
            } else if (HALF_HEIGHT.contains(w.getBlockAt(x, y, z).getType())) {
                newy = y + 0.5626D;
            } else if (HEIGHT_AND_HALF.contains(w.getBlockAt(x, y, z).getType())) {
                newy = y + 1.5001D;
            } else {
                newy = y + 1.0D;
            }
        return newy;
    }

    private boolean safe(World w, int x, int y, int z) {
        Block bottom = w.getBlockAt(x, y, z);
        Block top = w.getBlockAt(x, y + 1, z);
        Material bottomMaterial = bottom.getType();
        Material topMaterial = top.getType();
        boolean safe = ((bottom.isEmpty() || SAFE_TO_SHARE.contains(bottomMaterial)) && (
                top.isEmpty() || SAFE_TO_SHARE.contains(topMaterial)));
        return safe;
    }

    static {
        SAFE_TO_SHARE.add(Material.RED_MUSHROOM);
        SAFE_TO_SHARE.add(Material.BROWN_MUSHROOM);
        SAFE_TO_SHARE.add(Material.SNOW);
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("sapling")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        SAFE_TO_SHARE.add(Material.ACACIA_SAPLING);
        SAFE_TO_SHARE.add(Material.TORCH);
        SAFE_TO_SHARE.add(Material.REDSTONE);
        SAFE_TO_SHARE.add(Material.ROSE_BUSH);
        SAFE_TO_SHARE.add(Material.WITHER_ROSE);
        SAFE_TO_SHARE.add(Material.LEGACY_RED_ROSE);
        SAFE_TO_SHARE.add(Material.LEGACY_YELLOW_FLOWER);
        SAFE_TO_SHARE.add(Material.WHEAT);
        SAFE_TO_SHARE.add(Material.PUMPKIN_STEM);
        SAFE_TO_SHARE.add(Material.LILY_PAD);
        SAFE_TO_SHARE.add(Material.MELON_STEM);
        SAFE_TO_SHARE.add(Material.SUGAR_CANE);
        SAFE_TO_SHARE.add(Material.DEAD_BUSH);
        SAFE_TO_SHARE.add(Material.TALL_GRASS);
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("sign")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        SAFE_TO_SHARE.add(Material.STONE_BUTTON);
        SAFE_TO_SHARE.add(Material.LEVER);
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("rail")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("plate")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        DONT_STAND_ON.add(Material.WATER);
        DONT_STAND_ON.add(Material.LAVA);
        DONT_STAND_ON.add(Material.FIRE);
        DONT_STAND_ON.add(Material.CACTUS);
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("step")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("bed")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("fence")) {
                SAFE_TO_SHARE.add(material);
            }
        }
        for(Material material : Material.values()) {
            if(material.name().toLowerCase().endsWith("fence_gate")) {
                SAFE_TO_SHARE.add(material);
            }
        }
    }
}