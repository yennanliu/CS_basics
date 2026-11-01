package LeetCodeJava.Greedy;

// https://leetcode.com/problems/construct-string-with-repeat-limit/description/

/**
 * 2182. Construct String With Repeat Limit
 * Medium
 *
 * You are given a string s and an integer repeatLimit. Construct a new string repeatLimitedString using the
 * characters of s such that no letter appears more than repeatLimit times in a row. You do not have to use
 * all characters from s.
 *
 * Return the lexicographically largest repeatLimitedString possible.
 *
 * A string a is lexicographically larger than a string b if in the first position where a and b differ,
 * string a has a letter that appears later in the alphabet than the corresponding letter in b. If the first
 * min(a.length, b.length) characters do not differ, then the longer string is the lexicographically larger
 * one.
 *
 *
 * Example 1:
 *
 * Input: s = "cczazcc", repeatLimit = 3
 * Output: "zzcccac"
 *
 * Example 2:
 *
 * Input: s = "aababab", repeatLimit = 2
 * Output: "bbabaa"
 *
 *
 * Constraints:
 *
 * 1 <= repeatLimit <= s.length <= 10^5
 * s consists of lowercase English letters.
 *
 */
public class ConstructStringWithRepeatLimit {

    // V0
    // IDEA: GREEDY + COUNTING - EMIT THE LARGEST LETTER, BREAK EACH FULL RUN WITH ONE COPY OF THE NEXT LARGEST
    /**
     * time = O(N + 26)
     * space = O(N)
     */
    public String repeatLimitedString(String s, int repeatLimit) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        StringBuilder result = new StringBuilder();
        // `current` = the largest letter still available; `filler` = the next smaller letter with stock.
        // both only ever move downwards, so the scan over the alphabet is linear
        int current = 25;
        int filler = 24;

        while (true) {
            // find the largest letter that still has copies
            while (current >= 0 && count[current] == 0) {
                current--;
            }
            if (current < 0) {
                break;
            }

            // emit as many copies as the limit allows
            int take = Math.min(repeatLimit, count[current]);
            for (int t = 0; t < take; t++) {
                result.append((char) ('a' + current));
            }
            count[current] -= take;

            if (count[current] == 0) {
                // this letter is used up; the next round picks the next largest
                continue;
            }

            // copies remain but the run is full: break it with the largest smaller letter
            if (filler >= current) {
                filler = current - 1;
            }
            while (filler >= 0 && count[filler] == 0) {
                filler--;
            }
            if (filler < 0) {
                // nothing smaller to break the run with: the leftover copies cannot be used
                break;
            }
            result.append((char) ('a' + filler));
            count[filler]--;
        }
        return result.toString();
    }
}
