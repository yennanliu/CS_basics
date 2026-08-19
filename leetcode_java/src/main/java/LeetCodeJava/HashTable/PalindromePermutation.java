package LeetCodeJava.HashTable;

// https://leetcode.com/problems/palindrome-permutation/

import java.util.*;

/**
 *  266. Palindrome Permutation
 *  Easy
 *
 *  Given a string s, return true if a permutation of the string could form a
 *  palindrome and false otherwise.
 *
 *  Example 1:
 *  Input: s = "code"
 *  Output: false
 *
 *  Example 2:
 *  Input: s = "aab"
 *  Output: true
 *
 *  Example 3:
 *  Input: s = "carerac"
 *  Output: true
 *
 *  Constraints:
 *   - 1 <= s.length <= 5000
 *   - s consists of only lowercase English letters.
 */
public class PalindromePermutation {

    // V0
    // IDEA: HASH SET -> at most ONE char can have an odd count
    /**
     * time = O(n)
     * space = O(k)   # k = number of distinct chars
     */
    public boolean canPermutePalindrome(String s) {

        if (s == null || s.length() <= 1) {
            return true;
        }

        /**
         *  NOTE !!!
         *
         *  keep only the chars with an ODD count so far :
         *  add on 1st, 3rd, ... occurrence, remove on 2nd, 4th, ...
         */
        Set<Character> oddSet = new HashSet<>();

        for (char c : s.toCharArray()) {
            if (oddSet.contains(c)) {
                oddSet.remove(c);
            } else {
                oddSet.add(c);
            }
        }

        return oddSet.size() <= 1;
    }
}
