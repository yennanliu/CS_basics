package LeetCodeJava.Array;

// https://leetcode.com/problems/sort-array-by-parity/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  905. Sort Array By Parity
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, move all the even integers at the beginning of the array followed by all the odd integers.
 *
 * Return any array that satisfies this condition.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,2,4]
 * Output: [2,4,3,1]
 * Explanation: The outputs [4,2,3,1], [2,4,1,3], and [4,2,1,3] would also be accepted.
 * Example 2:
 *
 * Input: nums = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5000
 * 0 <= nums[i] <= 5000
 *
 *
 *
 */
public class SortArrayByParity {

    // V0
    public int[] sortArrayByParity(int[] nums) {
        // edge
        if (nums == null || nums.length <= 1) {
            return nums;
        }

        List<Integer> list = new ArrayList<>();
        for (int x : nums) {
            list.add(x);
        }

        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                /** NOTE !!!
                 *
                 *  0.
                 *
                 *   - if we want to put o1 BEFORE o2,
                 *     `isEven1 && !isEven2` should return -1
                 *
                 *   - if we want to put o1 AFTER o2,
                 *       `!isEven1 && isEven2` should return 1
                 *
                 *
                 *  1.   below, how we do custom order
                 *   by return custom val (e.g. 1 or -1)
                 *
                 *  2.  In a Java Comparator:
                 *
                 *    negative → o1 comes before o2
                 *    positive → o1 comes after o2
                 *    0 → order unchanged
                 *
                 */
                boolean isEven1 = o1 % 2 == 0;
                boolean isEven2 = o2 % 2 == 0;
                //return o1.compareTo(o2); // ????
                if (isEven1 && !isEven2) {
                    return -1;
                } else if (!isEven1 && isEven2) {
                    return 1;
                }
                return 0;
            }
        });

        // prepare res
        int[] res = new int[list.size()];
        int i = 0;
        for (int x : list) {
            res[i] = x; //list.get(i);
            i += 1;
        }

        return res;
    }
    // V0-1
    // IDEA: CUSTOM SORT (gpt)
    public int[] sortArrayByParity_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length <= 1) {
            return nums;
        }

        List<Integer> list = new ArrayList<>();
        for (int x : nums) {
            list.add(x);
        }

        // custom sort
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {

                /** NOTE !!!
                 *
                 *  1.   below, how we do custom order
                 *   by return custom val (e.g. 1 or -1)
                 *
                 *  2.
                 *
                 *  In a Java Comparator:
                 *
                 *    negative → o1 comes before o2
                 *    positive → o1 comes after o2
                 *    0 → order unchanged
                 *
                 */
                // o1 even, o2 odd -> o1 should come first
                if (o1 % 2 == 0 && o2 % 2 == 1) {
                    return -1;
                }

                // o1 odd, o2 even -> o2 should come first
                if (o1 % 2 == 1 && o2 % 2 == 0) {
                    return 1;
                }

                // both even or both odd
                return 0;
            }
        });

        int[] res = new int[list.size()];
        int i = 0;
        for (int x : list) {
            res[i++] = x;
        }

        return res;
    }



    // V0-2
    // IDEA: CUSTOM SORT (gemini)
    public int[] sortArrayByParity_0_2(int[] nums) {
        if (nums == null || nums.length <= 1)
            return nums;

        List<Integer> list = new ArrayList<>();
        for (int x : nums)
            list.add(x);

        Collections.sort(list, (o1, o2) -> {
            int v1 = o1 % 2;
            int v2 = o2 % 2;
            // Integer.compare returns:
            // -1 if v1 < v2 (o1 is even 0, o2 is odd 1) -> Correct!
            //  0 if same parity
            //  1 if v1 > v2 (o1 is odd 1, o2 is even 0) -> Correct!
            return Integer.compare(v1, v2);
        });

        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++)
            res[i] = list.get(i);
        return res;
    }


    // V0-2
    // IDEA: 2 POINTERS (gpt)
    public int[] sortArrayByParity_0_3(int[] nums) {
        int l = 0, r = nums.length - 1;

        while (l < r) {
            if (nums[l] % 2 > nums[r] % 2) {
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
            }

            if (nums[l] % 2 == 0)
                l++;
            if (nums[r] % 2 == 1)
                r--;
        }

        return nums;
    }



    // V1
    // https://leetcode.com/problems/sort-array-by-parity/solutions/6280114/video-how-we-think-about-a-solution-one-0ks18/
    public int[] sortArrayByParity_1(int[] nums) {
        int left = 0;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 0) {
                int temp = nums[left];
                nums[left] = nums[i];
                nums[i] = temp;
                left++;
            }
        }

        return nums;
    }

    // V2
    // https://leetcode.com/problems/sort-array-by-parity/solutions/170734/cjava-in-place-swap-by-lee215-yf1d/
    public int[] sortArrayByParity_2(int[] A) {
        for (int i = 0, j = 0; j < A.length; j++)
            if (A[j] % 2 == 0) {
                int tmp = A[i];
                A[i++] = A[j];
                A[j] = tmp;
                ;
            }
        return A;
    }


    // V3




}
