package LeetCodeJava.Stack;

// https://leetcode.com/problems/next-greater-element-ii/description/

import java.util.*;

/**
 * 503. Next Greater Element II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a circular integer array nums (i.e., the next element of nums[nums.length - 1] is nums[0]), return the next greater number for every element in nums.
 *
 * The next greater number of a number x is the first greater number to its traversing-order next in the array, which means you could search circularly to find its next greater number. If it doesn't exist, return -1 for this number.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1]
 * Output: [2,-1,2]
 * Explanation: The first 1's next greater number is 2;
 * The number 2 can't find next greater number.
 * The second 1's next greater number needs to search circularly, which is also 2.
 * Example 2:
 *
 * Input: nums = [1,2,3,4,3]
 * Output: [2,3,4,-1,4]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -109 <= nums[i] <= 109
 *
 *
 */
public class NextGreaterElement_II {

    // V0
    // IDEA: HASHMAP, STACK (mono increase stack) + LC 496
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] nextGreaterElements(int[] nums) {

        // edge
        if (nums == null || nums.length == 0) {
            return new int[0]; // Return empty array instead of null
        }

        int[] res = new int[nums.length];
        Arrays.fill(res, -1);

        /**
         *  NOTE !!!  map: { idx : next_bigger_val_idx }
         *
         *   in order to deal with `duplicated val`
         *   -> in map, we save INDEX !!!
         *
         *   e.g.
         *
         *   map: { idx : next_bigger_val_idx }
         *
         */
        Map<Integer, Integer> map = new HashMap<>();

        // mono stack
        /**
         *  NOTE !!!  stack : { next_bigger_val_idx }
         *
         *   in order to deal with `duplicated val`
         *   -> in stack, we save INDEX !!!
         *
         *   e.g.
         *
         *    stack : { next_bigger_val_idx }
         *
         */
        Stack<Integer> st = new Stack<>();

        int _len = nums.length;

        // double size of nums
        for (int i = 0; i < _len * 2; i++) {

            // adjust `idx`
            int adjusted_idx = i % _len;
            int val = nums[adjusted_idx];

            while (!st.isEmpty() && nums[st.peek()] < val) {
                int prev_idx = st.pop();

                map.put(prev_idx, adjusted_idx);
            }

            // NOTE !!! ONLY push the `first n` element
            if (i < _len) {
                st.add(adjusted_idx);
            }

        }

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(i)) {
                /**
                 *  NOTE !!! we modify `res`, instead of nums
                 */
                res[i] = nums[map.get(i)];
            }
        }

        return res;

    }

    // V0-1
    // IDEA: STACK + `circular array` simulation + op ( x % y = z )
    /**
     *  NOTE !!!
     *
     *   for stack, we need to store `next bigger val INDEX` (instead of val)
     *
     *
     *   ----
     *
     *
     *   We store indices instead of values in the stack because:
     *
     *   -> It allows us to:
     *
     *      - Track where each number came from (i.e., its position)
     *      - Update the result array (res[]) at the correct index
     *      - Correctly handle `DUPLICATED` values
     */
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] nextGreaterElements_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return new int[0]; // Return empty array instead of null
        }

        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, -1); // Default to -1 (no greater element)

        /**
         *  NOTE !!!
         *
         *  Stack : [idx_1, idx_2,...]
         *
         *  -> ONLY need to storage idx in stack,
         *     since we can get its value from nums[idx]
         */
        Stack<Integer> stack = new Stack<>(); // Store indices

        /**
         *  NOTE !!!
         *
         *  Iterate twice (simulate `circular` behavior)
         */
        for (int i = 0; i < 2 * n; i++) {
            /**
             *  NOTE !!!
             *
             *  below condition
             *
             *   1) stack is NOT empty
             *   2) nums[stack.peek()] < nums[i % n]
             *      - while `top` stack element is smaller than cur element, pop it from stack
             */
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n]) {
                /**
                 *  NOTE !!!
                 *
                 *  we update res, since the `1st bigger num` is found
                 */
                res[stack.pop()] = nums[i % n]; // Assign next greater element
            }
            if (i < n) { // Only push first `n` elements
                stack.push(i);
            }

        }

        return res;
    }

    // V0-2
    // IDEA: BRUTE FORCE + DOUBLE STACK
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] nextGreaterElements_0_2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        // `duplicate` nums
        int k = 0;

        int[] res = new int[nums.length];
        Arrays.fill(res, -1);

        List<Integer> keys = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < nums.length; i++) {
                int val = nums[i];
                keys.add(val);
            }
        }

//        // System.out.println(">>> nums2 = " + nums2);
//        System.out.println(">>> keys = " + keys);
//        System.out.println(">>> res (before) = " + res);

        Stack<Integer> st = new Stack<>();
        // ???
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < keys.size(); j++) {
                int k_ = keys.get(j);
                if (k_ > nums[i]) {
                    res[i] = k_;
                    break;
                }
            }
        }

        return res;
    }

    // V1-1
    // https://leetcode.com/problems/next-greater-element-ii/editorial/
    // IDEA: BRUTE FORCE
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] nextGreaterElements_1_1(int[] nums) {
        int[] res = new int[nums.length];
        int[] doublenums = new int[nums.length * 2];
        System.arraycopy(nums, 0, doublenums, 0, nums.length);
        System.arraycopy(nums, 0, doublenums, nums.length, nums.length);
        for (int i = 0; i < nums.length; i++) {
            res[i] = -1;
            for (int j = i + 1; j < doublenums.length; j++) {
                if (doublenums[j] > doublenums[i]) {
                    res[i] = doublenums[j];
                    break;
                }
            }
        }
        return res;
    }

    // V1-2
    // https://leetcode.com/problems/next-greater-element-ii/editorial/
    // IDEA: BETTER BRUTE FORCE
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] nextGreaterElements_1_2(int[] nums) {
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = -1;
            for (int j = 1; j < nums.length; j++) {
                if (nums[(i + j) % nums.length] > nums[i]) {
                    res[i] = nums[(i + j) % nums.length];
                    break;
                }
            }
        }
        return res;
    }

    // V1-3
    // https://leetcode.com/problems/next-greater-element-ii/editorial/
    // IDEA: STACK
    /**
     * time = O(1)
     * space = O(1)
     */
    public int[] nextGreaterElements_1_3(int[] nums) {
        int[] res = new int[nums.length]; // Initialize the result array to store the next greater element for each element in nums
        Stack<Integer> stack = new Stack<>(); // Stack to keep track of indices of elements while finding the next greater element
        /**
         *
         * 2. Two-Pass Logic (Circular Array Simulation):
         *
         *   The loop is set to iterate over the array twice
         *   (2 * nums.length - 1), simulating the circular nature of the array.
         *   This means that when you reach the end of the array,
         *   you "wrap around" and continue from the beginning.
         *
         *   i % nums.length ensures that the index wraps around to
         *   simulate the circular behavior of the array.
         *
         */
        for (int i = 2 * nums.length - 1; i >= 0; --i) { // Iterate over the array twice (simulating circular behavior)
            // While the stack is not empty and the current element is greater than or equal to the element at the index on the top of the stack
            /**
             *  Checking the stack:
             *
             *    The while loop inside the main loop checks
             *    if the current element (nums[i % nums.length])
             *    is greater than or equal to the element at the index stored
             *    at the top of the stack (nums[stack.peek()]).
             *    If the condition is true, it pops the stack because the
             *    element at the top of the stack can't be the next greater element
             *    for the current element. The stack only keeps indices of potential
             *    next greater elements.
             *
             *
             *    -> current element: (nums[i % nums.length])
             *    -> element at top of the stack:  (nums[stack.peek()]).
             */
            while (!stack.empty() && nums[stack.peek()] <= nums[i % nums.length]) {
                stack.pop(); // Pop the stack because we have found a smaller or equal element and it can't be the next greater for future elements
            }

            // If the stack is empty, there is no greater element, so assign -1
            // Otherwise, the next greater element is the element at the index on top of the stack
            /**
             *  Assigning the next greater element:
             *      After popping the stack, the next greater element for
             *      the current element is either the element at the top of
             *      the stack (if the stack is not empty), or -1 (if the stack is empty).
             *
             *   -  res[i % nums.length] = stack.empty() ? -1 :
             *      nums[stack.peek()] stores the result for the current element.
             *      If the stack is empty, it means no greater element exists for
             *      that element, so -1 is stored. Otherwise, the value at the index
             *      on top of the stack (nums[stack.peek()]) is the next greater element.
             *
             */
            res[i % nums.length] = stack.empty() ? -1 : nums[stack.peek()];

            // Push the current index to the stack (this index might be the next greater element for a future element)
            /**
             *
             * 4. Pushing the current index to the stack:
             *
             *    After processing the current element, its index (i % nums.length)
             *    is pushed onto the stack. This index might become the next
             *    greater element for an earlier element in future iterations.
             */
            stack.push(i % nums.length);
        }
        return res; // Return the result array
    }


    // V2

}
