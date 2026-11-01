package LeetCodeJava.Math;

// https://leetcode.com/problems/time-needed-to-buy-tickets/description/

import java.util.*;

/**
 * 2073. Time Needed to Buy Tickets
 * Easy
 *
 * There are n people in a line queuing to buy tickets, where the 0th person is at the front of the line and the (n - 1)th person is at the back of the line.
 *
 * You are given a 0-indexed integer array tickets of length n where the number of tickets that the ith person would like to buy is tickets[i].
 *
 * Each person takes exactly 1 second to buy a ticket. A person can only buy 1 ticket at a time and has to go back to the end of the line (which happens instantaneously) in order to buy more tickets. If a person does not have any tickets left to buy, the person will leave the line.
 *
 * Return the time taken for the person at position k (0-indexed) to finish buying tickets.
 *
 *
 * Example 1:
 *
 * Input: tickets = [2,3,2], k = 2
 * Output: 6
 * Explanation:
 * - In the first pass, everyone in the line buys a ticket and the line becomes [1, 2, 1].
 * - In the second pass, everyone in the line buys a ticket and the line becomes [0, 1, 0].
 * The person at position 2 has successfully bought 2 tickets and it took 3 + 3 = 6 seconds.
 *
 * Example 2:
 *
 * Input: tickets = [5,1,1,1], k = 0
 * Output: 8
 * Explanation:
 * - In the first pass, everyone in the line buys a ticket and the line becomes [4, 0, 0, 0].
 * - In the next 4 passes, only the person in position 0 is buying tickets.
 * The person at position 0 has successfully bought 5 tickets and it took 4 + 4 = 8 seconds.
 *
 *
 * Constraints:
 *
 * n == tickets.length
 * 1 <= n <= 100
 * 1 <= tickets[i] <= 100
 * 0 <= k <= n - 1
 *
 */
public class TimeNeededToBuyTickets {

    // V0
    // IDEA: COUNT EACH PERSON'S CONTRIBUTION DIRECTLY (no simulation)
    //  people at or before k buy min(t, tickets[k]); people after k buy one round fewer.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int timeRequiredToBuy(int[] tickets, int k) {
        int target = tickets[k];
        int seconds = 0;
        for (int i = 0; i < tickets.length; i++) {
            if (i <= k) {
                // served before k in every round k still takes part in
                seconds += Math.min(tickets[i], target);
            } else {
                // served after k, so k is done before their target-th round
                seconds += Math.min(tickets[i], target - 1);
            }
        }
        return seconds;
    }

    // V1
    // IDEA: QUEUE SIMULATION (n, tickets[i] <= 100 -> at most 10^4 seconds)
    /**
     * time = O(N * max(tickets))
     * space = O(N)
     */
    public int timeRequiredToBuy_1(int[] tickets, int k) {
        // queue of person indices; remaining[i] = tickets person i still needs
        Deque<Integer> queue = new ArrayDeque<>();
        int[] remaining = tickets.clone();
        for (int i = 0; i < tickets.length; i++) {
            queue.offer(i);
        }

        int seconds = 0;
        while (!queue.isEmpty()) {
            int person = queue.poll();
            remaining[person]--;
            seconds++;
            if (remaining[person] == 0) {
                if (person == k) {
                    return seconds;
                }
            } else {
                // still needs tickets -> back to the end of the line
                queue.offer(person);
            }
        }
        return seconds;
    }
}
