package LeetCodeJava.Array;

// https://leetcode.com/problems/sort-array-by-parity-ii/

/**
 *  922. Sort Array By Parity II
 *  Easy
 *
 *  Given an array of integers nums, half of the integers in nums are odd, and the
 *  other half are even.
 *
 *  Sort the array so that whenever nums[i] is odd, i is odd, and whenever nums[i]
 *  is even, i is even.
 *
 *  Return any answer array that satisfies this condition.
 *
 *  Example 1:
 *  Input: nums = [4,2,5,7]
 *  Output: [4,5,2,7]
 *  Explanation: [4,7,2,5], [2,5,4,7], [2,7,4,5] would also be accepted.
 *
 *  Example 2:
 *  Input: nums = [2,3]
 *  Output: [2,3]
 *
 *  Constraints:
 *   - 2 <= nums.length <= 2 * 10^4
 *   - nums.length is even.
 *   - Half of the integers in nums are even.
 *   - 0 <= nums[i] <= 1000
 */
public class SortArrayByParityII {

    // V0
    // IDEA: in-place two pointers. `i` walks even indexes, `j` walks odd indexes;
    //       when an even slot holds an odd value, find the next odd slot holding
    //       an even value and swap them.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] sortArrayByParityII(int[] nums) {
        int n = nums.length;
        int j = 1;
        for (int i = 0; i < n; i += 2) {
            if (nums[i] % 2 != 0) {
                while (nums[j] % 2 != 0) {
                    j += 2;
                }
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
            }
        }
        return nums;
    }

    // V1
    // IDEA: extra output array - stream the input once, dropping even values on
    //       even slots (0,2,4...) and odd values on odd slots (1,3,5...).
    /**
     * time = O(n)
     * space = O(n)
     */
    public int[] sortArrayByParityII_1(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        int even = 0;
        int odd = 1;
        for (int v : nums) {
            if (v % 2 == 0) {
                res[even] = v;
                even += 2;
            } else {
                res[odd] = v;
                odd += 2;
            }
        }
        return res;
    }

    // V2
    // IDEA: brute force O(n^2) - for every badly placed index, linear scan for a
    //       partner that is badly placed the other way and swap them.
    //       Kept as a readable correctness reference.
    /**
     * time = O(n^2)
     * space = O(n) (works on a copy so the input is untouched)
     */
    public int[] sortArrayByParityII_2(int[] nums) {
        int[] res = nums.clone();
        int n = res.length;
        for (int i = 0; i < n; i++) {
            if (res[i] % 2 == i % 2) {
                continue; // already fine
            }
            for (int j = i + 1; j < n; j++) {
                // res[j] is misplaced too, and its value fits index i
                if (res[j] % 2 != j % 2 && res[j] % 2 == i % 2) {
                    int tmp = res[i];
                    res[i] = res[j];
                    res[j] = tmp;
                    break;
                }
            }
        }
        return res;
    }

}
