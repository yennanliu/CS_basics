package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-rotations-to-dial-a-number-ii/description/

/**
 * 4071. Minimum Rotations to Dial a Number II
 * Medium
 *
 * You are given an integer n and a string s of length n consisting of digits.
 *
 * The dial contains the digits 0 through 9 in order and is circular, so 0 and
 * 9 are adjacent. The pointer initially points to 0.
 *
 * To dial each digit of s in order, rotate the pointer until it points to that
 * digit. Each rotation moves the pointer to an adjacent digit, and you may
 * rotate in either direction. Dialing a digit that the pointer already points
 * to requires no rotations.
 *
 * Before dialing, you may perform the following operation at most once:
 *
 * Choose an index k such that 0 <= k < n and reverse the suffix s[k..n - 1].
 *
 * Return the minimum total number of rotations needed to dial the string after
 * optimally choosing whether to perform the operation and which suffix to
 * reverse.
 *
 *
 * Example 1:
 *
 * Input: n = 4, s = "1502"
 * Output: 9
 * Explanation:
 * Reverse the suffix starting at k = 1 to obtain "1205", then dial it.
 * 0 -> 1 : 1,  1 -> 2 : 1,  2 -> 0 : 2,  0 -> 5 : 5
 * The total is 1 + 1 + 2 + 5 = 9, which is the minimum total number of
 * rotations.
 *
 * Example 2:
 *
 * Input: n = 4, s = "2916"
 * Output: 12
 * Explanation:
 * Choose not to reverse a suffix and dial "2916".
 * 0 -> 2 : 2,  2 -> 9 : 3,  9 -> 1 : 2,  1 -> 6 : 5
 * The total is 2 + 3 + 2 + 5 = 12, which is the minimum total number of
 * rotations.
 *
 * Example 3:
 *
 * Input: n = 4, s = "4219"
 * Output: 6
 * Explanation:
 * Reverse the suffix starting at k = 0, which reverses the entire string, to
 * obtain "9124", then dial it.
 * 0 -> 9 : 1,  9 -> 1 : 2,  1 -> 2 : 1,  2 -> 4 : 2
 * The total is 1 + 2 + 1 + 2 = 6, which is the minimum total number of
 * rotations.
 *
 *
 * Constraints:
 *
 * 1 <= n == s.length <= 10^5
 * s consists only of digits '0' to '9'
 *
 */
public class MinimumRotationsToDialANumberII {

    private static final int DIAL_SIZE = 10;

    // V0
    // IDEA: REVERSING A SUFFIX ONLY CHANGES ONE EDGE -- base total - old edge + new edge
    //
    //   dist(a, b) is symmetric, so walking the suffix backwards costs exactly what
    //   walking it forwards does: the cost INSIDE the suffix never changes. the only
    //   transition that changes is the one ENTERING the suffix:
    //
    //       before: prev -> s[k]          after: prev -> s[n - 1]
    //       (prev = s[k - 1], or the starting digit 0 when k == 0)
    //
    //   so  candidate(k) = baseTotal - dist(prev, s[k]) + dist(prev, s[n - 1])
    //
    //   brute force (build every reversed string and re-dial it) is O(n^2) and TLEs.
    //
    //   e.g. s = "1502": baseTotal = 1 + 4 + 5 + 2 = 12;
    //        k = 1 -> 12 - dist(1,5) + dist(1,2) = 12 - 4 + 1 = 9
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minRotations(int n, String s) {
        // edge case: nothing to dial
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int length = s.length();
        int lastDigit = s.charAt(length - 1) - '0';

        // option 1: do not reverse anything
        int baseTotal = totalCost(s);
        int best = baseTotal;

        // option 2: reverse the suffix s[k..n-1], for every k
        for (int k = 0; k < length; k++) {
            // NOTE !!! for k == 0 the digit before the suffix is the pointer's start, 0
            int prev;
            if (k == 0) {
                prev = 0;
            } else {
                prev = s.charAt(k - 1) - '0';
            }

            int oldCost = dist(prev, s.charAt(k) - '0');
            // after reversing, the suffix starts with the old last digit
            int newCost = dist(prev, lastDigit);

            int candidate = baseTotal - oldCost + newCost;
            best = Math.min(best, candidate);
        }

        return best;
    }

    // V0-1
    // IDEA: TRY EVERY k + PREFIX / SUFFIX SUMS (price each reversal in O(1))
    //
    //   split the cost of dialing s[0..k-1] + reversed(s[k..n-1]) into 3 parts:
    //       1) prefix[k] = cost to dial s[0..k-1] starting from 0
    //       2) the jump  = dist(s[k-1], s[n-1])   (or dist(0, s[n-1]) if k == 0)
    //       3) inner[k]  = sum of dist(s[i], s[i+1]) for i in [k, n-2]
    //                      (same in either direction, since dist is symmetric)
    //
    //   same answer as V0, written as explicit prefix / suffix arrays -- the shape
    //   to reach for when the inside of the suffix is NOT symmetric.
    //   k = n - 1 reverses one digit, which equals "no operation".
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minRotations_0_1(int n, String s) {
        // edge case: nothing to dial
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int length = s.length();
        int[] digits = new int[length];
        for (int i = 0; i < length; i++) {
            digits[i] = s.charAt(i) - '0';
        }

        // prefix[k] = cost to dial digits[0..k-1], pointer starting at 0
        int[] prefix = new int[length + 1];
        int pos = 0;
        for (int i = 0; i < length; i++) {
            prefix[i + 1] = prefix[i] + dist(pos, digits[i]);
            pos = digits[i];
        }

        // inner[k] = cost of walking digits[k..n-1] between adjacent digits
        int[] inner = new int[length];
        for (int k = length - 2; k >= 0; k--) {
            inner[k] = inner[k + 1] + dist(digits[k], digits[k + 1]);
        }

        int lastDigit = digits[length - 1];
        int best = prefix[length]; // no operation

        for (int k = 0; k < length; k++) {
            // where the pointer rests before the reversed suffix starts
            int posBefore;
            if (k == 0) {
                posBefore = 0;
            } else {
                posBefore = digits[k - 1];
            }

            // NOTE !!! the reversed suffix is dialed starting from s[n-1]
            int total = prefix[k] + dist(posBefore, lastDigit) + inner[k];
            best = Math.min(best, total);
        }

        return best;
    }

    /** Cost to dial s in order, pointer starting at 0 (LC 4070). */
    private int totalCost(String s) {
        int total = 0;
        int pos = 0;
        for (int i = 0; i < s.length(); i++) {
            int val = s.charAt(i) - '0';
            total += dist(pos, val);
            pos = val;
        }
        return total;
    }

    /** Fewest rotations between digits a and b on the circular dial. */
    private int dist(int a, int b) {
        int direct = Math.abs(a - b);
        return Math.min(direct, DIAL_SIZE - direct);
    }
}
