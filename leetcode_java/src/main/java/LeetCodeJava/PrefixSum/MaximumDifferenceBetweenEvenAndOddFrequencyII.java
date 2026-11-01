package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/maximum-difference-between-even-and-odd-frequency-ii/description/

/**
 * 3445. Maximum Difference Between Even and Odd Frequency II
 * Hard
 *
 * You are given a string s and an integer k. Your task is to find the maximum difference between
 * the frequency of two characters, freq[a] - freq[b], in a substring subs of s, such that:
 *
 * subs has a size of at least k.
 * Character a has an odd frequency in subs.
 * Character b has a non-zero even frequency in subs.
 *
 * Return the maximum difference.
 *
 * Note that subs can contain more than 2 distinct characters.
 *
 *
 * Example 1:
 *
 * Input: s = "12233", k = 4
 * Output: -1
 * Explanation:
 * For the substring "12233", the frequency of '1' is 1 and the frequency of '3' is 2. The
 * difference is 1 - 2 = -1.
 *
 * Example 2:
 *
 * Input: s = "1122211", k = 3
 * Output: 1
 * Explanation:
 * For the substring "11222", the frequency of '2' is 3 and the frequency of '1' is 2. The
 * difference is 3 - 2 = 1.
 *
 * Example 3:
 *
 * Input: s = "110", k = 3
 * Output: -1
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 3 * 10^4
 * s consists only of digits '0' to '4'.
 * The input is generated that at least one substring has a character with an even frequency and
 * a character with an odd frequency.
 * 1 <= k <= s.length
 *
 */
public class MaximumDifferenceBetweenEvenAndOddFrequencyII {

    private static final int DIGITS = 5;
    private static final int INF = Integer.MAX_VALUE;

    // V0
    // IDEA: PREFIX COUNTS + 4 PARITY BUCKETS OF RUNNING MINIMA, PER ORDERED PAIR (a, b)
    //  with A, B the prefix counts, substring (l, r] scores (A[r] - B[r]) - (A[l] - B[l]), so
    //  for each r we want the smallest A[l] - B[l] over legal l:
    //    length >= k       ->  l <= r - k
    //    freq(a) odd       ->  parity(A[l]) != parity(A[r])
    //    freq(b) even > 0  ->  parity(B[l]) == parity(B[r]) and B[l] < B[r]
    //  B is non-decreasing, so { l : B[l] < B[r] } only grows with r: one forward pointer
    //  releases l into its parity bucket once both l <= r - k and B[l] < B[r] hold.
    /**
     * time = O(20 * N)
     * space = O(N)
     */
    public int maxDifference(String s, int k) {
        int n = s.length();

        // prefix[d][i] = occurrences of digit d in s[0..i-1]
        int[][] prefix = new int[DIGITS][n + 1];
        for (int i = 0; i < n; i++) {
            int digit = s.charAt(i) - '0';
            for (int d = 0; d < DIGITS; d++) {
                prefix[d][i + 1] = prefix[d][i];
            }
            prefix[digit][i + 1]++;
        }

        int best = Integer.MIN_VALUE;
        for (int a = 0; a < DIGITS; a++) {
            for (int b = 0; b < DIGITS; b++) {
                if (a == b) {
                    continue;
                }
                int[] countA = prefix[a];
                int[] countB = prefix[b];

                // minDiff[parityA][parityB] = smallest A[l] - B[l] among released l
                int[][] minDiff = {{INF, INF}, {INF, INF}};
                int released = 0;

                for (int right = k; right <= n; right++) {
                    // release every l that is far enough back and has B[l] < B[right]
                    while (released <= right - k && countB[released] < countB[right]) {
                        int diff = countA[released] - countB[released];
                        int parityA = countA[released] % 2;
                        int parityB = countB[released] % 2;
                        minDiff[parityA][parityB] = Math.min(minDiff[parityA][parityB], diff);
                        released++;
                    }
                    // need opposite parity for a (odd count) and same parity for b (even count)
                    int wantParityA = 1 - countA[right] % 2;
                    int wantParityB = countB[right] % 2;
                    int smallest = minDiff[wantParityA][wantParityB];
                    if (smallest != INF) {
                        int candidate = (countA[right] - countB[right]) - smallest;
                        best = Math.max(best, candidate);
                    }
                }
            }
        }
        return best;
    }
}
