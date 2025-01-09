package dev.Sorting;

import org.junit.Before;
import org.junit.Test;

import java.sql.Array;
import java.util.*;

public class HashMapSortTest {

//    @Before
//    public void init(){
//
//    }


    @Test
    public void testSortMapWithValue(){

        // ------------------- TEST 6 : sort map on val, key len
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 3);
        map.put("banana", 2);
        map.put("lem", 3);
        map.put("kiwi", 5);
        map.put("peachhh", 3);

        System.out.println("Original map: " + map);

        /**
         *  Note !!!
         *
         *   difference between below:
         *
         *
         * 1)  List<Set<String>> keys = Arrays.asList(map.keySet());
         *     In this line, you're using Arrays.asList() to wrap map.keySet() into a list.
         *     map.keySet() returns a Set<String>, which is a set of the map's keys.
         *     - Arrays.asList() takes an array (or a list of elements)
         *       and converts it into a fixed-size list. When you pass a single Set to Arrays.asList(),
         *       it wraps this single Set<String> into a list.
         *
         * 2) Arrays.asList() takes an array (or a list of elements)
         *    and converts it into a fixed-size list. When you pass a single Set
         *    to Arrays.asList(), it wraps this single Set<String> into a list.
         *   -> So, keys will be a List<Set<String>> where the list contains a single Set<String>, which is the set of keys from the map.
         *
         */
        //List<Set<String>> keys = Arrays.asList(map.keySet());
        ArrayList<String> keyList = new ArrayList<>(map.keySet());
        System.out.println(">>> (before sort) keyList = " + keyList);

        Collections.sort(keyList, (x,y) -> {
            // compare val, bigger first (decreasing order)
            int valDiff = map.get(y) - map.get(x);
            if (valDiff == 0){
                // compare key len, shorter first (increasing order)
                return x.length() - y.length();
            }
            return valDiff;
        });

        System.out.println(">>> (after sort) keyList = " + keyList);
    }

}
