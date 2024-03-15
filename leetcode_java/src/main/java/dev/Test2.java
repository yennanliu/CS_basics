package dev;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

public class Test2 {
    public static void main(String[] args) {

//        String word = "heloooo 123 111";
//        for (String x : word.split("")){
//            System.out.println(x);
//        }

//        int r = 3;
//        System.out.println("start");
//        while (r >= 0){
//            r -= 1;
//            System.out.println(r);
//            if (r == 2){
//                break;
//                //continue;
//            }
//          }
//        System.out.println("end");


        System.out.println(Arrays.asList(1,2,3));

        System.out.println(Arrays.asList(1,2));

        List<Integer> x = Arrays.asList(1, 2);

        System.out.println(new int[]{1,2,3});

        List<Integer> x2 = new ArrayList<>(Arrays.asList(1, 2, 3));


        // PriorityQueue for PriorityQueue
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(1);

        // LinkedList for Queue
        Queue<Integer> q = new LinkedList<>();
        q.add(1);


        String str = "abcd";

        System.out.println(str);
        System.out.println(str.indexOf(1));

    }
    
}
