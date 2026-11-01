package LeetCodeJava.Math;

// https://leetcode.com/problems/count-number-of-trapezoids-ii/description/

import java.util.*;

/**
 * 3625. Count Number of Trapezoids II
 * Hard
 *
 * You are given a 2D integer array points where points[i] = [xi, yi] represents
 * the coordinates of the ith point on the Cartesian plane.
 *
 * Return the number of unique trapezoids that can be formed by choosing any four
 * distinct points from points.
 *
 * A trapezoid is a convex quadrilateral with at least one pair of parallel sides.
 * Two lines are parallel if and only if they have the same slope.
 *
 *
 * Example 1:
 *
 * Input: points = [[-3,2],[3,0],[2,3],[3,2],[2,-3]]
 * Output: 2
 * Explanation:
 * There are two distinct ways to pick four points that form a trapezoid:
 * The points [-3,2], [2,3], [3,2], [2,-3] form one trapezoid.
 * The points [2,3], [3,2], [3,0], [2,-3] form another trapezoid.
 *
 * Example 2:
 *
 * Input: points = [[0,0],[1,0],[0,1],[2,1]]
 * Output: 1
 * Explanation:
 * There is only one trapezoid which can be formed.
 *
 *
 * Constraints:
 *
 * 4 <= points.length <= 500
 * -1000 <= xi, yi <= 1000
 * All points are pairwise distinct.
 *
 */
public class CountNumberOfTrapezoidsII {

    // V0
    // IDEA: PAIR UP PARALLEL SEGMENTS ON DIFFERENT LINES, THEN SUBTRACT PARALLELOGRAMS ONCE
    //       two parallel segments on distinct lines give 4 distinct points in convex position,
    //       i.e. exactly one trapezoid with those segments as its parallel sides; conversely every
    //       trapezoid comes from a pair of its parallel sides.
    //       a quadrilateral with TWO pairs of parallel sides (a parallelogram) is generated twice,
    //       so subtract the parallelograms once. a parallelogram = two segments sharing a midpoint
    //       (diagonals bisect each other) with different directions (same direction = collinear).
    //       everything is exact integers: direction reduced by gcd with a canonical sign,
    //       line id = dy*x - dx*y, midpoint = (x1 + x2, y1 + y2).
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int countTrapezoids(int[][] points) {
        int n = points.length;

        // byDirection: direction -> (line id -> number of segments on that line)
        Map<Long, Map<Long, Integer>> byDirection = new HashMap<>();
        // byMidpoint: midpoint -> (direction -> number of segments with that midpoint)
        Map<Long, Map<Long, Integer>> byMidpoint = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int x1 = points[i][0];
            int y1 = points[i][1];
            for (int j = 0; j < i; j++) {
                int x2 = points[j][0];
                int y2 = points[j][1];

                // reduce the direction vector and give it a canonical sign
                int dx = x2 - x1;
                int dy = y2 - y1;
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx /= g;
                dy /= g;
                if (dx < 0 || (dx == 0 && dy < 0)) {
                    dx = -dx;
                    dy = -dy;
                }
                long direction = encode(dx, dy);

                // points on the same line with this direction share dy*x - dx*y
                long lineId = (long) dy * x1 - (long) dx * y1;
                Map<Long, Integer> lines = byDirection.computeIfAbsent(direction, key -> new HashMap<>());
                lines.put(lineId, lines.getOrDefault(lineId, 0) + 1);

                long midpoint = encode(x1 + x2, y1 + y2);
                Map<Long, Integer> directions = byMidpoint.computeIfAbsent(midpoint, key -> new HashMap<>());
                directions.put(direction, directions.getOrDefault(direction, 0) + 1);
            }
        }

        long answer = 0;
        // same direction, different line -> one trapezoid per pair of segments
        for (Map<Long, Integer> lines : byDirection.values()) {
            answer += countCrossPairs(lines.values());
        }
        // same midpoint, different direction -> a parallelogram, which was counted twice
        for (Map<Long, Integer> directions : byMidpoint.values()) {
            answer -= countCrossPairs(directions.values());
        }
        return (int) answer;
    }

    /** Number of pairs taken from two DIFFERENT groups, given the group sizes. */
    private long countCrossPairs(Collection<Integer> groupSizes) {
        long pairs = 0;
        long seen = 0;
        for (int size : groupSizes) {
            pairs += seen * size;
            seen += size;
        }
        return pairs;
    }

    /** Packs a pair of ints into one long key (unique while |b| <= 2,000,000). */
    private long encode(int a, int b) {
        return (long) a * 4_000_003L + b;
    }

    /** Euclid's algorithm on non-negative ints. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
