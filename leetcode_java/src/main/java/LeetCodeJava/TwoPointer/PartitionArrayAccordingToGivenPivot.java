package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/partition-array-according-to-given-pivot/description/

/**
 * 2161. Partition Array According to Given Pivot
 * Medium
 *
 * You are given a 0-indexed integer array nums and an integer pivot. Rearrange nums such that the following conditions are satisfied:
 *
 * Every element less than pivot appears before every element greater than pivot.
 * Every element equal to pivot appears in between the elements less than and greater than pivot.
 * The relative order of the elements less than pivot and the elements greater than pivot is maintained.
 * More formally, consider every pi, pj where pi is the new position of the ith element and pj is the new position of the jth element. For elements less than pivot, if i < j and nums[i] < pivot and nums[j] < pivot, then pi < pj. Similarly for elements greater than pivot, if i < j and nums[i] > pivot and nums[j] > pivot, then pi < pj.
 *
 * Return nums after the rearrangement.
 *
 *
 * Example 1:
 *
 * Input: nums = [9,12,5,10,14,3,10], pivot = 10
 * Output: [9,5,3,10,10,12,14]
 * Explanation:
 * The elements 9, 5, and 3 are less than the pivot so they are on the left side of the array.
 * The elements 12 and 14 are greater than the pivot so they are on the right side of the array.
 * The relative ordering of the elements less than and greater than pivot is also maintained. [9, 5, 3] and [12, 14] are the respective orderings.
 *
 * Example 2:
 *
 * Input: nums = [-3,4,3,2], pivot = 2
 * Output: [-3,2,4,3]
 * Explanation:
 * The element -3 is less than the pivot so it is on the left side of the array.
 * The elements 4 and 3 are greater than the pivot so they are on the right side of the array.
 * The relative ordering of the elements less than and greater than pivot is also maintained. [-3] and [4, 3] are the respective orderings.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^6 <= nums[i] <= 10^6
 * pivot equals to an element of nums.
 *
 */
public class PartitionArrayAccordingToGivenPivot {

    // V0
    // IDEA: STABLE THREE-WAY SPLIT — FILL "< pivot" FROM THE FRONT, "> pivot" FROM THE BACK
    //  the order inside the < group and the > group must be preserved, so an in-place
    //  quicksort partition does not work. write the smaller values left to right from the
    //  front, the larger values right to left from the back (scanning nums backwards keeps
    //  them in order), and fill the gap in the middle with the pivot.
    /**
     * time = O(N)
     * space = O(N)   (the output)
     */
    public int[] pivotArray(int[] nums, int pivot) {
        int n = nums.length;
        int[] result = new int[n];

        // smaller values, in their original order, from the front
        int front = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] < pivot) {
                result[front] = nums[i];
                front++;
            }
        }

        // larger values: scan backwards and fill from the back, which keeps their order
        int back = n - 1;
        for (int i = n - 1; i >= 0; i--) {
            if (nums[i] > pivot) {
                result[back] = nums[i];
                back--;
            }
        }

        // whatever is left in the middle equals the pivot
        for (int i = front; i <= back; i++) {
            result[i] = pivot;
        }
        return result;
    }
}
