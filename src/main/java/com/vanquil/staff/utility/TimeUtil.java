package com.vanquil.staff.utility;

public class TimeUtil {

    public static long serialize(String text) {
        char timeSuffix = text.charAt(text.length() - 1);
        String finalText = text.replace("y", "");
        finalText += text.replace("M","");
        finalText += text.replace("w", "");
        finalText += text.replace("d", "");
        finalText += text.replace("h", "");
        finalText += text.replace("m", "");
        finalText += text.replace("s", "");
        long time = Long.parseLong(finalText);
        switch (timeSuffix) {
            case 'y':
                time *= 31556926;
                return time;
            case 'M':
                time *= 2629743;
                return time;
            case 'w':
                time *= 604800;
                return time;
            case 'd':
                time *= 86400;
                return time;
            case 'h':
                time *= 3600;
                return time;
            case 'm':
                time *= 60;
                return time;
            case 's':
                return time;
        }
        return 0;
    }

    public static long serialize(String[] text) {
        for (String t : text) {
            return serialize(t);
        }
        return 0;
    }

    public static long serializeRaw(String text) {
        if(!text.contains(" ")) {
            return serialize(text);
        }
        return serialize(text.split(" "));
    }
}
