package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimize-deviation-in-array/description/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  1675. Minimize Deviation in Array
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an array nums of n positive integers.
 *
 * You can perform two types of operations on any element of the array any number of times:
 *
 * If the element is even, divide it by 2.
 * For example, if the array is [1,2,3,4], then you can do this operation on the last element, and the array will be [1,2,3,2].
 * If the element is odd, multiply it by 2.
 * For example, if the array is [1,2,3,4], then you can do this operation on the first element, and the array will be [2,2,3,4].
 * The deviation of the array is the maximum difference between any two elements in the array.
 *
 * Return the minimum deviation the array can have after performing some number of operations.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 1
 * Explanation: You can transform the array to [1,2,3,2], then to [2,2,3,2], then the deviation will be 3 - 2 = 1.
 * Example 2:
 *
 * Input: nums = [4,1,5,20,3]
 * Output: 3
 * Explanation: You can transform the array after two operations to [4,2,5,5,3], then the deviation will be 5 - 2 = 3.
 * Example 3:
 *
 * Input: nums = [2,10,8]
 * Output: 3
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 5 * 104
 * 1 <= nums[i] <= 109
 *
 *
 */
public class MinimizeDeviationInArray {

    // V0
//    public int minimumDeviation(int[] nums) {
//
//    }



    // V1


    // V2
    // IDEA: PQ
    // https://leetcode.com/problems/minimize-deviation-in-array/solutions/3223541/day-55-priority_queue-easiest-beginner-f-yo4h/
    public int minimumDeviation_2(int[] nums) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int minVal = Integer.MAX_VALUE;
        for (int num : nums) {
            if (num % 2 == 1)
                num = num * 2;
            pq.offer(num);
            minVal = Math.min(minVal, num);
        }
        int minDeviation = Integer.MAX_VALUE;
        while (true) {
            int maxVal = pq.poll();
            minDeviation = Math.min(minDeviation, maxVal - minVal);
            if (maxVal % 2 == 1)
                break;
            maxVal = maxVal / 2;
            minVal = Math.min(minVal, maxVal);
            pq.offer(maxVal);
        }
        return minDeviation;
    }


    // V3
    // IDEA: PQ
    // https://leetcode.com/problems/minimize-deviation-in-array/solutions/3223767/clean-codes-full-explanation-priority-qu-q9rp/
    public int minimumDeviation_3(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Integer.MAX_VALUE;
        }

        PriorityQueue<Integer> evens = new PriorityQueue<>(nums.length, Collections.reverseOrder());
        int min = Integer.MAX_VALUE;

        for (int num : nums) {
            if (num % 2 == 0) {
                evens.offer(num);
                min = Math.min(num, min);
            } else {
                evens.offer(num * 2);
                min = Math.min(num * 2, min);
            }
        }

        int res = Integer.MAX_VALUE;
        while (evens.peek() % 2 == 0) {
            int max = evens.poll();
            res = Math.min(res, max - min);
            int newNum = max / 2;
            evens.offer(newNum);
            min = Math.min(min, newNum);
        }
        res = Math.min(evens.peek() - min, res);
        return res;
    }



    // V4
    // IDEA: PQ
    // https://leetcode.com/problems/minimize-deviation-in-array/solutions/3223829/java-easy-priority-queue-with-explanatio-kqf4/
    public int minimumDeviation_4(int[] nums) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int minValue = Integer.MAX_VALUE;
        for (int x : nums) {
            if ((x & 1) == 1)
                x <<= 1;
            pq.add(x);
            minValue = Math.min(minValue, x);
        }
        int minDeviation = Integer.MAX_VALUE;
        while (!pq.isEmpty()) {
            int curr = pq.poll();
            minDeviation = Math.min(minDeviation, curr - minValue);
            if ((curr & 1) == 1)
                break;
            curr >>= 1;
            minValue = Math.min(minValue, curr);
            pq.add(curr);
        }
        return minDeviation;
    }




}
