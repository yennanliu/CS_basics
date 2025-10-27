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
        /** NOTE !!!
         *
         *  use `TreeMap` for using the `higherKey` method
         */
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
    // LC 2593
    // 19.29 - 46 pm
    // https://leetcode.com/problems/find-score-of-an-array-after-marking-all-elements/description/
    /**
     *
     *  -> Return the `score` you get
     *     after applying the above algorithm.
     *
     *    - start from score = 0
     *    - choose the `smallest` int that is NOT marked
     *       - if a tie, get the one with smallest idx
     *    - score += val (chosen num)
     *    - Mark the chosen element and its two adjacent elements
     *      if they exist.
     *    - Repeat until ALL the array elements are MARKED.
     *
     *
     *  IDEA 1) TREE MAP  + pointers ???
     *
     *   map: { val: [idx_1, idx_2,...] }
     *
     *   marked: [false, true, ...]
     *
     *  1.  go through the key (small -> big)
     *  2.  mark `chosen` to the idx and its adjacent
     *  3.  find the `next candidate`
     *  4.  repeat above, till the end
     *
     *
     *
     *  exp 1)  nums = [2,1,3,4,5,2], ans = 7
     *
     *  -> map: { 2:[0, 5], 1: [1], 3: [2], 4: [3], 5: [4] }
     *
     *  nums = [2,1,3,4,5,2], { 2:[5], 4: [3], 5: [4] }
     *          x x x
     *
     *   nums = [2,1,3,4,5,2], { 4: [3] }
     *           x x x   x  x
     *
     *
     *
     *  exp 2) nums = [2,3,5,1,3,2], ans = 5
     *
     *  -> map: { 2: [0, 5], 1: [3], 3:[1, 4], 5: [2] }
     *
     *
     *   nums = [2,3,5,1,3,2], map: { 2: [0, 5], 3:[1] }
     *               x x x
     *
     *  nums = [2,3,5,1,3,2], map: { 2: [ 5] }
     *          x x x x x
     *
     */
//    public long findScore(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            return nums[0];
//        }
//        long ans = 0L;
//
//        // map: { val: small-PQ (idx) } ????
//        // PQ default is small PQ ??? (small -> big)
//        PriorityQueue<Integer> smallPQ = new PriorityQueue<>();
//        Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();
//        for(int i = 0; i < nums.length; i++){
//            int x = nums[i];
//            PriorityQueue<Integer> tmpPQ = new PriorityQueue<>();
//            if(map.containsKey(x)){
//                tmpPQ = map.get(x);
//            }
//            tmpPQ.add(i);
//            map.put(x, tmpPQ);
//        }
//        System.out.println(">>> map = " + map);
//
//        // ??? default is `false` ???
//        boolean[] visited = new boolean[nums.length]; // ??
//        int visitedCnt = 0;
//
//
//        while (visitedCnt < nums.length){
//            int val = smallPQ.poll();
//            if (map.containsKey(val)) {
//                int idx = map.get(val).poll();
//                // mark as visted
//                int left = idx - 1;
//                int right = idx + 1;
//                if(left >= 0){
//                    visited[left] = true;
//                    //PriorityQueue<Integer> tmpPQ1 = map.get(left);
//                    //tmpPQ1
//                }
//                if(right < nums.length - 1){
//                    visited[right] = true;
//                }
//                ans += val;
//            }
//
//        }
//
//
//
//        return ans;
//    }


    // V1 (fixed by gpt)
    public long findScore(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        long ans = 0L;

        /**
         * We use a min-heap (PriorityQueue) that always
         * gives us the smallest value, breaking ties by index.
         * Each element is stored as [value, index].
         *
         */
        // min-PQ: (value, index)
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);

        // push all elements
        for (int i = 0; i < n; i++) {
            pq.offer(new int[] { nums[i], i });
        }

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int val = cur[0];
            int idx = cur[1];

            if (visited[idx])
                continue; // already marked

            // add to score
            ans += val;

            // mark itself and neighbors
            visited[idx] = true;
            if (idx - 1 >= 0)
                visited[idx - 1] = true;
            if (idx + 1 < n)
                visited[idx + 1] = true;
        }

        return ans;
    }




    // V2 (fixed by gemini)
    public long findScore_2(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        long ans = 0L;
        int n = nums.length;

        // A boolean array to track which elements are marked.
        // Defaults to false.
        boolean[] marked = new boolean[n];

        // --- Fix: Use a single Min-Heap (Priority Queue) ---
        // It will store {value, index} pairs.
        // We need a custom comparator to sort:
        // 1. By value (smallest first).
        // 2. If values are tied, by index (smallest first).
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) {
                    return a[0] - b[0]; // Compare by value
                } else {
                    return a[1] - b[1]; // Compare by index if values are tied
                }
            }
        });

        // --- Fix: Populate the heap ---
        // Add all {value, index} pairs to the heap.
        for (int i = 0; i < n; i++) {
            minHeap.offer(new int[] { nums[i], i });
        }

        // Loop until all elements have been processed from the heap.
        while (!minHeap.isEmpty()) {

            // 1. Get the smallest element.
            int[] current = minHeap.poll();
            int val = current[0];
            int idx = current[1];

            // 2. --- Fix: Check if already marked ---
            // If this element was already marked by a neighbor, skip it.
            if (marked[idx]) {
                continue;
            }

            // 3. Process the element:
            // Add its value to the score.
            ans += val;

            // Mark the element itself as "visited".
            marked[idx] = true;

            // 4. Mark its neighbors.
            int left = idx - 1;
            int right = idx + 1;

            if (left >= 0) {
                marked[left] = true;
            }

            // --- Fix: Correct boundary check ---
            // Check should be < n (the length), not < n - 1 (the last index).
            if (right < n) {
                marked[right] = true;
            }
        }

        return ans;
    }



    // Q4
    // LC 2594
    // 20.26 - 36 PM
    // https://leetcode.com/problems/minimum-time-to-repair-cars/description/
    /**
     *  -> Return the `min` time taken to repair ALL the cars.
     *
     *
     *  - All the mechanics can repair the cars `simultaneously.`
     *  - ranks: `level` of mechanics
     *    - rank_i: rank of mechanics_i
     *  - cars: total # of cars need to be repaired
     *  - A mechanic with a rank r can repair n cars in `r * n2 minutes.`
     *       - it takes `r * n2` minutes to repair n cars with rank r
     *
     *
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DP ????
     *
     *   IDEA 3) MATH ????
     *
     *   -> time cost = r1 * n1 * n1 + r2 * n2 * n2 + ...
     *   -> a * (n1^2) +  b * (n2 ^ 2) + .... <= ???
     *
     *
     */
    // https://leetcode.com/problems/minimum-time-to-repair-cars/editorial/
    public long repairCars(int[] ranks, int cars) {

        return 0L;
    }


}
