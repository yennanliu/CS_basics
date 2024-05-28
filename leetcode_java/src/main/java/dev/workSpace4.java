package dev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        System.out.println("list2 = " + list2);



    }

}
