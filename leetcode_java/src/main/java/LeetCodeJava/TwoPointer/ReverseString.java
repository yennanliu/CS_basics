package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/reverse-string/

/**
 *  344. Reverse String
 *  Easy
 *
 *  Write a function that reverses a string. The input string is given as an
 *  array of characters s.
 *
 *  You must do this by modifying the input array in-place with O(1) extra memory.
 *
 *  Example 1:
 *    Input: s = ["h","e","l","l","o"]
 *    Output: ["o","l","l","e","h"]
 *
 *  Example 2:
 *    Input: s = ["H","a","n","n","a","h"]
 *    Output: ["h","a","n","n","a","H"]
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s[i] is a printable ascii character.
 */
public class ReverseString {

    // V0
    // IDEA: 2 POINTERS - swap from both ends toward the middle
    /**
     * time = O(N)
     * space = O(1)
     */
    public void reverseString(char[] s) {
        if (s == null || s.length <= 1) {
            return;
        }
        int l = 0;
        int r = s.length - 1;
        while (l < r) {
            char tmp = s[l];
            s[l] = s[r];
            s[r] = tmp;
            l++;
            r--;
        }
    }
}
