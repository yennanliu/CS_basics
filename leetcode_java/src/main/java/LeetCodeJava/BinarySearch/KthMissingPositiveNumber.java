package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/kth-missing-positive-number/description/

/**
 * 1539. Kth Missing Positive Number
 * Easy
 *
 * Given an array arr of positive integers sorted in a strictly increasing order, and an integer k.
 *
 * Return the kth positive integer that is missing from this array.
 *
 *
 * Example 1:
 *
 * Input: arr = [2,3,4,7,11], k = 5
 * Output: 9
 * Explanation: The missing positive integers are [1,5,6,8,9,10,12,13,...]. The 5th missing positive
 * integer is 9.
 *
 * Example 2:
 *
 * Input: arr = [1,2,3,4], k = 2
 * Output: 6
 * Explanation: The missing positive integers are [5,6,7,...]. The 2nd missing positive integer is 6.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 1000
 * 1 <= arr[i] <= 1000
 * 1 <= k <= 1000
 * arr[i] < arr[j] for 1 <= i < j <= arr.length
 *
 *
 * Follow up:
 *
 * Could you solve this problem in less than O(n) complexity?
 *
 */
public class KthMissingPositiveNumber {

    // V0
    // IDEA: BINARY SEARCH ON "HOW MANY ARE MISSING BEFORE INDEX i"
    /**
     *  missing(i) = arr[i] - (i + 1): the positive integers absent before arr[i], because a
     *  gap-free prefix would read 1, 2, ..., i + 1.
     *
     *  missing() is non-decreasing, so binary search the first index whose missing count
     *  reaches k (possibly arr.length). Exactly that many array elements sit below the
     *  answer, so the answer is index + k. The arr.length case needs no special handling.
     */
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int findKthPositive(int[] arr, int k) {
        int left = 0;
        int right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            int missingBefore = arr[mid] - (mid + 1);
            if (missingBefore >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left + k;
    }

    // V1
    // IDEA: LINEAR SCAN
    // every array value <= the running answer pushes the k-th missing number one further
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findKthPositive_1(int[] arr, int k) {
        int answer = k;
        for (int value : arr) {
            if (value <= answer) {
                answer++;
            } else {
                break;
            }
        }
        return answer;
    }
}
