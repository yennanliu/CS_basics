package LeetCodeJava.Sort;

// https://leetcode.com/problems/rank-teams-by-votes/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  1366. Rank Teams by Votes
 *  Medium
 *
 *  In a special ranking system, each voter gives a rank from highest to lowest to
 *  all teams participating in the competition.
 *
 *  The ordering of teams is decided by who received the most position-one votes.
 *  If two or more teams tie in the first position, we consider the second position
 *  to resolve the conflict, if they tie again, we continue this process until the
 *  ties are resolved. If two or more teams are still tied after considering all
 *  positions, we rank them alphabetically based on their team letter.
 *
 *  You are given an array of strings votes which is the votes of all voters in the
 *  ranking system. Sort all teams according to the ranking system described above
 *  and return a string of all teams sorted by the ranking system.
 *
 *  Example 1:
 *    Input: votes = ["ABC","ACB","ABC","ACB","ACB"]
 *    Output: "ACB"
 *
 *  Example 2:
 *    Input: votes = ["WXYZ","XYZW"]
 *    Output: "XWYZ"
 *
 *  Example 3:
 *    Input: votes = ["ZMNAGUEDSJYLBOPHRQICWFXTVK"]
 *    Output: "ZMNAGUEDSJYLBOPHRQICWFXTVK"
 *
 *  Constraints:
 *    1 <= votes.length <= 1000
 *    1 <= votes[i].length <= 26
 *    votes[i].length == votes[j].length
 *    votes[i][j] is an English uppercase letter, all unique within a vote.
 *    All the characters that occur in votes[0] also occur in every votes[j].
 */
public class RankTeamsByVotes {

    // V0
    // IDEA: COUNT PER (TEAM, POSITION) + CUSTOM COMPARATOR
    //       cnt[team][p] = how many voters put `team` at position p.
    //
    //       the ranking rule is exactly a lexicographic comparison of that count
    //       vector DESCENDING, with the team letter ASCENDING as the final
    //       tie-break - which is what the comparator below spells out.
    /**
     * time = O(V*M + M^2 log M)   // V = votes.length, M = number of teams
     * space = O(M^2)
     */
    public String rankTeams(String[] votes) {
        int m = votes[0].length();

        // 26 letters x m positions
        final int[][] cnt = new int[26][m];
        for (String vote : votes) {
            for (int p = 0; p < m; p++) {
                cnt[vote.charAt(p) - 'A'][p]++;
            }
        }

        Character[] teams = new Character[m];
        for (int i = 0; i < m; i++) {
            teams[i] = votes[0].charAt(i);
        }

        Arrays.sort(teams, new Comparator<Character>() {
            @Override
            public int compare(Character a, Character b) {
                int[] ca = cnt[a - 'A'];
                int[] cb = cnt[b - 'A'];
                for (int p = 0; p < ca.length; p++) {
                    if (ca[p] != cb[p]) {
                        return Integer.compare(cb[p], ca[p]);   // more votes first
                    }
                }
                return Character.compare(a, b);                 // alphabetical
            }
        });

        StringBuilder sb = new StringBuilder();
        for (char c : teams) {
            sb.append(c);
        }
        return sb.toString();
    }
}
