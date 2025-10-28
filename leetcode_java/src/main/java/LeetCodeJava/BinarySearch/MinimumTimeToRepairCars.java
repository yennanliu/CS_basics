package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-time-to-repair-cars/description/

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * 2594. Minimum Time to Repair Cars
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer array ranks representing the ranks of some mechanics. ranksi is the rank of the ith mechanic. A mechanic with a rank r can repair n cars in r * n2 minutes.
 *
 * You are also given an integer cars representing the total number of cars waiting in the garage to be repaired.
 *
 * Return the minimum time taken to repair all the cars.
 *
 * Note: All the mechanics can repair the cars simultaneously.
 *
 *
 *
 * Example 1:
 *
 * Input: ranks = [4,2,3,1], cars = 10
 * Output: 16
 * Explanation:
 * - The first mechanic will repair two cars. The time required is 4 * 2 * 2 = 16 minutes.
 * - The second mechanic will repair two cars. The time required is 2 * 2 * 2 = 8 minutes.
 * - The third mechanic will repair two cars. The time required is 3 * 2 * 2 = 12 minutes.
 * - The fourth mechanic will repair four cars. The time required is 1 * 4 * 4 = 16 minutes.
 * It can be proved that the cars cannot be repaired in less than 16 minutes.​​​​​
 * Example 2:
 *
 * Input: ranks = [5,1,8], cars = 6
 * Output: 16
 * Explanation:
 * - The first mechanic will repair one car. The time required is 5 * 1 * 1 = 5 minutes.
 * - The second mechanic will repair four cars. The time required is 1 * 4 * 4 = 16 minutes.
 * - The third mechanic will repair one car. The time required is 8 * 1 * 1 = 8 minutes.
 * It can be proved that the cars cannot be repaired in less than 16 minutes.​​​​​
 *
 *
 * Constraints:
 *
 * 1 <= ranks.length <= 105
 * 1 <= ranks[i] <= 100
 * 1 <= cars <= 106
 *
 */
public class MinimumTimeToRepairCars {

    // V0
//    public long repairCars(int[] ranks, int cars) {
//
//    }

    // V1-1
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/minimum-time-to-repair-cars/editorial/
    public long repairCars_1_1(int[] ranks, int cars) {
        int minRank = ranks[0], maxRank = ranks[0];

        // Find min and max rank dynamically
        for (int rank : ranks) {
            minRank = Math.min(minRank, rank);
            maxRank = Math.max(maxRank, rank);
        }
        // Frequency array to count mechanics with each rank
        int[] freq = new int[maxRank + 1];
        for (int rank : ranks) {
            minRank = Math.min(minRank, rank);
            freq[rank]++;
        }

        // Minimum possible time, Maximum possible time
        long low = 1, high = 1L * minRank * cars * cars;

        // Perform binary search to find the minimum time required
        while (low < high) {
            long mid = (low + high) / 2;
            long carsRepaired = 0;

            // Calculate the total number of cars that can be repaired in 'mid' time
            for (int rank = 1; rank <= maxRank; rank++) {
                carsRepaired += freq[rank] * (long) Math.sqrt(mid / (long) rank);
            }

            // Adjust the search boundaries based on the number of cars repaired
            if (carsRepaired >= cars) {
                high = mid; // Try to find a smaller time
            } else {
                low = mid + 1; // Need more time
            }
        }

        return low;
    }

    // V1-2
    // IDEA:  Space Optimized Binary Search
    // https://leetcode.com/problems/minimum-time-to-repair-cars/editorial/
    public long repairCars_1_2(int[] ranks, int cars) {
        // The minimum possible time is 1,
        // and the maximum possible time is when the slowest mechanic (highest rank) repairs all cars.
        long low = 1, high = 1L * ranks[0] * cars * cars;

        // Perform binary search to find the minimum time required.
        while (low < high) {
            long mid = (low + high) / 2, carsRepaired = 0;

            // Calculate the number of cars that can be repaired in 'mid' time by all mechanics.
            for (int rank : ranks)
                carsRepaired += (long) (Math.sqrt(
                        (1.0 * mid) / rank));

            // If the total cars repaired is less than required, increase the time.
            if (carsRepaired < cars)
                low = mid + 1;
            else
                high = mid; // Otherwise, try a smaller time.
        }

        return low;
    }

    // V1-3
    // IDEA:  HEAP (PQ)
    // https://leetcode.com/problems/minimum-time-to-repair-cars/editorial/
    public long repairCars_1_3(int[] ranks, int cars) {
        // Count the frequency of each rank
        Map<Integer, Integer> count = new HashMap<>();
        for (int rank : ranks) {
            count.put(rank, count.getOrDefault(rank, 0) + 1);
        }

        // Create a Min-heap storing [time, rank, n, count]
        // - time: time for the next repair
        // - rank: mechanic's rank
        // - n: cars repaired by this mechanic
        // - count: number of mechanics with this rank
        // Initial time = rank (as rank * 1^2 = rank)
        PriorityQueue<long[]> minHeap = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));

        // Add initial entries to the heap
        for (int rank : count.keySet()) {
            // Elements: [time, rank, cars_repaired, mechanic_count]
            minHeap.offer(new long[] { rank, rank, 1, count.get(rank) });
        }

        long time = 0;
        // Process until all cars are repaired
        while (cars > 0) {
            // Pop the mechanic with the smallest current repair time
            long[] current = minHeap.poll();
            time = current[0];
            int rank = (int) current[1];
            long n = current[2];
            int mechCount = (int) current[3];

            // Deduct the number of cars repaired by this mechanic group
            cars -= mechCount;

            // Increment the number of cars repaired by this mechanic
            n += 1;

            // Push the updated repair time back into the heap
            // The new repair time is rank * n^2 (time increases quadratically with n)
            minHeap.offer(new long[] { rank * n * n, rank, n, mechCount });
        }

        return time;
    }


}
