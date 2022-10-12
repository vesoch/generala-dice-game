package com.generala;

import java.util.*;

public class Helper {

    /**
     * Checks if a string is containing only numeric characters.
     *
     * @param string A string on which the check will be performed.
     * @return boolean
     */
    public static boolean isNumeric(String string)
    {
        if (string == null) {
            return false;
        }

        int size = string.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the string is an Y/N answer.
     *
     * @param string
     * @return boolean
     */
    public static boolean isYesNoAnswer(String string)
    {
        return string.equals("Y") || string.equals("y") || string.equals("N") || string.equals("n");
    }

    /**
     * Sorts a HashMap by value in descending order.
     *
     * @param map The hashmap to be sorted.
     * @return
     */
    public static LinkedHashMap<Long, Long> sortHashMapByValuesDescending(HashMap<Long, Long> map)
    {
        LinkedHashMap<Long, Long> descendingMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> descendingMap.put(x.getKey(), x.getValue()));

        return descendingMap;
    }
}
