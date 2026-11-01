package LeetCodeJava.HashTable;

// https://leetcode.com/problems/threshold-majority-queries/description/

import java.util.*;

/**
 * 3636. Threshold Majority Queries
 * Hard
 *
 * You are given an integer array nums of length n and an array queries, where
 * queries[i] = [li, ri, thresholdi].
 *
 * Return an array of integers ans where ans[i] is equal to the element in the
 * subarray nums[li...ri] that appears at least thresholdi times, selecting the
 * element with the highest frequency (choosing the smallest in case of a tie),
 * or -1 if no such element exists.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2,2,1,1], queries = [[0,5,4],[0,3,3],[2,3,2]]
 * Output: [1,-1,2]
 * Explanation:
 *
 * Query       Sub-array          Threshold  Frequency table    Answer
 * [0, 5, 4]   [1,1,2,2,1,1]      4          1 -> 4, 2 -> 2     1
 * [0, 3, 3]   [1,1,2,2]          3          1 -> 2, 2 -> 2     -1
 * [2, 3, 2]   [2,2]              2          2 -> 2             2
 *
 * Example 2:
 *
 * Input: nums = [3,2,3,2,3,2,3], queries = [[0,6,4],[1,5,2],[2,4,1],[3,3,1]]
 * Output: [3,2,3,2]
 * Explanation:
 *
 * Query       Sub-array          Threshold  Frequency table    Answer
 * [0, 6, 4]   [3,2,3,2,3,2,3]    4          3 -> 4, 2 -> 3     3
 * [1, 5, 2]   [2,3,2,3,2]        2          2 -> 3, 3 -> 2     2
 * [2, 4, 1]   [3,2,3]            1          3 -> 2, 2 -> 1     3
 * [3, 3, 1]   [2]                1          2 -> 1             2
 *
 *
 * Constraints:
 *
 * 1 <= nums.length == n <= 10^4
 * 1 <= nums[i] <= 10^9
 * 1 <= queries.length <= 5 * 10^4
 * queries[i] = [li, ri, thresholdi]
 * 0 <= li <= ri < n
 * 1 <= thresholdi <= ri - li + 1
 *
 */
public class ThresholdMajorityQueries {

    private static final int BLOCK = 24;

    // V0
    // IDEA: RANGE MODE BY BLOCK PRECOMPUTATION + A CANDIDATE SCAN OF THE RAGGED EDGES
    //   without the threshold this is a range MODE query (most frequent value in
    //   nums[l..r], smallest on ties), then compare its count with the threshold.
    //   mode does not merge across halves, so a segment tree is out.
    //   split [l, r] into whole blocks in the middle plus two ragged edges. a value
    //   whose count in [l, r] beats the middle's mode must occur in an edge (a value
    //   living only in the middle was already measured there). so the candidates
    //   are the middle mode plus the O(BLOCK) edge values; each candidate's exact
    //   count is two binary searches in its sorted list of positions.
    //   the mode of every block range [s..e] is precomputed by fixing s and
    //   sweeping right with a frequency table. short ranges are counted directly.
    /**
     * time = O(N^2 / B + Q * B * log N)
     * space = O((N / B)^2 + N)
     */
    public int[] subarrayMajority(int[] nums, int[][] queries) {
        int n = nums.length;

        // compress values to ids 0..m-1; ids keep the value order, so "smallest
        // value on a tie" is "smallest id on a tie"
        int[] sortedValues = distinctSorted(nums);
        int m = sortedValues.length;
        int[] ids = new int[n];
        for (int i = 0; i < n; i++) {
            ids[i] = Arrays.binarySearch(sortedValues, nums[i]);
        }

        // positions[v] = the indices where id v occurs, ascending
        List<List<Integer>> positions = new ArrayList<>();
        for (int v = 0; v < m; v++) {
            positions.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            positions.get(ids[i]).add(i);
        }

        // modeCount[s][e] / modeId[s][e] = mode of the blocks s..e inclusive
        int blockCount = (n + BLOCK - 1) / BLOCK;
        int[][] modeCount = new int[blockCount][blockCount];
        int[][] modeId = new int[blockCount][blockCount];
        for (int startBlock = 0; startBlock < blockCount; startBlock++) {
            int[] frequency = new int[m];
            int bestCount = 0;
            int bestId = 0;
            int index = startBlock * BLOCK;
            for (int endBlock = startBlock; endBlock < blockCount; endBlock++) {
                int stop = Math.min((endBlock + 1) * BLOCK, n);
                // extend the range by one more block, keeping the running mode
                while (index < stop) {
                    int v = ids[index];
                    frequency[v]++;
                    if (frequency[v] > bestCount || (frequency[v] == bestCount && v < bestId)) {
                        bestCount = frequency[v];
                        bestId = v;
                    }
                    index++;
                }
                modeCount[startBlock][endBlock] = bestCount;
                modeId[startBlock][endBlock] = bestId;
            }
        }

        int[] answer = new int[queries.length];
        for (int qi = 0; qi < queries.length; qi++) {
            int left = queries[qi][0];
            int right = queries[qi][1];
            int threshold = queries[qi][2];
            int leftBlock = left / BLOCK;
            int rightBlock = right / BLOCK;

            int bestCount = 0;
            int bestId = 0;
            if (rightBlock - leftBlock <= 1) {
                // no whole block in between: the range is short, count it directly
                Map<Integer, Integer> frequency = new HashMap<>();
                for (int i = left; i <= right; i++) {
                    int v = ids[i];
                    int c = frequency.getOrDefault(v, 0) + 1;
                    frequency.put(v, c);
                    if (c > bestCount || (c == bestCount && v < bestId)) {
                        bestCount = c;
                        bestId = v;
                    }
                }
            } else {
                // start from the precomputed mode of the whole middle blocks
                bestCount = modeCount[leftBlock + 1][rightBlock - 1];
                bestId = modeId[leftBlock + 1][rightBlock - 1];

                // every value on the two ragged edges is a candidate
                Set<Integer> checked = new HashSet<>();
                int leftEdgeEnd = (leftBlock + 1) * BLOCK;   // exclusive
                int rightEdgeStart = rightBlock * BLOCK;
                int[][] edges = {{left, leftEdgeEnd - 1}, {rightEdgeStart, right}};
                for (int[] edge : edges) {
                    for (int i = edge[0]; i <= edge[1]; i++) {
                        int v = ids[i];
                        if (!checked.add(v)) {
                            continue;
                        }
                        // exact count of v inside [left, right]
                        List<Integer> where = positions.get(v);
                        int c = upperBound(where, right) - lowerBound(where, left);
                        if (c > bestCount || (c == bestCount && v < bestId)) {
                            bestCount = c;
                            bestId = v;
                        }
                    }
                }
            }

            if (bestCount >= threshold) {
                answer[qi] = sortedValues[bestId];
            } else {
                answer[qi] = -1;
            }
        }
        return answer;
    }

    /** The distinct values of nums in ascending order. */
    private int[] distinctSorted(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int size = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[size] = sorted[i];
                size++;
            }
        }
        return Arrays.copyOf(sorted, size);
    }

    /** First index in list whose value is >= target. */
    private int lowerBound(List<Integer> list, int target) {
        int lo = 0;
        int hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /** First index in list whose value is > target. */
    private int upperBound(List<Integer> list, int target) {
        int lo = 0;
        int hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}
