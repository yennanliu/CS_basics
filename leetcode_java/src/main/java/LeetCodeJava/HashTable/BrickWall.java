package LeetCodeJava.HashTable;

import java.util.*;

// https://leetcode.com/problems/brick-wall/discuss/101728/I-DON'T-THINK-THERE-IS-A-BETTER-PERSON-THAN-ME-TO-ANSWER-THIS-QUESTION
// https://leetcode.ca/2017-06-06-554-Brick-Wall/

/**
 *  554. Brick Wall
 * Description
 * There is a rectangular brick wall in front of you with n rows of bricks. The ith row has some number of bricks each of the same height (i.e., one unit) but they can be of different widths. The total width of each row is the same.
 *
 * Draw a vertical line from the top to the bottom and cross the least bricks. If your line goes through the edge of a brick, then the brick is not considered as crossed. You cannot draw a line just along one of the two vertical edges of the wall, in which case the line will obviously cross no bricks.
 *
 * Given the 2D array wall that contains the information about the wall, return the minimum number of crossed bricks after drawing such a vertical line.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: wall = [[1,2,2,1],[3,1,2],[1,3,2],[2,4],[3,1,2],[1,3,1,1]]
 * Output: 2
 * Example 2:
 *
 * Input: wall = [[1],[1],[1]]
 * Output: 3
 *
 *
 * Constraints:
 *
 * n == wall.length
 * 1 <= n <= 104
 * 1 <= wall[i].length <= 104
 * 1 <= sum(wall[i].length) <= 2 * 104
 * sum(wall[i]) is the same for each row i.
 * 1 <= wall[i][j] <= 231 - 1
 *
 *
 */

public class BrickWall {

    // V0
    // IDEA: HASH TABLE + SORTING
    // TODO: fix
//    public int leastBricks(List<List<Integer>> wall) {
//
//        // edge
//        if (wall.isEmpty()){
//            return 0;
//        }
//
//        int res = 0;
//        Map<Integer, Integer> cnt = new HashMap<>();
//        // build counter map
//        // TODO: optimize double loop
//        for(List<Integer> w: wall){
//            int cumSum = 0;
//            for(Integer x: w){
//                cumSum += x;
//                int val = cnt.getOrDefault(x, 0);
//                cnt.put(x, val+1);
//            }
//        }
//
//        // sort on val
//        List<Integer> keys = new ArrayList<>(cnt.keySet());
//
//        // edge case : wall are all the same
//        if(keys.size() == 1){
//            return wall.size();
//        }
//
//        System.out.println(">>> (before) keys = " + keys);
//
//        Collections.sort(keys, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                // sort on val, decreasing order
//                return o2 - o1;
//            }
//        });
//
//        System.out.println(">>> (before) keys = " + keys);
//
//        return wall.size() - keys.get(0);
//    }

    // V0-1
    // IDEA: HASH TABLE + SORTING (fixed by gpt)
    /**
     * time = O(N*M)
     * space = O(N*M)
     */
    public int leastBricks_0_1(List<List<Integer>> wall) {
        // Edge case: if the wall is empty, return 0
        if (wall.isEmpty()) {
            return 0;
        }

        Map<Integer, Integer> cnt = new HashMap<>();

        // Loop through each row (wall) to build the counter map
        for (List<Integer> w : wall) {
            int cumSum = 0;
            // Loop through each brick in the current row
            for (int i = 0; i < w.size() - 1; i++) { // We don't count the last brick to avoid the edge of the wall
                cumSum += w.get(i);
                cnt.put(cumSum, cnt.getOrDefault(cumSum, 0) + 1);
            }
        }

        // If cnt is empty, it means there are no gaps, so all bricks are aligned perfectly, and we'd have to cross all rows.
        if (cnt.isEmpty()) {
            return wall.size();
        }

        // Find the maximum count of any position
        int maxCount = 0;
        for (int count : cnt.values()) {
            maxCount = Math.max(maxCount, count);
        }

        // The least number of bricks crossed will be the total number of rows minus the max number of rows that share a common edge
        return wall.size() - maxCount;
    }

    // V1
    // https://leetcode.ca/2017-06-06-554-Brick-Wall/
    /**
     * time = O(N*M)
     * space = O(N*M)
     */
    public int leastBricks_1(List<List<Integer>> wall) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (List<Integer> row : wall) {
            int width = 0;
            for (int i = 0, n = row.size() - 1; i < n; i++) {
                width += row.get(i);
                cnt.merge(width, 1, Integer::sum);
            }
        }
        int max = cnt.values().stream().max(Comparator.naturalOrder()).orElse(0);
        return wall.size() - max;
    }

    // V2

}
