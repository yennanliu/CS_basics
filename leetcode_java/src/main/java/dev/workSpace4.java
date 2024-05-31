package dev;

import java.util.*;
import java.util.stream.Collectors;

public class workSpace4 {
    public static void main(String[] args) {


//        String x = "leetcode";
//        String y = "code";
//
//        System.out.println(x.contains(y));
//        System.out.println(y.contains(x));

        /**
         *
         * 1) Array -> List
         *
         */
        Integer [] arr1 = new Integer[]{1,2,3};
        List<Integer> list1 = Arrays.asList(arr1);

        System.out.println("arr1 = " + arr1);
        System.out.println("list1 = " + list1);


        /**
         *
         * 2) List -> Array
         *
         */
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        Integer [] arr2 = list2.toArray(new Integer[list2.size()]);

        System.out.println("arr2 = " + arr2);
        System.out.println("list2 = " + list2); // list2 = [1, 2, 3]


        /** Reverse List */
        Collections.reverse(list2);
        System.out.println("list2 = " + list2); // list2 = [3, 2, 1]

        /** Init M x N matrix */
        Boolean[][] matrix = new Boolean[3][4];
        System.out.println(matrix);
        System.out.println(matrix[0][0]);

        // fill
        Boolean[] x = new Boolean[3];
        Arrays.fill(x, false);
        System.out.println(x[0]);

        /** Sort array */
        int[][] intervals = new int[][]{ {15,20}, {0,30}, {5,10} };
        System.out.println("---> intervals");
        for (int[] interval : intervals){
            System.out.println("1st = " + interval[0] + ", 2nd = " + interval[1]);
        }

        Arrays.stream(intervals).sorted();

        // sort (Arrays.sort)
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        System.out.println("---> sorted intervals");
        for (int[] interval : intervals){
            System.out.println("1st = " + interval[0] + ", 2nd = " + interval[1]);
            /**
             * 1st = 0, 2nd = 30
             * 1st = 5, 2nd = 10
             * 1st = 15, 2nd = 20
             *
             */
        }

        // sort (Arrays.stream(intervals).sorted)
//        int[][] sortedIntervals = Arrays.stream(intervals)
//                .sorted((a, b) -> Integer.compare(a[0], b[0]))
//                .toArray(int[][]::new);
//
//        System.out.println("---> sorted intervals V2");
//        for (int[] interval : sortedIntervals){
//            System.out.println("1st = " + sortedIntervals[0] + ", 2nd = " + sortedIntervals[1]);
//        }


        /** Array sort example 1 */
        System.out.println("---> array sort example 1 ");
        Integer[] _array = {5, 5, 7, 8, 9, 0};
        for (int a : _array){
            System.out.println("1st = " + a);
        }

        //Arrays.sort(_array, (a,b) -> Integer.compare(a, b));
        Arrays.sort(_array, (a,b) -> Integer.compare(-a, -b));

        System.out.println("---> array sort example 1 -- after sorted ");
        //Arrays.sort(_array, Collections.reverseOrder());
        for (int a : _array){
            System.out.println("1st = " + a);
        }

        /** List sort example 1 */
        System.out.println("----> List sort example 1");
        List<Integer> _list = new ArrayList();
        _list.add(5);
        _list.add(1);
        _list.add(3);
        _list.add(0);
        for (int a : _list){
            System.out.println("1st = " + a);
        }

        System.out.println("----> List sort example 1 -- after sorted ");
        // sort
        //Collections.sort(_list, (a,b) -> Integer.compare(a, b));
        Collections.sort(_list, (a,b) -> Integer.compare(-a, -b));
        for (int a : _list){
            System.out.println("1st = " + a);
        }


        /** List sort example 2 */

        System.out.println("----> List sort example 2");

        List<Integer> _list2 = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5));
//        for (int a : _list2){
//            System.out.println("1st = " + a);
//        }
        System.out.println(_list2);

        // Sort using Stream API
        _list2 = _list2.stream()
                .sorted((a, b) -> b - a)  // Using Comparator to sort in reverse order
                .collect(Collectors.toList());

        System.out.println("----> List sort example 2 -- after sorted ");
        System.out.println(_list2);



        System.out.println("----------------");

        String x_ = "leetcode";
        String[] x_arr = x_.split("");
        System.out.println(x_arr[0]);
        System.out.println(x_arr[1]);
        System.out.println(x_arr[0].equals("l"));

        System.out.println("----------------");

        String s = "www.runoob.com";
        char result = s.charAt(6);
        System.out.println(result);
        System.out.println(String.valueOf(result));



        System.out.println(" ---------------- SORT ON HASH MAP ----------------");


        // Create a HashMap
        HashMap<String, Integer> map = new HashMap<>();
        map.put("apple", 5);
        map.put("banana", 2);
        map.put("cherry", 8);
        map.put("date", 1);

        // Print the original map
        System.out.println("Original map: " + map);

        // Sort the map by values
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        // Create a new LinkedHashMap to preserve the order of the sorted entries
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

//        // V1 : via Entry
//        for (Map.Entry<String, Integer> entry : list) {
//            sortedMap.put(entry.getKey(), entry.getValue());
//        }


        // V2 : via key
        // Get the list of keys
        List<String> keys = new ArrayList<>(map.keySet());

        // Sort the keys based on their corresponding values
        keys.sort((k1, k2) -> map.get(k1).compareTo(map.get(k2)));

        /**
         * You can modify the code to avoid using Map.Entry by converting the
         * HashMap to a list of keys and then sorting the keys based on
         * their corresponding values. Here is the modified version:
         */
        for (String key : keys) {
            sortedMap.put(key, map.get(key));
        }

        // Print the sorted map
        System.out.println("Sorted map: " + sortedMap);


        //--------------

//        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
//        sortedEntries.sort(Map.Entry.comparingByValue());
//
//        // Print the sorted entries
//        for (Map.Entry<String, Integer> entry : sortedEntries) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }

        System.out.println("sub string --------------");
        String str1 = "abcd";
        System.out.println(str1);
        System.out.println(str1.substring(0,2));

    }

}
