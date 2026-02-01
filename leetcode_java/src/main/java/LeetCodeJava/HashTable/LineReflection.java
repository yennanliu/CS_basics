package LeetCodeJava.HashTable;

// https://leetcode.ca/all/356.html
// https://leetcode.com/problems/line-reflection/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *356. Line Reflection
 * Given n points on a 2D plane, find if there is such a line parallel to y-axis that reflect the given points.
 *
 * Example 1:
 *
 * Input: [[1,1],[-1,1]]
 * Output: true
 * Example 2:
 *
 * Input: [[1,1],[-1,-1]]
 * Output: false
 * Difficulty:
 * Medium
 */
public class LineReflection {

    // V0
//    public boolean isReflected(int[][] points) {
//    }

    // V0-1
    // TODO: validate & fix
//    public boolean isReflected(int[][] points) {
//        // edge
//        if(points == null || points.length == 0){
//            return true; // ??
//        }
//        if(points.length == 1){
//            return false;
//        }
//
//        HashSet<int[]> left_set = new HashSet<>();
//        HashSet<int[]> right_set = new HashSet<>();
//        for(int[] p: points){
//            if(p[0] > 0){
//                right_set.add(p);
//            }else{
//                left_set.add(p);
//            }
//        }
//
//        if(right_set.size() != left_set.size()){
//            return false;
//        }
//
//        for(int[] x: left_set){
//            int[] reflect_cell = new int[]{-1 * x[0], x[1]};
//            if(!right_set.contains(reflect_cell)){
//                return false;
//            }
//        }
//
//        return true;
//    }

    // V0-2
    // IDEA: HASHSET (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isReflected_0_2(int[][] points) {
        if (points == null || points.length == 0) return true;

        Set<String> pointSet = new HashSet<>();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;

        // Build set and find min/max x
        for (int[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);
            pointSet.add(p[0] + "#" + p[1]); // Use string as key
        }

        // NOTE !!! the `mirror` may NOT exists at `x=0` line
        double mirror = (minX + maxX) / 2.0;

        // Check reflection
        for (int[] p : points) {
            double reflectedX = 2 * mirror - p[0];
            String reflectedPoint = (int)reflectedX + "#" + p[1];
            if (!pointSet.contains(reflectedPoint)) {
                return false;
            }
        }

        return true;
    }

    // V1
    // https://leetcode.ca/2016-11-20-356-Line-Reflection/
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isReflected_1(int[][] points) {
        final int inf = 1 << 30;
        int minX = inf, maxX = -inf;
        Set<List<Integer>> pointSet = new HashSet<>();
        for (int[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);

            //pointSet.add(List.of(p[0], p[1]));
            List<Integer> tmp = new ArrayList<>();
            tmp.add(p[0]);
            tmp.add(p[1]);
            pointSet.add(tmp);
        }
        int s = minX + maxX;
        for (int[] p : points) {
            // TODO: check if below are equals
//            if (!pointSet.contains(List.of(s - p[0], p[1]))) {
//                return false;
//            }
            if (!pointSet.contains(new int[]{s - p[0], p[1]})) {
                return false;
            }
        }
        return true;
    }

    // V2

}
