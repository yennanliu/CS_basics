package LeetCodeJava.Greedy;

// https://leetcode.com/problems/smallest-palindromic-rearrangement-ii/description/

/**
 * 3518. Smallest Palindromic Rearrangement II
 * Hard
 *
 * You are given a palindromic string s and an integer k.
 *
 * Return the k-th lexicographically smallest palindromic permutation of s. If
 * there are fewer than k distinct palindromic permutations, return an empty
 * string.
 *
 * Note: Different rearrangements that yield the same palindromic string are
 * considered identical and are counted once.
 *
 * Example 1:
 *
 * Input: s = "abba", k = 2
 *
 * Output: "baab"
 *
 * Explanation:
 *
 * The two distinct palindromic rearrangements of "abba" are "abba" and "baab".
 *
 * Lexicographically, "abba" comes before "baab". Since k = 2, the output is
 * "baab".
 *
 * Example 2:
 *
 * Input: s = "aa", k = 2
 *
 * Output: ""
 *
 * Explanation:
 *
 * There is only one palindromic rearrangement: "aa".
 *
 * The output is an empty string since k = 2 exceeds the number of possible
 * rearrangements.
 *
 * Example 3:
 *
 * Input: s = "bacab", k = 1
 *
 * Output: "abcba"
 *
 * Explanation:
 *
 * The two distinct palindromic rearrangements of "bacab" are "abcba" and "bacab".
 *
 * Lexicographically, "abcba" comes before "bacab". Since k = 1, the output is
 * "abcba".
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 *
 * s consists of lowercase English letters.
 *
 * s is guaranteed to be palindromic.
 *
 * 1 <= k <= 10^6
 *
 */
public class SmallestPalindromicRearrangementII {

    // V0
    // IDEA: K-TH MULTISET PERMUTATION OF THE HALF, ON A SHORT FREE SUFFIX ONLY
    //       a palindrome is fixed by its first half (+ middle char), and palindromes
    //       compare like their halves, so we want the k-th smallest permutation of the
    //       half's multiset. k <= 10^6, so only a short suffix of the half can vary:
    //       grow a "free" suffix from the largest letters ('z' downward), updating
    //       its permutation count incrementally (count = count * total / copies),
    //       until count >= k. everything else stays frozen as the sorted prefix.
    //       then build the k-th permutation of the free part letter by letter:
    //       arrangements starting with letter c number count * free[c] / total.
    /**
     * time = O(26 * N)
     * space = O(N)
     */
    public String smallestPalindrome(String s, int k) {
        int n = s.length();
        int half = n / 2;

        // letters of the first half (a palindrome's halves are mirror images)
        int[] halfCount = new int[26];
        for (int i = 0; i < half; i++) {
            halfCount[s.charAt(i) - 'a']++;
        }

        // free[c] = copies of letter c in the movable suffix; count = its permutations
        int[] free = new int[26];
        long freeTotal = 0;
        long count = 1;
        long remainingK = k;
        int letter = 25;
        while (count < remainingK && letter >= 0) {
            while (free[letter] < halfCount[letter] && count < remainingK) {
                free[letter]++;
                freeTotal++;
                // multinomial update: one more slot, one more copy of `letter`
                count = count * freeTotal / free[letter];
            }
            if (count < remainingK) {
                letter--;
            }
        }
        if (count < remainingK) {
            return ""; // fewer than k distinct palindromes
        }

        // frozen prefix: smallest possible arrangement, i.e. sorted
        StringBuilder firstHalf = new StringBuilder();
        for (int c = 0; c < 26; c++) {
            for (int copy = 0; copy < halfCount[c] - free[c]; copy++) {
                firstHalf.append((char) ('a' + c));
            }
        }

        // k-th permutation of the free suffix
        while (freeTotal > 0) {
            for (int c = 0; c < 26; c++) {
                if (free[c] == 0) {
                    continue;
                }
                // permutations that start with letter c
                long block = count * free[c] / freeTotal;
                if (block < remainingK) {
                    remainingK -= block; // skip this whole block
                    continue;
                }
                count = block;
                free[c]--;
                freeTotal--;
                firstHalf.append((char) ('a' + c));
                break;
            }
        }

        String left = firstHalf.toString();
        String middle = (n % 2 == 1) ? String.valueOf(s.charAt(half)) : "";
        String right = new StringBuilder(left).reverse().toString();
        return left + middle + right;
    }
}
