package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-deletions-to-make-string-balanced/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1653. Minimum Deletions to Make String Balanced
 * Medium
 *
 * You are given a string s consisting only of characters 'a' and 'b'.
 *
 * You can delete any number of characters in s to make s balanced. s is balanced
 * if there is no pair of indices (i,j) such that i < j and s[i] = 'b' and
 * s[j]= 'a'.
 *
 * Return the minimum number of deletions needed to make s balanced.
 *
 *
 * Example 1:
 *
 * Input: s = "aababbab"
 * Output: 2
 * Explanation: Delete the characters at positions 2 and 6 ("aababbab" -> "aaabbb"),
 * or at positions 3 and 6 ("aababbab" -> "aabbbb").
 *
 * Example 2:
 *
 * Input: s = "bbaaaaabb"
 * Output: 2
 * Explanation: The only solution is to delete the first two characters.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is 'a' or 'b'.
 *
 */
public class MinimumDeletionsToMakeStringBalanced {

    // V0
    // IDEA: RUNNING DP (BEST COST FOR THE PREFIX SEEN SO FAR)
    //       best = min deletions to balance the prefix, bCount = 'b's seen so far.
    //       on 'b': keeping it is free (it joins the trailing b-block).
    //       on 'a': either delete it (best + 1) or keep it and delete every earlier
    //               'b' (bCount) -> best = min(best + 1, bCount).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumDeletions(String s) {
        int bCount = 0;
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'b') {
                bCount++;
            } else {
                best = Math.min(best + 1, bCount);
            }
        }
        return best;
    }

    // V1
    // IDEA: STACK (EVERY "ba" PAIR FORCES ONE DELETION)
    //       scan left to right; an 'a' that meets a 'b' on top of the stack forms a
    //       bad pair, and deleting one char of it is unavoidable -> pop and count.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumDeletions_1(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        int deletions = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == 'a' && !stack.isEmpty() && stack.peek() == 'b') {
                // drop the "ba" pair: one deletion resolves it
                stack.pop();
                deletions++;
            } else {
                stack.push(ch);
            }
        }
        return deletions;
    }
}
