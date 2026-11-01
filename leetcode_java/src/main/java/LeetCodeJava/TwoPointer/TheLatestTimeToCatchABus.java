package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/the-latest-time-to-catch-a-bus/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 2332. The Latest Time to Catch a Bus
 * Medium
 *
 * You are given a 0-indexed integer array buses of length n, where buses[i] represents the departure time of the ith bus. You are also given a 0-indexed integer array passengers of length m, where passengers[j] represents the arrival time of the jth passenger. All bus departure times are unique. All passenger arrival times are unique.
 *
 * You are given an integer capacity, which represents the maximum number of passengers that can get on each bus.
 *
 * When a passenger arrives, they will wait in line for the next available bus. You can get on a bus that departs at x minutes if you arrive at y minutes where y <= x, and the bus is not full. Passengers with the earliest arrival times get on the bus first.
 *
 * More formally when a bus arrives, either:
 *
 * If capacity or fewer passengers are waiting for a bus, they will all get on the bus, or
 * The capacity passengers with the earliest arrival times will get on the bus.
 *
 * Return the latest time you may arrive at the bus station to catch a bus. You cannot arrive at the same time as another passenger.
 *
 * Note: The arrays buses and passengers are not necessarily sorted.
 *
 *
 * Example 1:
 *
 * Input: buses = [10,20], passengers = [2,17,18,19], capacity = 2
 * Output: 16
 * Explanation: Suppose you arrive at time 16.
 * At time 10, the first bus departs with the 0th passenger.
 * At time 20, the second bus departs with you and the 1st passenger.
 * Note that you may not arrive at the same time as another passenger, which is why you must arrive before the 1st passenger to catch the bus.
 *
 * Example 2:
 *
 * Input: buses = [20,30,10], passengers = [19,13,26,4,25,11,21], capacity = 2
 * Output: 20
 * Explanation: Suppose you arrive at time 20.
 * At time 10, the first bus departs with the 3rd passenger.
 * At time 20, the second bus departs with the 5th and 1st passengers.
 * At time 30, the third bus departs with the 0th passenger and you.
 * Notice if you had arrived any later, then the 6th passenger would have taken your seat on the third bus.
 *
 *
 * Constraints:
 *
 * n == buses.length
 * m == passengers.length
 * 1 <= n, m <= 10^5
 * 2 <= buses[i], passengers[i] <= 10^9
 * Each element in buses is unique.
 * Each element in passengers is unique.
 *
 */
public class TheLatestTimeToCatchABus {

    // V0
    // IDEA: SIMULATE THE BOARDING WITH TWO POINTERS, THEN WALK BACK FROM THE BEST SEAT
    //  sort both arrays; each bus takes up to `capacity` waiting passengers who arrived no
    //  later than its departure. after the LAST bus:
    //    - it still had a free seat -> try arriving exactly at its departure time
    //    - it left full            -> try arriving 1 before the last passenger who boarded it
    //  arrival times must be unique, so step back while the time is already taken.
    /**
     * time = O(N log N + M log M)
     * space = O(M)
     */
    public int latestTimeCatchTheBus(int[] buses, int[] passengers, int capacity) {
        int[] busTimes = buses.clone();
        int[] arrivals = passengers.clone();
        Arrays.sort(busTimes);
        Arrays.sort(arrivals);

        Set<Integer> taken = new HashSet<>();
        for (int time : arrivals) {
            taken.add(time);
        }

        // next passenger still waiting
        int next = 0;
        // free seats left on the bus processed last
        int freeSeats = 0;
        for (int depart : busTimes) {
            freeSeats = capacity;
            while (next < arrivals.length && freeSeats > 0 && arrivals[next] <= depart) {
                next++;
                freeSeats--;
            }
        }

        int best;
        if (freeSeats > 0) {
            // room left on the last bus: arrive exactly when it departs
            best = busTimes[busTimes.length - 1];
        } else {
            // the last bus left full: cut in just ahead of its last boarder
            best = arrivals[next - 1] - 1;
        }

        // the arrival must not coincide with another passenger's
        while (taken.contains(best)) {
            best--;
        }
        return best;
    }
}
