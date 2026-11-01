package LeetCodeJava.Array;

// https://leetcode.com/problems/count-houses-in-a-circular-street-ii/description/

/**
 * 2753. Count Houses in a Circular Street II
 * Hard
 *
 * You are given an object street of class Street that represents a circular street and a positive integer k which represents a maximum bound for the number of houses in that street (in other words, the number of houses is less than or equal to k). Houses' doors could be open or closed initially (at least one is open).
 *
 * Initially, you are standing in front of a door to a house on this street. Your task is to count the number of houses in the street.
 *
 * The class Street contains the following functions which may help you:
 *
 * void closeDoor(): Close the door of the house you are in front of.
 * boolean isDoorOpen(): Returns true if the door of the current house is open and false otherwise.
 * void moveRight(): Move to the right house.
 *
 * Note that by circular street, we mean if you number the houses from 1 to n, then the right house of house_i is house_(i+1) for i < n, and the right house of house_n is house_1.
 *
 * Return ans which represents the number of houses on this street.
 *
 *
 * Example 1:
 *
 * Input: street = [1,1,1,1], k = 10
 * Output: 4
 * Explanation: There are 4 houses, and all their doors are open.
 * The number of houses is less than k, which is 10.
 *
 * Example 2:
 *
 * Input: street = [1,0,1,1,0], k = 5
 * Output: 5
 * Explanation: There are 5 houses, and the doors of the 1st, 3rd, and 4th house (moving in the right direction) are open, and the rest are closed.
 * The number of houses is equal to k, which is 5.
 *
 *
 * Constraints:
 *
 * n == number of houses
 * 1 <= n <= k <= 10^5
 * street is circular by definition provided in the statement.
 * The input is generated such that at least one of the doors is open.
 *
 */
public class CountHousesInACircularStreetII {

    /**
     * The street's control interface (provided by LeetCode).
     */
    public interface Street {
        void closeDoor();

        boolean isDoorOpen();

        void moveRight();
    }

    // V0
    // IDEA: INTERACTIVE - USE ONE OPEN DOOR AS AN ANCHOR
    //       walk right until we stand on an open door: that is the anchor, left open. then
    //       take k more steps, recording the step number and closing each open door we land
    //       on. the sweep closes every other open door during steps 1..n, and at step n we
    //       are back on the anchor (still open) -> the last recorded step is exactly n.
    /**
     * time = O(K)
     * space = O(1)
     */
    public int houseCount(Street street, int k) {
        // step 1: park on an open door (at least one is guaranteed)
        while (!street.isDoorOpen()) {
            street.moveRight();
        }

        // step 2: one guarded lap; k >= n, so step n is always reached
        int houses = 0;
        for (int step = 1; step <= k; step++) {
            street.moveRight();
            if (street.isDoorOpen()) {
                houses = step;
                street.closeDoor();
            }
        }
        return houses;
    }
}
