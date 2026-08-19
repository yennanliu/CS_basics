package LeetCodeJava.Array;

// https://leetcode.com/problems/shortest-distance-to-a-character/

/**
 *  821. Shortest Distance to a Character
 *  Easy
 *
 *  Given a string s and a character c that occurs in s, return an array of
 *  integers answer where answer.length == s.length and answer[i] is the distance
 *  from index i to the closest occurrence of character c in s.
 *
 *  The distance between two indices i and j is abs(i - j).
 *
 *  Example 1:
 *    Input: s = "loveleetcode", c = "e"
 *    Output: [3,2,1,0,1,0,0,1,2,2,1,0]
 *    Explanation: The character 'e' appears at indices 3, 5, 6, and 11.
 *
 *  Example 2:
 *    Input: s = "aaab", c = "b"
 *    Output: [3,2,1,0]
 *
 *  Constraints:
 *    1 <= s.length <= 10^4
 *    s[i] and c are lowercase English letters.
 *    It is guaranteed that c occurs at least once in s.
 */
public class ShortestDistanceToACharacter {

    // V0
    // IDEA: TWO PASSES. Left-to-right pass keeps the distance to the closest c on
    //       the left, right-to-left pass keeps the one on the right; take the min.
    /**
     * time = O(n)
     * space = O(1) (excluding output)
     */
    public int[] shortestToChar(String s, char c) {
        int n = s.length();
        int[] res = new int[n];
        final int BIG = n * 2;

        int last = -BIG;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == c) {
                last = i;
            }
            res[i] = i - last;
        }

        last = BIG;
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == c) {
                last = i;
            }
            res[i] = Math.min(res[i], last - i);
        }
        return res;
    }
}
