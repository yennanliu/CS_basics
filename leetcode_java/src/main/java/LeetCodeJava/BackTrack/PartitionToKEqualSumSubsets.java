package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/partition-to-k-equal-sum-subsets/

import java.util.*;

/**
 * 698. Partition to K Equal Sum Subsets
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer array nums and an integer k, return true if it is possible to divide this array into k non-empty subsets whose sums are all equal.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,2,3,5,2,1], k = 4
 * Output: true
 * Explanation: It is possible to divide it into 4 subsets (5), (1, 4), (2,3), (2,3) with equal sums.
 * Example 2:
 *
 * Input: nums = [1,2,3,4], k = 3
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 16
 * 1 <= nums[i] <= 104
 * The frequency of each element is in the range [1, 4].
 *
 */
public class PartitionToKEqualSumSubsets {

    // V0
    // IDEA: BACKTRACK
    /**
     *
     * target_: the required sum for each of the k subsets.
     *
     * used[]: boolean array to track which numbers have been a
     *         ssigned to a subset already.
     *
     * subsetSum: running sum of the current subset being built.
     *
     * k: how many subsets still need to be built.
     *
     */
    int target_;

    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        if (sum % k != 0){
            return false;
        }

        target_ = sum / k;
        boolean[] used = new boolean[nums.length];
        return backtrack_(nums, 0, k, 0, used);
    }

    /**
     * If k == 0: All subsets have been formed — return true.
     *
     * If subsetSum == target_:
     *       You’ve successfully built a subset
     *     — `RECURSIVELY build` the next one by resetting sum and decrementing k.
     *
     */
    private boolean backtrack_(int[] nums, int i, int k, int subsetSum, boolean[] used) {
        if (k == 0){
            return true;
        }

        // NOTE !!! below
        // if `cur sub set sum equals to target`, we MOVE to NEXT sub set
        // (instead of return true directly)
        if (subsetSum == target_){
            return backtrack_(nums, 0, k - 1, 0, used);
        }

        /**
         *
         * Iteration:
         *
         * -  Loop from index i to the end:
         *
         *    - Skip duplicates to avoid redundant paths (nums[j] == nums[j-1]).
         *    - Skip used numbers.
         *    - Skip if adding nums[j] exceeds target.
         *
         * - If you find a number that fits, mark it as used, recurse deeper.
         *
         * - If recursion fails, backtrack by unmarking the number.
         *
         *
         */
        for (int j = i; j < nums.length; j++) {
            // NOTE !!! via `nums[j] == nums[j - 1]` we avoid duplicates path
            if (j > 0 && !used[j - 1] && nums[j] == nums[j - 1]){
                continue;
            }
            if (used[j] || subsetSum + nums[j] > target_){
                continue;
            }

            used[j] = true;

            // NOTE !!! early quit
            // NOTE !!! next start_idx is `j+1` (not `j`)
            /**
             *
             *  NOTE !!! if found a solution, need to return True IMEDIATELLY
             *
             *  NOTE !!! next start_idx is `j + 1` (but NOT `j`)
             *
             */
            /**
             * Ah, got it — you're asking:
             *
             * > What happens if we **remove the `if (...) return true;` wrapper** and simply write:
             * >
             * > ```java
             * > backtrack_(nums, j + 1, k, subsetSum + nums[j], used);
             * > ```
             *
             * ### ✅ Short Answer:
             *
             * If you **remove the `if (...) return true;`
             * and just call the function without checking its result**, then:
             *
             * 🔴 **Your code will continue exploring even after finding a valid solution.**
             *
             * This breaks the **early termination** logic, and in many cases, will lead to:
             *
             * * **Performance issues** (unnecessary recursive calls)
             * * **Incorrect results** (e.g., function may return `false` even if a valid partition exists)
             *
             * ---
             *
             * ### ✅ Why You Need `if (...) return true;`
             *
             * This line is critical because once you've found **any valid way**
             * to divide the array into `k` equal-sum subsets,
             * you want to **stop immediately** and return `true`
             * all the way up the recursion stack.
             *
             * Without it, you'd just keep exploring the remaining recursive paths,
             * possibly backtracking out of a valid one — and end up returning `false`
             * even when a solution was found.
             *
             * ---
             *
             * ### 🔁 Example Difference:
             *
             * **With:**
             *
             * ```java
             * if (backtrack_(...)) {
             *     return true; // found a valid partition early — stop here
             * }
             * ```
             *
             * **Without:**
             *
             * ```java
             * backtrack_(...); // just call it — even if true, do nothing
             * ```
             *
             * 🧨 The second version **ignores** the result of a successful path and continues the loop — **eventually returning `false`** even when a valid partition was found.
             *
             * ---
             *
             * ### ✅ Summary:
             *
             * | Code                                | Result                                                              |
             * | ----------------------------------- | ------------------------------------------------------------------- |
             * | `if (backtrack_(...)) return true;` | ✅ Correct. Early exit on success                                    |
             * | `backtrack_(...)` only              | ❌ Wrong. Ignores result and continues, may return false incorrectly |
             *
             * ---
             *
             */
            if (backtrack_(nums, j + 1, k, subsetSum + nums[j], used)){
                return true;
            }

            used[j] = false;
        }

        return false;
    }

    // V0-1
    // IDEA: HASHMAP + BACKTRACK (fixed by gpt)
    public boolean canPartitionKSubsets_0_1(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0 || k <= 0)
            return false;

        int totalSum = 0;
        for (int num : nums)
            totalSum += num;

        if (totalSum % k != 0)
            return false;
        int target = totalSum / k;

        // Step 1: Count frequencies using HashMap
        Map<Integer, Integer> cnt_map = new HashMap<>();
        List<Integer> num_list = new ArrayList<>();
        for (int num : nums) {
            cnt_map.put(num, cnt_map.getOrDefault(num, 0) + 1);
        }
        // Fill num_list with unique values
        num_list.addAll(cnt_map.keySet());

        // Step 2: Sort in descending order for optimization
        num_list.sort((a, b) -> b - a);

        // Step 3: Use an array to track current sum of each subset
        int[] buckets = new int[k];

        return backtrack(0, num_list, cnt_map, buckets, target);
    }

    /**
     *  NOTE !!!
     *
     *   we have 2 helper fumc for this problem
     *
     *   1. backtrack
     *   2. tryPlacing
     *
     */
    private boolean backtrack(int index, List<Integer> num_list, Map<Integer, Integer> cnt_map, int[] buckets,
                              int target) {
        if (index == num_list.size()) {
            // All numbers placed, check all buckets
            for (int b : buckets) {
                if (b != target)
                    return false;
            }
            return true;
        }

        int num = num_list.get(index);
        int count = cnt_map.get(num);

        // Try placing `num` (count times) into different buckets
        return tryPlacing(num, count, buckets, 0, target, index, num_list, cnt_map);
    }

    private boolean tryPlacing(int num, int count, int[] buckets, int bucketIndex, int target,
                               int numIndex, List<Integer> num_list, Map<Integer, Integer> cnt_map) {
        if (count == 0) {
            // Done placing this number, move to next
            return backtrack(numIndex + 1, num_list, cnt_map, buckets, target);
        }

        /**
         *  NOTE !!!
         *
         *   we loop over `bucket`,
         *   -> try every bucket, try to add new element (tryPlacing), validate result
         *
         *   (instead of loop over cnt_map)
         *
         */
        for (int i = bucketIndex; i < buckets.length; i++) {

            if (buckets[i] + num > target)
                continue;

            buckets[i] += num;

            /**
             *  NOTE !!!!
             *
             *   if below tryPlacing recursive call work, return true directly
             */
            if (tryPlacing(num, count - 1, buckets, i, target, numIndex, num_list, cnt_map)){
                return true;
            }

            // undo
            buckets[i] -= num;

            // Optimization: if placing in an empty bucket didn't work, don't try other empty buckets
            if (buckets[i] == 0){
                break;
            }

        }

        return false;
    }

    // V0-2
    // IDEA: BACKTRACK (fixed by gpt)
    public boolean canPartitionKSubsets_0_2(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return false;
        }

        // Sum of all elements in the array
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }

        // If total sum cannot be divided by k, it's impossible to partition
        if (sum % k != 0) {
            return false;
        }

        // Target sum for each subset
        int target = sum / k;

        // Sort the array in descending order to optimize backtracking
        Arrays.sort(nums);
        reverse_0_2(nums);

        // Create a bucket list to track the current sum of each subset
        int[] buckets = new int[k];

        // Backtracking to partition the array into k subsets
        return backtrack_0_2(nums, 0, buckets, target);
    }

    private boolean backtrack_0_2(int[] nums, int index, int[] buckets, int target) {
        if (index == nums.length) {
            // If all numbers are used, check if all buckets have the target sum
            for (int bucket : buckets) {
                if (bucket != target) {
                    return false;
                }
            }
            return true;
        }

        int num = nums[index];

        // Try placing the current number in any bucket
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] + num <= target) {
                buckets[i] += num; // Choose this bucket

                if (backtrack_0_2(nums, index + 1, buckets, target)) {
                    return true;
                }

                buckets[i] -= num; // Undo the choice if it leads to a dead-end
            }

            // Optimization: If the bucket is empty or this number is already placed in a previous bucket, no need to try this bucket again
            if (buckets[i] == 0) {
                break;
            }
        }

        return false;
    }

    // Helper method to reverse the array (sorting in descending order)
    private void reverse_0_2(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }

    // V1
    // https://www.youtube.com/watch?v=mBk4I0X46oI
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0698-partition-to-k-equal-sum-subsets.java
    int target;

    public boolean canPartitionKSubsets_1(int[] nums, int k) {
        int sum = 0;
        for(int n : nums){
            sum += n;
        }
        if(sum % k != 0)
            return false;

        target = sum / k;
        boolean[] used = new boolean[nums.length];
        return backtrack(nums, 0, k, 0, used);
    }

    private boolean backtrack(int[] nums, int i, int k, int subsetSum, boolean[] used){
        if(k == 0)
            return true;
        if(subsetSum == target)
            return backtrack(nums, 0, k-1, 0, used);

        for(int j = i; j < nums.length; j++){
            if(j > 0 && !used[j-1] && nums[j] == nums[j-1])
                continue;
            if(used[j] || subsetSum + nums[j] > target)
                continue;

            used[j] = true;
            if(backtrack(nums, j+1, k, subsetSum + nums[j], used))
                return true;

            used[j] = false;
        }
        return false;
    }

    // V2
    // https://leetcode.com/problems/partition-to-k-equal-sum-subsets/solutions/1772704/java-solution-with-comments-100-faster-1-3x14/
    public boolean canPartitionKSubsets_2(int[] nums, int k) {
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }

        //sum%k must equal to 0 if not just return false
        //if we have to to divide the array greater than array size retun false(we can't)
        if (sum % k != 0 || nums.length < k)
            return false;

        //sort so we can take last element and start filling our bucket
        Arrays.sort(nums);

        //our target is sum/k and we have to find this in nums, k times then it is valid
        return canPartitionKSubsets(nums, sum / k, nums.length - 1, new int[k]);

    }

    public boolean canPartitionKSubsets(int a[], int target, int i, int bucket[]) {

        //we have taken all the elements
        if (i == -1)
            return true;

        //start filling the buckets
        for (int j = 0; j < bucket.length; j++) {

            //can we take this ith element
            if (bucket[j] + a[i] <= target) {

                //if we take this element
                bucket[j] += a[i];

                //go to next element (in our case go to smallest ele bcz we sorted)
                //if we can fill all buckets then return true
                if (canPartitionKSubsets(a, target, i - 1, bucket))
                    return true;

                //means we can't fill other buckets if we take ith element so leave this element
                bucket[j] -= a[i];

            }

            //if our bucket is empty means we have not taken any elements in the buckets
            if (bucket[j] == 0)
                break;

        }

        //all buckets are full but i is pointing to some element (elements still left)
        //or our bucket is empty means we haven't take any element (not valid)
        return false;

    }

    // V3-1
    // https://leetcode.com/problems/partition-to-k-equal-sum-subsets/solutions/5559337/crazy-best-problem-to-solve-must-solve-t-6ksg/
    public boolean canPartitionKSubsets_3_1(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0)
            return false;

        int targetSum = sum / k;
        Arrays.sort(nums);
        // add a reverse it helps!
        return backtrack(nums.length - 1, nums, new int[k], targetSum);
    }

    private boolean backtrack(int index, int[] nums, int[] sums, int targetSum) {
        if (index < 0)
            return true;

        for (int i = 0; i < sums.length; i++) {
            if (sums[i] + nums[index] > targetSum)
                continue;

            sums[i] += nums[index];

            if (backtrack(index - 1, nums, sums, targetSum)) {
                return true;
            }

            sums[i] -= nums[index];

            if (sums[i] == 0)
                break;// why ? explanation given below
        }

        return false;
    }

    // V3-2
    // https://leetcode.com/problems/partition-to-k-equal-sum-subsets/solutions/5559337/crazy-best-problem-to-solve-must-solve-t-6ksg/
    public boolean canPartitionKSubsets_3_2(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0)
            return false;

        int targetSum = sum / k;
        Arrays.sort(nums);
        reverse(nums); // Reverse the array to have larger numbers first

        List<List<Integer>> subsets = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            subsets.add(new ArrayList<>());
        }

        boolean result = backtrack(0, nums, subsets, new int[k], targetSum);

        if (result) {
            System.out.println("Valid partition found:");
            for (int i = 0; i < subsets.size(); i++) {
                System.out.println("Subset " + (i + 1) + ": " + subsets.get(i));
            }
        } else {
            System.out.println("No valid partition found.");
        }

        return result;
    }

    private boolean backtrack(int index, int[] nums, List<List<Integer>> subsets, int[] sums, int targetSum) {
        if (index == nums.length) {
            return true; // All numbers have been used
        }

        for (int i = 0; i < subsets.size(); i++) {
            if (sums[i] + nums[index] > targetSum)
                continue;
            if (i > 0 && sums[i] == sums[i - 1])
                continue;

            subsets.get(i).add(nums[index]); // Add to subset
            sums[i] += nums[index]; // Update sum

            if (backtrack(index + 1, nums, subsets, sums, targetSum)) {
                return true;
            }

            sums[i] -= nums[index]; // Revert sum
            subsets.get(i).remove(subsets.get(i).size() - 1); // Remove from subset

            if (sums[i] == 0)
                break; // Optimization: no need to try empty subsets
        }

        return false;
    }

    private void reverse(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }


}
