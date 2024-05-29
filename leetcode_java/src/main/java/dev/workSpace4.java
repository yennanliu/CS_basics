package dev;

import java.util.*;

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


    }

}
