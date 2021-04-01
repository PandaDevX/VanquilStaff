package com.vanquil.staff.data;

import com.vanquil.staff.Staff;

import java.util.HashMap;

public final class Storage {

    // chat control data
    public static long slow = Staff.getInstance().getConfig().getLong("ChatControl.default");
    public static HashMap<String, Long> coolDown = new HashMap<>();

}
