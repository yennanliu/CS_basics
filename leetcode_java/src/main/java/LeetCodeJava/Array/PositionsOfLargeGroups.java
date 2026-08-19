package LeetCodeJava.Array;

// https://leetcode.com/problems/positions-of-large-groups/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  830. Positions of Large Groups
 *  Easy
 *
 *  In a string s of lowercase letters, these letters form consecutive groups of
 *  the same character.
 *
 *  For example, a string like s = "abbxxxxzyy" has the groups "a", "bb", "xxxx",
 *  "z", and "yy".
 *
 *  A group is identified by an interval [start, end], where start and end denote
 *  the start and end indices (inclusive) of the group. In the above example,
 *  "xxxx" has the interval [3,6].
 *
 *  A group is considered large if it has 3 or more characters.
 *
 *  Return the intervals of every large group sorted in increasing order by start
 *  index.
 *
 *  Example 1:
 *  Input: s = "abbxxxxzzy"
 *  Output: [[3,6]]
 *
 *  Example 2:
 *  Input: s = "abc"
 *  Output: []
 *
 *  Example 3:
 *  Input: s = "abcdddeeeeaabbbcd"
 *  Output: [[3,5],[6,9],[12,14]]
 *
 *  Constraints:
 *   - 1 <= s.length <= 1000
 *   - s contains lowercase English letters only.
 */
public class PositionsOfLargeGroups {

    // V0
    // IDEA: single scan, remember the start index of the current run of equal chars
    /**
     * time = O(n)
     * space = O(1) (excluding output)
     */
    public List<List<Integer>> largeGroupPositions(String s) {
        List<List<Integer>> res = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return res;
        }
        int n = s.length();
        int start = 0;
        for (int i = 1; i <= n; i++) {
            if (i == n || s.charAt(i) != s.charAt(start)) {
                if (i - start >= 3) {
                    res.add(Arrays.asList(start, i - 1));
                }
                start = i;
            }
        }
        return res;
    }
}
