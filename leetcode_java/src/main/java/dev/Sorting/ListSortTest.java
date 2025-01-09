package dev.Sorting;

import org.junit.Test;

import java.util.*;

public class ListSortTest {

    @Test
    public void testSortListWithVal_1(){

        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(99);
        list.add(1);
        list.add(-3);

        System.out.println(">>> (before) list = " + list);

        // V1: lambda
//        Collections.sort(list, (x,y) -> {
//            int diff = y - x;
//            return diff;
//        });

        // V2: Comparator.compare
        /**
         * NOTE !!!
         *
         *
         *  need to offer TYPE for comparator
         *
         *  e.g. `new Comparator<Integer>()`
         */
        Collections.sort(list, new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                //int diff = o1 - o2;  // smaller first (increasing order)
                int diff = o2 - o1; // bigger first, (decreasing order)
//                if(diff == 0){
//                    return
//                }
                return diff;
            }
        });

        System.out.println(">>> (after) list = " + list);

    }
}
