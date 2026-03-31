package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-sum-with-exactly-k-elements/description/

import java.util.Arrays;

/**
 *
 Code
 Testcase
 Test Result
 2656. Maximum Sum With Exactly K Elements
 Easy
 Topics
 premium lock icon
 Companies
 You are given a 0-indexed integer array nums and an integer k. Your task is to perform the following operation exactly k times in order to maximize your score:

 Select an element m from nums.
 Remove the selected element m from the array.
 Add a new element with a value of m + 1 to the array.
 Increase your score by m.
 Return the maximum score you can achieve after performing the operation exactly k times.



 Example 1:

 Input: nums = [1,2,3,4,5], k = 3
 Output: 18
 Explanation: We need to choose exactly 3 elements from nums to maximize the sum.
 For the first iteration, we choose 5. Then sum is 5 and nums = [1,2,3,4,6]
 For the second iteration, we choose 6. Then sum is 5 + 6 and nums = [1,2,3,4,7]
 For the third iteration, we choose 7. Then sum is 5 + 6 + 7 = 18 and nums = [1,2,3,4,8]
 So, we will return 18.
 It can be proven, that 18 is the maximum answer that we can achieve.
 Example 2:

 Input: nums = [5,5,5], k = 2
 Output: 11
 Explanation: We need to choose exactly 2 elements from nums to maximize the sum.
 For the first iteration, we choose 5. Then sum is 5 and nums = [5,5,6]
 For the second iteration, we choose 6. Then sum is 5 + 6 = 11 and nums = [5,5,7]
 So, we will return 11.
 It can be proven, that 11 is the maximum answer that we can achieve.


 Constraints:

 1 <= nums.length <= 100
 1 <= nums[i] <= 100
 1 <= k <= 100

 *
 */
public class MaximumSumWithExactlyKElements {

    // V0
//    public int maximizeSum(int[] nums, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/maximum-sum-with-exactly-k-elements/solutions/7673449/maximum-sum-with-exactly-k-elements-java-gjue/
    public int maximizeSum_1(int[] nums, int k) {
        int sum = 0;
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < k; i++) {
            sum += nums[n - 1];
            nums[n - 1] = nums[n - 1] + 1;
        }
        return sum;
    }


    // V2
    // https://leetcode.com/problems/maximum-sum-with-exactly-k-elements/solutions/7528424/just-a-loop-by-bhavasree_j_k-x3na/
    public int maximizeSum_2(int[] nums, int k) {
        Arrays.sort(nums);
        int sum = 0;

        int ans = nums[nums.length - 1];
        while (k != 0) {

            sum += ans;

            k--;
            ans = ans + 1;
        }
        return sum;
    }




}
