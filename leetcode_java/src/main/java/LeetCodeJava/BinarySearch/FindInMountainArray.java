package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-in-mountain-array/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1095. Find in Mountain Array
 * Hard
 * Topics
 * Companies
 * Hint
 * (This problem is an interactive problem.)
 *
 * You may recall that an array arr is a mountain array if and only if:
 *
 * arr.length >= 3
 * There exists some i with 0 < i < arr.length - 1 such that:
 * arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
 * arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
 * Given a mountain array mountainArr, return the minimum index such that mountainArr.get(index) == target. If such an index does not exist, return -1.
 *
 * You cannot access the mountain array directly. You may only access the array using a MountainArray interface:
 *
 * MountainArray.get(k) returns the element of the array at index k (0-indexed).
 * MountainArray.length() returns the length of the array.
 * Submissions making more than 100 calls to MountainArray.get will be judged Wrong Answer. Also, any solutions that attempt to circumvent the judge will result in disqualification.
 *
 *
 *
 * Example 1:
 *
 * Input: mountainArr = [1,2,3,4,5,3,1], target = 3
 * Output: 2
 * Explanation: 3 exists in the array, at index=2 and index=5. Return the minimum index, which is 2.
 * Example 2:
 *
 * Input: mountainArr = [0,1,2,4,2,1], target = 3
 * Output: -1
 * Explanation: 3 does not exist in the array, so we return -1.
 *
 *
 * Constraints:
 *
 * 3 <= mountainArr.length() <= 104
 * 0 <= target <= 109
 * 0 <= mountainArr.get(index) <= 109
 *
 *
 *
 */
public class FindInMountainArray {

    /**
     * // This is MountainArray's API interface.
     * // You should not implement it, or speculate about its implementation
     * interface MountainArray {
     *     public int get(int index) {}
     *     public int length() {}
     * }
     */
    // V0
    class MountainArray {
        public int get(int index) {return 0;}
        public int length() {return 0;}
    }
//    public int findInMountainArray(int target, MountainArray mountainArr) {
//
//    }

    // V1-1
    // https://youtu.be/BGgYC-YkGvc?si=SQ0EqJ6n_w0BrNlB

    // V2-1
    // https://leetcode.com/problems/find-in-mountain-array/editorial/
    // IDEA: Binary Search
    public int findInMountainArray_2_1(int target, MountainArray mountainArr) {
        // Save the length of the mountain array
        int length = mountainArr.length();

        // 1. Find the index of the peak element
        int low = 1;
        int high = length - 2;
        while (low != high) {
            int testIndex = (low + high) / 2;
            if (mountainArr.get(testIndex) < mountainArr.get(testIndex + 1)) {
                low = testIndex + 1;
            } else {
                high = testIndex;
            }
        }
        int peakIndex = low;

        // 2. Search in the strictly increasing part of the array
        low = 0;
        high = peakIndex;
        while (low != high) {
            int testIndex = (low + high) / 2;
            if (mountainArr.get(testIndex) < target) {
                low = testIndex + 1;
            } else {
                high = testIndex;
            }
        }
        // Check if the target is present in the strictly increasing part
        if (mountainArr.get(low) == target) {
            return low;
        }

        // 3. Otherwise, search in the strictly decreasing part
        low = peakIndex + 1;
        high = length - 1;
        while (low != high) {
            int testIndex = (low + high) / 2;
            if (mountainArr.get(testIndex) > target) {
                low = testIndex + 1;
            } else {
                high = testIndex;
            }
        }
        // Check if the target is present in the strictly decreasing part
        if (mountainArr.get(low) == target) {
            return low;
        }

        // Target is not present in the mountain array
        return -1;
    }

    // V2-2
    // https://leetcode.com/problems/find-in-mountain-array/editorial/
    // IDEA:  Minimizing get Calls with Early Stopping and Caching
    public int findInMountainArray_2_2(int target, MountainArray mountainArr) {
        // Save the length of the mountain array
        int length = mountainArr.length();

        // Initialize the cache
        Map<Integer, Integer> cache = new HashMap<>();

        // 1. Find the index of the peak element
        int low = 1;
        int high = length - 2;
        while (low != high) {
            int testIndex = (low + high) >> 1;

            int curr;
            if (cache.containsKey(testIndex)) {
                curr = cache.get(testIndex);
            } else {
                curr = mountainArr.get(testIndex);
                cache.put(testIndex, curr);
            }

            int next;
            if (cache.containsKey(testIndex + 1)) {
                next = cache.get(testIndex + 1);
            } else {
                next = mountainArr.get(testIndex + 1);
                cache.put(testIndex + 1, next);
            }

            if (curr < next) {
                if (curr == target) {
                    return testIndex;
                }
                if (next == target) {
                    return testIndex + 1;
                }
                low = testIndex + 1;
            } else {
                high = testIndex;
            }
        }

        int peakIndex = low;

        // 2. Search in the strictly increasing part of the array
        // If found, will be returned in the loop itself.
        low = 0;
        high = peakIndex;
        while (low <= high) {
            int testIndex = (low + high) >> 1;

            int curr;
            if (cache.containsKey(testIndex)) {
                curr = cache.get(testIndex);
            } else {
                curr = mountainArr.get(testIndex);
            }

            if (curr == target) {
                return testIndex;
            } else if (curr < target) {
                low = testIndex + 1;
            } else {
                high = testIndex - 1;
            }
        }

        // 3. Search in the strictly decreasing part of the array
        // If found, will be returned in the loop itself.
        low = peakIndex + 1;
        high = length - 1;
        while (low <= high) {
            int testIndex = (low + high) >> 1;

            int curr;
            if (cache.containsKey(testIndex)) {
                curr = cache.get(testIndex);
            } else {
                curr = mountainArr.get(testIndex);
            }

            if (curr == target) {
                return testIndex;
            } else if (curr > target) {
                low = testIndex + 1;
            } else {
                high = testIndex - 1;
            }
        }

        // Target is not present in the mountain array
        return -1;
    }


}
