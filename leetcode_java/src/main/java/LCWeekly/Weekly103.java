package LCWeekly;

import java.util.*;

/**
 *  - EN:
 *  https://leetcode.com/contest/biweekly-contest-103/
 *
 *
 *  - ZH:
 *  https://leetcode.cn/contest/biweekly-contest-103/
 *
 *
 */
// 14.58 - 16:28 (pm)
public class Weekly103 {


    // LC 2656
    // https://leetcode.com/problems/maximum-sum-with-exactly-k-elements/description/
    /**
     *  -> Return the `maximum` score
     *  you can achieve after performing
     *  the operation exactly `k times.`
     *
     *
     *   - 0 index nums
     *   - int k
     *
     *   - op:
     *     1. select m from nums
     *     2. remove m from nums
     *     3. add element = m + 1 to nums
     *     4. increase score by m
     *
     *
     *  -------------
     *
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) PQ
     *
     *
     *
     *  -------------
     *
     *
     */
    // IDEA 1) GREEDY
    public int maximizeSum(int[] nums, int k) {
        // edge
        if(k == 0 || nums.length == 0){
            return 0;
        }

        // big PQ (big -> small)
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });
        //List<Integer> list = new ArrayList<>();
        for(int x: nums){
            pq.add(x);
        }

        int score = 0;
        for(int i = 0; i < k; i++){
            if(!pq.isEmpty()){
                //int m = list.get(list.size() - 1);
                int val = pq.poll();
                int tmp = val + 1;
                score += tmp;
                pq.add(tmp);
            }
        }

        return score;
    }



    // LC 2657
    // https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/
    /**
     *
     *  -> Return the prefix common array of A and B.
     *
     *
     *  - 0 index permutation: A, B with len = n
     *  - prefix common array of A, B
     *     - C[i] is equal to the count of numbers
     *       that are present at or before
     *       the index i in both A and B
     *
     *
     *
     *  permutations - 排列
     *
     *  NOTE:
     *
     *   - A sequence of n integers is
     *     called a `permutation`
     *      -> if it contains all integers
     *       from 1 to n `exactly once.`
     *
     *
     *
     *
     *  -----------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) SET ??
     *
     *
     *  -----------
     *
     *  ex 1)
     *
     *  Input: A = [1,3,2,4], B = [3,1,2,4]
     *   Output: [0,2,3,4]
     *
     *   array with 0=not existed, 1 = existd ???
     *
     *
     *   A = [1,3,2,4],  arr=[1,0,0,0]
     *        i
     *
     *   B = [3,1,2,4]    arr = [0,0,1,0]
     *        i
     *
     *
     *
     *
     *   A = [1,3,2,4],   [1,3]   arr=[1,0,1,0]
     *          i
     *
     *  B = [3,1,2,4]     [3,1], arr = [1,0,1,0]
     *         j
     *
     *
     */
    //   IDEA 1) BRUTE FORCE + set ???
    // a better way ???
    public int[] findThePrefixCommonArray(int[] A, int[] B) {
        // edge
        if(A == null || B == null){
            return null; // ???
        }

        int size = A.length;
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();

        int[] res = new int[size];

        for(int i = 0; i < size; i++){
            setA.add(A[i]);
            setB.add(B[i]);
            res[i] = countHelper(setA, setB);
        }

        return res;
    }

    // /??
    private int countHelper(Set<Integer> setA, Set<Integer> setB){
        int cnt = 0;
        for(int x: setA){
            if(setB.contains(x)){
                cnt += 1;
            }
        }
        return cnt;
    }






    // LC 2658
    // https://leetcode.com/problems/maximum-number-of-fish-in-a-grid/description/


    // LC 2659
    // https://leetcode.com/problems/make-array-empty/




}
