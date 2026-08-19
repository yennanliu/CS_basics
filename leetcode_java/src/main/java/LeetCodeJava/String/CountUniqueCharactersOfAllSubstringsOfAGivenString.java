package LeetCodeJava.String;

// https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/

/**
 *  828. Count Unique Characters of All Substrings of a Given String
 *  Hard
 *
 *  Let's define a function countUniqueChars(s) that returns the number of
 *  unique characters in s.
 *
 *  For example, calling countUniqueChars(s) if s = "LEETCODE" then "L", "T",
 *  "C", "O", "D" are the unique characters since they appear only once in s,
 *  therefore countUniqueChars(s) = 5.
 *
 *  Given a string s, return the sum of countUniqueChars(t) where t is a
 *  substring of s. The test cases are generated such that the answer fits in
 *  a 32-bit integer.
 *
 *  Notice that some substrings can be repeated so in this case you have to
 *  count the repeated ones too.
 *
 *  Example 1:
 *  Input: s = "ABC"
 *  Output: 10
 *  Explanation: All possible substrings are: "A","B","C","AB","BC" and "ABC".
 *  Every substring is composed with only unique letters.
 *  Sum of lengths of all substring is 1 + 1 + 1 + 2 + 2 + 3 = 10
 *
 *  Example 2:
 *  Input: s = "ABA"
 *  Output: 8
 *  Explanation: The same as example 1, except countUniqueChars("ABA") = 1.
 *
 *  Constraints:
 *   - 1 <= s.length <= 10^5
 *   - s consists of uppercase English letters only.
 */
public class CountUniqueCharactersOfAllSubstringsOfAGivenString {

    // V0
    // IDEA: CONTRIBUTION COUNTING - for each occurrence of a char at index i,
    //       with previous occurrence j and the one before that k, it is the
    //       unique instance of that char in (i - j) * (j - k) substrings.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int uniqueLetterString(String s) {
        int n = s.length();
        // last[c] = most recent index of c, prev[c] = the index before that
        int[] last = new int[26];
        int[] prev = new int[26];
        for (int i = 0; i < 26; i++) {
            last[i] = -1;
            prev[i] = -1;
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'A';
            int j = last[c];
            int k = prev[c];
            res += (long) (i - j) * (j - k);
            prev[c] = j;
            last[c] = i;
        }

        // tail: treat "n" as a virtual next occurrence for every letter
        for (int c = 0; c < 26; c++) {
            int j = last[c];
            int k = prev[c];
            res += (long) (n - j) * (j - k);
        }

        return (int) res;
    }

    // V1
    // IDEA: same contribution idea, but expressed as "count how many substrings
    //       contain exactly one copy of s[i]" via explicit left/right bounds.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int uniqueLetterString_1(String s) {
        int n = s.length();
        int[] left = new int[n];   // index of previous same char (-1 if none)
        int[] right = new int[n];  // index of next same char (n if none)

        int[] lastSeen = new int[26];
        for (int i = 0; i < 26; i++) {
            lastSeen[i] = -1;
        }
        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'A';
            left[i] = lastSeen[c];
            lastSeen[c] = i;
        }

        for (int i = 0; i < 26; i++) {
            lastSeen[i] = n;
        }
        for (int i = n - 1; i >= 0; i--) {
            int c = s.charAt(i) - 'A';
            right[i] = lastSeen[c];
            lastSeen[c] = i;
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            res += (long) (i - left[i]) * (right[i] - i);
        }
        return (int) res;
    }
}
