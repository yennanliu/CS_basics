package LeetCodeJava.Array;

// https://leetcode.com/problems/car-pooling/description/
/**
 *
 Code
 Testcase
 1094. Car Pooling
 Medium
 Topics
 Companies
 Hint
 There is a car with capacity empty seats. The vehicle only drives east (i.e., it cannot turn around and drive west).

 You are given the integer capacity and an array trips where trips[i] = [numPassengersi, fromi, toi] indicates that the ith trip has numPassengersi passengers and the locations to pick them up and drop them off are fromi and toi respectively. The locations are given as the number of kilometers due east from the car's initial location.

 Return true if it is possible to pick up and drop off all passengers for all the given trips, or false otherwise.



 Example 1:

 Input: trips = [[2,1,5],[3,3,7]], capacity = 4
 Output: false
 Example 2:

 Input: trips = [[2,1,5],[3,3,7]], capacity = 5
 Output: true


 Constraints:

 1 <= trips.length <= 1000
 trips[i].length == 3
 1 <= numPassengersi <= 100
 0 <= fromi < toi <= 1000
 1 <= capacity <= 105

 */
public class CarPooling {

    // V0
//    public boolean carPooling(int[][] trips, int capacity) {
//
//    }

    // V0-1
    // IDEA: PREFIX SUM
    /**
     *  NOTE !!!
     *
     *   via
     *     - lengthOfTrip[trip[1]] += trip[0];
     *     - lengthOfTrip[trip[2]] -= trip[0];
     *
     *    and
     *
     *      - carLoad += lengthOfTrip[i];
     *
     *
     *  -> we can SIMULATE the `pickup` and `dropOff` scenario
     *
     */
    public boolean carPooling_0_1(int[][] trips, int capacity) {
        // Because from and to is between 0 and 1000. Idea is to store counts in an array of size 1001.
        int lengthOfTrip[] = new int[1001];
        for (int trip[] : trips){
            /**
             *
             *  For each trip:
             *
             *  - We increment the value at lengthOfTrip[trip[1]] by trip[0].
             *    This indicates that the given number of passengers (from trip[0])
             *    board the vehicle at the pick-up location (trip[1]).
             *
             *
             *  - We decrement the value at lengthOfTrip[trip[2]] by trip[0].
             *    This indicates that the same number of passengers (from trip[0])
             *    leave the vehicle at the drop-off location (trip[2]).
             *
             */
            lengthOfTrip[trip[1]] += trip[0]; // Increment when passenger is on board
            lengthOfTrip[trip[2]] -= trip[0]; // decrement when they depart
        }
        // Count total passenger for each bus top
        int carLoad = 0;
        // we have the count array, we perform a line sweep from 0 to 1000 and track its total
        for (int i = 0; i < 1001; i++){
            carLoad += lengthOfTrip[i];
            // Reject when the car is overloaded somewhere
            if(carLoad > capacity) return false;
        }

        return true; // Accept only if all trip is safe
    }

    // V0-2
    // IDEA: PREFIX SUM (gpt)
    public boolean carPooling_0_2(int[][] trips, int capacity) {
        // Edge case: if there are no trips, the vehicle is never needed
        if (trips == null || trips.length == 0) {
            return true;
        }

        // Find the maximum drop-off location to limit the size of the arrays
        int maxLocation = 0;
        for (int[] trip : trips) {
            maxLocation = Math.max(maxLocation, trip[2]);
        }

        // Create a "timeline" to track passengers being picked up and dropped off
        int[] passengersAtLocation = new int[maxLocation + 1];

        // Populate the pick-up and drop-off changes
        for (int[] trip : trips) {
            int numPassengers = trip[0];
            int pickUpLocation = trip[1];
            int dropOffLocation = trip[2];

            // Add passengers at the pick-up location
            passengersAtLocation[pickUpLocation] += numPassengers;

            // Subtract passengers at the drop-off location
            passengersAtLocation[dropOffLocation] -= numPassengers;
        }

        // Now, check if at any point the number of passengers exceeds the capacity
        int currentPassengers = 0;
        for (int passengers : passengersAtLocation) {
            currentPassengers += passengers;
            if (currentPassengers > capacity) {
                return false; // Capacity exceeded at some point
            }
        }

        // If we never exceed capacity, return true
        return true;
    }

    // V1
    // https://leetcode.com/problems/car-pooling/solutions/1669644/well-explained-2-waysjava-cpythonjavascr-djso/
    public boolean carPooling_1(int[][] trips, int capacity) {
        // Because from and to is between 0 and 1000. Idea is to store counts in an array of size 1001.
        int lengthOfTrip[] = new int[1001];
        for (int trip[] : trips){
            /**
             *
             *  For each trip:
             *
             *  - We increment the value at lengthOfTrip[trip[1]] by trip[0].
             *    This indicates that the given number of passengers (from trip[0])
             *    board the vehicle at the pick-up location (trip[1]).
             *
             *
             *  - We decrement the value at lengthOfTrip[trip[2]] by trip[0].
             *    This indicates that the same number of passengers (from trip[0])
             *    leave the vehicle at the drop-off location (trip[2]).
             *
             */
            lengthOfTrip[trip[1]] += trip[0]; // Increment when passenger is on board
            lengthOfTrip[trip[2]] -= trip[0]; // decrement when they depart
        }
        // Count total passenger for each bus top
        int carLoad = 0;
        // we have the count array, we perform a line sweep from 0 to 1000 and track its total
        for (int i = 0; i < 1001; i++){
            /**
             *  Sweeping Through the Stops:
             *
             *
             *  -  After processing all trips, we now have the "net change in passengers"
             *     at each stop (location).
             *
             *  - We loop through the lengthOfTrip[] array from index 0 to 1000 to
             *    simulate the journey and track the total number of passengers in
             *    the vehicle at each stop.
             *
             *  - We maintain a variable carLoad which starts at 0 and keeps a running
             *    total of passengers in the vehicle at each stop.
             *
             *  - At each location i, we add lengthOfTrip[i] to carLoad to account for
             *    the passengers boarding or leaving at that stop.
             *
             *  - If at any point carLoad exceeds the vehicle's capacity,
             *    it means the car is overloaded, so we immediately return false.
             *
             *  - If we successfully go through all stops without exceeding the capacity,
             *    we return true.
             *
             */
            carLoad += lengthOfTrip[i];
            // Reject when the car is overloaded somewhere
            if(carLoad > capacity) return false;
        }

        return true; // Accept only if all trip is safe
    }

    // V2
    // https://leetcode.com/problems/car-pooling/solutions/1670309/cjavapython-donot-sort-on-95-faster-imag-da8q/
    public boolean carPooling(int[][] trips, int capacity) {
        int in_car = 0;
        int[] increase = new int[1001];
        for (int i = 0; i < trips.length; i++) { // find out the number of the **net increase** passengers for each stop
            increase[trips[i][1]] += trips[i][0];
            increase[trips[i][2]] -= trips[i][0];
        }
        for (int i = 0; i <= 1000; i++) { // from start to end, for each stop we calculate the number of passengers in
            // the car.
            in_car += increase[i];
            if (in_car > capacity)
                return false; // once the number of passengers in the car exceeds the capacity
        }
        return true;
    }

}
