package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-people-to-teach/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 1733. Minimum Number of People to Teach
 * Medium
 *
 * On a social network consisting of m users and some friendships between users, two
 * users can communicate with each other if they know a common language.
 *
 * You are given an integer n, an array languages, and an array friendships where:
 *
 * - There are n languages numbered 1 through n,
 * - languages[i] is the set of languages the ith user knows, and
 * - friendships[i] = [ui, vi] denotes a friendship between the users ui and vi.
 *
 * You can choose one language and teach it to some users so that all friends can
 * communicate with each other. Return the minimum number of users you need to teach.
 *
 * Note that friendships are not transitive, meaning if x is a friend of y and y is
 * a friend of z, this doesn't guarantee that x is a friend of z.
 *
 *
 * Example 1:
 *
 * Input: n = 2, languages = [[1],[2],[1,2]], friendships = [[1,2],[1,3],[2,3]]
 * Output: 1
 * Explanation: You can either teach user 1 the second language or user 2 the first
 * language.
 *
 * Example 2:
 *
 * Input: n = 3, languages = [[2],[1,3],[1,2],[3]], friendships = [[1,4],[1,2],[3,4],[2,3]]
 * Output: 2
 * Explanation: Teach the third language to users 1 and 3, yielding two users to teach.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 500
 * languages.length == m
 * 1 <= m <= 500
 * 1 <= languages[i].length <= n
 * 1 <= languages[i][j] <= n
 * 1 <= ui < vi <= languages.length
 * 1 <= friendships.length <= 500
 * All tuples (ui, vi) are unique
 * languages[i] contains only unique values
 *
 */
public class MinimumNumberOfPeopleToTeach {

    // V0
    // IDEA: GREEDY / COUNTING (ONLY BROKEN FRIENDSHIPS MATTER)
    //       collect every user on a friendship that shares no language. Teaching
    //       language L to all of them fixes every broken pair, and costs
    //       |broken users| - (#broken users already knowing L). Try every L.
    /**
     * time = O(F * L + U * L)   # F = friendships, U = users, L = languages per user
     * space = O(U * L)
     */
    public int minimumTeachings(int n, int[][] languages, int[][] friendships) {
        int users = languages.length;

        // knows[u][lang] for 0-indexed user u
        boolean[][] knows = new boolean[users][n + 1];
        for (int u = 0; u < users; u++) {
            for (int lang : languages[u]) {
                knows[u][lang] = true;
            }
        }

        // users sitting on at least one friendship that cannot communicate
        Set<Integer> brokenUsers = new HashSet<>();
        for (int[] friendship : friendships) {
            int u = friendship[0] - 1;
            int v = friendship[1] - 1;
            if (!shareLanguage(languages[u], knows[v])) {
                brokenUsers.add(u);
                brokenUsers.add(v);
            }
        }
        if (brokenUsers.isEmpty()) {
            return 0;
        }

        // speakers[lang] = broken users who already know lang
        int[] speakers = new int[n + 1];
        for (int u : brokenUsers) {
            for (int lang : languages[u]) {
                speakers[lang]++;
            }
        }
        int mostSpeakers = 0;
        for (int lang = 1; lang <= n; lang++) {
            mostSpeakers = Math.max(mostSpeakers, speakers[lang]);
        }
        return brokenUsers.size() - mostSpeakers;
    }

    /** True if any language in `languagesOfU` is also known by v. */
    private boolean shareLanguage(int[] languagesOfU, boolean[] knowsV) {
        for (int lang : languagesOfU) {
            if (knowsV[lang]) {
                return true;
            }
        }
        return false;
    }
}
