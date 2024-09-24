package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1011. Capacity To Ship Packages Within D Days
 * <p>
 * A conveyor belt has packages that must be shipped from one port to another within days days.
 * <p>
 * The ith package on the conveyor belt has a weight of weights[i]. Each day, we load the ship with packages on the conveyor belt (in the order given by weights). We may not load more weight than the maximum weight capacity of the ship.
 * <p>
 * Return the least weight capacity of the ship that will result in all the packages on the conveyor belt being shipped within days days.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5
 * Output: 15
 * Explanation: A ship capacity of 15 is the minimum to ship all the packages in 5 days like this:
 * 1st day: 1, 2, 3, 4, 5
 * 2nd day: 6, 7
 * 3rd day: 8
 * 4th day: 9
 * 5th day: 10
 * <p>
 * Note that the cargo must be shipped in the order given, so using a ship of capacity 14 and splitting the packages into parts like (2, 3, 4, 5), (1, 6, 7), (8), (9), (10) is not allowed.
 * Example 2:
 * <p>
 * Input: weights = [3,2,2,4,1,4], days = 3
 * Output: 6
 * Explanation: A ship capacity of 6 is the minimum to ship all the packages in 3 days like this:
 * 1st day: 3, 2
 * 2nd day: 2, 4
 * 3rd day: 1, 4
 * Example 3:
 * <p>
 * Input: weights = [1,2,3,1,1], days = 4
 * Output: 3
 * Explanation:
 * 1st day: 1
 * 2nd day: 2
 * 3rd day: 3
 * 4th day: 1, 1
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= days <= weights.length <= 5 * 104
 * 1 <= weights[i] <= 500
 */
public class CapacityToShipPackagesWithinDDays {

    // V0
    // IDEA : BINARY SEARCH
    public int shipWithinDays(int[] weights, int days) {

        if (weights.length==1){
            return weights[0] / days;
        }

        List<Integer> weightsList = new ArrayList<>();
        /**
         *  NOTE !!!
         *
         *   we get mex weight, and total weight from weights array
         */
        int maxWeight = 0;
        int totalWeight = 0;
        for (int w : weights){
            weightsList.add(w);
            maxWeight = Math.max(maxWeight, w);
            totalWeight += w;
        }

        /**
         *  NOTE !!!
         *
         *   we use mex weight, and total weight as
         *   left, and right pointer of binary search
         *
         *   (not mex,  smallest weight or anything else)
         *   (since we want to get the least weight capacity within D days)
         */
        int left = maxWeight;
        int right = totalWeight;
        // binary search
        while(right > left){
            int mid = (left + right) / 2;
            int calculatedDays = getDays(weightsList, mid);
            System.out.println(">>> mid = " + mid  + ", maxWeight = " + maxWeight + ", totalWeight = " + totalWeight);
            // need to return max possible speed within D days
            if (calculatedDays <= days){
                right = mid;
            }else{
                left = mid+1;
            }
        }

        return left; // ??? or return mid
    }

    private int getDays(List<Integer> weightsList, int speed){
        int cur = 0;
        int days = 0;
        for (Integer w :weightsList){
            if (cur + w <= speed){
                cur += w;
            }else{
                days += 1;
                cur = w;
            }
        }
        if (cur > 0){
            days += 1;
        }
        System.out.println(">>> speed = " + speed + ", days = " + days);
        return days;
    }

    // V0
    // IDEA : BINARY SEARCH (modified by GPT)
    public int shipWithinDays_0_1(int[] weights, int days) {
        int maxWeight = 0;
        int totalWeight = 0;

        // Find the maximum single weight and the total weight
        for (int weight : weights) {
            maxWeight = Math.max(maxWeight, weight);
            totalWeight += weight;
        }

        // Binary search between maxWeight (minimum capacity) and totalWeight (maximum capacity)
        int left = maxWeight;
        int right = totalWeight;

        while (left < right) {

            int mid = left + (right - left) / 2;

            int calculatedDays = getShipDays(mid, weights);

            /** NOTE !!!!
             *
             *  we CAN'T return result directly if calculatedDays == days,
             *  since what this problem wants is : minimum capacity that can move all goods
             *  so, instead of return directly, we need to KEEP FINDING smaller possible capacity
             */
//            if (calculatedDays==days){
//                return mid;
//            }

            if (calculatedDays <= days) {
                // NOTE !!! If we can ship within 'days', try for a smaller capacity
                right = mid;
            } else {
                // If it takes more than 'days', we need a larger capacity
                left = mid + 1;
            }
        }

        // can return right as well
        return left; // Left and right will converge to the minimum valid capacity
    }

    private int getShipDays(int capacity, int[] weights) {
        int curSum = 0;
        int days = 1;  // Start with 1 day

        for (int weight : weights) {
            if (curSum + weight > capacity) {
                // If adding the current weight exceeds capacity, ship on a new day
                days++;
                curSum = 0;
            }
            curSum += weight;
        }

        return days;
    }

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/solutions/3216547/day-53-binary-search-easiest-beginner-friendly-sol/
    public int shipWithinDays_1(int[] weights, int days) {
        int maxWeight = -1, totalWeight = 0;
        for (int weight : weights) {
            maxWeight = Math.max(maxWeight, weight);
            totalWeight += weight;
        }
        int left = maxWeight, right = totalWeight;
        while (left < right) {
            int mid = (left + right) / 2;
            int daysNeeded = 1, currWeight = 0;
            for (int weight : weights) {
                if (currWeight + weight > mid) {
                    daysNeeded++;
                    currWeight = 0;
                }
                currWeight += weight;
            }
            if (daysNeeded > days) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // V2
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/solutions/256756/java-simple-java-solution-with-explanation/
    public int shipWithinDays_2(int[] weights, int D) {
        /*
         * Find the least possible capacity of ship. It will be maximum of -> the
         * largest item or the weight on one ship if the weight is evenly distributed on
         * all the ships i.e. (sum_of_all_items)/(total_ships)
         */
        int heaviestItem = weights[0];
        int weightSum = 0;
        for (int x : weights) {
            heaviestItem = Math.max(heaviestItem, x);
            weightSum += x;
        }
        int avgWeightOnShip = (int) weightSum / D;
        // Minimum required weight capacity of a ship
        int minWeight = Math.max(heaviestItem, avgWeightOnShip);

        // Start from minimum possible size to maximum possible
        for (int i = minWeight; i <= weights.length * minWeight; i++) {
            int[] ship = new int[D];
            int index = 0;
            // Fill all the ships
            for (int j = 0; j < ship.length; j++) {
                // Try to fit as many items as possible
                while (index < weights.length && ship[j] + weights[index] < i) {
                    ship[j] += weights[index];
                    index++;
                }
            }
            if (index == weights.length)
                return i - 1;
        }
        return 0;
    }

    // V3
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/solutions/3216643/clean-codes-full-explanation-binary-search-c-java-python3/
    public int shipWithinDays_3(int[] weights, int days) {
        int l = Arrays.stream(weights).max().getAsInt();
        int r = Arrays.stream(weights).sum();

        while (l < r) {
            final int m = (l + r) / 2;
            if (shipDays(weights, m) <= days)
                r = m;
            else
                l = m + 1;
        }
        return l;
    }

    private int shipDays(int[] weights, int shipCapacity) {
        int days = 1;
        int capacity = 0;
        for (final int weight : weights) {
            if (capacity + weight > shipCapacity) {
                ++days;
                capacity = weight;
            } else
                capacity += weight;
        }
        return days;
    }

}
