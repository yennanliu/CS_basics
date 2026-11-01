package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-number-of-bad-pairs/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2364. Count Number of Bad Pairs
 * Medium
 *
 * You are given a 0-indexed integer array nums. A pair of indices (i, j) is a bad pair if i < j and j - i != nums[j] - nums[i].
 *
 * Return the total number of bad pairs in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,1,3,3]
 * Output: 5
 * Explanation: The pair (0, 1) is a bad pair since 1 - 0 != 1 - 4.
 * The pair (0, 2) is a bad pair since 2 - 0 != 3 - 4, 2 != -1.
 * The pair (0, 3) is a bad pair since 3 - 0 != 3 - 4, 3 != -1.
 * The pair (1, 2) is a bad pair since 2 - 1 != 3 - 1, 1 != 2.
 * The pair (2, 3) is a bad pair since 3 - 2 != 3 - 3, 1 != 0.
 * There are a total of 5 bad pairs, so we return 5.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 0
 * Explanation: There are no bad pairs.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class CountNumberOfBadPairs {

    // V0
    // IDEA: COUNT THE COMPLEMENT - GOOD PAIRS SHARE THE KEY i - nums[i]
    //   j - i == nums[j] - nums[i]  <=>  i - nums[i] == j - nums[j].
    //   count good pairs by bucketing on that key, then bad = n(n-1)/2 - good.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long countBadPairs(int[] nums) {
        int n = nums.length;
        Map<Integer, Integer> keyCount = new HashMap<>();
        long goodPairs = 0;

        for (int i = 0; i < n; i++) {
            int key = i - nums[i];
            // every earlier index with the same key forms a good pair with i
            int earlier = keyCount.getOrDefault(key, 0);
            goodPairs += earlier;
            keyCount.put(key, earlier + 1);
        }

        long totalPairs = (long) n * (n - 1) / 2;
        return totalPairs - goodPairs;
    }
}
