package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StaffGuiOnline {

    Inventory inventory = null;


    public void setup() {

        InventoryBuilder builder = new InventoryBuilder("&4Online Staffs", getRow());

        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "staffs.yml", true);

        for(int i = 0; i < fileUtil.get().getConfigurationSection("Staffs").getKeys(false).size(); i++) {
            List<String> staff = new ArrayList<>(fileUtil.get().getConfigurationSection("Staffs").getKeys(false));
            Player player = Bukkit.getPlayer(staff.get(i));
            if(player == null)
                continue;
            List<String> lore =  fileUtil.get().get("Staffs." + staff.get(i) + ".description") != null ? fileUtil.get().getStringList("Staffs." + staff.get(i) + ".description") : null;
            ItemStack head = Utility.getSkull(player, fileUtil.get().getString("Staffs." + staff.get(i) + ".prefix") + player.getName());
            if(lore != null) {
                String status = "&c&l█";
                if(player.isOnline()) {
                }
                ItemMeta headMeta = head.getItemMeta();
                for(int j = 0; j < lore.size(); j++) {
                    lore.set(j, Utility.colorize(lore.get(j).replace("{status}", status)));
                }
                headMeta.setLore(lore);
                head.setItemMeta(headMeta);
            }
            builder.setItem(i + 1, head);


            staff = null;
            head = null;
            player = null;
        }

        inventory = builder.build();
        fileUtil = null;
        builder = null;

    }


    public void openInventory(Player player) {
        player.openInventory(inventory);
    }

    public int getRow() {
        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "staffs.yml", true);
        Set<String> online = new HashSet<>();
        for(String name : fileUtil.get().getConfigurationSection("Staffs").getKeys(false)) {
            Player player = Bukkit.getPlayer(name);
            if(player == null)
                continue;
            online.add(player.getUniqueId().toString());
        }
        int numbers = online.isEmpty() ? 0 : online.size();
        if (numbers <= 9) {
            return 1;
        }
        online = null;
        fileUtil = null;
        return numbers / 9 + Math.min(numbers % 9, 1);
    }
}
