package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StaffGui {

    Inventory inventory = null;


    public void setup(Player viewer) {

        InventoryBuilder builder = new InventoryBuilder("&4Staffs", getRow());

        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "staffs.yml", true);

        for(int i = 0; i < fileUtil.get().getConfigurationSection("Staffs").getKeys(false).size(); i++) {
            List<String> staff = new ArrayList<>(fileUtil.get().getConfigurationSection("Staffs").getKeys(false));
            OfflinePlayer player = Bukkit.getOfflinePlayer(staff.get(i));
            List<String> lore =  fileUtil.get().get("Staffs." + staff.get(i) + ".description") != null ? fileUtil.get().getStringList("Staffs." + staff.get(i) + ".description") : null;
            ItemStack head = Utility.getSkull(player, fileUtil.get().getString("Staffs." + staff.get(i) + ".prefix") + player.getName());
            if(lore != null) {
                String status = "&c&l█";
                if(player.isOnline()) {
                    if(viewer.canSee(player.getPlayer())) {
                        status = "&a&l█";
                    }
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
        int numbers = fileUtil.get().getConfigurationSection("Staffs").getKeys(false).size();
        if (numbers <= 9) {
            return 1;
        }
        fileUtil = null;
        return numbers / 9 + Math.min(numbers % 9, 1);
    }
}
