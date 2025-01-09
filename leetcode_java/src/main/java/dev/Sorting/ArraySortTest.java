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
