package LeetCodeJava.Greedy;

// https://leetcode.com/problems/change-minimum-characters-to-satisfy-one-of-three-conditions/description/

/**
 * 1737. Change Minimum Characters to Satisfy One of Three Conditions
 * Medium
 *
 * You are given two strings a and b that consist of lowercase letters. In one
 * operation, you can change any character in a or b to any lowercase letter.
 *
 * Your goal is to satisfy one of the following three conditions:
 *
 * - Every letter in a is strictly less than every letter in b in the alphabet.
 * - Every letter in b is strictly less than every letter in a in the alphabet.
 * - Both a and b consist of only one distinct letter.
 *
 * Return the minimum number of operations needed to achieve your goal.
 *
 *
 * Example 1:
 *
 * Input: a = "aba", b = "caa"
 * Output: 2
 *
 * Example 2:
 *
 * Input: a = "dabadd", b = "cda"
 * Output: 3
 * Explanation: The best way is to make condition 1 true by changing b to "eee".
 *
 *
 * Constraints:
 *
 * 1 <= a.length, b.length <= 10^5
 * a and b consist only of lowercase letters.
 *
 */
public class ChangeMinimumCharactersToSatisfyOneOfThreeConditions {

    private static final int ALPHABET = 26;

    // V0
    // IDEA: LETTER COUNTS + ENUMERATE THE 26 SPLIT POINTS
    //       only the histograms matter.
    //       cond 3: cost = (m + n) - countA[c] - countB[c] for each letter c.
    //       cond 1: pick a boundary i in 1..25 with a < i <= b:
    //               cost = #a chars >= i  +  #b chars < i  (running prefix sums).
    //       cond 2: same with a and b swapped.
    /**
     * time = O(M + N + 26)
     * space = O(26)
     */
    public int minCharacters(String a, String b) {
        int m = a.length();
        int n = b.length();

        int[] countA = new int[ALPHABET];
        int[] countB = new int[ALPHABET];
        for (int i = 0; i < m; i++) {
            countA[a.charAt(i) - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            countB[b.charAt(i) - 'a']++;
        }

        int best = m + n;

        // condition 3: rewrite both strings to a single letter c
        for (int c = 0; c < ALPHABET; c++) {
            best = Math.min(best, m + n - countA[c] - countB[c]);
        }

        // conditions 1 and 2: boundary letter i (i = 0 would leave no letter below it)
        int aBelow = 0; // chars of a with index < i
        int bBelow = 0; // chars of b with index < i
        for (int i = 1; i < ALPHABET; i++) {
            aBelow += countA[i - 1];
            bBelow += countB[i - 1];
            // a entirely < i, b entirely >= i
            best = Math.min(best, (m - aBelow) + bBelow);
            // b entirely < i, a entirely >= i
            best = Math.min(best, (n - bBelow) + aBelow);
        }
        return best;
    }
}
