package LeetCodeJava.Stack;

import java.util.*;

// https://leetcode.com/problems/car-fleet/
/**
 * 853. Car Fleet
 * Solved
 * Medium
 * Topics
 * Companies
 * There are n cars at given miles away from the starting mile 0, traveling to reach the mile target.
 *
 * You are given two integer array position and speed, both of length n, where position[i] is the starting mile of the ith car and speed[i] is the speed of the ith car in miles per hour.
 *
 * A car cannot pass another car, but it can catch up and then travel next to it at the speed of the slower car.
 *
 * A car fleet is a car or cars driving next to each other. The speed of the car fleet is the minimum speed of any car in the fleet.
 *
 * If a car catches up to a car fleet at the mile target, it will still be considered as part of the car fleet.
 *
 * Return the number of car fleets that will arrive at the destination.
 *
 *
 *
 * Example 1:
 *
 * Input: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
 *
 * Output: 3
 *
 * Explanation:
 *
 * The cars starting at 10 (speed 2) and 8 (speed 4) become a fleet, meeting each other at 12. The fleet forms at target.
 * The car starting at 0 (speed 1) does not catch up to any other car, so it is a fleet by itself.
 * The cars starting at 5 (speed 1) and 3 (speed 3) become a fleet, meeting each other at 6. The fleet moves at speed 1 until it reaches target.
 * Example 2:
 *
 * Input: target = 10, position = [3], speed = [3]
 *
 * Output: 1
 *
 * Explanation:
 *
 * There is only one car, hence there is only one fleet.
 * Example 3:
 *
 * Input: target = 100, position = [0,2,4], speed = [4,2,1]
 *
 * Output: 1
 *
 * Explanation:
 *
 * The cars starting at 0 (speed 4) and 2 (speed 2) become a fleet, meeting each other at 4. The car starting at 4 (speed 1) travels to 5.
 * Then, the fleet at 4 (speed 2) and the car at position 5 (speed 1) become one fleet, meeting each other at 6. The fleet moves at speed 1 until it reaches target.
 *
 *
 * Constraints:
 *
 * n == position.length == speed.length
 * 1 <= n <= 105
 * 0 < target <= 106
 * 0 <= position[i] < target
 * All the values of position are unique.
 * 0 < speed[i] <= 106
 *
 *
 *
 */

public class CarFleet {

    // V0
    // IDEA: pair position and speed, sorting (gpt)
    /**
     * IDEA :
     *
     * The approach involves sorting the cars by their starting positions
     * (from farthest to nearest to the target)
     * and computing their time to reach the target.
     * We then iterate through these times to count the number of distinct fleets.
     *
     *
     *
     * Steps in the Code:
     * 	1.	Pair Cars with Their Speeds:
     * 	    •	Combine position and speed into a 2D array cars for easier sorting and access.
     * 	2.	Sort Cars by Position Descending:
     * 	    •	Use Arrays.sort with a custom comparator to sort cars from farthest to nearest relative to the target.
     * 	3.	Calculate Arrival Times:
     * 	    •	Compute the time each car takes to reach the target using the formula:
     *
     *  time = (target - position) / speed
     *
     * 	4.	Count Fleets:
     * 	    •	Iterate through the times array:
     * 	    •	If the current car’s arrival time is greater than the lastTime (time of the last fleet), it forms a new fleet.
     * 	    •	Update lastTime to the current car’s time.
     * 	5.	Return Fleet Count:
     * 	    •	The number of distinct times that exceed lastTime corresponds to the number of fleets.
     *
     */
    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;
        // Pair positions with speeds and `sort by position in descending order`
        // cars : [position][speed]
        int[][] cars = new int[n][2];
        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }

        /**
         * NOTE !!!
         *
         *  Sort by position descending (simulate the "car arriving" process
         */
        Arrays.sort(cars, (a, b) -> b[0] - a[0]); // Sort by position descending

        // Calculate arrival times
        double[] times = new double[n];
        for (int i = 0; i < n; i++) {
            times[i] = (double) (target - cars[i][0]) / cars[i][1];
        }

        // Count fleets
        int fleets = 0;
        double lastTime = 0;
        for (double time : times) {
            /**
             * 	4.	Count Fleets:
             * 	•	Iterate through the times array:
             * 	•	If the current car’s arrival time is greater than the lastTime (time of the last fleet), it forms a new fleet.
             * 	•	Update lastTime to the current car’s time.
             */
            // If current car's time is greater than the last fleet's time, it forms a new fleet
            if (time > lastTime) {
                fleets++;
                lastTime = time;
            }
        }

        return fleets;
    }

    // V0-1
    // IDEA : HASH MAP + Array sorting
    /**
     *  Step 1) calculate all arriving time for all cars and save as map
     *  Step 2) sort distance array
     *      - Sorting ensures that cars are processed in the order they reach the target, making it easier to determine fleet formation.
     *  Step 3) compare previous arrive time and cur arrive time, if can't form a "fleet" then append to fleet
     *  Step 4) return fleet size
     *
     */
    public int carFleet_0_1(int target, int[] position, int[] speed) {

        int[] dist = new int[position.length];
        double[] arrived_time = new double[position.length];
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < position.length; i++){
            int _position = position[i];
            int _speed = speed[i];
            dist[i] = target - _position;
            arrived_time[i] = (double) dist[i] / (double) speed[i];
            map.put(target - _position, (double) dist[i] / (double) speed[i]);
        }
        Arrays.sort(dist);
        List<Double> fleet = new ArrayList<Double>();
        for (int j = 0; j < dist.length; j++){
            int _size = fleet.size();
            double _time = map.get(dist[j]);
            if (_size == 0 || _time > fleet.get(_size-1)){
                fleet.add(_time);
            }
        }

        return fleet.size();
    }

    // V1
    // https://leetcode.com/problems/car-fleet/solutions/2013259/java-simple-solution-100-faster/
    public int carFleet_2(int target, int[] position, int[] speed) {

        int res = 0;
        double[] timeArr = new double[target];
        for (int i = 0; i < position.length; i++)
        {
            timeArr[position[i]]= (double)(target - position[i]) / speed[i];
        }
        double prev = 0.0;
        for (int i = target-1; i >=0 ; i--)
        {
            double cur = timeArr[i];
            if (cur > prev)
            {
                prev = cur;
                res++;
            }
        }
        return res;

    }

    // V2
    // https://leetcode.com/problems/car-fleet/solutions/411641/java-easy-understand-fast-solution/
    // IDEA :
    // calculate distance left for each car
    // calculate time needed for each car
    // if a car has greater distance left than another car & its time needed is smaller than another car, it wil chase up and become the same fleet
    public int carFleet_3(int target, int[] position, int[] speed) {
        int n = position.length;
        int[] dist = new int[n];
        double[] timeAr = new double[n];
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            dist[i] = target - position[i]; //calculate distance left for each car
            timeAr[i] = (double) dist[i] / (double) speed[i]; //calculate original time needed for each car
            map.put(dist[i], timeAr[i]); //map distance left with original time needed
        }

        Arrays.sort(dist); //the cars with larger distance left & smaller time needed will chase up the car before
        List<Double> rTimeL = new ArrayList<Double>(); //store time used for each fleet in ascending order
        for (int i = 0; i < n; i++) {
            Double time = map.get(dist[i]);
            int m = rTimeL.size();
            if (m == 0 || time > rTimeL.get(m - 1)) {
                rTimeL.add(time); //new fleet is created
            }
        }
        return rTimeL.size();
    }

    // V3
    // https://leetcode.com/problems/car-fleet/solutions/3224677/fast-100-16ms-space-efficient-99-511-mb-and-compact/
    public int carFleet_4(int target, int[] position, int[] speed) {
        float arraytime[] = new float[target+1],max=0;
        for(int i=0;i<position.length;i++){
            arraytime[position[i]] = (float)(target-position[i])/speed[i];
        }
        int count=0;
        for(int i=target;i>=0;i--){
            if(arraytime[i]>max){
                count++;
                max=arraytime[i];
            }
        }
        return count;
    }

}
