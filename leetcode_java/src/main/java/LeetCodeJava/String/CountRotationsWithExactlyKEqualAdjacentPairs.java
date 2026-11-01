package LeetCodeJava.String;

// https://leetcode.com/problems/count-rotations-with-exactly-k-equal-adjacent-pairs/description/

/**
 * 4043. Count Rotations With Exactly K Equal Adjacent Pairs
 * Easy
 *
 * You are given a string s of length n and an integer k.
 *
 * A cyclic rotation of s is formed by choosing a prefix of s within [0, n - 1],
 * moving that prefix to the end, and preserving the order of all characters.
 *
 * The score of a rotation is the number of indices i such that 0 <= i < n - 1
 * and the characters at positions i and i + 1 are equal.
 *
 * Return the number of cyclic rotations of s whose score equals k.
 *
 *
 * Example 1:
 *
 * Input: s = "aab", k = 1
 * Output: 2
 * Explanation:
 * The three rotations are "aab" (score 1), "aba" (score 0) and "baa" (score 1),
 * so two of them score 1.
 *
 * Example 2:
 *
 * Input: s = "ab", k = 0
 * Output: 2
 * Explanation:
 * Both "ab" and "ba" score 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 0 <= k < s.length
 * s consists of lowercase English letters
 *
 */
public class CountRotationsWithExactlyKEqualAdjacentPairs {

    // V0
    // IDEA: BRUTE FORCE -- SCORE EVERY ROTATION, COUNT THE ONES EQUAL TO k
    //
    //   rotation t is s[t..n-1] + s[0..t-1]; its i-th character is s[(t + i) % n],
    //   so it can be scored without building the string.
    //
    //   NOTE !!! there are n ROTATIONS, not n distinct strings -- "aa" rotates to
    //            "aa" twice and BOTH count, so never de-duplicate the strings.
    //
    //   e.g. s = "aab" -> "aab"(1), "aba"(0), "baa"(1) -> k = 1 -> 2
    //
    //   O(n^2): the obvious reading of the statement, too slow for n = 10^5.
    //   use V1 in an interview.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int countRotations(String s, int k) {
        // edge case: empty string has no rotation
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int n = s.length();
        int matchingRotations = 0;

        for (int t = 0; t < n; t++) {
            // score of rotation t: equal pairs among its n - 1 linear neighbours
            int score = 0;
            for (int i = 0; i < n - 1; i++) {
                char current = s.charAt((t + i) % n);
                char next = s.charAt((t + i + 1) % n);
                if (current == next) {
                    score++;
                }
            }

            if (score == k) {
                matchingRotations++;
            }
        }

        return matchingRotations;
    }

    // V1
    // IDEA: COUNT THE CYCLIC PAIRS ONCE -- A ROTATION ONLY DROPS ONE OF THEM
    //
    //   lay s out on a circle: there are exactly n cyclic pairs
    //   (s[j], s[(j + 1) % n]), and let C be how many of them match.
    //
    //   rotating by t gives n - 1 LINEAR pairs = every cyclic pair EXCEPT
    //   j = t - 1, the one cut open to make the two ends. so
    //
    //      score(t) = C - (1 if cyclic pair t - 1 matches else 0)
    //
    //   -> a rotation scores only C or C - 1, so just count the cuts:
    //         k == C      -> cut a NON-matching pair -> n - C rotations
    //         k == C - 1  -> cut a MATCHING pair     -> C rotations
    //         otherwise   -> 0
    //
    //   e.g. s = "aab": (a,a) Y, (a,b) N, (b,a) N -> C = 1; k = 1 -> 3 - 1 = 2
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countRotations_1(String s, int k) {
        // edge case: empty string has no rotation
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int n = s.length();

        // NOTE !!! the wrap-around pair (s[n-1], s[0]) is included: the circle has
        //          n pairs, while any one rotation only ever sees n - 1 of them
        int cyclicMatches = 0;
        for (int j = 0; j < n; j++) {
            if (s.charAt(j) == s.charAt((j + 1) % n)) {
                cyclicMatches++;
            }
        }

        if (k == cyclicMatches) {
            // the cut must land on a non-matching pair
            return n - cyclicMatches;
        }
        if (k == cyclicMatches - 1) {
            // the cut must land on a matching pair
            return cyclicMatches;
        }
        return 0;
    }
}
