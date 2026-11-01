package LeetCodeJava.String;

// https://leetcode.com/problems/find-first-palindromic-string-in-the-array/description/

/**
 * 2108. Find First Palindromic String in the Array
 * Easy
 *
 * Given an array of strings words, return the first palindromic string in the array. If there is no such string, return an empty string "".
 *
 * A string is palindromic if it reads the same forward and backward.
 *
 *
 * Example 1:
 *
 * Input: words = ["abc","car","ada","racecar","cool"]
 * Output: "ada"
 * Explanation: The first string that is palindromic is "ada".
 * Note that "racecar" is also palindromic, but it is not the first.
 *
 * Example 2:
 *
 * Input: words = ["notapalindrome","racecar"]
 * Output: "racecar"
 * Explanation: The first and only string that is palindromic is "racecar".
 *
 * Example 3:
 *
 * Input: words = ["def","ghi"]
 * Output: ""
 * Explanation: There are no palindromes, so the empty string is returned.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 100
 * words[i] consists only of lowercase English letters.
 *
 */
public class FindFirstPalindromicStringInTheArray {

    // V0
    // IDEA: LINEAR SCAN, TWO-POINTER PALINDROME TEST, RETURN ON THE FIRST MATCH
    /**
     * time = O(N * L)
     * space = O(1)
     */
    public String firstPalindrome(String[] words) {
        for (String word : words) {
            if (isPalindrome(word)) {
                return word;
            }
        }
        // no palindrome in the array
        return "";
    }

    /** Compares characters from both ends moving inward. */
    private boolean isPalindrome(String word) {
        int left = 0;
        int right = word.length() - 1;
        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
