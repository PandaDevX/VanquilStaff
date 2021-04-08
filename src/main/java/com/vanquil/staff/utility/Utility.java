package com.vanquil.staff.utility;

import com.vanquil.staff.Staff;
import com.vanquil.staff.data.Storage;
import com.vanquil.staff.database.PinDatabase;
import com.vanquil.staff.database.Report;
import com.vanquil.staff.database.ReportDatabase;
import com.vanquil.staff.gui.inventory.Pin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    public static void saveInventory(Player player) {
        Storage.playerInventory.put(player.getUniqueId().toString(), player.getInventory().getContents());
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
        PinDatabase pinDatabase = new PinDatabase(player);
        final String type = pinDatabase.isRegistered() ? "login" : "register";
        if(pinDatabase.isLoggedIn()) return;


        Storage.staffInventory.put(player.getUniqueId().toString(), player.getInventory().getContents());
        player.getInventory().clear();



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

    public static void sendHelpPage(Player player, String label) {
        HashMap<String, String> descriptions = new HashMap<>();
        HashMap<String, String> aliases = new HashMap<>();

        descriptions.put("slowchat (seconds)", "Slows chat");
        descriptions.put("unslow", "Un slow chat");
        descriptions.put("freeze [player / -all]", "Freeze a player");
        descriptions.put("falerts", "Listen to all censored chat");
        descriptions.put("filter", "Disable or enable chat filtering");
        descriptions.put("blacklist (player) (reason) [-p]", "Black list a player");
        descriptions.put("revive", "Restore lost inventory");
        descriptions.put("vanish", "Hide to other players");
        descriptions.put("staffs", "Staff list");
        descriptions.put("cps", "Listen to all click per listeners over 1 minute period");
        descriptions.put("report (player) (reason)", "Opens a menu to report a player");
        descriptions.put("reports", "Lists of all the reports");
        descriptions.put(label + " reload", "Reload the config");
        descriptions.put(label + " help", "Displays this");
        descriptions.put("staff", "Staff mode");
        descriptions.put("adminmode", "Admin mode");

        aliases.put("slowchat (seconds)", "&6or &8{ &c/chatslow &8}");
        aliases.put("revive", "&6or &8{ &c/invrestore &8}");
        aliases.put("vanish", "&6or &8{ &c/hidestaff, /v &8}");
        aliases.put("staffs", "&6or &8{ &c/stafflist,/slist &8}");
        aliases.put("cps", "&6or &8{ &c/cpsreport &8}");
        aliases.put(label + " reload", "&6or &8{ &c/vs reload &8}");
        aliases.put(label + " help", "&6or &8{ &c/vs help &8}");
        aliases.put("staff", "&6or &8{ &c/mod, /staffmode &8}");
        aliases.put("adminmode", "&6or &8{ &c/am, /amode &8}");

        player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &7Help Page"));
        player.sendMessage("");
        player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &6[] - &fNot Required"));
        player.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &6() - &fRequired"));
        player.sendMessage("");
        for (String key : descriptions.keySet()) {

            TextComponent part1 = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&a&lVanquil &8>> ")));
            TextComponent part2 = new TextComponent(TextComponent.fromLegacyText(Utility.colorize("&f/" + key)));
            if(aliases.containsKey(key)) {
                TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(" " + Utility.colorize(aliases.get(key))));
                part2.addExtra(textComponent);
            }
            part2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + key));
            part2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(descriptions.get(key)).color(net.md_5.bungee.api.ChatColor.YELLOW).create()));
            player.spigot().sendMessage(part1, part2);

            part1 = null;
            part2 = null;
        }
        descriptions = null;
        aliases = null;
    }

    public static void sendHelpPage(CommandSender sender, String label) {
        HashMap<String, String> descriptions = new HashMap<>();
        HashMap<String, String> aliases = new HashMap<>();

        descriptions.put("slowchat (seconds)", "Slows chat");
        descriptions.put("unslow", "Un slow chat");
        descriptions.put("freeze [player / -all]", "Freeze a player");
        descriptions.put("falerts", "Listen to all censored chat");
        descriptions.put("filter", "Disable or enable chat filtering");
        descriptions.put("blacklist (player) (reason) [-p]", "Black list a player");
        descriptions.put("revive", "Restore lost inventory");
        descriptions.put("vanish", "Hide to other players");
        descriptions.put("staffs", "Staff list");
        descriptions.put("cps", "Listen to all click per listeners over 1 minute period");
        descriptions.put("report (player) (reason)", "Opens a menu to report a player");
        descriptions.put("reports", "Lists of all the reports");
        descriptions.put(label + " reload", "Reload the config");
        descriptions.put(label + " help", "Displays this");
        descriptions.put("staff", "Staff mode");
        descriptions.put("adminmode", "Admin mode");

        aliases.put("slowchat (seconds)", "&6or &8{ &c/chatslow &8}");
        aliases.put("revive", "&6or &8{ &c/invrestore &8}");
        aliases.put("vanish", "&6or &8{ &c/hidestaff, /v &8}");
        aliases.put("staffs", "&6or &8{ &c/stafflist,/slist &8}");
        aliases.put("cps", "&6or &8{ &c/cpsreport &8}");
        aliases.put(label + " reload", "&6or &8{ &c/vs reload &8}");
        aliases.put(label + " help", "&6or &8{ &c/vs help &8}");
        aliases.put("staff", "&6or &8{ &c/mod, /staffmode &8}");
        aliases.put("adminmode", "&6or &8{ &c/am, /amode &8}");

        sender.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &7Help Page"));
        sender.sendMessage("");
        sender.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &6[] - &fNot Required"));
        sender.sendMessage(Utility.colorize("&a&lVanquil Staff &8>> &6() - &fRequired"));
        sender.sendMessage("");
        for (String key : descriptions.keySet()) {

            String alias = aliases.getOrDefault(key, "");
            sender.sendMessage(Utility.colorize("&a&lVanquil &8>> &f" + "/" + key + " " + alias + "&f: " + descriptions.get(key)));
        }
        descriptions = null;
        aliases = null;
    }
}
