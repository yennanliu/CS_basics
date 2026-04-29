package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/cutting-ribbons/description/
// https://leetcode.ca/2021-07-24-1891-Cutting-Ribbons/
/**
 *  1891 - Cutting Ribbons
 * Posted on July 24, 2021 · 5 minute read
 * Welcome to Subscribe On Youtube
 *
 * Formatted question description: https://leetcode.ca/all/1891.html
 *
 * 1891. Cutting Ribbons
 * Level
 * Medium
 *
 * Description
 * You are given an integer array ribbons, where ribbons[i] represents the length of the i-th ribbon, and an integer k. You may cut any of the ribbons into any number of segments of positive integer lengths, or perform no cuts at all.
 *
 * For example, if you have a ribbon of length 4, you can:
 * Keep the ribbon of length 4,
 * Cut it into one ribbon of length 3 and one ribbon of length 1,
 * Cut it into two ribbons of length 2,
 * Cut it into one ribbon of length 2 and two ribbons of length 1, or
 * Cut it into four ribbons of length 1.
 * Your goal is to obtain k ribbons of all the same positive integer length. You are allowed to throw away any excess ribbon as a result of cutting.
 *
 * Return the maximum possible positive integer length that you can obtain k ribbons of, or 0 if you cannot obtain k ribbons of the same length.
 *
 * Example 1:
 *
 * Input: ribbons = [9,7,5], k = 3
 *
 * Output: 5
 *
 * Explanation:
 *
 * Cut the first ribbon to two ribbons, one of length 5 and one of length 4.
 * Cut the second ribbon to two ribbons, one of length 5 and one of length 2.
 * Keep the third ribbon as it is.
 * Now you have 3 ribbons of length 5.
 *
 * Example 2:
 *
 * Input: ribbons = [7,5,9], k = 4
 *
 * Output: 4
 *
 *
 */
public class CuttingRibbons {

    // V0
//    public int maxLength(int[] ribbons, int k) {
//    }


    // V1


    // V2-1
    // IDEA: BINARY SEARCH
    // https://leetcode.ca/2021-07-24-1891-Cutting-Ribbons/
    public int maxLength_2_1(int[] ribbons, int k) {
        int maxRibbon = 0;
        for (int ribbon : ribbons)
            maxRibbon = Math.max(maxRibbon, ribbon);
        int low = 0, high = maxRibbon;
        while (low < high) {
            int mid = (high - low + 1) / 2 + low;
            int count = 0;
            for (int ribbon : ribbons)
                count += ribbon / mid;
            if (count >= k)
                low = mid;
            else
                high = mid - 1;
        }
        return low;
    }


    // V2-2
    // IDEA: BINARY SEARCH
    // https://leetcode.ca/2021-07-24-1891-Cutting-Ribbons/
    public int maxLength_2_2(int[] ribbons, int k) {
        int left = 0, right = 100000;
        while (left < right) {
            int mid = (left + right + 1) >>> 1;
            int cnt = 0;
            for (int x : ribbons) {
                cnt += x / mid;
            }
            if (cnt >= k) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }



    // V3


}
