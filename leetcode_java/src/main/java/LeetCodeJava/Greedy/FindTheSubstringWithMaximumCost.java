package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-substring-with-maximum-cost/description/

/**
 * 2606. Find the Substring With Maximum Cost
 * Medium
 *
 * You are given a string s, a string chars of distinct characters and an integer array vals of the same length as chars.
 *
 * The cost of the substring is the sum of the values of each character in the substring. The cost of an empty string is considered 0.
 *
 * The value of the character is defined in the following way:
 *
 * - If the character is not in the string chars, then its value is its corresponding position (1-indexed) in the alphabet.
 *   - For example, the value of 'a' is 1, the value of 'b' is 2, and so on. The value of 'z' is 26.
 * - Otherwise, assuming i is the index where the character occurs in the string chars, then its value is vals[i].
 *
 * Return the maximum cost among all substrings of the string s.
 *
 *
 * Example 1:
 *
 * Input: s = "adaa", chars = "d", vals = [-1000]
 * Output: 2
 * Explanation: The value of the characters "a" and "d" is 1 and -1000 respectively.
 * The substring with the maximum cost is "aa" and its cost is 1 + 1 = 2.
 * It can be proven that 2 is the maximum cost.
 *
 * Example 2:
 *
 * Input: s = "abc", chars = "abc", vals = [-1,-1,-1]
 * Output: 0
 * Explanation: The value of the characters "a", "b" and "c" is -1, -1, and -1 respectively.
 * The substring with the maximum cost is the empty substring "" and its cost is 0.
 * It can be proven that 0 is the maximum cost.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consist of lowercase English letters.
 * 1 <= chars.length <= 26
 * chars consist of distinct lowercase English letters.
 * vals.length == chars.length
 * -1000 <= vals[i] <= 1000
 *
 */
public class FindTheSubstringWithMaximumCost {

    // V0
    // IDEA: KADANE (MAXIMUM SUBARRAY SUM) ON THE MAPPED VALUES
    //       map each char to its value (alphabet position unless overridden by chars / vals),
    //       then run Kadane. the empty substring scores 0, so the answer is never negative.
    /**
     * time = O(N)
     * space = O(1)   (a 26-slot table)
     */
    public int maximumCostSubstring(String s, String chars, int[] vals) {
        // default value of each letter is its 1-indexed alphabet position
        int[] value = new int[26];
        for (int i = 0; i < 26; i++) {
            value[i] = i + 1;
        }
        for (int i = 0; i < chars.length(); i++) {
            value[chars.charAt(i) - 'a'] = vals[i];
        }

        int best = 0;       // the empty substring
        int endingHere = 0; // best cost of a substring ending at the current char
        for (int i = 0; i < s.length(); i++) {
            int v = value[s.charAt(i) - 'a'];
            // extend the previous run only if it is worth something, otherwise restart here
            if (endingHere > 0) {
                endingHere = endingHere + v;
            } else {
                endingHere = v;
            }
            best = Math.max(best, endingHere);
        }
        return best;
    }
}
