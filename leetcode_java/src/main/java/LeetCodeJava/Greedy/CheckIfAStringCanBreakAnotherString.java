package LeetCodeJava.Greedy;

// https://leetcode.com/problems/check-if-a-string-can-break-another-string/description/

import java.util.*;

/**
 * 1433. Check If a String Can Break Another String
 * Medium
 *
 * Given two strings: s1 and s2 with the same size, check if some permutation of
 * string s1 can break some permutation of string s2 or vice-versa. In other words
 * s2 can break s1 or vice-versa.
 *
 * A string x can break string y (both of size n) if x[i] >= y[i] (in alphabetical
 * order) for all i between 0 and n-1.
 *
 *
 * Example 1:
 *
 * Input: s1 = "abc", s2 = "xya"
 * Output: true
 * Explanation: "ayx" is a permutation of s2="xya" which can break to string "abc"
 * which is a permutation of s1="abc".
 *
 * Example 2:
 *
 * Input: s1 = "abe", s2 = "acd"
 * Output: false
 * Explanation: All permutations for s1="abe" are: "abe", "aeb", "bae", "bea",
 * "eab" and "eba" and all permutation for s2="acd" are: "acd", "adc", "cad",
 * "cda", "dac" and "dca". However, there is not any permutation from s1 which can
 * break some permutation from s2 and vice-versa.
 *
 * Example 3:
 *
 * Input: s1 = "leetcodee", s2 = "interview"
 * Output: true
 *
 *
 * Constraints:
 *
 * s1.length == n
 * s2.length == n
 * 1 <= n <= 10^5
 * All strings consist of lowercase English letters.
 *
 */
public class CheckIfAStringCanBreakAnotherString {

    // V0
    // IDEA: GREEDY + SORT (if any pairing works, pairing the i-th smallest with the i-th smallest works)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public boolean checkIfCanBreak(String s1, String s2) {
        char[] first = s1.toCharArray();
        char[] second = s2.toCharArray();
        Arrays.sort(first);
        Arrays.sort(second);

        // check both directions on the sorted strings
        boolean firstBreaksSecond = true;
        boolean secondBreaksFirst = true;
        for (int i = 0; i < first.length; i++) {
            if (first[i] < second[i]) {
                firstBreaksSecond = false;
            }
            if (second[i] < first[i]) {
                secondBreaksFirst = false;
            }
        }
        return firstBreaksSecond || secondBreaksFirst;
    }

    // V1
    // IDEA: COUNTING SORT (26 letters): compare running prefix counts instead of sorting
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean checkIfCanBreak_1(String s1, String s2) {
        int[] count1 = new int[26];
        int[] count2 = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            count1[s1.charAt(i) - 'a']++;
            count2[s2.charAt(i) - 'a']++;
        }

        // sorted(s1)[i] >= sorted(s2)[i] for every i
        // <=> for every letter c, (# letters of s1 <= c) <= (# letters of s2 <= c)
        boolean firstBreaksSecond = true;
        boolean secondBreaksFirst = true;
        int prefix1 = 0;
        int prefix2 = 0;
        for (int letter = 0; letter < 26; letter++) {
            prefix1 += count1[letter];
            prefix2 += count2[letter];
            if (prefix1 > prefix2) {
                firstBreaksSecond = false;
            }
            if (prefix2 > prefix1) {
                secondBreaksFirst = false;
            }
        }
        return firstBreaksSecond || secondBreaksFirst;
    }
}
