package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-time-to-build-blocks/description/

import java.util.*;

/**
 * 1199. Minimum Time to Build Blocks
 * Hard
 *
 * You are given a list of blocks, where blocks[i] = t means that the i-th block
 * needs t units of time to be built. A block can only be built by exactly one
 * worker.
 *
 * A worker can either split into two workers (number of workers increases by one)
 * or build a block then go home. Both decisions cost some time.
 *
 * The time cost of spliting one worker into two workers is given as an integer
 * split. Note that if two workers split at the same time, they split in parallel
 * so the cost would be split.
 *
 * Output the minimum time needed to build all blocks.
 *
 * Initially, there is only one worker.
 *
 *
 * Example 1:
 *
 * Input: blocks = [1], split = 1
 * Output: 1
 * Explanation: We use 1 worker to build 1 block in 1 time unit.
 *
 * Example 2:
 *
 * Input: blocks = [1,2], split = 5
 * Output: 7
 * Explanation: We split the worker into 2 workers in 5 time units then assign each
 * of them to a block so the cost is 5 + max(1, 2) = 7.
 *
 * Example 3:
 *
 * Input: blocks = [1,2,3], split = 1
 * Output: 4
 * Explanation: Split 1 worker into 2, then assign the first worker to the last
 * block and split the second worker into 2.
 * Then, use the two unassigned workers to build the first two blocks.
 * The cost is 1 + max(3, 1 + max(1, 2)) = 4.
 *
 *
 * Constraints:
 *
 * 1 <= blocks.length <= 1000
 * 1 <= blocks[i] <= 10^5
 * 1 <= split <= 100
 *
 */
public class MinimumTimeToBuildBlocks {

    // V0
    // IDEA: GREEDY + MIN HEAP (reverse Huffman: repeatedly merge the two cheapest blocks)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minBuildTime(int[] blocks, int split) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int block : blocks) {
            minHeap.add(block);
        }

        // think backwards: merging two blocks = "one worker split, then built both".
        // the merged block costs split + max(a, b), and the deepest splits should
        // hold the cheapest blocks, so always merge the two smallest
        while (minHeap.size() > 1) {
            // the smallest one is built in parallel with the second, so only the larger counts
            minHeap.poll();
            int larger = minHeap.poll();
            minHeap.add(larger + split);
        }
        return minHeap.peek();
    }

    // V1
    // IDEA: BINARY SEARCH ON THE ANSWER + GREEDY CHECK (largest blocks need the fewest splits)
    /**
     * time = O(N log N + N log(maxTime))
     * space = O(N)
     */
    public int minBuildTime_1(int[] blocks, int split) {
        int[] sorted = blocks.clone();
        Arrays.sort(sorted);

        int low = 0;
        int high = sorted[sorted.length - 1] + split * sorted.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (canFinish(sorted, split, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /** Can all blocks be built within `limit`, walking split levels from the top? */
    private boolean canFinish(int[] sorted, int split, int limit) {
        // workers available at the current time; take the largest blocks first
        long workers = 1;
        int time = 0;
        int index = sorted.length - 1;
        while (index >= 0) {
            if (workers <= 0 || time + sorted[index] > limit) {
                return false;
            }
            // every block that still fits after one more split can wait; the rest
            // must be built now by the current workers
            int mustBuildNow = 0;
            int k = index;
            while (k >= 0 && time + split + sorted[k] > limit) {
                mustBuildNow++;
                k--;
            }
            if (mustBuildNow > workers) {
                return false;
            }
            workers -= mustBuildNow;
            index = k;
            if (index < 0) {
                return true;
            }
            // remaining workers all split in parallel
            workers *= 2;
            time += split;
            // more workers than blocks left: no need to track beyond that
            if (workers > sorted.length) {
                workers = sorted.length;
            }
        }
        return true;
    }
}
