package LeetCodeJava.HashTable;

// https://leetcode.com/problems/prison-cells-after-n-days/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  957. Prison Cells After N Days
 *  Medium
 *
 *  There are 8 prison cells in a row and each cell is either occupied or vacant.
 *  Each day, whether the cell is occupied or vacant changes according to the
 *  following rules:
 *    - If a cell has two adjacent neighbors that are both occupied or both vacant,
 *      then the cell becomes occupied.
 *    - Otherwise, it becomes vacant.
 *  Note that because the prison is a row, the first and the last cells in the row
 *  can't have two adjacent neighbors, so they are always vacant after day 1.
 *
 *  You are given an integer array cells where cells[i] == 1 if the ith cell is
 *  occupied and cells[i] == 0 if it is vacant, and an integer n.
 *
 *  Return the state of the prison after n days.
 *
 *  Example 1:
 *  Input: cells = [0,1,0,1,1,0,0,1], n = 7
 *  Output: [0,0,1,1,0,0,0,0]
 *
 *  Example 2:
 *  Input: cells = [1,0,0,1,0,0,1,0], n = 1000000000
 *  Output: [0,0,1,1,1,1,1,0]
 *
 *  Constraints:
 *  cells.length == 8
 *  cells[i] is either 0 or 1.
 *  1 <= n <= 10^9
 */
public class PrisonCellsAfterNDays {

    // V0
    // IDEA: CYCLE DETECTION. After the very first day both ends are 0, and the
    //       states from then on repeat with a period of 14. So we can map any
    //       n >= 1 into [1, 14] via  n = (n - 1) % 14 + 1  and simulate.
    /**
     * time = O(1)   (at most 14 steps of 8 cells)
     * space = O(1)
     */
    public int[] prisonAfterNDays(int[] cells, int n) {

        // edge
        if (cells == null || cells.length != 8 || n <= 0) {
            return cells;
        }

        // states after day 1 form a cycle of length 14
        n = (n - 1) % 14 + 1;

        int[] cur = Arrays.copyOf(cells, 8);
        for (int d = 0; d < n; d++) {
            cur = nextDay(cur);
        }

        return cur;
    }

    private int[] nextDay(int[] cur) {
        int[] next = new int[8];
        // ends are always 0
        for (int i = 1; i < 7; i++) {
            next[i] = (cur[i - 1] == cur[i + 1]) ? 1 : 0;
        }
        return next;
    }

    // V1
    // IDEA: HASH MAP CYCLE DETECTION (no hard-coded period of 14)
    /**
     * time = O(1)   (state space is bounded by 2^6)
     * space = O(1)
     */
    public int[] prisonAfterNDays_1(int[] cells, int n) {

        if (cells == null || cells.length != 8 || n <= 0) {
            return cells;
        }

        Map<String, Integer> seen = new HashMap<>();
        int[] cur = Arrays.copyOf(cells, 8);

        while (n > 0) {
            String key = Arrays.toString(cur);
            if (seen.containsKey(key)) {
                int cycle = seen.get(key) - n;
                n %= cycle;
                seen.clear(); // avoid re-entering the branch
            } else {
                seen.put(key, n);
            }
            if (n > 0) {
                n--;
                cur = nextDay(cur);
            }
        }

        return cur;
    }
}
