package dev;

import LeetCodeJava.LinkedList.FlattenMultilevelDoublyLinkedList;

import java.util.*;

public class workspace15 {

    // LC 430
    // 4.28 - 4.38 pm
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
    };

    /**
     *  IDEA 1) LINKED LIST OP
     *
     *   step 1) check if `child exists`
     *   step 2) re-point
     *       next to child
     *       child prev to cur
     *   step 3) if a `nested child`, repeat above step
     *   step 4) re-point `next` to next
     *
     */
    // IDEA 2) ITERATIVE + LINKED LIST
    public Node flatten(Node head) {
        if (head == null){
            return null;
        }

        Stack<Node> st = new Stack<>();
        // `null` pointer
        Node _prev = new Node(); // ???

        st.add(head); // ???

        // stack: FILO, so we can add `next` first, then child
        // and `pop` `child` then `next`
        // e.g. process child -> next
        while(!st.isEmpty()){

            // step 1) pop `last` node
            Node cur = st.pop();
            // step 2) point cur prev to _prev ????
            cur.prev = _prev;

            // step 3) point _prev next to cur
            _prev.next = cur;
//            // step 4) point _prev child to null
//            _prev.child = null; // ???

            // step 5) move _prev to cur
            _prev = cur;

            if(cur.next != null){
                st.add(cur.next);
            }

            // ???? (dealing with `nested` child structure)
            if(cur.child != null){
                st.add(cur.child);
            }
        }

        // remove dummy ????
        _prev.next.prev = null; // /????

        return _prev.next; // ???
    }



    // IDEA 1) RECURSIVE + LINKED LIST
    public Node flatten_2(Node head) {
        // edge
        if(head == null){
            return head;
        }
        if(head.child == null){
            return head;
        }

        Node res = head; // ????
        // cache cur node ???
        Node _cur = head;

        // cache `next` ???
        Node _next = head.next;

        // child
        while (head.child != null){
            Node _cuild_end = flatten(head.child); // ????
            res.next = _cuild_end; // ???
            res = _cuild_end;
        }

        // next
        res.next = _next;


        return res;
    }

    // ???
    private Node childHelper(Node head){
        return null;
    }

  // LC 53
  // 2.19 - 2.29 pm
  /**
   *
   *  Given an integer array nums,
   *  find the `subarray` with the `largest sum`, and return its sum.
   *
   *
   *  -> A subarray is a contiguous non-empty
   *  sequence of elements within an array.
   *
   *
   *
   *  IDEA 1) BRUTE FORCE
   *  IDEA 2) KADANE ALGO
   *  IDEA 3)  SLIDE WINDOW ???
   *
   *
   *  exp 1)
   *
   *   Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
   *   Output: 6
   *
   *
   *   [-2,1,-3,4,-1,2,1,-5,4]   l_b = -2, g_b = -2, ans = -2
   *    x
   *
   *   [-2,1,-3,4,-1,2,1,-5,4]   l_b = -1, g_b = 1, ans = 1
   *       x
   *
   *   [-2,1,-3,4,-1,2,1,-5,4]   l_b = 0, g_b = 1, ans = 1
   *         x
   */
  // IDEA 2) KADANE ALGO
  // -> maintain local, global max
  public int maxSubArray(int[] nums) {
        // edge
        if(nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }
        // brute force
        //int ans = Integer.MIN_VALUE; // ???
        int global_max = -1 * Integer.MAX_VALUE;
        int local_max = -1 * Integer.MAX_VALUE;

        for(int x: nums){
            if(local_max == -1 * Integer.MAX_VALUE){
                local_max = x;
            }else if(local_max + x < 0 || local_max + x < x){
                local_max = x; // ???
            }else{
                local_max += x;
            }

            global_max = Math.max(global_max, Math.max(local_max, x));

            System.out.println(">>> x = " + x + ", local_max = "
                    + local_max + ", global_max = " + global_max);
        }

        return global_max;
    }



    // IDEA 1) BRUTE FORCE -> double loop
    public int maxSubArray_1(int[] nums) {
        // edge
        if(nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }
        // brute force
        int ans = Integer.MIN_VALUE; // ???
        for(int i = 0; i < nums.length; i++){
            int tmp = 0;
            for(int j = i; j < nums.length; j++){
                tmp += nums[j];
                ans = Math.max(ans, tmp);
            }
        }

        return ans;
    }

  // LC 152
  // 3.19 - 3.29 pm
  /**
   *
   *  Given an integer array nums,
   *  find a `subarray` that has the largest product,
   *  and return the product.
   *
   *  -> A subarray is a contiguous non-empty
   *  sequence of elements within an array.
   *
   *
   *
   *  IDEA 1) BRUTE FORCE
   *
   *  IDEA 2) KADANE ALGO
   *   -> local, global max
   *   -> local, global min
   *
   *
   *
   *  exp 1)
   *
   *  Input: nums = [2,3,-2,4]
   *  Output: 6
   *
   *   [2,3,-2,4]  l_b = 2, g_b = 2, l_m = 2, l_m = 2,  ans = 2
   *    x
   *
   *   [2,3,-2,4]  l_b = 6, g_b = 6, l_m = 2, l_m = 2, ans = 6
   *      x
   *
   *   [2,3,-2,4]  l_b = -2, g_b = 6, g_m = -12, l_m = -12,, ans = 6
   *        x
   *
   *   [2,3,-2,4]  l_b = 4, g_b = 6, g_m = -48, l_m = -4,, ans = 6
   *           x
   *
   */
  // IDEA 2) KADANE ALGO
  public int maxProduct(int[] nums) {
      // edge
      if(nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }

      int local_min = nums[0];
      int local_max = nums[0];
     // int global_min = nums[0];
      int global_max = nums[0];

     // NOTE !!! start from idx = 1
      for(int i = 1; i < nums.length; i++){

          int x = nums[i];

          int _local_max = local_max;

          // V2
          local_max = Math.max(
                  x, Math.max(x * local_max, x * local_min)
          );

          local_min = Math.min(
                  x, Math.min(x * _local_max, x * local_min)
          );

          // ???
          global_max = Math.max(
                  global_max, local_max
          );

      }

      return global_max;
    }

    // LC Q1
    // https://leetcode.com/contest/weekly-contest-468/problems/bitwise-or-of-even-numbers-in-an-array/description/
    public int evenNumberBitwiseORs(int[] nums) {
      // edge
      if(nums.length == 0){
          return 0;
      }
      List<Integer> list = new ArrayList<>();
      for(int x: nums){
          if(x % 2 == 0){
              list.add(x);
          }
      }
      if(list.isEmpty()){
          return 0;
      }
        if(list.size() == 1){
            return list.get(0);
        }
      //int res = -1;

      //int tmp = list.get(0) | list.get(1);
      int tmp = 0;

      for(int i = 0; i < list.size(); i++){
         // tmp = tmp | (list.get(i) | list.get(i+1));
          tmp = (tmp | list.get(i));
      }

      return tmp;
    }

  // LC Q2
  // https://leetcode.com/contest/weekly-contest-468/problems/maximum-total-subarray-value-i/description/
  /**
   *  -> Return the `MAX` possible total value you can achieve.©leetcode
   *
   *
   *  1. You need to choose exactly `k ` non-empty subarrays nums[l..r] of nums©leetcode
   *  2. Subarrays may
   *     1) overlap, and
   *     2) the exact same subarray (same l and r) can be chosen` more than once.`
   *  3. !!!!
   *
   *   The value of a subarray nums[l..r] is defined as:
   *      max(nums[l..r]) - min(nums[l..r]).
   *
   *   -> val of sub arr is
   *      max (sub) - min(sub)
   *
   *  NOTE: A subarray is a contiguous non-empty sequence of elements within an array
   *
   */
  public long maxTotalValue_1(int[] nums, int k) {
      // edge
      if(nums.length <= 1){
          return 0;
      }
      if(nums.length == 2){
          return (long) Math.abs(nums[0] - nums[1]) * k;
      }

      // ???
      int min_val = Integer.MAX_VALUE;
      int max_val = -1 * Integer.MAX_VALUE;

      for(int x: nums){
          min_val = Math.min(min_val, x);
          max_val = Math.max(max_val, x);
      }

      return (long) (max_val - min_val) * k;
    }

    // LC Q3
    // https://leetcode.com/contest/weekly-contest-468/problems/split-and-merge-array-transformation/description/
    /**
     *
     * -> Return the `minimum` number of split-and-merge operations
     *   needed to transform `nums1 into nums2.`
     *
     *
     *  1. given nums1, nums2, both with len = n
     *  2. transform nums1 to nums2
     *  3. the op as below:
     *      1) choose sub arr [L, R]
     *      2) remove sub arr, keep prefix and suffix ??
     *      3) re-insert the removed part back to nums1, at ANY position
     *
     */
    public int minSplitMerge(int[] nums1, int[] nums2) {

      return 0;
    }


    // LC Q4
    //https://leetcode.com/contest/weekly-contest-468/problems/maximum-total-subarray-value-ii/description/
    /**
     *  -> Return the `MAX` possible total value you can achieve.©leetcode
     *
     *
     *  1. You need to choose exactly `k ` `DISTINCT`
     *    non-empty subarrays nums[l..r] of nums©leetcode
     *
     *
     *  2. Subarrays may
     *     1) overlap, and
     *     2) the exact same subarray (same l and r) can be chosen` more than once.`
     *
     *  3. !!!!
     *
     *   The value of a subarray nums[l..r] is defined as:
     *      max(nums[l..r]) - min(nums[l..r]).
     *
     *   -> val of sub arr is
     *      max (sub) - min(sub)
     *
     *  NOTE: A subarray is a contiguous non-empty sequence of elements within an array
     *
     *
     *  IDEA 1) GREEDY (BRUTE FORCE)
     *
     *  IDEA 2) SORTING (global sort), then get max1, max2, ... min1, min2...
     *        -> and we can the TOP K `big diff` between max, and max
     *
     *  IDEA 3) MAX, MIN PQ
     *
     *  IDEA 3) SLIDE WINDOW + HASHMAP RECORD {"start_end_idx" : biggest_diff }
     *
     *
     *
     */
    // BRUTE FORCE OPTIMIZED
    //
    public long maxTotalValue(int[] nums, int k) {
        // edge
        if(nums.length <= 1){
            return 0;
        }
//        if(nums.length == 2){
//            return (long) Math.abs(nums[0] - nums[1]) * k;
//        }

        Long res = 0L;
        // brute force
        // ???
        Long tmpSum = 0L;
        //List<Integer> cache = new ArrayList<>();
        PriorityQueue<Integer> max_pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1-o2;
                return diff;
            }
        });


        for(int i = 0; i < nums.length - 1; i++){
            int local_min = nums[i];
            int local_max = nums[i];
            for(int j = i+1; j < nums.length; j++){

                while(max_pq.size() > k){
                    max_pq.poll();
                }

                local_min = Math.min(local_min, nums[j]);
                local_max = Math.max(local_max, nums[j]);

                int diff = local_max - local_min;

                //cache.add(local_max - local_min);
                max_pq.add(diff);
                tmpSum += diff;
            }
        }

        // ordering

        //edge k > cache size
        if(k >= max_pq.size()){
            return tmpSum;
        }

        System.out.println(">>> max_pq = " + max_pq);
        int cnt = 0;
        List<Integer> tmp = new ArrayList<>();
        while(!max_pq.isEmpty()){
            tmp.add(max_pq.poll());
        }
        // reverse
        Collections.reverse(tmp);
        for(int j = 0; j < k; j++){
            res += tmp.get(j);
        }

        return res;
    }





    // brute force: OUT OF MEMORY
    public long maxTotalValue_100(int[] nums, int k) {
        // edge
        if(nums.length <= 1){
            return 0;
        }
//        if(nums.length == 2){
//            return (long) Math.abs(nums[0] - nums[1]) * k;
//        }

        Long res = 0L;
        // brute force
        // ???
        Long tmpSum = 0L;
        List<Integer> cache = new ArrayList<>();
        for(int i = 0; i < nums.length - 1; i++){
            int local_min = nums[i];
            int local_max = nums[i];
            for(int j = i+1; j < nums.length; j++){
                local_min = Math.min(local_min, nums[j]);
                local_max = Math.max(local_max, nums[j]);

                cache.add(local_max - local_min);
                tmpSum += (local_max - local_min);
            }
        }

        // ordering
        Collections.sort(cache, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        //edge k > cache size
        if(k >= cache.size()){
            return tmpSum;
        }

        for(int i = 0; i < k; i++){
            res += cache.get(i);
        }

        return res;
    }




//    public long maxTotalValue(int[] nums, int k) {
//        // edge
//        if(nums.length <= 1){
//            return 0;
//        }
//        if(nums.length == 2){
//            return (long) Math.abs(nums[0] - nums[1]) * k;
//        }
//
//        // ???
//        PriorityQueue<Integer> pq_max = new PriorityQueue<>(Comparator.reverseOrder());
//        PriorityQueue<Integer> pq_min = new PriorityQueue<>();
//
//        for(int x: nums){
//            while(!pq_max.isEmpty() && pq_max.size() < k){
//                pq_max.poll();
//            }
//            while(!pq_min.isEmpty() && pq_min.size() < k){
//                pq_min.poll();
//            }
//            pq_max.add(x);
//            pq_min.add(x);
//        }
//
//
//        Long res = 0L;
//        int cnt = 0;
//
//        while(cnt < k){
//            int pq_min_idx = 0;
//            while(pq_min_idx < pq_min.size()){
//                int max_val = pq_max.poll();
//                res += (max_val - pq_min.peek());
//                cnt += 1;
//            }
//        }
//
//        return res;
//    }



}
