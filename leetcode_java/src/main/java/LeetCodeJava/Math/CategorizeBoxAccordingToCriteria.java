package LeetCodeJava.Math;

// https://leetcode.com/problems/categorize-box-according-to-criteria/description/

/**
 * 2525. Categorize Box According to Criteria
 * Easy
 *
 * Given four integers length, width, height, and mass, representing the dimensions
 * and mass of a box, respectively, return a string representing the category of the box.
 *
 * The box is "Bulky" if:
 *     Any of the dimensions of the box is greater or equal to 10^4.
 *     Or, the volume of the box is greater or equal to 10^9.
 * If the mass of the box is greater or equal to 100, it is "Heavy".
 * If the box is both "Bulky" and "Heavy", then its category is "Both".
 * If the box is neither "Bulky" nor "Heavy", then its category is "Neither".
 * If the box is "Bulky" but not "Heavy", then its category is "Bulky".
 * If the box is "Heavy" but not "Bulky", then its category is "Heavy".
 *
 * Note that the volume of the box is the product of its length, width and height.
 *
 *
 * Example 1:
 *
 * Input: length = 1000, width = 35, height = 700, mass = 300
 * Output: "Heavy"
 *
 * Example 2:
 *
 * Input: length = 200, width = 50, height = 800, mass = 50
 * Output: "Neither"
 *
 *
 * Constraints:
 *
 * 1 <= length, width, height <= 10^5
 * 1 <= mass <= 10^3
 *
 */
public class CategorizeBoxAccordingToCriteria {

    private static final int DIMENSION_LIMIT = 10_000;
    private static final long VOLUME_LIMIT = 1_000_000_000L;
    private static final int MASS_LIMIT = 100;

    // V0
    // IDEA: SIMULATION -> TWO INDEPENDENT FLAGS (bulky, heavy), thresholds are INCLUSIVE
    /**
     * time = O(1)
     * space = O(1)
     */
    public String categorizeBox(int length, int width, int height, int mass) {
        // volume can reach 10^15 -> must be computed in long
        long volume = (long) length * width * height;

        boolean bulky = length >= DIMENSION_LIMIT
                || width >= DIMENSION_LIMIT
                || height >= DIMENSION_LIMIT
                || volume >= VOLUME_LIMIT;
        boolean heavy = mass >= MASS_LIMIT;

        if (bulky && heavy) {
            return "Both";
        }
        if (bulky) {
            return "Bulky";
        }
        if (heavy) {
            return "Heavy";
        }
        return "Neither";
    }
}
