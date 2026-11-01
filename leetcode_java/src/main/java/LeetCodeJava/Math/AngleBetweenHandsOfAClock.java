package LeetCodeJava.Math;

// https://leetcode.com/problems/angle-between-hands-of-a-clock/description/

/**
 * 1344. Angle Between Hands of a Clock
 * Medium
 *
 * Given two numbers, hour and minutes, return the smaller angle (in degrees)
 * formed between the hour and the minute hand.
 *
 * Answers within 10^-5 of the actual value will be accepted as correct.
 *
 *
 * Example 1:
 *
 * Input: hour = 12, minutes = 30
 * Output: 165
 *
 * Example 2:
 *
 * Input: hour = 3, minutes = 30
 * Output: 75
 *
 * Example 3:
 *
 * Input: hour = 3, minutes = 15
 * Output: 7.5
 *
 *
 * Constraints:
 *
 * 1 <= hour <= 12
 * 0 <= minutes <= 59
 *
 */
public class AngleBetweenHandsOfAClock {

    // V0
    // IDEA: MATH (degrees of each hand, then the smaller of the two arcs)
    //   minute hand: 360 / 60 = 6 degrees per minute
    //   hour hand:   360 / 12 = 30 degrees per hour, plus it drifts 0.5 degree per minute
    /**
     * time = O(1)
     * space = O(1)
     */
    public double angleClock(int hour, int minutes) {
        // hour 12 sits at the same place as hour 0
        double hourDegrees = 30.0 * (hour % 12) + 0.5 * minutes;
        double minuteDegrees = 6.0 * minutes;

        double diff = Math.abs(hourDegrees - minuteDegrees);
        // the two hands split the circle into two arcs; return the smaller one
        return Math.min(diff, 360.0 - diff);
    }
}
