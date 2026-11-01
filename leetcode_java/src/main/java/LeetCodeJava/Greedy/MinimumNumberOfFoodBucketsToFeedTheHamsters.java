package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-food-buckets-to-feed-the-hamsters/description/
// formerly: https://leetcode.com/problems/minimum-number-of-buckets-required-to-collect-rainwater-from-houses/

/**
 * 2086. Minimum Number of Food Buckets to Feed the Hamsters
 * Medium
 *
 * (formerly: 2086. Minimum Number of Buckets Required to Collect Rainwater from Houses)
 *
 * You are given a 0-indexed string street. Each character in street is either 'H' representing a house or
 * '.' representing an empty space.
 *
 * You can place buckets on the empty spaces to collect rainwater that falls from the adjacent houses. The
 * rainwater from a house at index i is collected if a bucket is placed at index i - 1 and/or index i + 1. A
 * single bucket, if placed adjacent to two houses, can collect the rainwater from both houses.
 *
 * Return the minimum number of buckets needed so that for every house, there is at least one bucket
 * collecting rainwater from it, or -1 if it is impossible.
 *
 *
 * Example 1:
 *
 * Input: street = "H..H"
 * Output: 2
 *
 * Example 2:
 *
 * Input: street = ".H.H."
 * Output: 1
 *
 * Example 3:
 *
 * Input: street = ".HHH."
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= street.length <= 10^5
 * street[i] is either 'H' or '.'.
 *
 */
public class MinimumNumberOfFoodBucketsToFeedTheHamsters {

    // V0
    // IDEA: GREEDY - ALWAYS PREFER THE BUCKET ON THE RIGHT OF A HOUSE (IT MAY ALSO SERVE THE NEXT HOUSE)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumBuckets(String street) {
        char[] cells = street.toCharArray();
        int n = cells.length;
        int buckets = 0;

        for (int i = 0; i < n; i++) {
            if (cells[i] != 'H') {
                continue;
            }
            // already covered by a bucket placed for the previous house
            if (i > 0 && cells[i - 1] == 'B') {
                continue;
            }
            if (i + 1 < n && cells[i + 1] == '.') {
                // right side first: that bucket can also cover a house at i + 2
                cells[i + 1] = 'B';
                buckets++;
            } else if (i > 0 && cells[i - 1] == '.') {
                cells[i - 1] = 'B';
                buckets++;
            } else {
                // both neighbours are houses (or walls): this house can never be served
                return -1;
            }
        }
        return buckets;
    }
}
