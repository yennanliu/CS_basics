package LeetCodeJava.String;

// https://leetcode.com/problems/di-string-match/

/**
 *  942. DI String Match
 *  Easy
 *
 *  A permutation perm of n + 1 integers of all the integers in the range
 *  [0, n] can be represented as a string s of length n where:
 *   - s[i] == 'I' if perm[i] < perm[i + 1], and
 *   - s[i] == 'D' if perm[i] > perm[i + 1].
 *
 *  Given a string s, reconstruct the permutation perm and return it. If there
 *  are multiple valid permutations perm, return any of them.
 *
 *  Example 1:
 *  Input: s = "IDID"
 *  Output: [0,4,1,3,2]
 *
 *  Example 2:
 *  Input: s = "III"
 *  Output: [0,1,2,3]
 *
 *  Example 3:
 *  Input: s = "DDI"
 *  Output: [3,2,0,1]
 *
 *  Constraints:
 *   - 1 <= s.length <= 10^5
 *   - s[i] is either 'I' or 'D'.
 */
public class DIStringMatch {

    // V0
    // IDEA: GREEDY 2 POINTERS - on 'I' emit the smallest unused value, on 'D'
    //       emit the largest unused value; the single leftover closes the run.
    /**
     * time = O(n)
     * space = O(n)   // output only
     */
    public int[] diStringMatch(String s) {
        int n = s.length();
        int[] res = new int[n + 1];
        int lo = 0;
        int hi = n;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'I') {
                res[i] = lo++;
            } else {
                res[i] = hi--;
            }
        }
        res[n] = lo; // lo == hi here
        return res;
    }
}
