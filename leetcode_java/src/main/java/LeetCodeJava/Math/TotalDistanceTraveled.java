package LeetCodeJava.Math;

// https://leetcode.com/problems/total-distance-traveled/description/

/**
 * 2739. Total Distance Traveled
 * Easy
 *
 * A truck has two fuel tanks. You are given two integers, mainTank representing the fuel present in the main tank in liters and additionalTank representing the fuel present in the additional tank in liters.
 *
 * The truck has a mileage of 10 km per liter. Whenever 5 liters of fuel get used up in the main tank, if the additional tank has at least 1 liters of fuel, 1 liters of fuel will be transferred from the additional tank to the main tank.
 *
 * Return the maximum distance which can be traveled.
 *
 * Note: Injection from the additional tank is not continuous. It happens suddenly and immediately for every 5 liters consumed.
 *
 *
 * Example 1:
 *
 * Input: mainTank = 5, additionalTank = 10
 * Output: 60
 *
 * Example 2:
 *
 * Input: mainTank = 1, additionalTank = 2
 * Output: 10
 *
 *
 * Constraints:
 *
 * 1 <= mainTank, additionalTank <= 100
 *
 */
public class TotalDistanceTraveled {

    private static final int KM_PER_LITER = 10;
    private static final int LITERS_PER_TRANSFER = 5;

    // V0
    // IDEA: SIMULATION -- burn the main tank in blocks of 5; each full block pulls 1 liter from the additional tank
    /**
     * time = O(mainTank + additionalTank)
     * space = O(1)
     */
    public int distanceTraveled(int mainTank, int additionalTank) {
        int litersUsed = 0;
        while (mainTank >= LITERS_PER_TRANSFER && additionalTank > 0) {
            // burn one block of 5, then get 1 liter back into the main tank
            litersUsed += LITERS_PER_TRANSFER;
            mainTank -= LITERS_PER_TRANSFER;
            mainTank += 1;
            additionalTank -= 1;
        }
        // whatever is left (or no more transfers available) is burned plainly
        litersUsed += mainTank;
        return litersUsed * KM_PER_LITER;
    }
}
