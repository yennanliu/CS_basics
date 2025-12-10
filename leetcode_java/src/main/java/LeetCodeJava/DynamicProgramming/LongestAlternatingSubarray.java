package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-alternating-subarray/description/
/**
 * 2765. Longest Alternating Subarray
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums. A subarray s of length m is called alternating if:
 *
 * m is greater than 1.
 * s1 = s0 + 1.
 * The 0-indexed subarray s looks like [s0, s1, s0, s1,...,s(m-1) % 2]. In other words, s1 - s0 = 1, s2 - s1 = -1, s3 - s2 = 1, s4 - s3 = -1, and so on up to s[m - 1] - s[m - 2] = (-1)m.
 * Return the maximum length of all alternating subarrays present in nums or -1 if no such subarray exists.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,4,3,4]
 *
 * Output: 4
 *
 * Explanation:
 *
 * The alternating subarrays are [2, 3], [3,4], [3,4,3], and [3,4,3,4]. The longest of these is [3,4,3,4], which is of length 4.
 *
 * Example 2:
 *
 * Input: nums = [4,5,6]
 *
 * Output: 2
 *
 * Explanation:
 *
 * [4,5] and [5,6] are the only two alternating subarrays. They are both of length 2.
 *
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 100
 * 1 <= nums[i] <= 104
 *
 */
public class LongestAlternatingSubarray {

    // V0
//    public int alternatingSubarray(int[] nums) {
//
//    }

    // V1-1
    // IDEA: BRUTE FORCE
    public int alternatingSubarray_1_1(int[] A) {
        int n = A.length, res = 0, j = 0;
        for (int i = 0; i < n; ++i)
            for (j = i + 1; j < n && A[j] == A[i] + (j - i) % 2; ++j)
                res = Math.max(res, j - i + 1);
        return res > 1 ? res : -1;
    }


    // V1-2
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/3737191/javacpython-two-pointers-and-dp-solution-4y71/
    public int alternatingSubarray_1_2(int[] A) {
        int n = A.length, res = 0, j = 0;
        for (int i = 0; i < n; i = Math.max(i + 1, j - 1))
            for (j = i + 1; j < n && A[j] == A[i] + (j - i) % 2; ++j)
                res = Math.max(res, j - i + 1);
        return res > 1 ? res : -1;
    }


    // V1-3
    // IDEA: DP
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/3737191/javacpython-two-pointers-and-dp-solution-4y71/
    public int alternatingSubarray_1_3(int[] A) {
        int n = A.length, res = -1, dp = -1;
        for (int i = 1; i < n; ++i, res = Math.max(res, dp))
            if (dp > 0 && A[i] == A[i - 2])
                dp++;
            else
                dp = A[i] == A[i - 1] + 1 ? 2 : -1;
        return res;
    }

    // V2
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/7243610/easy-two-pointer-approach-by-lokeshthaku-au5a/
    // IDEA: 2 POINTERS
    public int alternatingSubarray_2(int[] nums) {
        int ans = -1, i = 0, j = 1, check = 1;
        while (j < nums.length) {
            if (nums[j] - nums[j - 1] == check) {
                j++;
                check *= -1;
            } else {

                if (j - i + 1 > 2) {
                    ans = Math.max(j - i, ans);
                    i = j - 1;
                } else {
                    i = j;
                    j++;
                }
                check = 1;
            }
        }
        if (j - i + 1 > 2) {
            ans = Math.max(j - i, ans);
        }
        return ans;
    }


    // V3
    // IDEA: direction and counter
    // https://leetcode.com/problems/longest-alternating-subarray/solutions/3737352/java-on-solution-without-2-pointers-with-fr4h/
    public int alternatingSubarray_3(int[] nums) {
        int ans = -1;
        int n = nums.length;
        int curr = 1;
        int dir = 1;
        for (int i = 1; i < n; i++) {
            if (nums[i] - nums[i - 1] == dir) {
                curr++;
                dir = -dir;
                ans = Math.max(ans, curr);
            } else {
                if (nums[i] - nums[i - 1] == 1) {
                    curr = 2;
                    dir = -1;
                } else {
                    curr = 1;
                    dir = 1;
                }
            }
        }
        return ans;
    }



}
