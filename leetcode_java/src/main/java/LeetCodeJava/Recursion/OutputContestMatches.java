package LeetCodeJava.Recursion;

// https://leetcode.com/problems/output-contest-matches/

import java.util.ArrayList;
import java.util.List;

/**
 *  544. Output Contest Matches
 *  Medium
 *
 *  During the NBA playoffs, we always set the rather strong team to play with
 *  the rather weak team. Given n teams (ranked 1..n, 1 = strongest), output
 *  their final contest matches as a string, using parentheses for pairing and
 *  commas for partition. In every round the strongest remaining team is paired
 *  with the weakest remaining team.
 *
 *
 *  Example 1:
 *
 *  Input: n = 4
 *  Output: "((1,4),(2,3))"
 *  Explanation:
 *    Round 1: (1,4),(2,3)
 *    Round 2: ((1,4),(2,3))
 *
 *  Example 2:
 *
 *  Input: n = 8
 *  Output: "(((1,8),(4,5)),((2,7),(3,6)))"
 *
 *
 *  Constraints:
 *
 *  n == 2^k where k is in the range [0, 12].
 */
public class OutputContestMatches {

    // V0
    // IDEA: ITERATIVE simulation - pair head with tail each round, halve the list
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public String findContestMatch(int n) {
        String[] teams = new String[n];
        for (int i = 0; i < n; i++) {
            teams[i] = String.valueOf(i + 1);
        }

        int size = n;
        while (size > 1) {
            for (int i = 0; i < size / 2; i++) {
                teams[i] = "(" + teams[i] + "," + teams[size - 1 - i] + ")";
            }
            size = size / 2;
        }
        return teams[0];
    }

    // V1
    // IDEA: RECURSION on the current round's list
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public String findContestMatch_1(int n) {
        List<String> groups = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            groups.add(String.valueOf(i));
        }
        return solve(groups);
    }

    private String solve(List<String> groups) {
        int size = groups.size();
        if (size == 1) {
            return groups.get(0);
        }
        List<String> next = new ArrayList<>();
        for (int i = 0; i < size / 2; i++) {
            next.add("(" + groups.get(i) + "," + groups.get(size - 1 - i) + ")");
        }
        return solve(next);
    }
}
