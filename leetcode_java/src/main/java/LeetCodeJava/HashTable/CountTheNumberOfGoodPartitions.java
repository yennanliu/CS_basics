package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-the-number-of-good-partitions/description/

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 2963. Count the Number of Good Partitions
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed array nums consisting of positive integers.
 *
 * A partition of an array into one or more contiguous subarrays is called good if no two subarrays contain the same number.
 *
 * Return the total number of good partitions of nums.
 *
 * Since the answer may be large, return it modulo 109 + 7.
 *
 *   Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 8
 * Explanation: The 8 possible good partitions are: ([1], [2], [3], [4]), ([1], [2], [3,4]), ([1], [2,3], [4]), ([1], [2,3,4]), ([1,2], [3], [4]), ([1,2], [3,4]), ([1,2,3], [4]), and ([1,2,3,4]).
 * Example 2:
 *
 * Input: nums = [1,1,1,1]
 * Output: 1
 * Explanation: The only possible good partition is: ([1,1,1,1]).
 * Example 3:
 *
 * Input: nums = [1,2,1,3]
 * Output: 2
 * Explanation: The 2 possible good partitions are: ([1,2,1], [3]) and ([1,2,1,3]).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 109
 *
 *
 */
public class CountTheNumberOfGoodPartitions {

    // V0
//    public int numberOfGoodPartitions(int[] nums) {
//
//    }

    // V1
    // https://leetcode.com/problems/count-the-number-of-good-partitions/solutions/4384415/javacpython-two-passes-by-lee215-8cju/
    /** IDEA:
     *
     * 1. All same integers should be in the same subarray
     *
     * 2. We can concat two subarray in good partition,
     * and it's still good.
     *
     * 3. If we can split into atmost k subarray,
     * then we have k - 1 spaces we can concat two subarrays.
     *
     */
    public int numberOfGoodPartitions_1(int[] A) {
        int res = 1, n = A.length, mod = 1000000007;
        Map<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < n; ++i) {
            last.put(A[i], i);
        }
        for (int i = 0, j = 0; i < n; ++i) {
            res = i > j ? (res * 2) % mod : res;
            j = Math.max(j, last.get(A[i]));
        }
        return res;
    }

    // V2
    public long power(long b, long p, long m) {
        if (p == 0)
            return 1;
        long t = power(b, p / 2, m);
        return (p % 2 == 1) ? ((((t * t) % m) * b) % m) : (t * t) % m;
    }

    public int numberOfGoodPartitions_2(int[] nums) {
        long count = 0, m = 1000_000_007;
        HashMap<Integer, Integer> last = new HashMap<>();
        for (int i = 0; i < nums.length; ++i)
            last.put(nums[i], i);
        for (int i = 0, next = -1; i < nums.length; ++i) {
            if (i > next)
                ++count;
            next = Math.max(next, last.get(nums[i]));
        }
        return (int) power(2, count - 1, m);
    }


    // V3


}
