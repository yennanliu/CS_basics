package LeetCodeJava.Heap;

// https://leetcode.com/problems/time-to-cross-a-bridge/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 2532. Time to Cross a Bridge
 * Hard
 *
 * There are k workers who want to move n boxes from the right (old) warehouse to the left (new) warehouse. You are given the two integers n and k, and a 2D integer array time of size k x 4 where time[i] = [right_i, pick_i, left_i, put_i].
 *
 * The warehouses are separated by a river and connected by a bridge. Initially, all k workers are waiting on the left side of the bridge. To move the boxes, the ith worker can do the following:
 *
 * Cross the bridge to the right side in right_i minutes.
 * Pick a box from the right warehouse in pick_i minutes.
 * Cross the bridge to the left side in left_i minutes.
 * Put the box into the left warehouse in put_i minutes.
 *
 * The ith worker is less efficient than the jth worker if either condition is met:
 *
 * left_i + right_i > left_j + right_j
 * left_i + right_i == left_j + right_j and i > j
 *
 * The following rules regulate the movement of the workers through the bridge:
 *
 * Only one worker can use the bridge at a time.
 * When the bridge is unused prioritize the least efficient worker (who have picked up the box) on the right side to cross. If not, prioritize the least efficient worker on the left side to cross.
 * If enough workers have already been dispatched from the left side to pick up all the remaining boxes, no more workers will be sent from the left side.
 *
 * Return the elapsed minutes at which the last box reaches the left side of the bridge.
 *
 *
 * Example 1:
 *
 * Input: n = 1, k = 3, time = [[1,1,2,1],[1,1,3,1],[1,1,4,1]]
 * Output: 6
 * Explanation:
 * From 0 to 1 minutes: worker 2 crosses the bridge to the right.
 * From 1 to 2 minutes: worker 2 picks up a box from the right warehouse.
 * From 2 to 6 minutes: worker 2 crosses the bridge to the left.
 * From 6 to 7 minutes: worker 2 puts a box at the left warehouse.
 * The whole process ends after 7 minutes. We return 6 because the problem asks for the instance of time at which the last worker reaches the left side of the bridge.
 *
 * Example 2:
 *
 * Input: n = 3, k = 2, time = [[1,5,1,8],[10,10,10,10]]
 * Output: 37
 * Explanation:
 * The last box reaches the left side at 37 seconds. Notice, how we do not put the last boxes down, as that would take more time, and they are already on the left with the workers.
 *
 *
 * Constraints:
 *
 * 1 <= n, k <= 10^4
 * time.length == k
 * time[i].length == 4
 * 1 <= left_i, pick_i, right_i, put_i <= 1000
 *
 */
public class TimeToCrossABridge {

    // V0
    // IDEA: EVENT SIMULATION WITH 4 HEAPS
    //       rank the workers by (right + left) ascending, original index breaking ties;
    //       then a LARGER rank is a LESS efficient worker, so "least efficient first"
    //       is a max-heap on the rank.
    //         waitLeft  : max-heap of ranks idle on the left bank
    //         waitRight : max-heap of ranks on the right bank holding a box
    //         workLeft  : min-heap of (time the put finishes, rank)  -> joins waitLeft
    //         workRight : min-heap of (time the pick finishes, rank) -> joins waitRight
    //       each step: release everyone whose work is done by `now`, give the bridge to
    //       the right bank first, otherwise send someone right while boxes remain
    //       undispatched; if nobody can move, jump `now` to the next finish time.
    //       the answer is when the LAST box arrives on the left (before its put).
    /**
     * time = O((N + K) log K)
     * space = O(K)
     */
    public int findCrossingTime(int n, int k, int[][] time) {
        // order[r] = original worker id with efficiency rank r
        Integer[] order = new Integer[k];
        for (int i = 0; i < k; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> {
            int costA = time[a][0] + time[a][2];
            int costB = time[b][0] + time[b][2];
            if (costA != costB) {
                return Integer.compare(costA, costB);
            }
            return Integer.compare(a, b);
        });
        // times[r] = [right, pick, left, put] of the worker with rank r
        int[][] times = new int[k][];
        for (int r = 0; r < k; r++) {
            times[r] = time[order[r]];
        }

        PriorityQueue<Integer> waitLeft = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        PriorityQueue<Integer> waitRight = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
        PriorityQueue<int[]> workLeft = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        PriorityQueue<int[]> workRight = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        for (int r = 0; r < k; r++) {
            waitLeft.add(r);
        }

        int boxesToDispatch = n;
        int now = 0;
        while (true) {
            // workers who finished putting / picking by now start waiting at the bridge
            while (!workLeft.isEmpty() && workLeft.peek()[0] <= now) {
                waitLeft.add(workLeft.poll()[1]);
            }
            while (!workRight.isEmpty() && workRight.peek()[0] <= now) {
                waitRight.add(workRight.poll()[1]);
            }

            boolean canBringBoxBack = !waitRight.isEmpty();
            boolean canSendRight = boxesToDispatch > 0 && !waitLeft.isEmpty();

            if (!canBringBoxBack && !canSendRight) {
                // bridge idle: jump to the next moment someone finishes their work
                int nextEvent = Integer.MAX_VALUE;
                if (!workLeft.isEmpty()) {
                    nextEvent = Math.min(nextEvent, workLeft.peek()[0]);
                }
                if (!workRight.isEmpty()) {
                    nextEvent = Math.min(nextEvent, workRight.peek()[0]);
                }
                now = nextEvent;
                continue;
            }

            if (canBringBoxBack) {
                // the right bank has priority on the bridge
                int rank = waitRight.poll();
                now += times[rank][2];
                // nobody else will ever carry a box: this was the last one
                if (boxesToDispatch == 0 && waitRight.isEmpty() && workRight.isEmpty()) {
                    return now;
                }
                workLeft.add(new int[] {now + times[rank][3], rank});
            } else {
                // send a worker right; a box is counted as taken once someone is dispatched for it
                int rank = waitLeft.poll();
                now += times[rank][0];
                boxesToDispatch--;
                workRight.add(new int[] {now + times[rank][1], rank});
            }
        }
    }
}
