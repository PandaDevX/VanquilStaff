package com.vanquil.staff.gui.inventory;

import com.vanquil.staff.Staff;
import com.vanquil.staff.utility.InventoryBuilder;
import com.vanquil.staff.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Pin {

    Inventory inventory = null;

    public void setup(String type) {
        InventoryBuilder builder = new InventoryBuilder("&4Pin", 4);

        // create numbers item
        ItemBuilder zero = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
        .replace(" ", "_").toUpperCase()));
        zero.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
        .replace("{name}", "0"));

        ItemBuilder one = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        one.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "1"));

        ItemBuilder two = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        two.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "2"));

        ItemBuilder three = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        three.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "3"));

        ItemBuilder four = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        four.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "4"));

        ItemBuilder five = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        five.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "5"));

        ItemBuilder six = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        six.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "6"));

        ItemBuilder seven = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        seven.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "7"));

        ItemBuilder eight = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        eight.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "8"));

        ItemBuilder nine = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.numbers")
                .replace(" ", "_").toUpperCase()));
        nine.setName(Staff.getInstance().getConfig().getString("Auth Pin.name")
                .replace("{name}", "9"));
        ItemBuilder login = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.login")
                .replace(" ", "_").toUpperCase()));
        login.setName("&a&lLogin");
        if(type.equalsIgnoreCase("register")) {
            login = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.register")
                    .replace(" ", "_").toUpperCase()));
            login.setName("&a&lRegister");
        }
        ItemBuilder eraser = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.eraser")
        .replace(" ", "_").toUpperCase()));
        eraser.setName("&c&lEraser");
        eraser.setLore("", "&5CLICK &6To erase", "&5SHIFT CLICK &6To reset");

        ItemBuilder placeholder = new ItemBuilder(Material.valueOf(Staff.getInstance().getConfig().getString("Auth Pin.placeholder")
        .replace(" ", "_").toUpperCase()));
        // place placeholders
        builder.placePlaceHolders(placeholder);

        // set items
        builder.setItem(32, zero);

        // 1st row
        builder.setItem(4, one);
        builder.setItem(5, two);
        builder.setItem(6, three);

        // 2nd row
        builder.setItem(13, four);
        builder.setItem(14, five);
        builder.setItem(15, six);

        // 3rd row
        builder.setItem(21, eraser);
        builder.setItem(22, seven);
        builder.setItem(23, eight);
        builder.setItem(24, nine);
        builder.setItem(25, login);

        inventory = builder.build();

        // close objects
        zero = null;
        one = null;
        two = null;
        three = null;
        four = null;
        five = null;
        six = null;
        seven = null;
        eight = null;
        nine = null;
        login = null;
        placeholder = null;
        builder = null;
    }

    public void openInventory(Player player) {
        player.openInventory(inventory);
    }
}
