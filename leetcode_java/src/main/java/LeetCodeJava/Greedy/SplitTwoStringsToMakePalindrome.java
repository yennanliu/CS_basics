package LeetCodeJava.Greedy;

// https://leetcode.com/problems/split-two-strings-to-make-palindrome/description/

/**
 * 1616. Split Two Strings to Make Palindrome
 * Medium
 *
 * You are given two strings a and b of the same length. Choose an index and split
 * both strings at the same index, splitting a into two strings: aprefix and asuffix
 * where a = aprefix + asuffix, and splitting b into two strings: bprefix and
 * bsuffix where b = bprefix + bsuffix. Check if aprefix + bsuffix or
 * bprefix + asuffix forms a palindrome.
 *
 * When you split a string s into sprefix and ssuffix, either ssuffix or sprefix is
 * allowed to be empty. For example, if s = "abc", then "" + "abc", "a" + "bc",
 * "ab" + "c" , and "abc" + "" are valid splits.
 *
 * Return true if it is possible to form a palindrome string, otherwise return
 * false.
 *
 * Notice that x + y denotes the concatenation of strings x and y.
 *
 *
 * Example 1:
 *
 * Input: a = "x", b = "y"
 * Output: true
 *
 * Example 2:
 *
 * Input: a = "xbdef", b = "xecab"
 * Output: false
 *
 * Example 3:
 *
 * Input: a = "ulacfd", b = "jizalu"
 * Output: true
 * Explanation: Split them at index 3: "ula" + "alu" = "ulaalu", which is a palindrome.
 *
 *
 * Constraints:
 *
 * 1 <= a.length, b.length <= 10^5
 * a.length == b.length
 * a and b consist of lowercase English letters
 *
 */
public class SplitTwoStringsToMakePalindrome {

    // V0
    // IDEA: GREEDY TWO POINTERS
    //       for aprefix + bsuffix: walk i from the left of a and j from the right
    //       of b while a[i] == b[j]. Once they differ, the untouched middle [i..j]
    //       must come entirely from ONE string, so it must be a palindrome in a or
    //       in b. Repeat with the roles of a and b swapped.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean checkPalindromeFormation(String a, String b) {
        return canForm(a, b) || canForm(b, a);
    }

    /** True if some prefix of `first` + the matching suffix of `second` is a palindrome. */
    private boolean canForm(String first, String second) {
        int left = 0;
        int right = second.length() - 1;

        // match the outer characters as far as possible
        while (left < right && first.charAt(left) == second.charAt(right)) {
            left++;
            right--;
        }
        if (left >= right) {
            return true;
        }
        // the middle comes from one string only: split left of `left` or right of `right`
        return isPalindrome(first, left, right) || isPalindrome(second, left, right);
    }

    /** True if s[left..right] reads the same both ways. */
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
