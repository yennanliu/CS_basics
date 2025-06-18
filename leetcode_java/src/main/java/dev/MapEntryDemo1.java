package dev;

import java.util.HashMap;
import java.util.Map;

public class MapEntryDemo1 {
    public static void main(String[] args) {


        /**
         *
         *       for (Map.Entry<Integer, Integer> entry : node_cnt.entrySet()) {
         *             if (entry.getValue() == maxFreq) {
         *                 modes.add(entry.getKey());
         *             }
         *         }
         *
         */
        // LC 501
        Map<Integer, Integer> map = new HashMap<>();

        map.put(1,3);
        map.put(10,4);
        map.put(2,2);
        map.put(7,3);

        for(Map.Entry<Integer, Integer> entry: map.entrySet()){
            System.out.println(" key = " + entry.getKey() + ", val = " + entry.getValue());
        }

    }

}
