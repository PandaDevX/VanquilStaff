package com.vanquil.staff.player.staffs;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.FileUtil;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.Utility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class StaffGui {

    Inventory inventory = null;


    public void setup() {

        InventoryBuilder builder = new InventoryBuilder("&4Staffs", getRow());

        FileUtil fileUtil = new FileUtil(Staff.getInstance(), "staffs.yml", true);

        for(int i = 0; i < fileUtil.get().getConfigurationSection("Staffs").getKeys(false).size(); i++) {
            List<String> staff = new ArrayList<>(fileUtil.get().getConfigurationSection("Staffs").getKeys(false));
            Player player = Bukkit.getPlayer(staff.get(i));
            if(player == null) {
                continue;
            }
            String name = fileUtil.get().getBoolean("Staffs." + staff.get(i) + ".show_realname") ? staff.get(i) : player.getDisplayName();
            ItemStack head = Utility.getSkull(Bukkit.getOfflinePlayer(staff.get(i)), fileUtil.get().getString("Staffs." + staff.get(i) + ".prefix") + name);
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
