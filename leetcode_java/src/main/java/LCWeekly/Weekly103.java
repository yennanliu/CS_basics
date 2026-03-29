package LCWeekly;

import sun.security.krb5.internal.crypto.Aes128;

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


    // LC 2658
    // https://leetcode.com/problems/maximum-number-of-fish-in-a-grid/description/


    // LC 2659
    // https://leetcode.com/problems/make-array-empty/




}
