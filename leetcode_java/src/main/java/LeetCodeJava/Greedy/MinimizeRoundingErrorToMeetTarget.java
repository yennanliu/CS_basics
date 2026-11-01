package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimize-rounding-error-to-meet-target/description/

import java.util.*;

/**
 * 1058. Minimize Rounding Error to Meet Target
 * Medium
 *
 * Given an array of prices [p1, p2, ..., pn] and a target, round each price pi to
 * Round_i(pi) so that the rounded array [Round_1(p1), Round_2(p2), ..., Round_n(pn)]
 * sums to the given target. Each operation Round_i(pi) could be either Floor(pi) or Ceil(pi).
 *
 * Return the string "-1" if the rounded array is impossible to sum to target.
 * Otherwise, return the smallest rounding error, which is defined as
 * Sigma |Round_i(pi) - pi| for i from 1 to n, as a string with three places
 * after the decimal.
 *
 *
 * Example 1:
 *
 * Input: prices = ["0.700","2.800","4.900"], target = 8
 * Output: "1.000"
 * Explanation:
 * Use Floor, Ceil and Ceil operations to get (0.7 - 0) + (3 - 2.8) + (5 - 4.9) = 0.7 + 0.2 + 0.1 = 1.0 .
 *
 * Example 2:
 *
 * Input: prices = ["1.500","2.500","3.500"], target = 10
 * Output: "-1"
 * Explanation: It is impossible to meet the target.
 *
 * Example 3:
 *
 * Input: prices = ["1.500","2.500","3.500"], target = 9
 * Output: "1.500"
 *
 *
 * Constraints:
 *
 * 1 <= prices.length <= 500
 * Each string prices[i] represents a real number in the range [0.0, 1000.0] and has exactly 3 decimal places.
 * 0 <= target <= 10^6
 *
 */
public class MinimizeRoundingErrorToMeetTarget {

    // V0
    // IDEA: GREEDY + EXACT INTEGER (THOUSANDTHS) ARITHMETIC: ceil the d prices with the largest fractions
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String minimizeError(String[] prices, int target) {
        // sum of floor(price), and the non-zero fractional parts in 1/1000 units
        int floorSum = 0;
        List<Integer> fractions = new ArrayList<>();

        for (String price : prices) {
            int dot = price.indexOf('.');
            int whole;
            int fraction;
            if (dot < 0) {
                whole = Integer.parseInt(price);
                fraction = 0;
            } else {
                whole = Integer.parseInt(price.substring(0, dot));
                fraction = Integer.parseInt(price.substring(dot + 1));
            }
            floorSum += whole;
            if (fraction > 0) {
                fractions.add(fraction);
            }
        }

        // reachable totals: floor everything .. ceil every price with a fraction
        if (target < floorSum || target > floorSum + fractions.size()) {
            return "-1";
        }

        // we must ceil exactly `ceilCount` of the fractional prices.
        // ceiling fraction f costs (1000 - f), flooring it costs f,
        // so the largest fractions are the cheapest to ceil
        int ceilCount = target - floorSum;
        fractions.sort(Collections.reverseOrder());

        int errorThousandths = 0;
        for (int i = 0; i < fractions.size(); i++) {
            if (i < ceilCount) {
                errorThousandths += 1000 - fractions.get(i);
            } else {
                errorThousandths += fractions.get(i);
            }
        }

        // format as "x.yyy" from the exact integer value
        return String.format("%d.%03d", errorThousandths / 1000, errorThousandths % 1000);
    }
}
