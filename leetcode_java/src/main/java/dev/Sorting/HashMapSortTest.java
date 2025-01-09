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



        /**
         *  NOTE !!!
         *
         *  Why we can use `lambda expression` for above Comparator's compare method ?
         *
         *  -> The reason we can use a lambda expression
         *  in this case is that the Comparator interface in Java is a functional interface,
         *  which means it has exactly one abstract method. The method signature is:
         *
         *
         *  To summarize:
         *
         * - The Comparator interface has a single abstract method (compare(T o1, T o2)).
         * - This allows Java to use lambda expressions to provide an implementation for that method.
         * - The lambda expression is shorthand for an anonymous inner class that implements the compare method directly.
         *
         */
        

        /** V1 : lambda expression  */
//        Collections.sort(keyList, (x,y) -> {
//            // compare val, bigger first (decreasing order)
//            int valDiff = map.get(y) - map.get(x);
//            if (valDiff == 0){
//                // compare key len, shorter first (increasing order)
//                return x.length() - y.length();
//            }
//            return valDiff;
//        });

        /** V2 : regular way (use Comparator, then override `compare` method)  */
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String x, String y) {
                int valDiff = map.get(y) - map.get(x);
                if (valDiff == 0){
                    // compare key len, shorter first (increasing order)
                    return x.length() - y.length();
                }
                return valDiff;
            }
        });

        System.out.println(">>> (after sort) keyList = " + keyList);
    }

}
