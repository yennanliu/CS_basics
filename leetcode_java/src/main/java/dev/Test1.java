package dev;

import LeetCodeJava.DataStructure.ListNode;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test1 {

    public ListNode swapPairs(ListNode head) {

        while (head != null){

            ListNode firstNode = head;
            ListNode secondNode = head.next;

        }
        return null;
    }

    public static void main(String[] args) {
//        String[] my_array = new String[10];
//        my_array[1] = "wef";
//        my_array[2] = "xxxf";
//
//        System.out.println(String.valueOf(my_array[1]));

        // reverse list
        List<Integer> l1 = new ArrayList<Integer>();
        l1.add(10);
        l1.add(-1);
        l1.add(99);
        System.out.println(l1);
        Collections.reverse(l1);
        /**
         *  src code:
         *
         *     @SuppressWarnings({"rawtypes", "unchecked"})
         *     public static void reverse(List<?> list) {
         *         int size = list.size();
         *         if (size < REVERSE_THRESHOLD || list instanceof RandomAccess) {
         *             for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
         *                 swap(list, i, j);
         *         } else {
         *             // instead of using a raw type here, it's possible to capture
         *             // the wildcard but it will require a call to a supplementary
         *             // private method
         *             ListIterator fwd = list.listIterator();
         *             ListIterator rev = list.listIterator(size);
         *             for (int i=0, mid=list.size()>>1; i<mid; i++) {
         *                 Object tmp = fwd.next();
         *                 fwd.set(rev.previous());
         *                 rev.set(tmp);
         *             }
         *         }
         *     }
         */
        System.out.println(l1);


        System.out.println("========================");

        // https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Sort/MeetingRooms.java
        int[][] intervals = {{0,1}, {2,3}, {3,4}};
        //System.out.println(Arrays.stream(intervals).toArray().toString());
        // Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(intervals, (x,y) -> Integer.compare(x[1], y[1]));

        int[] _array = {0,1,2,3};
        System.out.println(_array);
    }

}
