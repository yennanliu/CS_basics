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
//    public int connectSticks(int[] sticks) {
//    }


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
