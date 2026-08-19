package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-smallest-letter-greater-than-target/

/**
 *  744. Find Smallest Letter Greater Than Target
 *  Easy
 *
 *  You are given an array of characters letters that is sorted in
 *  non-decreasing order, and a character target. There are at least two
 *  different characters in letters.
 *
 *  Return the smallest character in letters that is lexicographically greater
 *  than target. If such a character does not exist, return the first character
 *  in letters (the search wraps around).
 *
 *  Example 1:
 *
 *  Input: letters = ["c","f","j"], target = "a"
 *  Output: "c"
 *
 *  Example 2:
 *
 *  Input: letters = ["c","f","j"], target = "c"
 *  Output: "f"
 *
 *  Example 3:
 *
 *  Input: letters = ["x","x","y","y"], target = "z"
 *  Output: "x"
 *
 *  Constraints:
 *
 *  2 <= letters.length <= 10^4
 *  letters[i] is a lowercase English letter.
 *  letters is sorted in non-decreasing order.
 *  letters contains at least two different characters.
 *  target is a lowercase English letter.
 */
public class FindSmallestLetterGreaterThanTarget {

    // V0
    // IDEA: binary search for the first idx whose letter > target, wrap around
    /**
     * time = O(log n)
     * space = O(1)
     */
    public char nextGreatestLetter(char[] letters, char target) {
        int n = letters.length;
        int l = 0;
        int r = n;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (letters[mid] > target) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        // NOTE !!! if no letter is bigger than target, wrap to the first one
        return letters[l % n];
    }
}
