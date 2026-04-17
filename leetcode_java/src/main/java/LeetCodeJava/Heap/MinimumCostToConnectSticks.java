package LeetCodeJava.Heap;

// https://leetcode.ca/all/1167.html
// https://leetcode.com/problems/minimum-cost-to-connect-sticks/

import java.util.PriorityQueue;

/**
 *  1167. Minimum Cost to Connect Sticks
 * You have some sticks with positive integer lengths.
 *
 * You can connect any two sticks of lengths X and Y into one stick by paying a cost of X + Y.  You perform this action until there is one stick remaining.
 *
 * Return the minimum cost of connecting all the given sticks into one stick in this way.
 *
 *
 *
 * Example 1:
 *
 * Input: sticks = [2,4,3]
 * Output: 14
 * Example 2:
 *
 * Input: sticks = [1,8,3,5]
 * Output: 30
 *
 *
 * Constraints:
 *
 * 1 <= sticks.length <= 10^4
 * 1 <= sticks[i] <= 10^4
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Google
 * Problem Solution
 * 1167-Minimum-Cost-to-Connect-Sticks
 * All Problems:
 * Link to All Problems
 *
 */
public class MinimumCostToConnectSticks {

    // V0
    // IDEA: PQ (gpt)
    // TODO: validate
    public int connectSticks(int[] sticks) {
        if (sticks == null || sticks.length <= 1) {
            return 0;
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int x : sticks) {
            pq.offer(x);
        }

        int cost = 0;

        while (pq.size() > 1) {
            int s1 = pq.poll();
            int s2 = pq.poll();
            int sum = s1 + s2;
            cost += sum;
            pq.offer(sum);
        }

        return cost;
    }


    // V0-1
    // IDEA: PQ (GEMINI)
    // TODO: validate
    public int connectSticks_0_1(int[] sticks) {
        // 1. Edge case: If there's only 1 or 0 sticks, no cost to connect
        if (sticks == null || sticks.length <= 1) {
            return 0;
        }

        // 2. Optimization: Default PriorityQueue is already a Min-Heap.
        // No need for a custom Comparator for natural ordering of Integers.
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int s : sticks) {
            pq.add(s);
        }

        int totalCost = 0;

        // 3. Greedy Strategy: Always pick the two smallest sticks
        while (pq.size() > 1) {
            int first = pq.poll();
            int second = pq.poll();

            int combined = first + second;
            totalCost += combined;

            // Put the new stick back to potentially be combined again
            pq.add(combined);
        }

        return totalCost;
    }


    // V1
    // https://leetcode.ca/2019-02-09-1167-Minimum-Cost-to-Connect-Sticks/
    public int connectSticks_1(int[] sticks) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : sticks) {
            pq.offer(x);
        }
        int ans = 0;
        while (pq.size() > 1) {
            int z = pq.poll() + pq.poll();
            ans += z;
            pq.offer(z);
        }
        return ans;
    }



    // V2




}
