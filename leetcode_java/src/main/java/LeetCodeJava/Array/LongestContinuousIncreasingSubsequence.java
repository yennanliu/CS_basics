package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-continuous-increasing-subsequence/


public class LongestContinuousIncreasingSubsequence {

    // VO
//    public int findLengthOfLCIS(int[] nums) {
//    }

    // V1
    // https://leetcode.com/problems/longest-continuous-increasing-subsequence/solutions/2565636/clean-0ms-java-solution/
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
