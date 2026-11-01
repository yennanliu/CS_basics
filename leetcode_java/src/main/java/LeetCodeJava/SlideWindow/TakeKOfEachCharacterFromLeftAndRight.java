package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/take-k-of-each-character-from-left-and-right/description/

/**
 * 2516. Take K of Each Character From Left and Right
 * Medium
 *
 * You are given a string s consisting of the characters 'a', 'b', and 'c' and a non-negative integer k. Each minute, you may take either the leftmost character of s, or the rightmost character of s.
 *
 * Return the minimum number of minutes needed for you to take at least k of each character, or return -1 if it is not possible to take k of each character.
 *
 *
 * Example 1:
 *
 * Input: s = "aabaaaacaabc", k = 2
 * Output: 8
 * Explanation:
 * Take three characters from the left of s. You now have two 'a' characters, and one 'b' character.
 * Take five characters from the right of s. You now have four 'a' characters, two 'b' characters, and two 'c' characters.
 * A total of 3 + 5 = 8 minutes is needed.
 * It can be proven that 8 is the minimum number of minutes needed.
 *
 * Example 2:
 *
 * Input: s = "a", k = 1
 * Output: -1
 * Explanation: It is not possible to take one 'b' or 'c' so return -1.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only the letters 'a', 'b', and 'c'.
 * 0 <= k <= s.length
 *
 */
public class TakeKOfEachCharacterFromLeftAndRight {

    // V0
    // IDEA: SLIDING WINDOW ON THE COMPLEMENT
    //  taking a prefix plus a suffix leaves one contiguous middle block behind, so minimising
    //  what we take == maximising that middle block. slide a window [left, right] as the KEPT
    //  middle; `taken` counts the characters outside it. growing the window removes s[right]
    //  from `taken`; while that count drops below k, shrink from the left.
    //  answer = n - (largest legal window).
    //  NOTE: only the count of s[right] can have dropped below k, so only it is re-checked.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int takeCharacters(String s, int k) {
        int n = s.length();
        int[] taken = new int[3];
        for (int i = 0; i < n; i++) {
            taken[s.charAt(i) - 'a']++;
        }
        // even taking the whole string is not enough
        for (int c = 0; c < 3; c++) {
            if (taken[c] < k) {
                return -1;
            }
        }

        int bestKept = 0;
        int left = 0;
        for (int right = 0; right < n; right++) {
            int c = s.charAt(right) - 'a';
            // s[right] moves into the kept middle block
            taken[c]--;
            // too few of c outside the window -> give characters back from the left
            while (taken[c] < k) {
                taken[s.charAt(left) - 'a']++;
                left++;
            }
            bestKept = Math.max(bestKept, right - left + 1);
        }
        return n - bestKept;
    }
}
