package dev;

import java.util.*;


public class SortMapByValueAndKeyLength {
    public static void main(String[] args) {

        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 3);
        map.put("banana", 2);
        map.put("grape", 3);
        map.put("kiwi", 5);
        map.put("peach", 3);

        System.out.println("Original map: " + map);

        Map<String, Integer> sortedMap = sortMapByValueAndKeyLength(map);
        System.out.println("Sorted map: " + sortedMap);


    }


    public static Map<String, Integer> sortMapByValueAndKeyLength(Map<String, Integer> map) {
        // Convert the map to a list of entries
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());

        // Sort the entry list
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                // First, compare by value in decreasing order
                int valueCompare = entry2.getValue().compareTo(entry1.getValue());

                // If values are equal, compare by key length in increasing order
                if (valueCompare == 0) {
                    return entry1.getKey().length() - entry2.getKey().length();
                }

                return valueCompare;
            }
        });

        // Convert the sorted list back to a linked map to preserve the order
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
