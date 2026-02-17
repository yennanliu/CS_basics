package LeetCodeJava.Array;

// https://leetcode.com/problems/find-pivot-index/description/
/**
 *
 724. Find Pivot Index
 Easy
 Topics
 premium lock icon
 Companies
 Hint
 Given an array of integers nums, calculate the pivot index of this array.

 The pivot index is the index where the sum of all the numbers strictly to the left of the index is equal to the sum of all the numbers strictly to the index's right.

 If the index is on the left edge of the array, then the left sum is 0 because there are no elements to the left. This also applies to the right edge of the array.

 Return the leftmost pivot index. If no such index exists, return -1.



 Example 1:

 Input: nums = [1,7,3,6,5,6]
 Output: 3
 Explanation:
 The pivot index is 3.
 Left sum = nums[0] + nums[1] + nums[2] = 1 + 7 + 3 = 11
 Right sum = nums[4] + nums[5] = 5 + 6 = 11
 Example 2:

 Input: nums = [1,2,3]
 Output: -1
 Explanation:
 There is no index that satisfies the conditions in the problem statement.
 Example 3:

 Input: nums = [2,1,-1]
 Output: 0
 Explanation:
 The pivot index is 0.
 Left sum = 0 (no elements to the left of index 0)
 Right sum = nums[1] + nums[2] = 1 + -1 = 0


 Constraints:

 1 <= nums.length <= 104
 -1000 <= nums[i] <= 1000
 *
 *
 */
public class FindPivotIndex {

    // V0
    // IDEA: PREFIX SUM  + LEFT, RIGHT SUB ARR SUM
    public int pivotIndex(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return 0;
        }

        int sum = 0;
        for (int x : nums) {
            sum += x;
        }
        // prefix
        int prefix = 0;
        for (int i = 0; i < nums.length; i++) {
            int x = nums[i];
            prefix += x;
            /**
             *   pivot idx:
             *
             *   nums = [1,7,3,6,5,6]
             *                 p
             *
             *          |----|   |--|
             *           left     right
             *
             *  -> p is the `pivot point`
             *  -> so sum(left) == sum(right)
             *
             */
            int left = prefix - x;
            int right = sum - prefix;
            if (left == right) {
                return i;
            }
        }

        return -1;
    }

    // V0-1
    // IDEA: PREFIX SUM (gemini)
    public int pivotIndex_0_1(int[] nums) {
        int n = nums.length;
        int totalSum = 0;

        // 1. Build the prefix sum array (size n + 1)
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            totalSum += nums[i];
            prefix[i + 1] = totalSum;
        }

        // 2. Iterate through each index to see if it's the pivot
        for (int i = 0; i < n; i++) {
            // Sum of elements strictly to the left of index i
            int leftSum = prefix[i];

            // Sum of elements strictly to the right of index i
            // Total - (Everything up to i)
            int rightSum = totalSum - prefix[i + 1];

            if (leftSum == rightSum) {
                return i;
            }
        }

        return -1;
    }

    // V0-2
    // IDEA: PREFIX SUM (GPT)
    public int pivotIndex_0_2(int[] nums) {
        int totalSum = 0;
        int leftSum = 0;

        // Calculate total sum of the array
        for (int num : nums) {
            totalSum += num;
        }

        // Check for pivot index
        for (int i = 0; i < nums.length; i++) {
            // rightSum is totalSum - leftSum - nums[i]
            if (leftSum == totalSum - leftSum - nums[i]) {
                return i;
            }
            // Update leftSum for next iteration
            leftSum += nums[i];
        }

        return -1; // No pivot index found
    }



    // V1
    // https://leetcode.com/problems/find-pivot-index/solutions/2470014/very-easy-100-fully-explained-java-c-pyt-01to/
    public int pivotIndex_1(int[] nums) {
        // Initialize total sum of the given array...
        int totalSum = 0;
        // Initialize 'leftsum' as sum of first i numbers, not including nums[i]...
        int leftsum = 0;
        // Traverse the elements and add them to store the totalSum...
        for (int ele : nums)
            totalSum += ele;
        // Again traverse all the elements through the for loop and store the sum of i numbers from left to right...
        for (int i = 0; i < nums.length; leftsum += nums[i++])
            // sum to the left == leftsum.
            // sum to the right === totalSum - leftsum - nums[i]..
            // check if leftsum == totalSum - leftsum - nums[i]...
            if (leftsum * 2 == totalSum - nums[i])
                return i; // Return the pivot index...
        return -1; // If there is no index that satisfies the conditions in the problem statement...
    }


    // V2
    // https://leetcode.com/problems/find-pivot-index/solutions/6097948/video-calculate-left-total-and-right-tot-e5tj/
    public int pivotIndex_2(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }

        int leftTotal = 0;
        for (int i = 0; i < nums.length; i++) {
            int rightTotal = total - leftTotal - nums[i];
            if (rightTotal == leftTotal) {
                return i;
            }
            leftTotal += nums[i];
        }

        return -1;
    }



}
