package com.vanquil.staff.utility;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.DatabaseManager;
import com.vanquil.staff.database.PinDatabase;
import com.vanquil.staff.database.Report;
import com.vanquil.staff.database.ReportDatabase;
import com.vanquil.staff.gui.inventory.Pin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public final class Utility {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(colorize(Staff.getInstance().getConfig().getString("No Permission")));
    }

    public static void sendNoPermissionMessage(Player sender) {
        sender.sendMessage(colorize(Staff.getInstance().getConfig().getString("No Permission")));
    }

    public static void sendCorrectArgument(CommandSender sender, String argument) {
        sender.sendMessage(colorize(Staff.getInstance().getConfig().getString("Usage").replace("{argument}", argument)));
    }

    public static void sendCorrectArgument(Player sender, String argument) {
        sender.sendMessage(colorize(Staff.getInstance().getConfig().getString("Usage").replace("{argument}", argument)));
    }

    public static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean sensor(List<String> words) {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "words.yml", true);
        List<String> bannedWords = fileUtil.get().getStringList("Words");
        boolean found = false;
        StringBuilder builder = new StringBuilder();
        for(int x = 0; x < words.size(); x++) {
            for(int y = 0; y < bannedWords.size(); y++) {
                String findX = words.get(x).toLowerCase();
                findX = stripColor(findX);
                String findY = bannedWords.get(y).toLowerCase();
                findY = stripColor(findY);
                if(findX.contains(findY)) {
                    found = true;
                }
                findX = null;
                findY = null;
            }
            if(words.size() > 1) {
                builder.append(words.get(x));
            }
            for(int i = 0; i < bannedWords.size(); i++) {
                if(builder.toString().toLowerCase().contains(bannedWords.get(i).toLowerCase())) {
                    found = true;
                }
            }
            builder = null;
            // Blocks FuCk
            // Blocks f u c k
            // Blocks fuuu cckkkk
            // Blocks fu123/ck
            // Blocks fcuk
            // Blocks fc123/@uk
            // Blocks fc123123ccuk
            // Blocks fcccu 123@ck
            if(!bannedWords.isEmpty()) {
                StringBuilder sentence = new StringBuilder();
                Iterator<String> bannedWordsIterator = bannedWords.iterator();
                while(bannedWordsIterator.hasNext()) {
                    Iterator<String> wordsIterator = words.iterator();
                    String banWord = bannedWordsIterator.next();
                    while(wordsIterator.hasNext()) {
                        String word = wordsIterator.next();
                        word = filterDuplicate(word);
                        word = filterNonCharacter(word);
                        if(word.equalsIgnoreCase(banWord)) {
                            found = true;
                        }
                        if(isAnagramSort(word.toLowerCase(), banWord.toLowerCase())) {
                            found = true;
                        }
                        sentence.append(word);

                        word = null;
                    }
                    banWord = filterDuplicate(banWord);
                    banWord = filterNonCharacter(banWord);
                    if(sentence.toString().toLowerCase().contains(banWord)) {
                        found = true;
                    }
                    if(isAnagramSort(sentence.toString().toLowerCase(), banWord.toLowerCase())) {
                        found = true;
                    }
                    wordsIterator = null;
                    banWord = null;
                }
                sentence = null;
                bannedWordsIterator = null;
                fileUtil = null;
            }
        }
        return found;
    }

    public static String stripColor(String message) {
        return ChatColor.stripColor(message);
    }


    private static String filterDuplicate(String word) {
        char[] chars = word.toCharArray();
        Set<Character> charSet = new LinkedHashSet<Character>();
        for (char c : chars) {
            charSet.add(c);
        }

        StringBuilder sb = new StringBuilder();
        for (Character character : charSet) {
            sb.append(character);
        }
        chars = null;
        charSet = null;
        return sb.toString();
    }

    private static String filterNonCharacter(String word) {
        return word.replaceAll("[^a-zA-Z]", "");
    }

    private static boolean isAnagramSort(String word1, String word2) {
        if(word1.length() == word2.length()) {
            char[] a1 = word1.toCharArray();
            char[] a2 = word2.toCharArray();
            Arrays.sort(a1);
            Arrays.sort(a2);
            return Arrays.equals(a1, a2);
        }
        return false;
    }

    public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void restoreInventory(Player player) {
        ItemStack[] contents = Storage.playerInventory.getOrDefault(player, null);
        if(contents == null) {
            player.getInventory().clear();
        }
        player.getInventory().setContents(contents);
    }

    public static ItemStack getSkull(OfflinePlayer player, String displayName, String... lore) {

        boolean isNewVersion = Arrays.stream(Material.values())
                .map(Material::name)
                .collect(Collectors.toList())
                .contains("PLAYER_HEAD");

        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

        ItemStack item = new ItemStack(type);

        if(!isNewVersion) {
            item.setDurability((short) 3);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(isNewVersion) {
            meta.setOwningPlayer(player);
        }else {
            meta.setOwner(player.getName());
        }
        meta.setDisplayName(Utility.colorize(displayName));
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSkull(OfflinePlayer player, String displayName) {

        boolean isNewVersion = Arrays.stream(Material.values())
                .map(Material::name)
                .collect(Collectors.toList())
                .contains("PLAYER_HEAD");

        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

        ItemStack item = new ItemStack(type);

        if(!isNewVersion) {
            item.setDurability((short) 3);
        }

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(isNewVersion) {
            meta.setOwningPlayer(player);
        }else {
            meta.setOwner(player.getName());
        }
        meta.setDisplayName(Utility.colorize(displayName));

        item.setItemMeta(meta);

        return item;
    }

    public static Set<String> getStaffNames() {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "staffs.yml", true);
        return fileUtil.get().getConfigurationSection("Staffs").getKeys(false);
    }

    public static Set<OfflinePlayer> getReportedPlayers() {
        Set<OfflinePlayer> reportedPlayers = new HashSet<>();
        ReportDatabase reportDatabase = new ReportDatabase();
        if(!reportDatabase.getReports().isEmpty()) {
            Iterator<Report> reportIterator = reportDatabase.getReports().iterator();
            while(reportIterator.hasNext()) {
                reportedPlayers.add(reportIterator.next().getReportedPlayer());
            }
            reportIterator = null;
        }
        reportDatabase = null;
        return reportedPlayers;
    }

    public static void createReport(OfflinePlayer player, String report, OfflinePlayer reporter) {
        ReportDatabase reportDatabase = new ReportDatabase();
        reportDatabase.save(new Report(player, report, reporter, null, true, new SimpleDateFormat("MMMMM dd yyyy hh:mm a").format(new Date(System.currentTimeMillis()))));
        reportDatabase = null;
    }

    public static void createReport(OfflinePlayer player, String report, OfflinePlayer reporter, String url) {
        ReportDatabase reportDatabase = new ReportDatabase();
        reportDatabase.save(new Report(player, report, reporter, url, true, new SimpleDateFormat("MMMMM dd yyyy hh:mm a").format(new Date(System.currentTimeMillis()))));
        reportDatabase = null;
    }

    public static void auth(Player player) {
        if(DatabaseManager.getConnection() == null) return;
        PinDatabase pinDatabase = new PinDatabase(player);
        final String type = pinDatabase.isRegistered() ? "login" : "register";
        if(pinDatabase.isLoggedIn()) return;


        saveInventory(player);
        player.getInventory().clear();
        player.updateInventory();



        new BukkitRunnable() {
            public void run () {

                // open gui
                Pin pin = new Pin();
                pin.setup(type);
                pin.openInventory(player);
                pin = null;
            }
        }.runTaskLater(Staff.getInstance(), 40L);

        pinDatabase = null;

        // check if the player is already vanished
        if(Storage.vanishedPlayers.contains(player.getUniqueId().toString())) {
            return;
        }

        // vanish player
        if(Staff.getInstance().getConfig().getBoolean("Staff Vanish.invisibility")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 1, false));
        }
        Storage.vanishedPlayers.add(player.getUniqueId().toString());
        for(Player online : Bukkit.getOnlinePlayers()) {
            if(online.hasPermission("staff.vanish")) {
                continue;
            }
            online.hidePlayer(Staff.getInstance(), player);
        }
    }

    public static void sendStaffHelp(Player player) {
        String[] help = {
                colorize("&8===== &a&lVanquil Staff Mode &8====="),
                "",
                colorize("&6&lRandom Teleport &7>> &fUse to teleport safe random location"),
                colorize("&6&lExamine &7>> &fUse to examine players' inventory"),
                colorize("&6&lFreeze Tool &7>> &fUse to freeze players"),
                colorize("&6&lFollow Tool &7>> &fUse to follow player and stay behind"),
                colorize("&6&lPush Forward &7>> &fUse to push player forward to where they are facing"),
                colorize("&6&lStaffs &7>> &fUse to see all online staffs"),
                "",
                colorize("&6&l/staff &7>> &fCommand to enable or disable staff mode")};
        player.sendMessage(help);
    }

    public static void sendAdminHelp(Player player) {
        String[] help = {
                colorize("&8===== &a&lVanquil Staff Mode &8====="),
                "",
                colorize("&6&lRandom Teleport &7>> &fUse to teleport safe random location"),
                colorize("&6&lExamine &7>> &fUse to examine players' inventory and edit"),
                colorize("&6&lFreeze Tool &7>> &fUse to freeze players"),
                colorize("&6&lFollow Tool &7>> &fUse to follow player and stay behind"),
                colorize("&6&lPush Forward &7>> &fUse to push player forward to where they are facing"),
                colorize("&6&lStaffs &7>> &fUse to see all online staffs"),
                colorize("&6&lWE Wand &7>> &fUse to claim worldedit wand"),
                "",
                colorize("&6&l/adminmode &7>> &fCommand to enable or disable admin mode")};
        player.sendMessage(help);
    }

    public static void saveInventory(Player player) {
        if(!emptyInventory(player.getInventory())) {

            // save player inventory
            try {
                Storage.playerInventory.put(player.getUniqueId().toString(), player.getInventory().getStorageContents());
            }catch (NoSuchMethodError ex) {
                Storage.playerInventory.put(player.getUniqueId().toString(), player.getInventory().getContents());
            }
        }

        if(!emptyArmors(player.getInventory())) {
            Storage.playerArmors.put(player.getUniqueId().toString(), player.getInventory().getArmorContents());
        }


        if(!isOldVersion(player.getInventory())) {
            Storage.playerExtras.put(player.getUniqueId().toString(), player.getInventory().getExtraContents());
        }
    }

    public static void saveInventoryStaff(Player player) {
        if(!emptyInventory(player.getInventory())) {

            // save player inventory
            try {
                Storage.staffInventoryContents.put(player.getUniqueId().toString(), player.getInventory().getStorageContents());
            }catch (NoSuchMethodError ex) {
                Storage.staffInventoryContents.put(player.getUniqueId().toString(), player.getInventory().getContents());
            }
        }

        if(!emptyArmors(player.getInventory())) {
            Storage.staffArmors.put(player.getUniqueId().toString(), player.getInventory().getArmorContents());
        }


        if(!isOldVersion(player.getInventory())) {
            Storage.staffExtras.put(player.getUniqueId().toString(), player.getInventory().getExtraContents());
        }
    }
    public static void loadInventoryStaff(Player player) {
        if(!Storage.staffInventoryContents.containsKey(player.getUniqueId().toString()) && !Storage.staffExtras.containsKey(player.getUniqueId().toString()) && !Storage.staffArmors.containsKey(player.getUniqueId().toString())) {
            return;
        }
        if(Storage.staffInventoryContents.containsKey(player.getUniqueId().toString())) {
            try {
                player.getInventory().setStorageContents(Storage.staffInventoryContents.get(player.getUniqueId().toString()));
            }catch (NoSuchMethodError e) {
                player.getInventory().setContents(Storage.staffInventoryContents.get(player.getUniqueId().toString()));
            }
            Storage.staffInventoryContents.remove(player.getUniqueId().toString());
        }
        if(Storage.staffArmors.containsKey(player.getUniqueId().toString())) {
            player.getInventory().setArmorContents(Storage.staffArmors.get(player.getUniqueId().toString()));
            Storage.staffArmors.remove(player.getUniqueId().toString());
        }
        if(!isOldVersion(player.getInventory())) {
            if(Storage.staffExtras.containsKey(player.getUniqueId().toString())) {
                player.getInventory().setExtraContents(Storage.staffExtras.get(player.getUniqueId().toString()));
                Storage.staffExtras.remove(player.getUniqueId().toString());
            }
        }
        player.updateInventory();
    }

    public static void loadInventory(Player player) {
        if(!Storage.playerInventory.containsKey(player.getUniqueId().toString()) && !Storage.playerExtras.containsKey(player.getUniqueId().toString()) && !Storage.playerArmors.containsKey(player.getUniqueId().toString())) {
            return;
        }
            if(Storage.playerInventory.containsKey(player.getUniqueId().toString())) {
            try {
                player.getInventory().setStorageContents(Storage.playerInventory.get(player.getUniqueId().toString()));
            }catch (NoSuchMethodError e) {
                player.getInventory().setContents(Storage.playerInventory.get(player.getUniqueId().toString()));
            }
            Storage.playerInventory.remove(player.getUniqueId().toString());
        }
        if(Storage.playerArmors.containsKey(player.getUniqueId().toString())) {
            player.getInventory().setArmorContents(Storage.playerArmors.get(player.getUniqueId().toString()));
            Storage.playerArmors.remove(player.getUniqueId().toString());
        }
        if(!isOldVersion(player.getInventory())) {
            if(Storage.playerExtras.containsKey(player.getUniqueId().toString())) {
                player.getInventory().setExtraContents(Storage.playerExtras.get(player.getUniqueId().toString()));
                Storage.playerExtras.remove(player.getUniqueId().toString());
            }
        }
        player.updateInventory();
    }

    private static boolean emptyInventory(PlayerInventory inventory) {
        for(ItemStack item : inventory.getContents())
        {
            if(item != null)
                return false;
        }
        return true;
    }

    private static boolean emptyArmors(PlayerInventory inventory) {
        for(ItemStack item : inventory.getArmorContents()) {
            if(item != null)
                return false;
        }
        return true;
    }

    private static boolean isOldVersion(PlayerInventory inventory) {
        try {
            inventory.getExtraContents();
        }catch (NoSuchMethodError e) {
            return true;
        }
        return false;
    }

    public static void showWrongInfo(Inventory inventory, int slot) {
        ItemBuilder builder = new ItemBuilder(Material.REDSTONE_BLOCK);
        builder.setName("&cWrong Password");
        final ItemStack itemStack = inventory.getItem(slot);
        inventory.setItem(slot, builder.build());
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(slot, itemStack);
            }
        }.runTaskLater(Staff.getInstance(), 60L);
    }

    public static Report getLatestReport(final Collection<Report> c) {
        Report lastElement = null;
        for(Report report : c) {
            lastElement = report;
        }
        return lastElement;
    }

    public static Random getRandom() {
        return new Random();
    }

    public static void randomizeTeleport(Player player, World world) {
        int maxX = 1000;
        int maxZ = 1000;
        TeleportRunnable teleportRunnable = new TeleportRunnable(player, world, maxX, maxZ);
        teleportRunnable.run();
        teleportRunnable = null;
    }

    public static List<String> getBlackListBiomes() {
        return Staff.getInstance().getConfig().getStringList("Random Teleport.biome_blacklist");
    }

    public static List<String> getBlackListBlocks() {
        return Staff.getInstance().getConfig().getStringList("Random Teleport.block_blacklist");
    }

    public static void sendActionBar(Player player, String message) {
        try {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }catch(NoSuchMethodError e) {
            Bukkit.getScheduler().runTaskAsynchronously(Staff.getInstance(), () -> sendActionBarTask(player, message));
        }
    }

    public static void sendActionBarTask(Player player, String message) {
            if (!player.isOnline()) {
                return; // Player may have logged out
            }

            // Call the event, if cancelled don't send Action Bar

            try {
                Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getNMSVersion() + ".entity.CraftPlayer");
                Object craftPlayer = craftPlayerClass.cast(player);
                Object packet;
                Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".PacketPlayOutChat");
                Class<?> packetClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".Packet");
                if (useOldMethods()) {
                    Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".ChatSerializer");
                    Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".IChatBaseComponent");
                    Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                    Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
                    packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(cbc, (byte) 2);
                } else {
                    Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".ChatComponentText");
                    Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".IChatBaseComponent");
                    try {
                        Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".ChatMessageType");
                        Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                        Object chatMessageType = null;
                        for (Object obj : chatMessageTypes) {
                            if (obj.toString().equals("GAME_INFO")) {
                                chatMessageType = obj;
                            }
                        }
                        Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                        packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(chatCompontentText, chatMessageType);
                    } catch (ClassNotFoundException cnfe) {
                        Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                        packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(chatCompontentText, (byte) 2);
                    }
                }
                Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
                Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
                Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
                Object playerConnection = playerConnectionField.get(craftPlayerHandle);
                Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
                sendPacketMethod.invoke(playerConnection, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private static boolean useOldMethods() {
        return getNMSVersion().equalsIgnoreCase("v1_8_R1") || getNMSVersion().startsWith("v1_7");
    }

    public static void sendTitleRaw(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {

        try {
            Object e;
            Object chatTitle;
            Object chatSubtitle;
            Constructor subtitleConstructor;
            Object titlePacket;
            Object subtitlePacket;

            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                // Times packets
                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object) null);
                chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + title + "\"}"});
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                titlePacket = subtitleConstructor.newInstance(new Object[]{e, chatTitle, fadeIn, stay, fadeOut});
                sendPacket(player, titlePacket);

                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get((Object) null);
                chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + title + "\"}"});
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent")});
                titlePacket = subtitleConstructor.newInstance(new Object[]{e, chatTitle});
                sendPacket(player, titlePacket);
            }

            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                // Times packets
                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get((Object) null);
                chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + title + "\"}"});
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                subtitlePacket = subtitleConstructor.newInstance(new Object[]{e, chatSubtitle, fadeIn, stay, fadeOut});
                sendPacket(player, subtitlePacket);

                e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get((Object) null);
                chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke((Object) null, new Object[]{"{\"text\":\"" + subtitle + "\"}"});
                subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                subtitlePacket = subtitleConstructor.newInstance(new Object[]{e, chatSubtitle, fadeIn, stay, fadeOut});
                sendPacket(player, subtitlePacket);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }


    public static void sendTitleTask(Player player, String title, String subTitle, int x, int y, int z) {
        try {
            player.sendTitle(colorize(title), colorize(subTitle), x, y, z);
        }catch (NoSuchMethodError e) {
            sendTitleRaw(player, x, y, z, Utility.colorize(title), Utility.colorize(subTitle));
        }
    }

    public static void sendTitle(Player player, String title, String subTitle, int x, int y, int z) {
        Bukkit.getScheduler().runTaskAsynchronously(Staff.getInstance(), () -> sendTitleTask(player, title, subTitle, x,  y, z));
    }

    public static void sendTitle(Player player, String title, String subTitle) {
        Bukkit.getScheduler().runTaskAsynchronously(Staff.getInstance(), () -> sendTitleTask(player, title, subTitle, 10,  70, 20));
    }

    private static String getNMSVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    private static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch(Exception ex)
        {
            //Do something
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void followPlayer(Player stalker, Player suspect) {
        stalker.closeInventory();
        int distance = Staff.getInstance().getConfig().getInt("Follow.distance");
        if (suspect == null) {
            stalker.sendMessage("&6&lFollow Tool &cPlayer goes offline");
            return;
        }
        if (suspect.equals(stalker)) {
            sendTitle(stalker, "&6&lFollow Tool", "&cYou can't follow yourself");
            return;
        }
        if (FollowRoster.getInstance().isSuspect(stalker.getName())) {
            sendTitle(stalker, "&6&lFollow Tool", "&cYou can't follow someone while being followed");
            return;
        }

        FollowRoster.getInstance().follow(stalker, suspect, distance);
        sendTitle(stalker, "&6&lFollow Tool", "&aYou are now following &f" + suspect.getName());
        stalker.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &fType &ccancel &fto cancel"));
    }

    public static void vanishPlayer(Player player, Staff plugin) {
        if(Storage.vanishedPlayers.contains(player.getUniqueId().toString())) {
            if(plugin.getConfig().getBoolean("Staff Vanish.invisibility")) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            Storage.vanishedPlayers.remove(player.getUniqueId().toString());
            for(Player online : Bukkit.getOnlinePlayers()) {
                if(online.hasPermission("staff.vanish")) {
                    continue;
                }
                online.showPlayer(plugin, player);
            }
            player.sendMessage(Utility.colorize(plugin.getConfig().getString("Staff Vanish.show-message")));
            return;
        }

        // vanish player
        if(plugin.getConfig().getBoolean("Staff Vanish.invisibility")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 1, false));
        }
        Storage.vanishedPlayers.add(player.getUniqueId().toString());
        for(Player online : Bukkit.getOnlinePlayers()) {
            if(online.hasPermission("staff.vanish")) {
                continue;
            }
            online.hidePlayer(plugin, player);
        }
        player.sendMessage(Utility.colorize(plugin.getConfig().getString("Staff Vanish.hide-message")));
    }

    public static void vanishPlayerSilent(Player player, Staff plugin) {
        if(Storage.vanishedPlayers.contains(player.getUniqueId().toString())) {
            if(plugin.getConfig().getBoolean("Staff Vanish.invisibility")) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            Storage.vanishedPlayers.remove(player.getUniqueId().toString());
            for(Player online : Bukkit.getOnlinePlayers()) {
                if(online.hasPermission("staff.vanish")) {
                    continue;
                }
                online.showPlayer(plugin, player);
            }
            return;
        }

        // vanish player
        if(plugin.getConfig().getBoolean("Staff Vanish.invisibility")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 1, false));
        }
        Storage.vanishedPlayers.add(player.getUniqueId().toString());
        for(Player online : Bukkit.getOnlinePlayers()) {
            if(online.hasPermission("staff.vanish")) {
                continue;
            }
            online.hidePlayer(plugin, player);
        }
    }


    public static void vanishStaff(Player player, Staff plugin) {
        if(Storage.vanishedPlayers.contains(player.getUniqueId().toString())) {
            return;
        }

        // vanish player
        if(plugin.getConfig().getBoolean("Staff Vanish.invisibility")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 1, false));
        }
        Storage.vanishedPlayers.add(player.getUniqueId().toString());
        for(Player online : Bukkit.getOnlinePlayers()) {
            if(online.hasPermission("staff.vanish")) {
                continue;
            }
            online.hidePlayer(plugin, player);
        }
        player.sendMessage(Utility.colorize(plugin.getConfig().getString("Staff Vanish.hide-message")));
    }

    public static boolean pushPlayer(Player player) {
        if(player == null) {
            return false;
        }
        player.setVelocity(player.getLocation().getDirection().clone().multiply(Staff.getInstance().getConfig().getInt("Push.distance")));
        return true;
    }

    public static void stopFollow(AsyncPlayerChatEvent e) {
        Stalker s = FollowRoster.getInstance().unfollow(e.getPlayer());
        if(s == null) return;

        if(e.getMessage().equalsIgnoreCase("cancel")) {
            e.getPlayer().sendTitle(Utility.colorize("&6&lFollow Tool"), Utility.colorize("&fYou are no longer following &6" + s.getSuspectName()), 10, 70, 20);
        }

    }

    public static void sendHelpPage(Player player, int page, String label, String argument) {
        int currentPage = page;
        HashMap<String, String> descriptions = new HashMap<>();
        HashMap<String, String> aliases = new HashMap<>();

        descriptions.put(label + " help,? (page)", "displays the menu");
        descriptions.put("slowchat (seconds)", "slows chat");
        descriptions.put("unslow", "unslow chat");
        descriptions.put("freeze [player / -all]", "freeze a player");
        descriptions.put("falerts", "listen to all censored chat");
        descriptions.put("filter", "disable or enable chat filtering");
        descriptions.put("blacklist (player) (reason) [-p]", "black list a player");
        descriptions.put("revive", "restore lost inventory");
        descriptions.put("vanish", "hide to other players");
        descriptions.put("staffs", "staff list");
        descriptions.put("cps", "listen to all click per listeners over 1 minute period");
        descriptions.put("report (player) (reason)", "opens a menu to report a player");
        descriptions.put("reports", "lists of all the reports");
        descriptions.put(label + " reload", "reload the config");
        descriptions.put("staff", "staff mode");
        descriptions.put("adminmode", "admin mode");
        descriptions.put("hidestaff", "hide all staffs");
        descriptions.put("logs", "vanquil Staff logger");

        aliases.put("logs", "notifications");
        aliases.put("slowchat (seconds)", "chatslow");
        aliases.put("revive", "invrestore");
        aliases.put("vanish", "v");
        aliases.put("staffs", "stafflist,slist");
        aliases.put("cps", "cpsreport");
        aliases.put("staff", "mod,staffmode");
        aliases.put("adminmode", "am,amode");


        List<String> list = new ArrayList<>(descriptions.keySet());
        PaginatedList paginatedList = new PaginatedList(list, 8);
        if(page <= 0) {
            page = 1;
        }
        if(page > paginatedList.getMaximumPage()) {
            page = paginatedList.getMaximumPage();
        }
        String prev = page == 1 ? "&7[<]" : "&b[<]";
        String next = page == paginatedList.getMaximumPage() ? "&7[>]" : "&b[>]";
        TextComponent prevButton = new TextComponent(TextComponent.fromLegacyText(Utility.colorize(prev)));
        if(page > 1) {
            prevButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to previous page").color(net.md_5.bungee.api.ChatColor.YELLOW).create()));
            prevButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " " + argument + " " + (currentPage - 1)));
        }
        TextComponent nextButton = new TextComponent(TextComponent.fromLegacyText(Utility.colorize(next)));
        if(page < paginatedList.getMaximumPage()) {
            nextButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to next page").color(net.md_5.bungee.api.ChatColor.YELLOW).create()));
            nextButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " " + argument + " " + (currentPage + 1)));
        }
        TextComponent header = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&6____,[ &2Help for command \"" + label +  " " + argument + "\" ")));
        TextComponent pageModifier = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&6 " + page + "/" + paginatedList.getMaximumPage() + " ")));
        TextComponent subHeader = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&6 ],____")));
        player.spigot().sendMessage(header, prevButton, pageModifier, nextButton, subHeader);
        List<String> finalList = paginatedList.getPage(page);
        for (String key : finalList) {

            String alias = aliases.containsKey(key) ? "," + aliases.get(key) : "";
            TextComponent command = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&b/" + key + alias + " ")));
            command.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(descriptions.get(key)).color(net.md_5.bungee.api.ChatColor.YELLOW).create()));
            command.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + key + alias));
            TextComponent description = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&e" + descriptions.get(key))));

            player.spigot().sendMessage(command, description);

            command = null;
            description = null;
        }
        list = null;
        paginatedList = null;
        prev = null;
        next = null;
        prevButton = null;
        nextButton = null;
        header = null;
        pageModifier = null;
        subHeader = null;
        finalList = null;
        descriptions = null;
        aliases = null;
    }

    public static void sendHelpPage(CommandSender sender, int page, String label, String argument) {
        HashMap<String, String> descriptions = new HashMap<>();
        HashMap<String, String> aliases = new HashMap<>();

        descriptions.put(label + " help,? (page)", "display this");
        descriptions.put("slowchat (seconds)", "slows chat");
        descriptions.put("unslow", "unslow chat");
        descriptions.put("freeze [player / -all]", "freeze a player");
        descriptions.put("falerts", "listen to all censored chat");
        descriptions.put("filter", "disable or enable chat filtering");
        descriptions.put("blacklist (player) (reason) [-p]", "black list a player");
        descriptions.put("revive", "restore lost inventory");
        descriptions.put("vanish", "hide to other players");
        descriptions.put("staffs", "staff list");
        descriptions.put("cps", "listen to all click per listeners over 1 minute period");
        descriptions.put("report (player) (reason)", "opens a menu to report a player");
        descriptions.put("reports", "lists of all the reports");
        descriptions.put(label + " reload", "reload the config");
        descriptions.put("staff", "staff mode");
        descriptions.put("adminmode", "admin mode");
        descriptions.put("hidestaff", "hide all staffs");
        descriptions.put("logs", "vanquil Staff logger");

        aliases.put("logs", "notifications");
        aliases.put("slowchat (seconds)", "chatslow");
        aliases.put("revive", "invrestore");
        aliases.put("vanish", "v");
        aliases.put("staffs", "stafflist,slist");
        aliases.put("cps", "cpsreport");
        aliases.put("staff", "mod,staffmode");
        aliases.put("adminmode", "am,amode");


        List<String> list = new ArrayList<>(descriptions.keySet());
        PaginatedList paginatedList = new PaginatedList(list, 8);
        List<String> finalList = paginatedList.getPage(page);

        sender.sendMessage(Utility.colorize("&6____,[ &2Help for command \"" + label + " " + argument + "\" &6" + page + "/" + paginatedList.getMaximumPage() + " ],____"));
        for (String key : finalList) {

            String alias = "," + aliases.getOrDefault(key, "");
            sender.sendMessage(Utility.colorize("&b/" + key + alias + " &e" + descriptions.get(key)));
        }
        descriptions = null;
        aliases = null;
    }

    public static void freezePlayer(Player player, String duration) {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "frozen.yml", false);
        if(isFrozen(player)) {
            return;
        }
        fileUtil.get().set(player.getUniqueId().toString(), TimeUtil.serializeRaw(duration));
        fileUtil.save();
        fileUtil = null;

        Storage.playerFreezeNotifier.put(player.getUniqueId().toString(), System.currentTimeMillis());
        int id = new BukkitRunnable() {
            public void run() {
                long current = Storage.playerFreezeNotifier.get(player.getUniqueId().toString());
                if(((System.currentTimeMillis() - current)/1000) >= 30) {
                    Storage.playerFreezeNotifier.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    player.sendMessage(colorize("&8&b-------------------------"));
                    player.sendMessage(colorize("&7You have been &eFROZEN&7."));
                    player.sendMessage(colorize("&7You must join a support room in &e3 minutes &7or you will be &ebanned"));
                    player.sendMessage(" ");
                    player.sendMessage("&7Our discord: &ehttps://discord.gg/6TtVBAD7");
                    player.sendMessage(colorize("&8&b-------------------------"));
                }
                sendTitle(player, "&a&lVanquil", "&7You have been frozen");
            }
        }.runTaskTimer(Staff.getInstance(), 0, 1).getTaskId();

        Storage.playerFreezeScheduler.put(player.getUniqueId().toString(), id);
    }

    public static void freezePlayer(Player player) {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "frozen.yml", false);
        if (isFrozen(player)) {
            return;
        }
        fileUtil.get().set(player.getUniqueId().toString(), "");
        fileUtil.save();
        fileUtil = null;

        Storage.playerFreezeNotifier.put(player.getUniqueId().toString(), System.currentTimeMillis());
        int id = new BukkitRunnable() {
            public void run() {
                long current = Storage.playerFreezeNotifier.get(player.getUniqueId().toString());
                if(((System.currentTimeMillis() - current)/1000) >= 30) {
                    Storage.playerFreezeNotifier.put(player.getUniqueId().toString(), System.currentTimeMillis());
                    player.sendMessage(colorize("&8&b-------------------------"));
                    player.sendMessage(colorize("&7You have been &eFROZEN&7."));
                    player.sendMessage(colorize("&7You must join a support room in &e3 minutes &7or you will be &ebanned"));
                    player.sendMessage(" ");
                    player.sendMessage("&7Our discord: &ehttps://discord.gg/6TtVBAD7");
                    player.sendMessage(colorize("&8&b-------------------------"));
                }
                sendTitle(player, "&a&lVanquil", "&7You have been frozen");
            }
        }.runTaskTimer(Staff.getInstance(), 0, 20).getTaskId();

        Storage.playerFreezeScheduler.put(player.getUniqueId().toString(), id);
    }

    public static void unfreezePlayer(OfflinePlayer player) {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "frozen.yml", false);
        if (!isFrozen(player)) {
            return;
        }
        fileUtil.get().set(player.getUniqueId().toString(), null);
        fileUtil.save();
        fileUtil = null;
        Bukkit.getScheduler().cancelTask(Storage.playerFreezeScheduler.get(player.getUniqueId().toString()));
        Storage.playerFreezeNotifier.remove(player.getUniqueId().toString());
        Storage.playerFreezeScheduler.remove(player.getUniqueId().toString());
    }

    public static boolean isFrozen(OfflinePlayer player) {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "frozen.yml", false);
        if(fileUtil.get().get(player.getUniqueId().toString()) == null) {
            return false;
        }
        if(fileUtil.get().getString(player.getUniqueId().toString()).equals("")) {
            return true;
        }
        long duration = fileUtil.get().getLong(player.getUniqueId().toString()) * 1000;
        if(duration >
                System.currentTimeMillis()) {
            return true;
        }
        fileUtil.get().set(player.getUniqueId().toString(), null);
        fileUtil.save();
        return false;
    }
}
