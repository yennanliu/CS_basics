package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/longest-increasing-subsequence/description/
/**
 * 300. Longest Increasing Subsequence
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, return the length of the longest strictly increasing
 * subsequence
 * .
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 * Example 2:
 *
 * Input: nums = [0,1,0,3,2,3]
 * Output: 4
 * Example 3:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2500
 * -104 <= nums[i] <= 104
 *
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongestIncreasingSubsequence {

    // V0
    // IDEA : DP
    // TODO : check & implment again
    public int lengthOfLIS(int[] nums) {

        if(nums == null || nums.length == 0){
            return 0;
        }

        int n = nums.length;
        // init dp
        int[] dp = new int[nums.length];
        /**
         *
         * NOTE !!!
         *
         * Arrays.fill(dp, 1);
         * -> sets every element in the array dp to the value 1.
         *
         */
        Arrays.fill(dp,1);
        // set res as final result (Longest Increasing Subsequence length) , keep updating it
        int res = 1;
        /**
         * NOTE !!!
         *
         *  main dp logic : dp[i]=Math.max(dp[i],dp[j]+1);
         *
         *  -> we still use "brute force", double looping over i, j
         *  However, we use dp to "memorize" the result already calculated before
         *
         *
         *  i start from 1, while j start from 0
         */
        for(int i=1; i<n; i++){
            for(int j=0; j<i; j++){
                if(nums[i]>nums[j]){
                    dp[i] = Math.max(dp[i],dp[j]+1);
                    res = Math.max(res, dp[i]);
                }
            }
        }
        return res;
    }

    // V0-1
    // IDEA: BRUTE FORCE + BINARY SEARCH (GPT)
    public int lengthOfLIS_0_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int left = 0, right = tails.size();

            // Use binary search to find the right position for num
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // If left is equal to the size of tails, it means the current number
            // is larger than all the numbers in tails
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }

        return tails.size();
    }


    // V1
    // IDEA : DP
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4509493/300/
    public int lengthOfLIS_1(int[] nums) {
        if(nums == null || nums.length == 0)return 0;
        int n=nums.length;
        int[] dp=new int[n];
        Arrays.fill(dp,1);
        for(int i=1;i<n;i++){
            for(int j=0;j<i;j++){
                if(nums[i]>nums[j]){
                    dp[i]=Math.max(dp[i],dp[j]+1);
                }
            }
        }
        int maxi=1;
        for(int len : dp){
            maxi=Math.max(maxi,len);
        }
        return maxi;
    }

    // V2
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4509303/beats-100-binary-search-explained-with-video-c-java-python-js/
    public int lengthOfLIS_2(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int i = 0, j = size;
            while (i != j) {
                int m = (i + j) / 2;
                if (tails[m] < x)
                    i = m + 1;
                else
                    j = m;
            }
            tails[i] = x;
            if (i == size) ++size;
        }
        return size;
    }

    // V3
    // IDEA : DP
    // https://leetcode.com/problems/longest-increasing-subsequence/solutions/4510776/java-solution-explained-in-hindi/
    public int lengthOfLIS_3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        int maxLength = 0;
        for (int len : dp) {
            maxLength = Math.max(maxLength, len);
        }
        return maxLength;
    }

}
