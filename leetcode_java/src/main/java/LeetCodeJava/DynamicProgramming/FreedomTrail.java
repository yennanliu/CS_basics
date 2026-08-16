package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/freedom-trail/description/

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 514. Freedom Trail
 * Hard
 *
 * In the video game Fallout 4, the quest "Road to Freedom" requires players to reach a
 * metal dial called the "Freedom Trail Ring" and use the dial to spell a specific keyword
 * to open the door.
 *
 * Given a string ring that represents the code engraved on the outer ring and another
 * string key that represents the keyword that needs to be spelled, return the minimum
 * number of steps to spell all the characters in the keyword.
 *
 * Initially, the first character of the ring is aligned at the "12:00" direction.
 * You should spell all the characters in key one by one by rotating ring clockwise or
 * anticlockwise to make each character of the string key aligned at the "12:00" direction
 * and then by pressing the center button.
 *
 * At the stage of rotating the ring to spell the key character key[i]:
 *
 * 1. You can rotate the ring clockwise or anticlockwise by one place, which counts as one
 *    step. The final purpose of the rotation is to align one of ring's characters at the
 *    "12:00" direction, where this character must equal key[i].
 * 2. If the character key[i] has been aligned at the "12:00" direction, press the center
 *    button to spell, which also counts as one step.
 *
 * Example 1:
 *
 * Input: ring = "godding", key = "gd"
 * Output: 4
 * Explanation:
 * For the first key character 'g', since it is already in place, we just need 1 step to
 * spell this character.
 * For the second key character 'd', we need to rotate the ring "godding" anticlockwise by
 * two steps to make it become "ddinggo".
 * Also, we need 1 more step for spelling.
 * So the final output is 4.
 *
 * Example 2:
 *
 * Input: ring = "godding", key = "godding"
 * Output: 13
 *
 *
 * Constraints:
 *
 * 1 <= ring.length, key.length <= 100
 * ring and key consist of only lower case English letters.
 * It is guaranteed that key could always be spelled by rotating ring.
 *
 */
public class FreedomTrail {

    // V0
    // IDEA: DP over the ring positions
    /**
     *  DP def:
     *    - dp[j] = min steps to spell key[0..i] with ring index j aligned at 12:00
     *
     *  DP eq:
     *    - dpNew[j] = min over k in pos[key[i-1]] of
     *                    dp[k] + min(|j - k|, n - |j - k|) + 1
     *      ( rotating EITHER way on a circle, + 1 for pressing the button )
     *
     *  NOTE !!! the state is the RING INDEX, not the character -- two occurrences of
     *           the same letter sit at different distances and must be kept apart.
     *
     *  time  = O(m * n^2)  // m = key.length, n = ring.length
     *  space = O(n)
     */
    public int findRotateSteps(String ring, String key) {
        int n = ring.length();

        // char -> all indices it shows up in ring
        Map<Character, List<Integer>> pos = new HashMap<>();
        for (int i = 0; i < n; i++) {
            pos.computeIfAbsent(ring.charAt(i), c -> new ArrayList<>()).add(i);
        }

        // init : spell key[0] starting from index 0
        Map<Integer, Integer> dp = new HashMap<>();
        for (int j : pos.get(key.charAt(0))) {
            dp.put(j, Math.min(j, n - j) + 1);
        }

        for (int i = 1; i < key.length(); i++) {
            Map<Integer, Integer> ndp = new HashMap<>();
            for (int j : pos.get(key.charAt(i))) {
                int best = Integer.MAX_VALUE;
                for (Map.Entry<Integer, Integer> e : dp.entrySet()) {
                    int d = Math.abs(j - e.getKey());
                    best = Math.min(best, e.getValue() + Math.min(d, n - d) + 1);
                }
                ndp.put(j, best);
            }
            dp = ndp;
        }

        int res = Integer.MAX_VALUE;
        for (int v : dp.values()) {
            res = Math.min(res, v);
        }
        return res;
    }


    // V1
    // IDEA: TOP-DOWN MEMOISED RECURSION on (ring index, key index)
    /**
     *  best(pos, k) = min over occurrences of key[k] of
     *                 rotate(pos -> occ) + 1 + best(occ, k + 1)
     *
     *  Only the (position, key index) pairs that are actually reachable get
     *  evaluated, which matters when the ring holds many characters the key never
     *  uses.
     *
     *  time  = O(m * n^2)
     *  space = O(m * n)
     */
    private Integer[][] memoRing;

    public int findRotateSteps_1(String ring, String key) {
        memoRing = new Integer[ring.length()][key.length()];
        return spell(ring, key, 0, 0);
    }

    private int spell(String ring, String key, int pos, int k) {
        if (k == key.length()) {
            return 0;
        }
        if (memoRing[pos][k] != null) {
            return memoRing[pos][k];
        }
        int n = ring.length();
        int best = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (ring.charAt(i) != key.charAt(k)) {
                continue;
            }
            int d = Math.abs(i - pos);
            best = Math.min(best, Math.min(d, n - d) + 1 + spell(ring, key, i, k + 1));
        }
        memoRing[pos][k] = best;
        return best;
    }

    // V2
    // IDEA: FULL 2D TABLE over (key index, ring index)
    /**
     *  dp[k][i] = min steps to have spelled key[0..k] with ring index i at 12:00.
     *
     *  The whole table is materialised rather than rolled into two maps, so the
     *  chosen ring positions can be reconstructed afterwards -- useful when the
     *  answer wanted is the ROTATION SEQUENCE, not just its length.
     *
     *  time  = O(m * n^2)
     *  space = O(m * n)
     */
    public int findRotateSteps_2(String ring, String key) {
        int n = ring.length();
        int m = key.length();
        final int INF = Integer.MAX_VALUE / 2;

        int[][] dp = new int[m][n];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }

        for (int i = 0; i < n; i++) {
            if (ring.charAt(i) == key.charAt(0)) {
                dp[0][i] = Math.min(i, n - i) + 1;
            }
        }

        for (int k = 1; k < m; k++) {
            for (int i = 0; i < n; i++) {
                if (ring.charAt(i) != key.charAt(k)) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (dp[k - 1][j] == INF) {
                        continue;
                    }
                    int d = Math.abs(i - j);
                    dp[k][i] = Math.min(dp[k][i], dp[k - 1][j] + Math.min(d, n - d) + 1);
                }
            }
        }

        int res = INF;
        for (int i = 0; i < n; i++) {
            res = Math.min(res, dp[m - 1][i]);
        }
        return res;
    }

    // V3
    // IDEA: DIJKSTRA over (ring index, key index) states
    /**
     *  Treat (position, progress) as a node and a rotation-plus-press as a weighted
     *  edge, then run Dijkstra.
     *
     *  Overkill on this DAG, but it stops as soon as a full-progress state is
     *  popped, so on inputs where the key is spelled early it explores far fewer
     *  states than the exhaustive table.
     *
     *  time  = O(m * n^2 log(m n))
     *  space = O(m * n)
     */
    public int findRotateSteps_3(String ring, String key) {
        int n = ring.length();
        int m = key.length();

        Map<Character, List<Integer>> pos = new HashMap<>();
        for (int i = 0; i < n; i++) {
            pos.computeIfAbsent(ring.charAt(i), c -> new ArrayList<>()).add(i);
        }

        // {cost, ringIndex, keyIndex}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));
        pq.add(new int[] { 0, 0, 0 });
        int[][] best = new int[n][m + 1];
        for (int[] row : best) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0];
            int at = cur[1];
            int k = cur[2];

            if (k == m) {
                return cost;
            }
            if (cost > best[at][k]) {
                continue;
            }

            for (int nxt : pos.getOrDefault(key.charAt(k), Collections.emptyList())) {
                int d = Math.abs(nxt - at);
                int nc = cost + Math.min(d, n - d) + 1;
                if (nc < best[nxt][k + 1]) {
                    best[nxt][k + 1] = nc;
                    pq.add(new int[] { nc, nxt, k + 1 });
                }
            }
        }
        return -1;
    }

}
