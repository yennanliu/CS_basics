package ws;

import LeetCodeJava.Heap.LongestHappyString;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Workspace23 {

    // LC 1405
    // 7.51 - 8.01
    class ValCnt {
        char val;
        int cnt;

        ValCnt(char val, int cnt) {
            this.val = val;
            this.cnt = cnt;
        }
    }


    public String longestDiverseString(int a, int b, int c) {

        /** NOTE !!! we define custom class to trace both val and cnt */
        PriorityQueue<ValCnt> pq = new PriorityQueue<>(
                (x, y) -> y.cnt - x.cnt);

        StringBuilder sb = new StringBuilder();


        if (a > 0)
            pq.add(new ValCnt('a', a));
        if (b > 0)
            pq.add(new ValCnt('b', b));
        if (c > 0)
            pq.add(new ValCnt('c', c));


        /** NOTE !!!
         *
         *   ONLY 2 cases below:
         *
         *   1. if adding this char causes 3 consecutive
         *   2. else (can add cur char)
         */

        // /?????
        while(!pq.isEmpty()){

            // ???
            ValCnt first = pq.poll();
            int len = sb.length();

            //  1. if adding this char causes 3 consecutive
            if(len >= 2 &&
                    sb.charAt(len - 1) == first.val &&
                    sb.charAt(len - 2) == first.val){

                // edge case !!!
                if(pq.isEmpty()){
                    break;
                }

                // ???
                ValCnt second = pq.poll();
                sb.append(second.val);
                if(second.cnt - 1 > 0){
                    pq.add(new ValCnt(second.val, second.cnt - 1));
                }
                pq.add(first);
            }
            // 2. else (can add cur char)
            else{
                sb.append(first.val);
                if(first.cnt - 1 > 0){
                    pq.add(new ValCnt(first.val, first.cnt - 1));
                }
            }


        }

        return sb.toString();
    }








}
