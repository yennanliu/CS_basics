package LeetCodeJava.Greedy;

// https://leetcode.com/problems/make-string-anti-palindrome/description/

import java.util.*;

/**
 * 3088. Make String Anti-palindrome
 * Hard
 *
 * 🔒 (premium)
 *
 * We call a string s of even length n an anti-palindrome if for each index 0 <= i < n, s[i] != s[n - i - 1].
 *
 * Given a string s, your task is to make s an anti-palindrome by doing any number of operations (including zero).
 *
 * In one operation, you can select two characters from s and swap them.
 *
 * Return the resulting string. If multiple strings meet the conditions, return the lexicographically smallest one. If it can't be made into an anti-palindrome, return "-1".
 *
 *
 * Example 1:
 *
 * Input: s = "abca"
 * Output: "aabc"
 * Explanation: "aabc" is an anti-palindrome string since s[0] != s[3] and s[1] != s[2]. Also, it is a rearrangement of "abca". It can be proven that it's the lexicographically smallest.
 *
 * Example 2:
 *
 * Input: s = "abba"
 * Output: "aabb"
 * Explanation: "aabb" is an anti-palindrome string since s[0] != s[3] and s[1] != s[2]. Also, it is a rearrangement of "abba". It can be proven that it's the lexicographically smallest.
 *
 * Example 3:
 *
 * Input: s = "cccd"
 * Output: "-1"
 * Explanation: It can be proven that there is no way to make "cccd" an anti-palindrome string.
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 10^5
 * s.length % 2 == 0
 * s consists only of lowercase English letters.
 *
 */
public class MakeStringAntiPalindrome {

    // V0
    // IDEA: SORT, THEN REPAIR THE MIRROR CLASHES BY TOUCHING THE RIGHT HALF ONLY
    /**
     *  Sorting gives the lexicographically smallest arrangement, so the only
     *  work is fixing positions where t[i] == t[n - 1 - i]. If a letter appears
     *  more than n / 2 times it must collide with itself -> "-1". In a sorted
     *  string all clashes sit in one block of the middle letter c straddling
     *  the centre. For each clash in the right half, swap it with the nearest
     *  position after the c-block (the smallest larger letter left). Only the
     *  right half changes, so the more significant left half stays minimal.
     *
     *  time = O(N log N)
     *  space = O(N)
     */
    public String makeAntiPalindrome(String s) {
        int n = s.length();

        int[] count = new int[26];
        for (int i = 0; i < n; i++) {
            count[s.charAt(i) - 'a']++;
        }
        for (int c : count) {
            if (c > n / 2) {
                return "-1"; // some letter is forced to face itself
            }
        }

        char[] t = s.toCharArray();
        Arrays.sort(t);

        // c = the letter at the end of the left half; j skips past its block
        char middle = t[n / 2 - 1];
        int j = n / 2;
        while (j < n && t[j] == middle) {
            j++;
        }

        for (int p = n / 2; p < n; p++) {
            if (t[p] != t[n - 1 - p]) {
                continue; // no clash here
            }
            while (j < n && t[j] == middle) {
                j++;
            }
            if (j == n) {
                return "-1";
            }
            // swap the clash with the smallest larger letter available
            char temp = t[p];
            t[p] = t[j];
            t[j] = temp;
            j++;
        }
        return new String(t);
    }
}
