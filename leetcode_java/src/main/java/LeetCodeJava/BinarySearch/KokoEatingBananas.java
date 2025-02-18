package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/koko-eating-bananas/
/**
 *
 * 875. Koko Eating Bananas
 * Solved
 * Medium
 * Topics
 * Companies
 * Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and will come back in h hours.
 *
 * Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any more bananas during this hour.
 *
 * Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.
 *
 * Return the minimum integer k such that she can eat all the bananas within h hours.
 *
 *
 *
 * Example 1:
 *
 * Input: piles = [3,6,7,11], h = 8
 * Output: 4
 * Example 2:
 *
 * Input: piles = [30,11,23,4,20], h = 5
 * Output: 30
 * Example 3:
 *
 * Input: piles = [30,11,23,4,20], h = 6
 * Output: 23
 *
 *
 * Constraints:
 *
 * 1 <= piles.length <= 104
 * piles.length <= h <= 109
 * 1 <= piles[i] <= 109
 *
 *
 */
import java.util.Arrays;

public class KokoEatingBananas {

    // V0
    // IDEA : BINARY SEARCH (close boundary)
    /**
     * Example Walkthrough:
     *
     * Let's say the piles are [3, 6, 7, 11] and h = 8.
     *
     * - Initially, l = 1 and r = 11 (largest pile).
     *
     * - After checking mid = 6, we find that it's
     *   valid to finish the piles in 8 hours, so we reduce r = mid - 1 = 5.
     *
     * - Next, mid = 3 results in needing more than 8 hours,
     *   so we increase l = mid + 1 = 4.
     *
     * - Eventually, when l = 4 and r = 4,
     *   we find that 4 is the minimum valid eating speed.
     *
     *
     * -> The binary search narrows down the possibilities, and at the end, l holds the smallest valid speed.
     *
     *
     *
     * In Summary:
     *
     * The binary search finds the smallest valid eating speed that satisfies the condition.
     * After the search completes, l holds the minimum eating speed, so we return l.
     *
     */
    public int minEatingSpeed(int[] piles, int h) {

        if (piles.length == 0 || piles.equals(null)){
            return 0;
        }

        int l = 1; //Arrays.stream(piles).min().getAsInt();
        int r = Arrays.stream(piles).max().getAsInt();

        while (r >= l){
            int mid = (l + r) / 2;
            int _hour = getCompleteTime_(piles, mid);
            /**
             *  Return the minimum integer k such that she can eat all the bananas within h hours.
             *
             *  -> NOTE !!! so any speed make hr <= h hours could work
             *
             *  -> NOTE !!! ONLY  when _hour <= h, we update r
             */
            if (_hour <= h){
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        /**
         *  NOTE !!!
         *
         *
         *  Why Return l?
         *
         *  - After the binary search, l will be the smallest speed
         *    that can finish all piles in h hours.
         *
         *
         *  - When r < l, it means the smallest valid eating speed is l. Here's why:
         *
         *      - When r becomes less than l, it means the search has narrowed down
         *        to the smallest valid speed, which is l.
         *
         *      - In the binary search, the left pointer l is adjusted when
         *        the current speed (mid) is found to be valid (i.e., we can complete
         *        the piles in h hours). So, by the end of the search,
         *        l will have the smallest possible speed that allows us to
         *        complete the piles within the given time h.
         *
         *
         *  Why Not Return r?
         *
         *    - Returning r would return the largest speed that was valid during the search,
         *      but we want the smallest speed that is still valid. In the binary search,
         *      when the loop terminates, l will be the smallest speed that fulfills the condition,
         *      and r will have passed below l.
         *
         *    - At the point where r < l, the smallest valid speed will be l, not r,
         *      because r will be one step behind l.
         *
         */

        return l;
    }


    private int getCompleteTime_(int[] piles, int speed) {

        int _hour = 0;
        for (int pile : piles) {
            _hour += Math.ceil((double) pile / speed);
        }

        return _hour;
    }

    // V0-1
    // IDEA : BINARY SEARCH (open boundary)
    public int minEatingSpeed_0_1(int[] piles, int h) {

        if (piles.length == 0 || piles.equals(null)){
            return 0;
        }

        // https://stackoverflow.com/questions/1484347/finding-the-max-min-value-in-an-array-of-primitives-using-java
        int l = 1; //Arrays.stream(piles).min().getAsInt();
        int r = Arrays.stream(piles).max().getAsInt();

        while (r > l){
            int mid = (l + r) / 2;
            int _hour = getCompleteTime(piles, mid);
            //System.out.println("l = " + l + " r = " + r + " mid = " + mid + " _hour = " + _hour);
            if (_hour <= h){
                r = mid;
            }else{
                l = mid + 1;
            }
         }

        return r;
    }

    private int getCompleteTime(int[] piles, int speed){

        int _hour  = 0;
        for (int pile : piles) {
            _hour += Math.ceil((double) pile / speed);
        }

        return _hour;
    }

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/koko-eating-bananas/editorial/
    public int minEatingSpeed_1(int[] piles, int h) {
        // Start at an eating speed of 1.
        int speed = 1;

        while (true) {
            // hourSpent stands for the total hour Koko spends with
            // the given eating speed.
            int hourSpent = 0;

            // Iterate over the piles and calculate hourSpent.
            // We increase the hourSpent by ceil(pile / speed)
            for (int pile : piles) {
                hourSpent += Math.ceil((double) pile / speed);
                if (hourSpent > h) {
                    break;
                }
            }

            // Check if Koko can finish all the piles within h hours,
            // If so, return speed. Otherwise, let speed increment by
            // 1 and repeat the previous iteration.
            if (hourSpent <= h) {
                return speed;
            } else {
                speed += 1;
            }
        }
    }

    // V2
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/koko-eating-bananas/editorial/
    public int minEatingSpeed_2(int[] piles, int h) {
        // Initialize the left and right boundaries
        int left = 1, right = 1;
        for (int pile : piles) {
            right = Math.max(right, pile);
        }

        while (left < right) {
            // Get the middle index between left and right boundary indexes.
            // hourSpent stands for the total hour Koko spends.
            int middle = (left + right) / 2;
            int hourSpent = 0;

            // Iterate over the piles and calculate hourSpent.
            // We increase the hourSpent by ceil(pile / middle)
            for (int pile : piles) {
                hourSpent += Math.ceil((double) pile / middle);
            }

            // Check if middle is a workable speed, and cut the search space by half.
            if (hourSpent <= h) {
                right = middle;
            } else {
                left = middle + 1;
            }
        }

        // Once the left and right boundaries coincide, we find the target value,
        // that is, the minimum workable eating speed.
        return right;
    }

}
