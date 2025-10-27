package dev.LCWeekly;

import java.util.*;

// https://leetcode.com/contest/biweekly-contest-100/
// https://leetcode.cn/contest/biweekly-contest-100/
public class Weekly100 {

    // Q1
    // LC 2591
    // 18.19 - 29 PM
    // https://leetcode.com/problems/distribute-money-to-maximum-children/
    /**
     *
     *  -> Return the maximum number of children
     *     who may receive exactly 8 dollars
     *     or -1 if NOT possible
     *
     *     - money: amount of money
     *     - children: # of children
     *
     *    rule:
     *     - All money must be distributed.
     *     - Everyone must receive at least 1 dollar.
     *     - Nobody receives 4 dollars.
     *
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *
     *   ex 1) m = 20, c = 3
     *
     *   -> cnt = 2, rem = 20 - 8* 2 = 4
     *   -> cnt = 2-1 = 1
     *
     *   ex 2) m = 28, c = 3
     *
     *   -> cnt = 3, rem = 28 - 8*3 = 4
     *   ->
     *
     *
     */
//    public int distMoney(int money, int children) {
//        // edge
//        if(children == 0 || money == 0){
//            return -1;
//        }
//        int cnt = money / 8;
////        if(cnt <= 0){
////            return -1;
////        }
//        if(money == children){
//            return 0;
//        }
//        if(cnt == children){
//            return children;
//        }
//
//        //int cnt = money / 8;
//        //int ans = 0;
//        int res = money - cnt * 8;
//
//        return res == 4 ? cnt - 1: cnt;
//    }

    // FIX by gpt
    public int distMoney(int money, int children) {
        // Each child needs at least $1
        if (money < children)
            return -1;

        // Give each child $1 first
        money -= children;

        // Now see how many can get $7 more (so total $8)
        int cnt = Math.min(money / 7, children);
        money -= cnt * 7;
        children -= cnt;

        // If all children get $8 and no money left, perfect
        if (children == 0 && money == 0)
            return cnt;

        // If all children got $8 but still have money left, one must get >$8, invalid
        if (children == 0 && money > 0)
            return cnt - 1;

        // Special case: if one child left and they would end up with $4, that breaks the rule
        if (children == 1 && money == 3)
            return cnt - 1;

        return cnt;
    }



    // Q2
    // LC 2592
    // 18.43 - 53 pm
    // https://leetcode.com/problems/maximize-greatness-of-an-array/
    /**
     *  -> Return the MAX possible `greatness`
     *    you can achieve after permuting nums.
     *
     *    - 0 index arr
     *    - can `permute` nums into a new arr (perm)
     *    - greatness:  # of indices within 0 <= i < nums.length
     *                 which perm[i] > nums[i]
     *
     *
     *  IDEA 1) HASHMAP + BRUTE FORCE + PQ
     *
     *   -> get the `val - cnt` map
     *   -> go through nums, find if there is a `n+1` int in map,
     *      if yes, cnt += 1, update map as well.
     *      ... till the end
     *
     *
     *
     *  exp 1) nums = [1,3,5,2,1,3,1], ans = 4
     *
     *  -> nums = [1,3,5,2,1,3,1]
     *  -> perms = [2,5,1,3,3,1,1].
     *
     *  (val : cnt)
     *  -> map: {1:3, 2:1, 3:2, 5:1}
     *
     *
     *  -> nums = [1,3,5,2,1,3,1], map: {1:2, 3:1}
     *             2 5   3 2
     *
     *
     *  exp 2)  nums = [1,3,3,4], map: {1:1, 3:2, 4:1}
     *
     *   ->  nums = [1,3,3,4], map: {1:1, 3:1}
     *               3 4
     *
     */
//    public int maximizeGreatness(int[] nums) {
//        // edge
//        if(nums == null || nums.length <= 1){
//            return 0;
//        }
//        // PQ: big PQ
//        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o2 - o1;
//                return diff;
//            }
//        });
//        // map: {val: cnt}
//        Map<Integer, Integer> map = new HashMap<>();
//        // ???
//        for(int x: nums){
//            map.put(x, map.getOrDefault(x, 0) + 1);
//        }
//        pq.addAll(map.keySet());
//        System.out.println(">>> map = " + map + ", pq = " + pq );
//        int cnt = 0;
//
//        for(int i = 0; i < nums.length; i++) {
//            // ???
//            int x = nums[i];
//            for (int j = x + 1; j <= pq.peek(); j++) {
//                System.out.println(">>> x = " + x + ", j = "
//                        + j + ", cnt = " + cnt + ", PQ = " + pq);
//                if (map.containsKey(j)) {
//                    cnt += 1;
//                    map.put(j, map.get(j) - 1);
//                    if (map.get(j) - 1 == 0) {
//                        map.remove(j);
//                        pq.poll(); // ???
//                    }
//                    break;
//                }
//            }
//        }
//
//        return cnt;
//    }


    // IDEA: HASHMAP + PQ
    // TLE (fixed by gpt)
//    public int maximizeGreatness(int[] nums) {
//        if (nums == null || nums.length <= 1) {
//            return 0;
//        }
//
//        // Frequency map
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int x : nums) {
//            map.put(x, map.getOrDefault(x, 0) + 1);
//        }
//
//        // Use a *min*-heap (ascending order) to efficiently get smallest available > x
//        PriorityQueue<Integer> pq = new PriorityQueue<>(map.keySet());
//
//        int count = 0;
//
//        // Sort nums so we handle smaller numbers first
//        Arrays.sort(nums);
//
//        for (int x : nums) {
//            // Find the smallest available number greater than x
//            Integer chosen = null;
//            List<Integer> popped = new ArrayList<>();
//
//            while (!pq.isEmpty()) {
//                int top = pq.peek();
//                if (top <= x) {
//                    // too small → temporarily remove
//                    popped.add(pq.poll());
//                } else {
//                    // found smallest greater than x
//                    chosen = top;
//                    break;
//                }
//            }
//
//            if (chosen != null) {
//                count++;
//                // Use one occurrence of chosen
//                map.put(chosen, map.get(chosen) - 1);
//                if (map.get(chosen) == 0) {
//                    pq.poll(); // remove it from PQ if no more left
//                    map.remove(chosen);
//                }
//            }
//
//            // Put back temporarily popped values
//            for (int val : popped) {
//                if (map.containsKey(val) && !pq.contains(val)) {
//                    pq.add(val);
//                }
//            }
//        }
//
//        return count;
//    }


    // IDEA: TreeMap + 2 pointer (gpt)
    public int maximizeGreatness(int[] nums) {
        if (nums == null || nums.length <= 1)
            return 0;

        // count occurrences
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int count = 0;

        // iterate over nums (doesn’t matter if sorted or not)
        for (int x : nums) {
            // find the smallest available number > x
            Integer higher = map.higherKey(x);
            if (higher != null) {
                count++;
                // decrease frequency
                map.put(higher, map.get(higher) - 1);
                if (map.get(higher) == 0) {
                    map.remove(higher);
                }
            }
        }

        return count;
    }

    // Q3
    // https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/description/

    // Q4
    // https://leetcode.com/problems/minimum-time-to-repair-cars/description/

}
