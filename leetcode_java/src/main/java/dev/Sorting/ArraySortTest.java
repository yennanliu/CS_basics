package dev.Sorting;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ArraySortTest {

    @Test
    public void testOnArrayVal1(){

        Integer[] arr = new Integer[5];
        arr[0] = 10;
        arr[1] = -1;
        arr[2] = 99;
        arr[3] = 7;
        arr[4] = 2;

        /** NOTE !!! how we print context inside array
         *
         *   -> via Arrays.asList()
         */
        System.out.println(">>> (before) arr = " + Arrays.asList(arr));

        /**
         *  Why for hashMap, List we use `Collections.sort`
         *  but for array, we use `Arrays.sort` ?
         *
         *
         *  -> In brief, `array` is the basic data structure in java,
         *     it's a fixed size (not dynamic),  `Arrays utility` offers basic
         *     functions for support operation on such structures
         *
         *     while `collections` is the dynamic data structure,
         *     (e.g. ArrayList, LinkedList, HashMap...)
         *
         *
         *
         *
         *  1)
         *
         *  - Collections Framework:
         *
         *     Java provides the Collections Framework to
         *     work with dynamic collections that have flexible sizes,
         *     such as List, Set, and Queue. Since List is part of the framework,
         *     Collections.sort() is used for sorting List objects.
         *     The framework provides numerous utilities for adding, removing, and sorting elements.
         *
         *  - Arrays:
         *
         *     Arrays are a more primitive data structure in Java.
         *     The Arrays.sort() method is provided by the Arrays utility
         *     class specifically for sorting arrays, which do not have the flexibility
         *     of the List interface. Arrays are fixed-size,
         *     and Arrays.sort() provides an efficient way to sort them.
         *
         *
         *  2)  Key Differences Between Sorting with Arrays.sort() vs Collections.sort():
         *
         *     - Arrays.sort():
         *         Works with primitive arrays (e.g., int[], double[])
         *         and arrays of objects. It is optimized for the underlying
         *         fixed-size nature of arrays and is directly implemented in the Arrays class.
         *
         *
         *     - Collections.sort():
         *        Works only with List types (e.g., ArrayList, LinkedList, etc.).
         *        It is implemented in the Collections utility class and requires
         *        that the data be in a List form.
         *
         */

        /**
         *  NOTE !!!
         *
         *   instead of `Collections.sort`, we use Arrays.sort
         */
        // V1: Comparator.compare
        Arrays.sort(arr, new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o2;
                return diff;
            }
        });

        // V2: lambda
//        Arrays.sort(arr, (x, y) ->{
//            int diff = y - x;
//            return diff;
//        });


        // NOTE !!! below is WRONG

//        Collections.sort(arr,  new Comparator<Integer>(){
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return 0;
//            }
//        });

        System.out.println(">>> (after) arr = " + Arrays.asList(arr));
    }
}
