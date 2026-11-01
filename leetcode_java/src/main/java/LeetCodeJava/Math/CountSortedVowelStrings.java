package LeetCodeJava.Math;

// https://leetcode.com/problems/count-sorted-vowel-strings/description/

/**
 * 1641. Count Sorted Vowel Strings
 * Medium
 *
 * Given an integer n, return the number of strings of length n that consist only of vowels (a, e, i, o, u) and are lexicographically sorted.
 *
 * A string s is lexicographically sorted if for all valid i, s[i] is the same as or comes before s[i+1] in the alphabet.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 5
 * Explanation: The 5 sorted strings that consist of vowels only are ["a","e","i","o","u"].
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 15
 * Explanation: The 15 sorted strings that consist of vowels only are
 * ["aa","ae","ai","ao","au","ee","ei","eo","eu","ii","io","iu","oo","ou","uu"].
 * Note that "ea" is not a valid string since 'e' comes after 'a' in the alphabet.
 *
 * Example 3:
 *
 * Input: n = 33
 * Output: 66045
 *
 *
 * Constraints:
 *
 * 1 <= n <= 50
 *
 */
public class CountSortedVowelStrings {

    private static final int VOWELS = 5;

    // V0
    // IDEA: STARS AND BARS (a sorted string is just a multiset of 5 vowels)
    //   c1 + c2 + c3 + c4 + c5 = n with ci >= 0 has C(n + 4, 4) solutions.
    //   check: n = 1 -> C(5, 4) = 5, n = 2 -> C(6, 4) = 15.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int countVowelStrings(int n) {
        return (n + 1) * (n + 2) * (n + 3) * (n + 4) / 24;
    }

    // V1
    // IDEA: DP (startsWith[v] = #sorted strings of the current length whose first vowel is v)
    //   a string starting with vowel v continues with any string starting at vowel >= v,
    //   so each new length is a suffix sum of the previous one. base: length 1 -> 1 per vowel.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countVowelStrings_1(int n) {
        int[] startsWith = new int[VOWELS];
        for (int v = 0; v < VOWELS; v++) {
            startsWith[v] = 1;
        }

        for (int length = 2; length <= n; length++) {
            // suffix sum: starting with v, the rest may start with any vowel >= v
            for (int v = VOWELS - 2; v >= 0; v--) {
                startsWith[v] += startsWith[v + 1];
            }
        }

        int total = 0;
        for (int v = 0; v < VOWELS; v++) {
            total += startsWith[v];
        }
        return total;
    }
}
