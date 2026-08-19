package LeetCodeJava.Math;

// https://leetcode.com/problems/detonate-the-maximum-bombs/

import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;

/**
 *  2101. Detonate the Maximum Bombs
 *  Medium
 *
 *  You are given a list of bombs. The range of a bomb is defined as the area where its
 *  effect can be felt. This area is in the shape of a circle with the center as the
 *  location of the bomb.
 *
 *  The bombs are represented by a 0-indexed 2D integer array bombs where
 *  bombs[i] = [xi, yi, ri]. xi and yi denote the X-coordinate and Y-coordinate of the
 *  location of the ith bomb, whereas ri denotes the radius of its range.
 *
 *  You may choose to detonate a single bomb. When a bomb is detonated, it will detonate
 *  all bombs that lie in its range. These bombs will further detonate the bombs that lie
 *  in their ranges.
 *
 *  Given the list of bombs, return the maximum number of bombs that can be detonated if
 *  you are allowed to detonate only one bomb.
 *
 *  Example 1:
 *    Input: bombs = [[2,1,3],[6,1,4]]
 *    Output: 2
 *    Explanation: detonating the right bomb detonates both -> max(1, 2) = 2
 *
 *  Example 2:
 *    Input: bombs = [[1,1,5],[10,10,5]]
 *    Output: 1
 *
 *  Constraints:
 *    1 <= bombs.length <= 100
 *    bombs[i].length == 3
 *    1 <= xi, yi, ri <= 10^5
 */
public class DetonateTheMaximumBombs {

    // V0
    // IDEA: BUILD A DIRECTED REACHABILITY GRAPH, THEN DFS FROM EVERY BOMB
    //
    //   the relation is DIRECTED and not symmetric: bomb i triggers bomb j iff j's
    //   CENTER lies inside i's circle, i.e.
    //       (xi - xj)^2 + (yi - yj)^2 <= ri^2
    //   a big bomb can reach a small one without the reverse being true.
    //
    //   compare SQUARED distances (as long) so the check stays exact integer math.
    //   n <= 100, so an O(n^2) graph plus one DFS per start bomb is cheap.
    /**
     * time = O(N^3)
     * space = O(N^2)
     */
    public int maximumDetonation(int[][] bombs) {
        int n = bombs.length;
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < n; i++) {
            long xi = bombs[i][0];
            long yi = bombs[i][1];
            long ri = bombs[i][2];
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                long dx = xi - bombs[j][0];
                long dy = yi - bombs[j][1];
                if (dx * dx + dy * dy <= ri * ri) {
                    g.get(i).add(j);
                }
            }
        }

        int res = 0;
        for (int s = 0; s < n; s++) {
            boolean[] seen = new boolean[n];
            seen[s] = true;
            int cnt = 1;
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(s);
            while (!stack.isEmpty()) {
                int u = stack.pop();
                for (int v : g.get(u)) {
                    if (!seen[v]) {
                        seen[v] = true;
                        cnt++;
                        stack.push(v);
                    }
                }
            }
            res = Math.max(res, cnt);
        }
        return res;
    }
}
