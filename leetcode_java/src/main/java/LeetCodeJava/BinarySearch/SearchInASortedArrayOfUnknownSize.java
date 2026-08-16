package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-in-a-sorted-array-of-unknown-size/description/
/**
 * 702. Search in a Sorted Array of Unknown Size
 * Medium
 * Lock: Prime
 *
 * This is an interactive problem.
 *
 * You have a sorted array of unique elements and an unknown size. You do not have an access
 * to the array but you can use the ArrayReader interface to access it.
 * You can call ArrayReader.get(i) that:
 *
 *   - returns the value at the ith index (0-indexed) of the secret array (i.e., secret[i]), or
 *   - returns 2^31 - 1 if the i is out of the boundary of the array.
 *
 * You are also given an integer target.
 *
 * Return the index k of the hidden array where secret[k] == target or return -1 otherwise.
 *
 * You must write an algorithm with O(log n) runtime complexity.
 *
 *
 * Example 1:
 *
 * Input: secret = [-1,0,3,5,9,12], target = 9
 * Output: 4
 * Explanation: 9 exists in secret and its index is 4.
 *
 * Example 2:
 *
 * Input: secret = [-1,0,3,5,9,12], target = 2
 * Output: -1
 * Explanation: 2 does not exist in secret so return -1.
 *
 *
 * Constraints:
 *
 * 1 <= secret.length <= 10^4
 * -10^4 <= secret[i], target <= 10^4
 * secret is sorted in a strictly increasing order.
 *
 */
public class SearchInASortedArrayOfUnknownSize {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    interface ArrayReader {
        int get(int index);
    }

    // V0
    // IDEA: EXPONENTIAL SEARCH + BINARY SEARCH
    /**
     *   Step 1) we do NOT know the array length, so GROW a right bound by DOUBLING
     *           until reader.get(right) >= target. Out of bound reads return 2^31 - 1,
     *           which is >= target, so the doubling ALWAYS terminates.
     *
     *   Step 2) target (if present) now lies in [right / 2, right]
     *           -> plain binary search for the leftmost index with value >= target.
     *
     *   time  = O(log M), M = index of the target
     *   space = O(1)
     */
    public int search(ArrayReader reader, int target) {

        // step 1: exponentially widen the search window
        int right = 1;
        while (reader.get(right) < target) {
            right *= 2;
        }

        /** NOTE !!!
         *
         *  the previous bound `right / 2` is a SAFE left edge:
         *  we already know get(right / 2) < target (that is why we doubled),
         *  except on the very first iteration where right / 2 == 0
         */
        int left = right / 2;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (reader.get(mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return reader.get(left) == target ? left : -1;
    }

}
