package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/number-of-unique-flavors-after-sharing-k-candies/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2107. Number of Unique Flavors After Sharing K Candies
 * Medium
 *
 * You are given a 0-indexed integer array candies, where candies[i] represents the flavor of the ith candy. Your mom wants you to share these candies with your little sister by giving her k consecutive candies, but you want to keep as many flavors of candies as possible.
 *
 * Return the maximum number of unique flavors of candy you can keep after sharing with your sister.
 *
 *
 * Example 1:
 *
 * Input: candies = [1,2,2,3,4,3], k = 3
 * Output: 3
 * Explanation:
 * Give the candies in the range [1, 3] (inclusive) with flavors [2,2,3].
 * You can eat candies with flavors [1,4,3].
 * There are 3 unique flavors, so return 3.
 *
 * Example 2:
 *
 * Input: candies = [2,2,2,2,3,3], k = 2
 * Output: 2
 * Explanation:
 * Give the candies in the range [3, 4] (inclusive) with flavors [2,3].
 * You can eat candies with flavors [2,2,2,3].
 * There are 2 unique flavors, so return 2.
 *
 * Example 3:
 *
 * Input: candies = [2,4,5], k = 0
 * Output: 3
 * Explanation:
 * You do not have to give any candies.
 * You can eat the candies with flavors [2,4,5].
 * There are 3 unique flavors, so return 3.
 *
 *
 * Constraints:
 *
 * 1 <= candies.length <= 10^5
 * 1 <= candies[i] <= 10^5
 * 0 <= k <= candies.length
 *
 */
public class NumberOfUniqueFlavorsAfterSharingKCandies {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW OVER THE CANDIES GIVEN AWAY
    //  the sister takes exactly k consecutive candies; what we keep is everything outside that
    //  window. start from the count of every flavour, remove the first window, then on each
    //  shift put back the candy leaving the window and remove the one entering it. the number
    //  of flavours with a positive count outside the window is the map's size.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int shareCandies(int[] candies, int k) {
        // outside[flavour] = how many candies of that flavour we keep
        Map<Integer, Integer> outside = new HashMap<>();
        for (int flavour : candies) {
            outside.put(flavour, outside.getOrDefault(flavour, 0) + 1);
        }
        if (k == 0) {
            return outside.size();
        }

        // give away the first window candies[0 .. k - 1]
        for (int i = 0; i < k; i++) {
            removeOne(outside, candies[i]);
        }

        int best = outside.size();
        for (int i = k; i < candies.length; i++) {
            // candies[i - k] leaves the window -> we keep it again
            int back = candies[i - k];
            outside.put(back, outside.getOrDefault(back, 0) + 1);
            // candies[i] enters the window -> it is given away
            removeOne(outside, candies[i]);
            best = Math.max(best, outside.size());
        }
        return best;
    }

    /** Decrements a flavour's count, dropping the key at zero so size() counts flavours kept. */
    private void removeOne(Map<Integer, Integer> counts, int flavour) {
        int left = counts.get(flavour) - 1;
        if (left == 0) {
            counts.remove(flavour);
        } else {
            counts.put(flavour, left);
        }
    }
}
