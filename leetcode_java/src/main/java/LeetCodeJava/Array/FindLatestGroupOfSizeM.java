package LeetCodeJava.Array;

// https://leetcode.com/problems/find-latest-group-of-size-m/description/

/**
 * 1562. Find Latest Group of Size M
 * Medium
 *
 * Given an array arr that represents a permutation of numbers from 1 to n.
 *
 * You have a binary string of size n that initially has all its bits set to zero. At each
 * step i (assuming both the binary string and arr are 1-indexed) from 1 to n, the bit at
 * position arr[i] is set to 1.
 *
 * You are also given an integer m. Find the latest step at which there exists a group of
 * ones of length m. A group of ones is a contiguous substring of 1's such that it cannot
 * be extended in either direction.
 *
 * Return the latest step at which there exists a group of ones of length exactly m. If no
 * such group exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: arr = [3,5,1,2,4], m = 1
 * Output: 4
 * Explanation:
 * Step 1: "00100", groups: ["1"]
 * Step 2: "00101", groups: ["1", "1"]
 * Step 3: "10101", groups: ["1", "1", "1"]
 * Step 4: "11101", groups: ["111", "1"]
 * Step 5: "11111", groups: ["11111"]
 * The latest step at which there exists a group of size 1 is step 4.
 *
 * Example 2:
 *
 * Input: arr = [3,1,5,4,2], m = 2
 * Output: -1
 *
 *
 * Constraints:
 *
 * n == arr.length
 * 1 <= m <= n <= 10^5
 * 1 <= arr[i] <= n
 * All integers in arr are distinct.
 *
 */
public class FindLatestGroupOfSizeM {

    // V0
    // IDEA: INTERVAL MERGING (keep the group length at each group's two END cells)
    //       Setting bit x merges the group ending at x-1 (length leftLen) with the group
    //       starting at x+1 (length rightLen) into one of length leftLen + rightLen + 1,
    //       written at its new ends x - leftLen and x + rightLen. groupCount[L] = how many
    //       groups have length L right now; the answer is the last step with groupCount[m] > 0.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int findLatestStep(int[] arr, int m) {
        int n = arr.length;
        // padded by one on each side so x - 1 and x + 1 are always valid (and read 0)
        int[] lengthAtEnd = new int[n + 2];
        int[] groupCount = new int[n + 2];
        int latestStep = -1;

        for (int step = 1; step <= n; step++) {
            int x = arr[step - 1];
            int leftLen = lengthAtEnd[x - 1];
            int rightLen = lengthAtEnd[x + 1];
            int mergedLen = leftLen + rightLen + 1;

            // only the two boundary cells of a group are ever read, so update just those
            lengthAtEnd[x - leftLen] = mergedLen;
            lengthAtEnd[x + rightLen] = mergedLen;

            // the two neighbouring groups disappear into the merged one
            // (a length of 0 means "no group"; its counter is never read for m >= 1)
            groupCount[leftLen]--;
            groupCount[rightLen]--;
            groupCount[mergedLen]++;

            if (groupCount[m] > 0) {
                latestStep = step;
            }
        }
        return latestStep;
    }
}
