package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-strings-can-be-made-equal-with-operations-ii/description/

/**
 * 2840. Check if Strings Can be Made Equal With Operations II
 * Medium
 *
 * You are given two strings s1 and s2, both of length n, consisting of lowercase English letters.
 *
 * You can apply the following operation on any of the two strings any number of times:
 *
 * - Choose any two indices i and j such that i < j and the difference j - i is even, then swap the two characters at those indices in the string.
 *
 * Return true if you can make the strings s1 and s2 equal, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: s1 = "abcdba", s2 = "cabdab"
 * Output: true
 * Explanation: We can apply the following operations on s1:
 * - Choose the indices i = 0, j = 2. The resulting string is s1 = "cbadba".
 * - Choose the indices i = 2, j = 4. The resulting string is s1 = "cbbdaa".
 * - Choose the indices i = 1, j = 5. The resulting string is s1 = "cabdab" = s2.
 *
 * Example 2:
 *
 * Input: s1 = "abe", s2 = "bea"
 * Output: false
 * Explanation: It is not possible to make the two strings equal.
 *
 *
 * Constraints:
 *
 * n == s1.length == s2.length
 * 1 <= n <= 10^5
 * s1 and s2 consist only of lowercase English letters.
 *
 */
public class CheckIfStringsCanBeMadeEqualWithOperationsII {

    // V0
    // IDEA: PARITY CLASSES + COUNTING (2 x 26 balance table must cancel to zero)
    /**
     * time = O(N + 26)
     * space = O(26)
     */
    public boolean checkStrings(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        // j - i even <=> same parity, so chars shuffle freely inside each parity
        // class but never cross; +1 for s1, -1 for s2 in the same class
        int[][] balance = new int[2][26];
        for (int i = 0; i < s1.length(); i++) {
            balance[i % 2][s1.charAt(i) - 'a']++;
            balance[i % 2][s2.charAt(i) - 'a']--;
        }
        for (int parity = 0; parity < 2; parity++) {
            for (int letter = 0; letter < 26; letter++) {
                if (balance[parity][letter] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
