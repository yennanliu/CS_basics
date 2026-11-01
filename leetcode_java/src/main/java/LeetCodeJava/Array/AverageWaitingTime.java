package LeetCodeJava.Array;

// https://leetcode.com/problems/average-waiting-time/description/
/**
 * 1701. Average Waiting Time
 * Medium
 *
 * There is a restaurant with a single chef. You are given an array customers, where
 * customers[i] = [arrivali, timei]:
 *
 * - arrivali is the arrival time of the ith customer. The arrival times are sorted in
 *   non-decreasing order.
 * - timei is the time needed to prepare the order of the ith customer.
 *
 * When a customer arrives, he gives the chef his order, and the chef starts preparing it once he is
 * idle. The customer waits till the chef finishes preparing his order. The chef does not prepare
 * food for more than one customer at a time. The chef prepares food for customers in the order they
 * were given in the input.
 *
 * Return the average waiting time of all customers. Solutions within 10^-5 from the actual answer
 * are considered accepted.
 *
 *
 * Example 1:
 *
 * Input: customers = [[1,2],[2,5],[4,3]]
 * Output: 5.00000
 * Explanation: waits are 3 - 1 = 2, 8 - 2 = 6 and 11 - 4 = 7, so the average is (2 + 6 + 7) / 3 = 5.
 *
 * Example 2:
 *
 * Input: customers = [[5,2],[5,4],[10,3],[20,1]]
 * Output: 3.25000
 * Explanation: waits are 2, 6, 4 and 1, so the average is (2 + 6 + 4 + 1) / 4 = 3.25.
 *
 *
 * Constraints:
 *
 * 1 <= customers.length <= 10^5
 * 1 <= arrivali, timei <= 10^4
 * arrivali <= arrivali+1
 *
 */
public class AverageWaitingTime {

    // V0
    // IDEA: SINGLE-SERVER FIFO SIMULATION (track when the chef becomes free)
    // the chef starts customer i at max(chefFreeAt, arrival) and finishes cookTime later;
    // that customer's wait is finish - arrival. Sum as a long, divide once at the end.
    /**
     * time = O(N)
     * space = O(1)
     */
    public double averageWaitingTime(int[][] customers) {
        long chefFreeAt = 0;
        long totalWait = 0;
        for (int[] customer : customers) {
            int arrival = customer[0];
            int cookTime = customer[1];
            // an idle chef waits for the customer; a busy one makes the customer wait
            if (chefFreeAt < arrival) {
                chefFreeAt = arrival;
            }
            chefFreeAt += cookTime;
            totalWait += chefFreeAt - arrival;
        }
        return (double) totalWait / customers.length;
    }
}
