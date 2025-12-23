package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-black-blocks/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2768. Number of Black Blocks
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two integers m and n representing the dimensions of a 0-indexed m x n grid.
 *
 * You are also given a 0-indexed 2D integer matrix coordinates, where coordinates[i] = [x, y] indicates that the cell with coordinates [x, y] is colored black. All cells in the grid that do not appear in coordinates are white.
 *
 * A block is defined as a 2 x 2 submatrix of the grid. More formally, a block with cell [x, y] as its top-left corner where 0 <= x < m - 1 and 0 <= y < n - 1 contains the coordinates [x, y], [x + 1, y], [x, y + 1], and [x + 1, y + 1].
 *
 * Return a 0-indexed integer array arr of size 5 such that arr[i] is the number of blocks that contains exactly i black cells.
 *
 *
 *
 * Example 1:
 *
 * Input: m = 3, n = 3, coordinates = [[0,0]]
 * Output: [3,1,0,0,0]
 * Explanation: The grid looks like this:
 *
 * There is only 1 block with one black cell, and it is the block starting with cell [0,0].
 * The other 3 blocks start with cells [0,1], [1,0] and [1,1]. They all have zero black cells.
 * Thus, we return [3,1,0,0,0].
 * Example 2:
 *
 * Input: m = 3, n = 3, coordinates = [[0,0],[1,1],[0,2]]
 * Output: [0,2,2,0,0]
 * Explanation: The grid looks like this:
 *
 * There are 2 blocks with two black cells (the ones starting with cell coordinates [0,0] and [0,1]).
 * The other 2 blocks have starting cell coordinates of [1,0] and [1,1]. They both have 1 black cell.
 * Therefore, we return [0,2,2,0,0].
 *
 *
 * Constraints:
 *
 * 2 <= m <= 105
 * 2 <= n <= 105
 * 0 <= coordinates.length <= 104
 * coordinates[i].length == 2
 * 0 <= coordinates[i][0] < m
 * 0 <= coordinates[i][1] < n
 * It is guaranteed that coordinates contains pairwise distinct coordinates.
 *
 */
public class NumberOfBlackBlocks {

    // V0
//    public long[] countBlackBlocks(int m, int n, int[][] coordinates) {
//
//    }

    // V1
    // IDEA: HASHMAP
    // https://leetcode.com/problems/number-of-black-blocks/solutions/3746090/javacpython-hashmap-by-lee215-amk0/
    public long[] countBlackBlocks_1(int m, int n, int[][] coordinates) {
        HashMap<Long, Integer> cnt = new HashMap<>();
        long[] res = { (n - 1L) * (m - 1), 0, 0, 0, 0 };
        for (int[] c : coordinates)
            for (int i = c[0]; i < c[0] + 2; i++)
                for (int j = c[1]; j < c[1] + 2; j++)
                    if (0 < i && i < m && 0 < j && j < n) {
                        long idx = i * 100000L + j;
                        res[cnt.getOrDefault(idx, 0)]--;
                        cnt.put(idx, cnt.getOrDefault(idx, 0) + 1);
                        res[cnt.getOrDefault(idx, 0)]++;
                    }
        return res;
    }


    // V2
    // IDEA: HASHMAP
    // https://leetcode.com/problems/number-of-black-blocks/solutions/6657759/javahashmap-readable-code-to-understand-312zu/
    public long[] countBlackBlocks_2(int m, int n, int[][] coordinates) {
        HashMap<String, Integer> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        for (int[] xy : coordinates) {
            int x = xy[0];
            int y = xy[1];

            if (x + 1 < m && y + 1 < n)
                addToMap(sb, x, y, map); // Bottom-right
            if (x + 1 < m && y - 1 >= 0)
                addToMap(sb, x, y - 1, map); // Bottom-left
            if (x - 1 >= 0 && y + 1 < n)
                addToMap(sb, x - 1, y, map); // Top-right
            if (x - 1 >= 0 && y - 1 >= 0)
                addToMap(sb, x - 1, y - 1, map); // Top-left
        }

        long[] res = new long[5];
        for (int count : map.values()) {
            res[count]++;
        }

        res[0] = (long) (m - 1) * (n - 1) - map.size(); // Count of blocks with 0 black cells
        return res;
    }

    public void addToMap(StringBuilder sb, int x, int y, HashMap<String, Integer> map) {
        sb.setLength(0);
        sb.append(x).append(',').append(y);
        String key = sb.toString();
        map.put(key, map.getOrDefault(key, 0) + 1);
    }


    // V3
    // IDEA: MAP
    public long[] countBlackBlocks_3(int m, int n, int[][] coordinates) {
        Map<Long, Integer> countMap = new HashMap<>(); // Map to store the count of black blocks
        List<Long> res = new ArrayList<>(5); // Result list to store the final counts

        for (int[] coor : coordinates) {
            int x = coor[0];
            int y = coor[1];

            for (int dx = 0; dx <= 1; dx++) {
                for (int dy = 0; dy <= 1; dy++) {
                    if (x - dx >= 0 && y - dy >= 0 && x - dx < m - 1 && y - dy < n - 1) {
                        // Calculate the key for the map using the coordinates
                        long key = (long) (x - dx) * n + y - dy;
                        countMap.put(key, countMap.getOrDefault(key, 0) + 1); // Increment the count of black blocks for the key
                    }
                }
            }
        }

        // Initialize the result array
        for (int i = 0; i < 5; i++) {
            res.add(0L);
        }

        // Iterate over the countMap and update the result list
        for (Map.Entry<Long, Integer> entry : countMap.entrySet()) {
            int count = entry.getValue();
            res.set(count, res.get(count) + 1); // Increment the count in the result list at the corresponding index
        }

        long sum = 0;
        for (long count : res) {
            sum += count;
        }

        res.set(0, 1L * (m - 1) * (n - 1) - sum); // Calculate the count of white blocks and store it at index 0

        // Convert the result list to an array
        long[] resultArray = new long[res.size()];
        for (int i = 0; i < res.size(); i++) {
            resultArray[i] = res.get(i);
        }

        return resultArray; // Return the final result array
    }



}
