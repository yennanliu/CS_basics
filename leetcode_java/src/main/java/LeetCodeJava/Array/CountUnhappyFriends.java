package LeetCodeJava.Array;

// https://leetcode.com/problems/count-unhappy-friends/description/

/**
 * 1583. Count Unhappy Friends
 * Medium
 *
 * You are given a list of preferences for n friends, where n is always even.
 *
 * For each person i, preferences[i] contains a list of friends sorted in the order of
 * preference. In other words, a friend earlier in the list is more preferred than a friend
 * later in the list. Friends in each list are denoted by integers from 0 to n-1.
 *
 * All the friends are divided into pairs. The pairings are given in a list pairs, where
 * pairs[i] = [xi, yi] denotes xi is paired with yi and yi is paired with xi.
 *
 * However, this pairing may cause some of the friends to be unhappy. A friend x is unhappy
 * if x is paired with y and there exists a friend u who is paired with v but:
 *
 * x prefers u over y, and
 * u prefers x over v.
 *
 * Return the number of unhappy friends.
 *
 *
 * Example 1:
 *
 * Input: n = 4, preferences = [[1, 2, 3], [3, 2, 0], [3, 1, 0], [1, 2, 0]],
 *        pairs = [[0, 1], [2, 3]]
 * Output: 2
 * Explanation: Friends 1 and 3 are unhappy; friends 0 and 2 are happy.
 *
 * Example 2:
 *
 * Input: n = 2, preferences = [[1], [0]], pairs = [[1, 0]]
 * Output: 0
 *
 * Example 3:
 *
 * Input: n = 4, preferences = [[1, 3, 2], [2, 3, 0], [1, 3, 0], [0, 2, 1]],
 *        pairs = [[1, 3], [0, 2]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 2 <= n <= 500
 * n is even.
 * preferences.length == n
 * preferences[i].length == n - 1
 * 0 <= preferences[i][j] <= n - 1
 * preferences[i] does not contain i.
 * All values in preferences[i] are unique.
 * pairs.length == n/2
 * pairs[i].length == 2
 * xi != yi
 * 0 <= xi, yi <= n - 1
 * Each person is contained in exactly one pair.
 *
 */
public class CountUnhappyFriends {

    // V0
    // IDEA: RANK TABLE — rank[i][j] = position of j in preferences[i] (smaller = preferred),
    //       giving O(1) "does a prefer b over c?" checks. x (paired with y) is unhappy iff
    //       some u that x prefers over y also prefers x over its own partner.
    //       Scanning preferences[x] left to right and stopping at y yields exactly the
    //       candidates u that x prefers over y.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int unhappyFriends(int n, int[][] preferences, int[][] pairs) {
        int[][] rank = new int[n][n];
        for (int person = 0; person < n; person++) {
            for (int pos = 0; pos < preferences[person].length; pos++) {
                rank[person][preferences[person][pos]] = pos;
            }
        }

        int[] partner = new int[n];
        for (int[] pair : pairs) {
            partner[pair[0]] = pair[1];
            partner[pair[1]] = pair[0];
        }

        int unhappyCount = 0;
        for (int x = 0; x < n; x++) {
            int y = partner[x];
            for (int u : preferences[x]) {
                // everyone from here on is liked less than (or is) x's own partner
                if (u == y) {
                    break;
                }
                // does u also prefer x over u's own partner?
                if (rank[u][x] < rank[u][partner[u]]) {
                    unhappyCount++;
                    break;
                }
            }
        }
        return unhappyCount;
    }
}
