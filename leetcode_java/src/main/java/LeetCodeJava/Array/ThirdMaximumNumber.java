package LeetCodeJava.Array;

// https://leetcode.com/problems/third-maximum-number/description/

import DataStructure.Pair;

import java.util.*;

/**
 *  414. Third Maximum Number
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, return the third distinct maximum number in this array. If the third maximum does not exist, return the maximum number.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,1]
 * Output: 1
 * Explanation:
 * The first distinct maximum is 3.
 * The second distinct maximum is 2.
 * The third distinct maximum is 1.
 * Example 2:
 *
 * Input: nums = [1,2]
 * Output: 2
 * Explanation:
 * The first distinct maximum is 2.
 * The second distinct maximum is 1.
 * The third distinct maximum does not exist, so the maximum (2) is returned instead.
 * Example 3:
 *
 * Input: nums = [2,2,3,1]
 * Output: 1
 * Explanation:
 * The first distinct maximum is 3.
 * The second distinct maximum is 2 (both 2's are counted together since they have the same value).
 * The third distinct maximum is 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * -231 <= nums[i] <= 231 - 1
 *
 *
 */
public class ThirdMaximumNumber {

    // V0
//    public int thirdMax(int[] nums) {
//
//    }

    // V1-1
    // IDEA: SORTING
    // https://leetcode.com/problems/third-maximum-number/editorial/
    public int thirdMax_1(int[] nums) {
        // Sort the array in non-increasing order.
        Arrays.sort(nums);

        // Reverse array to make it non-increasing.
        for (int index = 0; index < nums.length / 2; ++index) {
            int temp = nums[index];
            nums[index] = nums[nums.length - 1 - index];
            nums[nums.length - 1 - index] = temp;
        }

        int elemCounted = 1;
        int prevElem = nums[0];

        for (int index = 1; index < nums.length; ++index) {
            // Current element is different from previous.
            if (nums[index] != prevElem) {
                elemCounted += 1;
                prevElem = nums[index];
            }

            // If we have counted 3 numbers then return current number.
            if (elemCounted == 3) {
                return nums[index];
            }
        }

        // We never counted 3 distinct numbers, return largest number.
        return nums[0];
    }

    // V1-2
    // IDEA: MIN HEAP (PQ)
    // https://leetcode.com/problems/third-maximum-number/editorial/
    public int thirdMax_1_2(int[] nums) {
        PriorityQueue<Integer> minHeap = new PriorityQueue();
        Set<Integer> taken = new HashSet<Integer>();

        for (int index = 0; index < nums.length; ++index) {
            // If current number was already taken, skip it.
            if (taken.contains(nums[index])) {
                continue;
            }

            // If min heap already has three numbers in it.
            // Pop the smallest if current number is bigger than it.
            if (minHeap.size() == 3) {
                if (minHeap.peek() < nums[index]) {
                    taken.remove(minHeap.poll());

                    minHeap.add(nums[index]);
                    taken.add(nums[index]);
                }
            }
            // If min heap does not have three numbers we can push it.
            else {
                minHeap.add(nums[index]);
                taken.add(nums[index]);
            }
        }

        // 'nums' has only one distinct element it will be the maximum.
        if (minHeap.size() == 1) {
            return minHeap.peek();
        }
        // 'nums' has two distinct elements.
        else if (minHeap.size() == 2) {
            int firstNum = minHeap.poll();
            return Math.max(firstNum, minHeap.peek());
        }

        return minHeap.peek();
    }

    // V1-3
    // IDEA: ORDERED SET
    // https://leetcode.com/problems/third-maximum-number/editorial/
    public int thirdMax_1_3(int[] nums) {
        // Sorted set to keep elements in sorted order.
        TreeSet<Integer> sortedNums = new TreeSet<Integer>();

        // Iterate on all elements of 'nums' array.
        for (int num : nums) {
            // Do not insert same element again.
            if (sortedNums.contains(num)) {
                continue;
            }

            // If sorted set has 3 elements.
            if (sortedNums.size() == 3) {
                // And the smallest element is smaller than current element.
                if (sortedNums.first() < num) {
                    // Then remove the smallest element and push the current element.
                    sortedNums.pollFirst();
                    sortedNums.add(num);
                }

            }
            // Otherwise push the current element of nums array.
            else {
                sortedNums.add(num);
            }
        }

        // If sorted set has three elements return the smallest among those 3.
        if (sortedNums.size() == 3) {
            return sortedNums.first();
        }

        // Otherwise return the biggest element of nums array.
        return sortedNums.last();
    }

    // V1-4
    // IDEA: 3 POINTERS
    // https://leetcode.com/problems/third-maximum-number/editorial/
    public int thirdMax_1_4(int[] nums) {
        // Three variables to store maxiumum three numbers till now.
        long firstMax = Long.MIN_VALUE;
        long secondMax = Long.MIN_VALUE;
        long thirdMax = Long.MIN_VALUE;

        for (int num : nums) {
            // This number is already used once, thus we skip it.
            if (firstMax == num || secondMax == num || thirdMax == num) {
                continue;
            }

            // If current number is greater than first maximum,
            // It means that this is the greatest number and first maximum and second max
            // will become the next two greater numbers.
            if (firstMax <= num) {
                thirdMax = secondMax;
                secondMax = firstMax;
                firstMax = num;
            }
            // When current number is greater than second maximum,
            // it means that this is the second greatest number.
            else if (secondMax <= num) {
                thirdMax = secondMax;
                secondMax = num;
            }
            // It is the third greatest number.
            else if (thirdMax <= num) {
                thirdMax = num;
            }
        }

        // If third max was never updated, it means we don't have 3 distinct numbers.
        if (thirdMax == Long.MIN_VALUE) {
            int ans = (int) firstMax;
            return ans;
        }

        int ans = (int) thirdMax;
        return ans;
    }

    // V1-5
    // IDEA: 3 POINTERS V2
    // https://leetcode.com/problems/third-maximum-number/editorial/
    public int thirdMax_1_5(int[] nums) {
        Pair<Integer, Boolean> firstMax = new Pair<Integer, Boolean>(-1, false);
        Pair<Integer, Boolean> secondMax = new Pair<Integer, Boolean>(-1, false);
        Pair<Integer, Boolean> thirdMax = new Pair<Integer, Boolean>(-1, false);

        for (int num : nums) {
            // If current number is already stored, skip it.
            if ((firstMax.getValue() && firstMax.getKey() == num) ||
                    (secondMax.getValue() && secondMax.getKey() == num) ||
                    (thirdMax.getValue() && thirdMax.getKey() == num)) {
                continue;
            }

            // If we never stored any variable in firstMax
            // or curr num is bigger than firstMax, then curr num is the biggest number.
            if (!firstMax.getValue() || firstMax.getKey() <= num) {
                thirdMax = secondMax;
                secondMax = firstMax;
                firstMax = new Pair<Integer, Boolean>(num, true);
            }
            // If we never stored any variable in secondMax
            // or curr num is bigger than secondMax, then curr num is 2nd biggest number.
            else if (!secondMax.getValue() || secondMax.getKey() <= num) {
                thirdMax = secondMax;
                secondMax = new Pair<Integer, Boolean>(num, true);
            }
            // If we never stored any variable in thirdMax
            // or curr num is bigger than thirdMax, then curr num is 3rd biggest number.
            else if (!thirdMax.getValue() || thirdMax.getKey() <= num) {
                thirdMax = new Pair<Integer, Boolean>(num, true);
            }
        }

        // If third max was never updated, it means we don't have 3 distinct numbers.
        if (!thirdMax.getValue()) {
            return firstMax.getKey();
        }

        return thirdMax.getKey();
    }


    // V2



}
