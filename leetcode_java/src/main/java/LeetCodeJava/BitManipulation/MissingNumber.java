package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/missing-number/?envType=list&envId=xoqag3yj
/**
 *
 Code
 Testcase
 Testcase
 Test Result
 268. Missing Number
 Solved
 Easy
 Topics
 premium lock icon
 Companies
 Given an array nums containing n distinct numbers in the range [0, n], return the only number in the range that is missing from the array.



 Example 1:

 Input: nums = [3,0,1]

 Output: 2

 Explanation:

 n = 3 since there are 3 numbers, so all numbers are in the range [0,3]. 2 is the missing number in the range since it does not appear in nums.

 Example 2:

 Input: nums = [0,1]

 Output: 2

 Explanation:

 n = 2 since there are 2 numbers, so all numbers are in the range [0,2]. 2 is the missing number in the range since it does not appear in nums.

 Example 3:

 Input: nums = [9,6,4,2,3,5,7,0,1]

 Output: 8

 Explanation:

 n = 9 since there are 9 numbers, so all numbers are in the range [0,9]. 8 is the missing number in the range since it does not appear in nums.








 Constraints:

 n == nums.length
 1 <= n <= 104
 0 <= nums[i] <= n
 All the numbers of nums are unique.


 Follow up: Could you implement a solution using only O(1) extra space complexity and O(n) runtime complexity?
 *
 *
 */
import java.util.Arrays;

public class MissingNumber {

    // V0
    // IDEA : MATH
    public int missingNumber(int[] nums) {

        int n = nums.length;
        // NOTE !!! the ordering
        int sum_ = ((1+n) * (n)) / 2;
        int curSum = Arrays.stream(nums).sum();
        //System.out.println("sum_ = " + sum_ + " curSum = " + curSum);
        return sum_ - curSum;
    }

    // V0'
    // IDEA : MATH
    public int missingNumber_0(int[] nums) {
        if (nums.length == 0 || nums == null){
            return 0;
        }
        int _len = nums.length;
        int sum1 = Arrays.stream(nums).sum();
        int sum2 = ((0 + _len) * (_len+1)) / 2;
        return sum2 - sum1;
    }

    // V1
    // IDEA : MATH
    // https://leetcode.com/problems/missing-number/solutions/4461518/beats-100-with-java-86-c-easiest-logic-6-lines-code-java-100-runtime/?envType=list&envId=xoqag3yj
    public int missingNumber_1(int[]nums) {
        //Step1: Find the length of array.
        int n = nums.length;

        //Step2: Calculate the actual sum using Mathematical formulae
        long actualSum = (n*(n+1))/2;

        //Step3: Calculate the given sum.
        long sum = 0;
        for(int element: nums)
        {
            sum += element;
        }

        //Step 4: return actualSum-givenSum.
        return (int)(actualSum-sum);
    }

}
