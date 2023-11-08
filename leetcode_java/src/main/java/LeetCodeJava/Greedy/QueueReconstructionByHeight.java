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
    // IDEA : GREEDY
    // TODO : fix it
    /**
     *  Steps :
     *      input : [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
     *
     *      step 1) sort
     *          -> [[7,0],[7,1],[6,1],[5,0],[5,2],[4,4]]
     *
     *      step 2) loop (i) from left, check k, and swap/insert
     *          -> i=0, [[7,0],[7,1],[6,1],[5,0],[5,2],[4,4]]
     *          -> i=1, [[7,0],[7,1],[6,1],[5,0],[5,2],[4,4]]
     *          -> i=2, [[7,0],[6,1],[7,1],[5,0],[5,2],[4,4]]
     *          -> i=3, [[5,0],[7,0],[6,1],[7,1],[5,2],[4,4]]
     *          -> i=4, [[5,0],[7,0],[5,2],[6,1],[7,1],[4,4]]
     *          -> i=5, [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
     *
     */
//    public int[][] reconstructQueue(int[][] people) {
//
//        if (people == null || people.length == 0){
//            return null;
//        }
//
//        // setup custom data structure
//        class Element<H,K>{
//            H height;
//            K k;
//
//            Element(H height, K k){
//                this.height = height;
//                this.k = k;
//            }
//        }
//        // init result
//        List<Element> res = new ArrayList<Element>();
//
//        // sort on height (1st priority) and k (2nd priority)
//        Arrays.sort(people, (x, y) -> (Integer.compare(-x[0], -y[0])));
//
//        // for loop from left, do insert by k if meat condition
//        for (int i =0; i < people.length; i++){
//            int h = people[i][0];
//            int k = people[i][1];
//            Element data = new Element<Integer, Integer>(h,k);
//            // insert
//            if (k < i){
//                // insert to k idx
//                res.add(k, data);
//            }else{
//                res.add(data);
//            }
//        }
//
//        return (int[][]) res.toArray();
//    }

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
