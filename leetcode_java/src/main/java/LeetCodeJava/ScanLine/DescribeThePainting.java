package LeetCodeJava.ScanLine;

// https://leetcode.com/problems/describe-the-painting/description/

import java.util.*;

/**
 *  1943. Describe the Painting
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There is a long and thin painting that can be represented by a number line. The painting was painted with multiple overlapping segments where each segment was painted with a unique color. You are given a 2D integer array segments, where segments[i] = [starti, endi, colori] represents the half-closed segment [starti, endi) with colori as the color.
 *
 * The colors in the overlapping segments of the painting were mixed when it was painted. When two or more colors mix, they form a new color that can be represented as a set of mixed colors.
 *
 * For example, if colors 2, 4, and 6 are mixed, then the resulting mixed color is {2,4,6}.
 * For the sake of simplicity, you should only output the sum of the elements in the set rather than the full set.
 *
 * You want to describe the painting with the minimum number of non-overlapping half-closed segments of these mixed colors. These segments can be represented by the 2D array painting where painting[j] = [leftj, rightj, mixj] describes a half-closed segment [leftj, rightj) with the mixed color sum of mixj.
 *
 * For example, the painting created with segments = [[1,4,5],[1,7,7]] can be described by painting = [[1,4,12],[4,7,7]] because:
 * [1,4) is colored {5,7} (with a sum of 12) from both the first and second segments.
 * [4,7) is colored {7} from only the second segment.
 * Return the 2D array painting describing the finished painting (excluding any parts that are not painted). You may return the segments in any order.
 *
 * A half-closed segment [a, b) is the section of the number line between points a and b including point a and not including point b.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: segments = [[1,4,5],[4,7,7],[1,7,9]]
 * Output: [[1,4,14],[4,7,16]]
 * Explanation: The painting can be described as follows:
 * - [1,4) is colored {5,9} (with a sum of 14) from the first and third segments.
 * - [4,7) is colored {7,9} (with a sum of 16) from the second and third segments.
 * Example 2:
 *
 *
 * Input: segments = [[1,7,9],[6,8,15],[8,10,7]]
 * Output: [[1,6,9],[6,7,24],[7,8,15],[8,10,7]]
 * Explanation: The painting can be described as follows:
 * - [1,6) is colored 9 from the first segment.
 * - [6,7) is colored {9,15} (with a sum of 24) from the first and second segments.
 * - [7,8) is colored 15 from the second segment.
 * - [8,10) is colored 7 from the third segment.
 * Example 3:
 *
 *
 * Input: segments = [[1,4,5],[1,4,7],[4,7,1],[4,7,11]]
 * Output: [[1,4,12],[4,7,12]]
 * Explanation: The painting can be described as follows:
 * - [1,4) is colored {5,7} (with a sum of 12) from the first and second segments.
 * - [4,7) is colored {1,11} (with a sum of 12) from the third and fourth segments.
 * Note that returning a single segment [1,7) is incorrect because the mixed color sets are different.
 *
 *
 * Constraints:
 *
 * 1 <= segments.length <= 2 * 104
 * segments[i].length == 3
 * 1 <= starti < endi <= 105
 * 1 <= colori <= 109
 * Each colori is distinct.
 *
 *
 */
public class DescribeThePainting {

    // V0
//    public List<List<Long>> splitPainting(int[][] segments) {
//
//    }

    // V1-1
    // IDEA: SCAN LINE (gpt)
    public List<List<Long>> splitPainting_1_1(int[][] segments) {
        List<int[]> events = new ArrayList<>();
        for (int[] seg : segments) {
            int start = seg[0], end = seg[1], color = seg[2];
            events.add(new int[] { start, color }); // start adds color
            events.add(new int[] { end, -color }); // end subtracts color
        }

        // Sort by position, start events before end if same position
        Collections.sort(events, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        List<List<Long>> res = new ArrayList<>();
        long curColor = 0;
        int prevPos = -1;

        for (int[] e : events) {
            int pos = e[0];
            int delta = e[1];

            // Record interval if there was color
            if (prevPos != -1 && prevPos != pos && curColor > 0) {
                res.add(Arrays.asList((long) prevPos, (long) pos, curColor));
            }

            curColor += delta;
            prevPos = pos;
        }

        return res;
    }


    // V1-2
    // IDEA: SCAN LINE (gemini)
    public List<List<Long>> splitPainting_1_2(int[][] segments) {
        // 1. Use TreeMap to store coordinate -> color change
        // TreeMap keeps coordinates sorted automatically
        Map<Integer, Long> diff = new TreeMap<>();

        for (int[] s : segments) {
            int start = s[0];
            int end = s[1];
            long color = s[2];

            // Add color at start, subtract at end
            diff.put(start, diff.getOrDefault(start, 0L) + color);
            diff.put(end, diff.getOrDefault(end, 0L) - color);
        }

        List<List<Long>> res = new ArrayList<>();
        long currentSum = 0;
        Integer prevCoordinate = null;

        // 2. Iterate through the sorted coordinates
        for (Map.Entry<Integer, Long> entry : diff.entrySet()) {
            int coordinate = entry.getKey();
            long colorChange = entry.getValue();

            // 3. If there was a previous coordinate and a non-zero color sum,
            // it means the interval [prev, current] has a consistent color.
            if (prevCoordinate != null && currentSum > 0) {
                res.add(Arrays.asList((long) prevCoordinate, (long) coordinate, currentSum));
            }

            // Update the running sum of colors
            currentSum += colorChange;
            prevCoordinate = coordinate;
        }

        return res;
    }


    // V2
    // https://leetcode.com/problems/describe-the-painting/solutions/1359720/line-sweep-by-votrubac-otky/
    public List<List<Long>> splitPainting_2(int[][] segments) {
        long mix[] = new long[100002], sum = 0, last_i = 0;
        boolean ends[] = new boolean[100002];
        List<List<Long>> res = new ArrayList<>();
        for (int[] s : segments) {
            mix[s[0]] += s[2];
            mix[s[1]] -= s[2];
            ends[s[0]] = ends[s[1]] = true;
        }
        for (int i = 1; i < 100002; ++i) {
            if (ends[i] && sum > 0)
                res.add(Arrays.asList(last_i, (long) i, sum));
            last_i = ends[i] ? i : last_i;
            sum += mix[i];
        }
        return res;
    }


    // V3
    // https://leetcode.com/problems/describe-the-painting/solutions/7686324/simple-java-solution-easy-to-understand-0j2av/
    public List<List<Long>> splitPainting_3(int[][] segments) {
        long[] x = new long[100001];
        long max = 0;
        List<List<Long>> res = new ArrayList<>();
        boolean[] vis = new boolean[100001];

        for (int i = 0; i < segments.length; i++) {
            max = Math.max(max, (long) segments[i][1]);
            x[segments[i][0]] += (long) segments[i][2];
            x[segments[i][1]] -= (long) segments[i][2];
            vis[segments[i][1]] = true;
        }
        long sum = 0;
        //long start=0;
        List<Long> list = new ArrayList<>();

        for (int i = 1; i <= max; i++) {
            if (x[i] == 0 && vis[i] == false)
                continue;
            if (list.size() == 0) {
                list = new ArrayList<>();
                list.add((long) i);
                sum += x[i];
                continue;
            }

            list.add((long) i);
            list.add(sum);
            res.add(list);
            sum += x[i];
            list = new ArrayList<>();
            if (sum != 0) {
                list = new ArrayList<>();
                list.add((long) i);
            }
        }
        //if(list.size()==1)
        return res;

    }


    // V4
    // IDEA: TREE MAP + SCAN LINE
    // https://leetcode.com/problems/describe-the-painting/solutions/1359819/java-easiest-solution-onlogn-same-as-car-znpy/
    public List<List<Long>> splitPainting_4(int[][] segments) {
        TreeMap<Integer, Long> map = new TreeMap<>();

        for (int segment[] : segments) {
            map.put(segment[0], map.getOrDefault(segment[0], 0L) + segment[2]);
            map.put(segment[1], map.getOrDefault(segment[1], 0L) - segment[2]);
        }

        List<List<Long>> result = new ArrayList<>();

        int prev = 0;
        long sum = 0;

        for (int key : map.keySet()) {
            if (sum != 0) { // Ignore the unpainted interval
                result.add(Arrays.asList((long) prev, (long) key, sum)); // Add the interval
            }

            sum += map.get(key);
            prev = key;
        }

        return result;
    }






}
