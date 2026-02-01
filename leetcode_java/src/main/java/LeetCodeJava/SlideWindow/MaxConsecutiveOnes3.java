package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/max-consecutive-ones-iii/description/
// https://leetcode.ca/2018-08-30-1004-Max-Consecutive-Ones-III/
/**
 * 1004. Max Consecutive Ones III
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a binary array nums and an integer k, return the maximum number of consecutive 1's in the array if you can flip at most k 0's.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
 * Output: 6
 * Explanation: [1,1,1,0,0,1,1,1,1,1,1]
 * Bolded numbers were flipped from 0 to 1. The longest subarray is underlined.
 * Example 2:
 *
 * Input: nums = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], k = 3
 * Output: 10
 * Explanation: [0,0,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1]
 * Bolded numbers were flipped from 0 to 1. The longest subarray is underlined.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * nums[i] is either 0 or 1.
 * 0 <= k <= nums.length
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 1,144,839/1.7M
 * Acceptance Rate
 * 66.6%
 *
 *
 */
public class MaxConsecutiveOnes3 {

    // V0
    // IDEA: SLIDE WINDOW
    /**
     *  for(int r = 0; r < nums.length; r++){
     *      while (isValid()){
     *      //  do sth
     *          l += 1;
     *      }
     *      //  do sth
     *  }
     */
    /**
     * time = O(N)
     * space = O(K)
     */
    public int longestOnes(int[] nums, int k) {
        // edge
        if (nums.length == 0) {
            return 0;
        }
        // ???
        if (nums.length <= k) {
            return nums.length;
        }
        int ans = 0;
        int l = 0;

        int z_cnt = 0;

        for (int r = 0; r < nums.length; r++) {
            int rightVal = nums[r];
            // NOTE !!!  update z cnt first
            if (rightVal == 0) {
                z_cnt += 1;
            }

            while (z_cnt > k) {
                int leftVal = nums[l];
                if (leftVal == 0) {
                    z_cnt -= 1;
                }
                l += 1;
            }

            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }

    // V0-1
    // IDEA: SLIDE WINDOW (gpt)
    /**
     * time = O(N)
     * space = O(K)
     */
    public int longestOnes_0_1(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int ans = 0;
        int l = 0;
        int z_cnt = 0;

        for (int r = 0; r < nums.length; r++) {
            if (nums[r] == 0) {
                z_cnt++;
            }

            // shrink window until valid
            while (z_cnt > k) {
                if (nums[l] == 0) {
                    z_cnt--;
                }
                l++;
            }

            ans = Math.max(ans, r - l + 1);
        }

        return ans;
    }

    // V1
    // https://leetcode.ca/2018-08-30-1004-Max-Consecutive-Ones-III/
    /**
     * time = O(N)
     * space = O(K)
     */
    public int longestOnes_1(int[] nums, int k) {
        int l = 0, r = 0;
        while (r < nums.length) {
            if (nums[r++] == 0) {
                --k;
            }
            if (k < 0 && nums[l++] == 0) {
                ++k;
            }
        }
        return r - l;
    }

    // V2
    // https://leetcode.com/problems/max-consecutive-ones-iii/solutions/6253008/easy-sliding-window-method-beatsbest-app-fa89/
    /**
     * time = O(N)
     * space = O(K)
     */
    public int longestOnes_2(int[] nums, int k) {
        int left = 0;
        int window = 0;
        int zero = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] != 1) {
                zero++;
            }
            if (zero > k) {
                if (nums[left] == 0) {
                    zero--;
                }
                left++;
            }
            window = Math.max(right - left + 1, window);
        }

        return window;
    }

    // V3
    // https://leetcode.com/problems/max-consecutive-ones-iii/solutions/3540704/solution-by-deleted_user-aqta/
    /**
     * time = O(N)
     * space = O(K)
     */
    public int longestOnes_3(int[] nums, int k) {
        int start = 0;
        int end = 0;
        int zeros = 0;

        while (end < nums.length) {
            if (nums[end] == 0) {
                zeros++;
            }
            end++;
            if (zeros > k) {
                if (nums[start] == 0) {
                    zeros--;
                }
                start++;
            }
        }
        return end - start;
    }

    // V4
    // https://leetcode.com/problems/max-consecutive-ones-iii/solutions/6826967/video-sliding-window-technique-with-two-y0j1s/
    /**
     * time = O(N)
     * space = O(K)
     */
    public int longestOnes_4(int[] nums, int k) {
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                k--;
            }

            if (k < 0) {
                if (nums[left] == 0) {
                    k++;
                }
                left++;
            }
        }

        return nums.length - left;
    }



}
