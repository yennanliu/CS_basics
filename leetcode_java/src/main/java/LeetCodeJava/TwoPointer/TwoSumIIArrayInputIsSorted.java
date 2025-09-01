package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
/**
 * 167. Two Sum II - Input Array Is Sorted
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.
 *
 * Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.
 *
 * The tests are generated such that there is exactly one solution. You may not use the same element twice.
 *
 * Your solution must use only constant extra space.
 *
 *
 *
 * Example 1:
 *
 * Input: numbers = [2,7,11,15], target = 9
 * Output: [1,2]
 * Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2. We return [1, 2].
 * Example 2:
 *
 * Input: numbers = [2,3,4], target = 6
 * Output: [1,3]
 * Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3. We return [1, 3].
 * Example 3:
 *
 * Input: numbers = [-1,0], target = -1
 * Output: [1,2]
 * Explanation: The sum of -1 and 0 is -1. Therefore index1 = 1, index2 = 2. We return [1, 2].
 *
 *
 * Constraints:
 *
 * 2 <= numbers.length <= 3 * 104
 * -1000 <= numbers[i] <= 1000
 * numbers is sorted in non-decreasing order.
 * -1000 <= target <= 1000
 * The tests are generated such that there is exactly one solution.
 *
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSumIIArrayInputIsSorted {

    class Solution {

        // V0
        // IDEA : 2 POINTERS
        // NOTE !!! we use `2 pointers` but NOT binary search
        public int[] twoSum(int[] numbers, int target) {

            if (numbers.length == 0 || numbers.equals(null)) {
                return null;
            }

            int left = 0;
            int right = numbers.length - 1;
            int[] ans = new int[2];
            //System.out.println("numbers = " + Arrays.toString(numbers));
            /**
             *  NOTE !!!
             *
             *   use `r >= l`
             *   since there is a case that candidate could be between l and r
             *   e.g. given `xxxxLxRxxxx` status, the answer could be as:
             *      1. [l, l+1]
             *      2. [r-1, r]
             */
            while (right >= left) {
                int cur = numbers[left] + numbers[right];
                if (cur == target) {
                    ans[0] = left + 1;
                    ans[1] = right + 1;
                    return ans;
                } else if (cur > target) {
                    //right = mid;
                    right -= 1;
                } else {
                    //left = mid + 1;
                    left += 1;
                }
            }

            ans[0] = -1;
            ans[1] = -1;
            return ans;
        }
    }

    // V0-1
    // IDEA: HASHMAP (001 2 sum)
    public int[] twoSum_0_1(int[] numbers, int target) {
        // { val: idx}
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            // a + b = target
            // -> a = target - b
            int diff = target - numbers[i];
            if (map.containsKey(diff) && map.get(diff) != i) {
                return new int[] { map.get(diff) + 1, i + 1 };
            }
            // if not found, update map
            map.put(numbers[i], i);
        }

        return new int[] { -1, -1 }; // if not found
    }

    // V0-2
    // IDEA: HASHMAP
    public int[] twoSum_0_2(int[] numbers, int target) {
        // edge
        if (numbers == null || numbers.length == 0) {
            return null;
        }
        if (numbers.length == 1) {
            return null;
        }

        int[] res = new int[2];

        // {val : idx}
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            // x + y = t
            // y = t - x
            int x = numbers[i];
            int diff = target - x;
            if (map.containsKey(diff)) {
                res[0] = map.get(diff);
                res[1] = i + 1;
                return res;
            }
            map.put(x, i + 1);
        }

        return res;
    }


}
