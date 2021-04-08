package com.vanquil.staff.utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public final class TeleportUtils {
    private static final Set<Material> UNSAFE_MATERIALS = new HashSet<>();

    public static final Vector3D[] VOLUME;

    static {
        UNSAFE_MATERIALS.add(Material.LAVA);
        UNSAFE_MATERIALS.add(Material.LEGACY_STATIONARY_LAVA);
        UNSAFE_MATERIALS.add(Material.FIRE);
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++)
                    pos.add(new Vector3D(x, y, z));
            }
        }
        Collections.sort(pos, (a, b) -> a.x * a.x + a.y * a.y + a.z * a.z - b.x * b.x + b.y * b.y + b.z * b.z);
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    public static class Vector3D {
        public int x;

        public int y;

        public int z;

        public Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static boolean isBlockAboveAir(World world, int x, int y, int z) {
        return (y > world.getMaxHeight() || !world.getBlockAt(x, y - 1, z).getType().isSolid());
    }

    public static boolean isBlockUnsafe(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        Block below = world.getBlockAt(x, y - 1, z);
        Block above = world.getBlockAt(x, y + 1, z);
        return (UNSAFE_MATERIALS.contains(below.getType()) || block
                .getType().isSolid() || above.getType().isSolid() ||
                isBlockAboveAir(world, x, y, z));
    }

    public static Location safeizeLocation(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = (int)location.getY();
        int z = location.getBlockZ();
        int origX = x;
        int origY = y;
        int origZ = z;
        location.setY(location.getWorld().getHighestBlockYAt(location));
        while (isBlockAboveAir(world, x, y, z)) {
            y--;
            if (y < 0) {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z)) {
            x = (Math.round(location.getX()) == origX) ? (x - 1) : (x + 1);
            z = (Math.round(location.getZ()) == origZ) ? (z - 1) : (z + 1);
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z)) {
            i++;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + 3;
                z = origZ;
                break;
            }
            x = origX + (VOLUME[i]).x;
            y = origY + (VOLUME[i]).y;
            z = origZ + (VOLUME[i]).z;
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y++;
            if (y >= world.getMaxHeight()) {
                x++;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y--;
            if (y <= 1) {
                x++;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > location.getBlockX())
                    return null;
            }
        }
        return new Location(world, x + 0.5D, y, z + 0.5D, location.getYaw(), location.getPitch());
    }
}
