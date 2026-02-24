package LeetCodeJava.Array;

// https://leetcode.com/problems/car-pooling/description/
/**
 *
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
    // IDEA: PREFIX SUM (gemini)
    public boolean carPooling(int[][] trips, int capacity) {
        int globalEnd = 0;
        for (int i = 0; i < trips.length; i++) {
            globalEnd = Math.max(globalEnd, trips[i][2]);
        }

        /** NOTE !!!
         *
         *  set `globalEnd + 1` as prefix size,
         *  so can cover `end idx`
         */
        // 1. Size must be globalEnd + 1 to accommodate the 'end' index
        int[] prefix = new int[globalEnd + 1];

        // 2. Loop through the TRIPS, not the distance
        for (int i = 0; i < trips.length; i++) {
            int cap = trips[i][0];
            int start = trips[i][1];
            int end = trips[i][2];

            prefix[start] += cap;
            prefix[end] -= cap;
        }

        /** NOTE !!!
         *
         *  how we update prefix,
         *  and validate if it's capable
         *
         *  NOTE !!!
         *
         *  we DON'T need below:
         *
         *   ```
         *     //        for(int i = 0; i < prefix.length; i++){
         *     //            prefixSum += prefix[i];
         *     //            prefix[i] = prefixSum;
         *     //        }
         *   ```
         *
         */
        // 3. Update prefix sum and validate
        int prefixSum = 0;
        for (int i = 0; i < prefix.length; i++) {
            prefixSum += prefix[i];
            // We can check immediately to save time
            if (prefixSum > capacity) {
                return false;
            }
        }

        return true;
    }


    // V0-0-1
    // IDEA: PREFIX SUM + `prefix interval handling` (improve efficience)
    /**

     * time = O(N + M)

     * space = O(M)

     */
    public boolean carPooling_0_0_1(int[][] trips, int capacity) {
        // edge
        if (trips == null || trips.length == 0) {
            return true;
        }
        if (trips.length == 1) {
            return capacity > trips[0][0];
        }

        // init prefix sum
        int[] prefixSum = new int[1001]; // the biggest array size given by problem
        // `init pre prefix sum`
        for (int[] t : trips) {
            int amount = t[0];
            int start = t[1];
            int end = t[2];

            /**
             *  NOTE !!!!
             *
             *   via trick below, we can `efficiently` setup prefix sum
             *   per start, end index
             *
             *   -> we ADD amount at start point (customer pickup up)
             *   -> we MINUS amount at `end point` (customer drop off)
             *
             *   -> via above, we get the `adjusted` `init prefix sum`
             *   -> so all we need to do next is :
             *      -> loop over the `init prefix sum`
             *      -> and keep adding `previous to current val`
             *      -> e.g. prefixSum[i] = prefixSum[i-1] + prefixSum[i]
             *
             */
            prefixSum[start] += amount;
            prefixSum[end] -= amount;
        }

        // update `prefix sum` array
        for (int i = 1; i < prefixSum.length; i++) {
            prefixSum[i] += prefixSum[i - 1];
        }

        // check if it's `possible` to get all passenger
        for (int j : prefixSum) {
            if (capacity < j) {
                return false;
            }
        }

        return true;
    }

    // V0-0-2
    // IDEA: PREFIX SUM (fixed by gpt)
    public boolean carPooling_0_0_2(int[][] trips, int capacity) {
        // Edge cases
        if (trips == null || trips.length == 0)
            return true;

        // 1️⃣ Find the furthest drop-off location (end point)
        int maxEnd = 0;
        for (int[] trip : trips) {
            maxEnd = Math.max(maxEnd, trip[2]);
        }

        // 2️⃣ Initialize difference array
        int[] diff = new int[maxEnd + 1]; // +1 to cover endpoint

        // 3️⃣ Apply each trip as a range update
        for (int[] trip : trips) {
            int passengers = trip[0];
            int start = trip[1];
            int end = trip[2];
            diff[start] += passengers; // pick up passengers
            /** NOTE !!!
             *
             *  since `DROP OFF` at `end` index
             *  -> so we should do the `minus op` at `end` index (instead of `end + 1`)
             */
            if (end < diff.length) {
                diff[end] -= passengers; // drop them off before `end`
            }
        }

        // 4️⃣ Compute prefix sum (active passengers)
        int currPassengers = 0;
        for (int i = 0; i < diff.length; i++) {
            /** NOTE !!!
             *
             *  we do the accumulated sum via currPassengers += diff[i]
             */
            currPassengers += diff[i];
            /** NOTE !!!
             *
             *  we check if is a validate trip via `currPassengers > capacity`
             */
            if (currPassengers > capacity) {
                return false; // 🚫 exceeded capacity at some point
            }
        }

        return true; // ✅ all trips fit within capacity
    }


    // V0-1
    // IDEA: PREFIX SUM
    // time: O(N + M), space: O(M)
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
    /**

     * time = O(N + M)

     * space = O(M)

     */
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
    // https://youtu.be/08sn_w4LWEE?feature=shared
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F1094-car-pooling.java
    /**

     * time = O(N + M)

     * space = O(M)

     */
    public boolean carPooling_1(int[][] trips, int capacity) {
        int[] passChange = new int[1001];
        for (int[] t : trips) {
            passChange[t[1]] += t[0];
            passChange[t[2]] -= t[0];
        }
        int curPass = 0;
        for (int i = 0; i < 1001; i++) {
            curPass += passChange[i];
            if (curPass > capacity) {
                return false;
            }
        }
        return true;
    }



    // V2
    // https://leetcode.com/problems/car-pooling/solutions/1669644/well-explained-2-waysjava-cpythonjavascr-djso/
    /**

     * time = O(N log N)

     * space = O(N)

     */
    public boolean carPooling_2(int[][] trips, int capacity) {
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

    // V3
    // https://leetcode.com/problems/car-pooling/solutions/1670309/cjavapython-donot-sort-on-95-faster-imag-da8q/
    /**

     * time = O(N log N)

     * space = O(N)

     */
    public boolean carPooling_3(int[][] trips, int capacity) {
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
