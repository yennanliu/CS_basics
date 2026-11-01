package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/count-substrings-that-satisfy-k-constraint-ii/description/

/**
 * 3261. Count Substrings That Satisfy K-Constraint II
 * Hard
 *
 * You are given a binary string s and an integer k.
 *
 * You are also given a 2D integer array queries, where queries[i] = [l_i, r_i].
 *
 * A binary string satisfies the k-constraint if either of the following conditions holds:
 *
 * The number of 0's in the string is at most k.
 * The number of 1's in the string is at most k.
 *
 * Return an integer array answer, where answer[i] is the number of substrings of s[l_i..r_i] that satisfy the k-constraint.
 *
 *
 * Example 1:
 *
 * Input: s = "0001111", k = 2, queries = [[0,6]]
 * Output: [26]
 * Explanation:
 * For the query [0, 6], all substrings of s[0..6] = "0001111" satisfy the k-constraint except for the substrings s[0..5] = "000111" and s[0..6] = "0001111".
 *
 * Example 2:
 *
 * Input: s = "010101", k = 1, queries = [[0,5],[1,4],[2,3]]
 * Output: [15,9,3]
 * Explanation:
 * The substrings of s with a length greater than 3 do not satisfy the k-constraint.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 1 <= k <= s.length
 * 1 <= queries.length <= 10^5
 * queries[i] == [l_i, r_i]
 * 0 <= l_i <= r_i < s.length
 * All queries are distinct.
 *
 */
public class CountSubstringsThatSatisfyKConstraint2 {

    // V0
    // IDEA: TWO POINTERS FOR THE LEFTMOST VALID START PER END + PREFIX SUM + BINARY SEARCH
    // leftmost[j] = smallest start with s[start..j] satisfying the constraint; a sliding window
    // gives it, and it is non-decreasing in j. A query [l, r] wants, over j in [l, r],
    //   j - max(l, leftmost[j]) + 1.
    // Since leftmost is monotone, binary search the first j >= l with leftmost[j] >= l (split):
    //   j in [l, split)  -> capped by l : lengths 1, 2, ..., split - l (arithmetic series)
    //   j in [split, r]  -> j - leftmost[j] + 1, read from a prefix-sum array
    /**
     * time = O(N + Q log N)
     * space = O(N)
     */
    public long[] countKConstraintSubstrings(String s, int k, int[][] queries) {
        int n = s.length();

        int[] leftmost = new int[n];
        int zeros = 0;
        int ones = 0;
        int windowStart = 0;
        for (int j = 0; j < n; j++) {
            if (s.charAt(j) == '0') {
                zeros++;
            } else {
                ones++;
            }
            // invalid only when BOTH counts exceed k
            while (zeros > k && ones > k) {
                if (s.charAt(windowStart) == '0') {
                    zeros--;
                } else {
                    ones--;
                }
                windowStart++;
            }
            leftmost[j] = windowStart;
        }

        // prefix[j + 1] = sum over i <= j of (i - leftmost[i] + 1)
        long[] prefix = new long[n + 1];
        for (int j = 0; j < n; j++) {
            prefix[j + 1] = prefix[j] + (j - leftmost[j] + 1);
        }

        long[] answer = new long[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int l = queries[q][0];
            int r = queries[q][1];

            // first j in [l, r + 1) with leftmost[j] >= l  (r + 1 if none)
            int lo = l;
            int hi = r + 1;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (leftmost[mid] >= l) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            int split = lo;

            long total = 0;
            long capped = split - l;
            total += capped * (capped + 1) / 2;
            if (split <= r) {
                total += prefix[r + 1] - prefix[split];
            }
            answer[q] = total;
        }
        return answer;
    }
}
