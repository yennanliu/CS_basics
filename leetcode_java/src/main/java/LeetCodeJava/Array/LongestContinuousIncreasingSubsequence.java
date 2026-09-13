package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-continuous-increasing-subsequence/


/**
 * 674. Longest Continuous Increasing Subsequence
 * Easy
 *
 * Given an unsorted array of integers nums, return the length of the longest continuous increasing subsequence (i.e. subarray). The subsequence must be strictly increasing.
 *
 * A continuous increasing subsequence is defined by two indices l and r (l < r) such that it is [nums[l], nums[l + 1], ..., nums[r - 1], nums[r]] and for each l <= i < r, nums[i] < nums[i + 1].
 *
 * Example 1:
 *
 * Input: nums = [1,3,5,4,7]
 * Output: 3
 * Explanation: The longest continuous increasing subsequence is [1,3,5] with length 3.
 * Even though [1,3,5,7] is an increasing subsequence, it is not continuous as elements 5 and 7 are separated by element
 * 4.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2,2,2]
 * Output: 1
 * Explanation: The longest continuous increasing subsequence is [2] with length 1. Note that it must be strictly
 * increasing.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class LongestContinuousIncreasingSubsequence {

    // V0
//    public int findLengthOfLCIS(int[] nums) {
//    }

    // V1
    // https://leetcode.com/problems/longest-continuous-increasing-subsequence/solutions/2565636/clean-0ms-java-solution/
    /**

     * time = O(N)

     * space = O(1)

     */
    public int findLengthOfLCIS_1(int[] nums)
    {
        int max =0;
        int count =0;

        for(int i=1; i<nums.length;i++)
        {
            if(nums[i-1] < nums[i])
            {
                count++;
                max = Math.max(count , max);
            }
            else
                count =0;
        }
        return max+1;
    }

    // V2
    // https://leetcode.com/problems/longest-continuous-increasing-subsequence/solutions/3426987/solution/
    /**

     * time = O(N)

     * space = O(1)

     */
    public int findLengthOfLCIS_2(int[] nums) {
        int maxCount = 1;
        int currentCount = 1;
        int i = 0 ;
        int j = 1;
        while(j<nums.length)
        {
            if(nums[j]>nums[i])
            {
                currentCount++;
                i++;
                j++;
            }
            else
            {
                i = j;
                j++;
                currentCount = 1;
            }
            if(maxCount<currentCount)
            {
                maxCount = currentCount;
            }
        }
        return maxCount;
    }

    // V3
    // https://leetcode.com/problems/longest-continuous-increasing-subsequence/solutions/3118835/simple-java-solution-using-2-pointer-approach-time-beats-100-of-the-solutions/
    /**

     * time = O(N)

     * space = O(1)

     */
    public int findLengthOfLCIS_3(int[] nums)
    {
        int maxCount = 1;
        int currentCount = 1;
        int i = 0 ;
        int j = 1;
        while(j<nums.length)
        {
            if(nums[j]>nums[i])
            {
                currentCount++;
                i++;
                j++;
            }
            else
            {
                i = j;
                j++;
                currentCount = 1;
            }
            if(maxCount<currentCount)
            {
                maxCount = currentCount;
            }

        }
        return maxCount;
    }

}
