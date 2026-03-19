package ws;

import LeetCodeJava.Heap.LongestHappyString;

import java.util.*;

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



    // LC 1054
    // 8.32 - 43 am
    /**
     * IDEA 1) BRUTE FORCE
     *
     * IDEA 2) PQ ???
     *
     *
     */
    class ValCnt2{
        int val;
        int cnt;

        ValCnt2(int val, int cnt) {
            this.val = val;
            this.cnt = cnt;
        }

        @Override
        public String toString() {
            System.out.println(">> val = " + val + ", cnt = " + cnt);
           // return super.toString();
            return ">> val = " + val + ", cnt = " + cnt;
        }

    }
    public int[] rearrangeBarcodes(int[] barcodes) {
        // edge

        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();

        for(int x: barcodes){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        // pq : big pq, sort on cnt (big -> small)
        PriorityQueue<ValCnt2> pq = new PriorityQueue<>(new Comparator<ValCnt2>() {
            @Override
            public int compare(ValCnt2 o1, ValCnt2 o2) {
                int diff = o2.cnt - o1.cnt;
                return diff;
            }
        });

        for(int k: map.keySet()){
            pq.add(new ValCnt2(k, map.get(k)));
        }

        int[] res = new int[barcodes.length];


        // ???
        int i = 0;
        while (!pq.isEmpty()){
            ValCnt2 first = pq.poll();
            int len = pq.size();

            System.out.println(">>> res = " + Arrays.toString(res) +
                    ", len = " + len +
                    ", first = " + first);

            // case 1) prev == cur
            if(len >= 1 && res[i-1] == first.val){
                ValCnt2 second = pq.poll();

                System.out.println(">>> second = " + second);

                res[i] = second.val;
                second.cnt -= 1;
                if(second.cnt > 0){
                    pq.add(second);
                }
                pq.add(first);
            }
            // case 2) else
            else{
                //pq.add(first);
                res[i] = first.val;
                first.cnt -= 1;
                if(first.cnt > 0){
                    pq.add(first);
                }
            }

            i += 1;
        }


        return res;
    }







}
