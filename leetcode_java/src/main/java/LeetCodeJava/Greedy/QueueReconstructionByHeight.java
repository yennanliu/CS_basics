package LeetCodeJava.Greedy;

// https://leetcode.com/problems/queue-reconstruction-by-height/

import java.util.*;

/**
 *  You are given an array of people, people,
 *  which are the attributes of some people in a queue (not necessarily in order).
 *  Each people[i] = [hi, ki] represents the ith person of height hi with exactly ki other people in front who have a height greater than or equal to hi.
 *
 */
public class QueueReconstructionByHeight {

    // V0
    // IDEA : GREEDY (sort by height DESC, then insert by k index)
    /**
     *  Steps :
     *      input : [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
     *
     *      step 1) sort on height (big -> small), if same height, sort on k (small -> big)
     *          -> [[7,0],[7,1],[6,1],[5,0],[5,2],[4,4]]
     *
     *      step 2) insert each people at idx = k
     *              (all already inserted people are TALLER or as tall as
     *               the current one, so `k` is exactly the final index)
     *          -> [[7,0]]
     *          -> [[7,0],[7,1]]
     *          -> [[7,0],[6,1],[7,1]]
     *          -> [[5,0],[7,0],[6,1],[7,1]]
     *          -> [[5,0],[7,0],[5,2],[6,1],[7,1]]
     *          -> [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
     *
     */
    /**
     * time = O(N^2)  (N sort + N list insert, each insert is O(N))
     * space = O(N)
     */
    public int[][] reconstructQueue(int[][] people) {

        if (people == null || people.length == 0) {
            return new int[0][0];
        }

        /** NOTE !!!
         *
         *  sort on height (big -> small),
         *  if `same height`, sort on k (small -> big)
         */
        Arrays.sort(people, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] == o2[0]) {
                    return Integer.compare(o1[1], o2[1]);
                }
                return Integer.compare(o2[0], o1[0]);
            }
        });

        /** NOTE !!!
         *
         *  use LinkedList, since we need `insert at idx` op
         */
        List<int[]> res = new LinkedList<>();
        for (int[] p : people) {
            // NOTE !!! insert at idx = k
            res.add(p[1], p);
        }

        return res.toArray(new int[people.length][2]);
    }

    // V1
    // IDEA : GREEDY
    // https://leetcode.com/problems/queue-reconstruction-by-height/editorial/
    public int[][] reconstructQueue_2(int[][] people) {
        Arrays.sort(people, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                // if the heights are equal, compare k-values
                return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
            }
        });

        List<int[]> output = new LinkedList<>();
        for(int[] p : people){
            output.add(p[1], p);
        }

        int n = people.length;
        return output.toArray(new int[n][2]);
    }

    // V2
    // IDEA : GREEDY
    // https://leetcode.com/problems/queue-reconstruction-by-height/solutions/2211635/python-java-c-short-greedy-solution-with-interview-tips/
    public int[][] reconstructQueue_3(int[][] people) {
        List<int[]> result = new ArrayList<>(); //return value

        Arrays.sort(people, (a, b) -> {
            int x = Integer.compare(b[0], a[0]);
            if(x == 0) return Integer.compare(a[1], b[1]);
            else return x; });

        for(int[] p: people)
            result.add(p[1], p);

        return result.toArray(new int[people.length][2]);
    }

}
