package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-time-to-activate-string/description/

/**
 * 3639. Minimum Time to Activate String
 * Medium
 *
 * You are given a string s of length n and an integer array order, where order
 * is a permutation of the numbers in the range [0, n - 1].
 *
 * Starting from time t = 0, replace the character at index order[t] in s with
 * '*' at each time step.
 *
 * A substring is valid if it contains at least one '*'.
 *
 * A string is active if the total number of valid substrings is greater than
 * or equal to k.
 *
 * Return the minimum time t at which the string s becomes active. If it is
 * impossible, return -1.
 *
 * Example 1:
 *
 * Input: s = "abc", order = [1,0,2], k = 2
 * Output: 0
 * Explanation:
 * t | order[t] | Modified s | Valid Substrings | Count | Active
 * (Count >= k)
 * 0 | 1 | "a*c" | "*", "a*", "*c", "a*c" | 4 | Yes
 * The string s becomes active at t = 0. Thus, the answer is 0.
 *
 * Example 2:
 *
 * Input: s = "cat", order = [0,2,1], k = 6
 * Output: 2
 * Explanation:
 * t | order[t] | Modified s | Valid Substrings | Count | Active
 * (Count >= k)
 * 0 | 0 | "*at" | "*", "*a", "*at" | 3 | No
 * 1 | 2 | "*a*" | "*", "*a", "*a*", "a*", "*" | 5 | No
 * 2 | 1 | "***" | All substrings (contain '*') | 6 | Yes
 * The string s becomes active at t = 2. Thus, the answer is 2.
 *
 * Example 3:
 *
 * Input: s = "xy", order = [0,1], k = 4
 * Output: -1
 * Explanation:
 * Even after all replacements, it is impossible to obtain k = 4 valid
 * substrings. Thus, the answer is -1.
 *
 * Constraints:
 *
 * 1 <= n == s.length <= 10^5
 * order.length == n
 * 0 <= order[i] <= n - 1
 * s consists of lowercase English letters.
 * order is a permutation of integers from 0 to n - 1.
 * 1 <= k <= 10^9
 *
 */
public class MinimumTimeToActivateString {

    // V0
    // IDEA: BINARY SEARCH ON TIME + COMPLEMENT COUNT OF STAR-FREE SUBSTRINGS
    // stars are only ever added, so the number of substrings containing a '*' never decreases
    // -> binary search the earliest t. Count the complement: substrings with no star are the
    // substrings of the maximal star-free gaps, so
    //   valid(t) = n(n+1)/2 - sum over gaps of g(g+1)/2.
    // Marking order[0..t] in a boolean array and sweeping once makes each check O(n).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minTime(String s, int[] order, int k) {
        int n = s.length();
        long total = (long) n * (n + 1) / 2;
        // even an all-star string has fewer than k substrings
        if (total < k) {
            return -1;
        }

        int left = 0;
        int right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (countWithStar(n, order, mid) >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Number of substrings containing at least one star after time t (order[0..t] starred). */
    private long countWithStar(int n, int[] order, int t) {
        boolean[] starred = new boolean[n];
        for (int i = 0; i <= t; i++) {
            starred[order[i]] = true;
        }
        long count = (long) n * (n + 1) / 2;
        long gapLength = 0;
        for (int i = 0; i < n; i++) {
            if (starred[i]) {
                // a gap just ended: remove its star-free substrings
                count -= gapLength * (gapLength + 1) / 2;
                gapLength = 0;
            } else {
                gapLength++;
            }
        }
        count -= gapLength * (gapLength + 1) / 2;
        return count;
    }
}
