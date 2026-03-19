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


    // LC 871
    // 9.50 - 10 am
    /**
     *  -> Return the `minimum` number of
     *     refueling stops the car must make in order
     *     to reach its destination.
     *       - If it cannot reach the destination,
     *          return -1.
     *
     *  - car: start -> dest
     *  - target miles away (east)
     *  - car has `startFuel` of gas at beginning
     *  - use 1 litter of gas / mile
     *
     *   - stations[i] = [positioni, fueli]
     *
     *     - positioni: positioni  miles east of the start point
     *     - fueli: has fueli of gas
     *
     *   -
     *
     *
     *  ------------------
     *
     *   IDEA 1) BRUTE FORCE ???
     *
     *   IDEA 2) PQ ???
     *
     *     -> big PQ. sort on positioni,
     *        ONLY add stations when its dist <= cur gas + cur_position
     *
     *        ...
     *
     *        repeat above till can or can't reach the destination
     *
     *  IDEA 3) GREEDY / DP ???
     *
     *
     *
     *  -----------------
     *
     *  ex 3)
     *   Input: target = 100, startFuel = 10,
     *   stations = [[10,60],[20,30],[30,30],[60,40]]
     *   Output: 2
     *
     *
     *   -> PQ =  [ [10,60] ],  p = 0, sf = 10, t = 100,
     *   -> PQ = [ [20,30],[30,30],[60,40] ]  p = 10, sf = 60, t = 100
     *   -> PQ =   [ [20,30],[30,30] ], p = 60, sf = 50, t = 100
     *   ->  p + sf >= 100, so we CAN already reach the dest,
     *       NO NEED to visit gas station anymore
     *
     *
     *
     */
    // IDEA 2) PQ ???
    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        // edge

        // PQ: big PQ: [positioni, fueli]
        // sort on positioni (big -> small)
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[0] - o1[0];
                return diff;
            }
        });

        // /??
        //init
        for(int[] s: stations){
            pq.add(new Integer[]{s[0], s[1]});
        }

        int curPosition = 0;
        int opCnt = 0;

        //List<Integer[]> backup = new ArrayList<>();

        // ????
        while (curPosition < target || !pq.isEmpty()){

            // ??? reset backup everytime ???? //?? better way ????
            List<Integer[]> backup = new ArrayList<>();

            while (!pq.isEmpty() && pq.peek()[0] > curPosition + startFuel){
                backup.add(pq.poll());
            }

            // edge ???
            if(pq.isEmpty() && curPosition < target){
                return -1;
            }

            // found the `next` station
            Integer[] next = pq.poll();
            target += next[0];
            startFuel = ( next[1] + startFuel - next[0] );
            opCnt += 1;

            // add backup back to PQ
            for(Integer[] x: backup){
                pq.add(x);
            }
        }



        return opCnt > 0 ? opCnt : -1;
    }




    // LC 834
    // 11.13 - 23 am
    /**
     * -> Return an array answer of length n
     *   where answer[i]
     *    - is the sum of the distances between the ith
     *       - node in the tree and all other nodes.
     *
     *
     *  - n nodes: 0 -> n-1
     *
     *  - n-1 edges
     *    - edges[i] = [ai, bi]
     *       -  edge between nodes ai and bi in the tree.
     *
     *
     *
     *
     *  -----------------
     *
     *   IDEA 1) DFS ?????
     *
     *     dist(a,b) =
     *       if `different` side
     *        - dist(a, root) + dist(b, root)
     *       if `same` side
     *         - dist(a, root) - dist(b, root)
     *
     *   IDEA 1) DFS + prefix sum ???
     *
     *
     *
     *
     *  -----------------
     *
     *
     */

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // edge


        // build tree ?????

        // { parent:
        Map<Integer, List<Integer>> map = new HashMap<>();

        // ??
        int[] pathList = new int[edges.length];

        return pathList;
    }


    private void distHelper(){

    }

//    private Throwable myBuildTree(int[][] edges, ){
//        // ???
//        int[] rootItem = edges[]
//    }







}
