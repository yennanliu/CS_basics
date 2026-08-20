package LeetCodeJava.Sort;

// https://leetcode.com/problems/count-of-matches-in-tournament/

/**
 *  1688. Count of Matches in Tournament
 *  Easy
 *
 *  You are given an integer n, the number of teams in a tournament that has
 *  strange rules:
 *    - If the current number of teams is even, each team gets paired with another
 *      team. A total of n / 2 matches are played, and n / 2 teams advance.
 *    - If the current number of teams is odd, one team randomly advances, and the
 *      rest gets paired. A total of (n - 1) / 2 matches are played, and
 *      (n - 1) / 2 + 1 teams advance.
 *
 *  Return the number of matches played in the tournament until a winner is decided.
 *
 *  Example 1:
 *    Input: n = 7
 *    Output: 6
 *    Explanation: 7 teams -> 3 matches, 4 teams -> 2 matches, 2 teams -> 1 match.
 *                 3 + 2 + 1 = 6.
 *
 *  Example 2:
 *    Input: n = 14
 *    Output: 13
 *    Explanation: 7 + 3 + 2 + 1 = 13.
 *
 *  Constraints:
 *    1 <= n <= 200
 */
public class CountOfMatchesInTournament {

    // V0
    // IDEA: COUNTING ARGUMENT (every match eliminates exactly one team)
    //       no matter how the rounds are structured, a match always removes
    //       exactly one team, and the tournament ends with exactly 1 team left
    //       -> n - 1 teams must be eliminated -> n - 1 matches, always.
    //       the odd-round "one team randomly advances" rule does not change this;
    //       a bye is not a match and eliminates nobody.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int numberOfMatches(int n) {
        return n - 1;
    }
}
