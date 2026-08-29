package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/set-mismatch/

/**
 *  645. Set Mismatch
 *  Easy
 *
 *  You have a set of integers s, which originally contains all the numbers from
 *  1 to n. Unfortunately, due to some error, one of the numbers in s got
 *  duplicated to another number in the set, which results in repetition of one
 *  number and loss of another number.
 *
 *  You are given an integer array nums representing the data status of this set
 *  after the error.
 *
 *  Find the number that occurs twice and the number that is missing and return
 *  them in the form of an array.
 *
 *  Example 1:
 *  Input: nums = [1,2,2,4]
 *  Output: [2,3]
 *
 *  Example 2:
 *  Input: nums = [1,1]
 *  Output: [1,2]
 *
 *  Constraints:
 *  2 <= nums.length <= 10^4
 *  1 <= nums[i] <= 10^4
 */
public class SetMismatch {

    // V0
    // IDEA: counting array - the value seen twice is the duplicate, the value
    //       never seen is the missing one
    /**
     * time = O(n)
     * space = O(n)
     */
    public int[] findErrorNums(int[] nums) {
        int n = nums.length;
        int[] cnt = new int[n + 1];
        for (int num : nums) {
            cnt[num]++;
        }
        int dup = -1;
        int missing = -1;
        for (int v = 1; v <= n; v++) {
            if (cnt[v] == 2) {
                dup = v;
            } else if (cnt[v] == 0) {
                missing = v;
            }
        }
        return new int[] { dup, missing };
    }

    // V1
    // IDEA: index marking (negate nums[|v| - 1]); a value already negative marks
    //       the duplicate, and the remaining positive slot marks the missing one
    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] findErrorNums_1(int[] nums) {
        int dup = -1;
        for (int num : nums) {
            int idx = Math.abs(num) - 1;
            if (nums[idx] < 0) {
                dup = Math.abs(num);
            } else {
                nums[idx] = -nums[idx];
            }
        }
        int missing = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                missing = i + 1;
            }
            // restore
            nums[i] = Math.abs(nums[i]);
        }
        return new int[] { dup, missing };
    }

    // V2
    // IDEA: XOR the array with 1..n -> dup ^ missing. The lowest set bit of that XOR
    //       is a bit where the two differ, so bucket BOTH the array and 1..n by that
    //       bit; each bucket XORs down to one candidate. A final scan says which of
    //       the two candidates actually occurs in nums (that one is the duplicate).
    /**
     * time = O(n)
     * space = O(1)
     */
    public int[] findErrorNums_2(int[] nums) {
        int n = nums.length;
        int xorAll = 0;
        for (int num : nums) {
            xorAll ^= num;
        }
        for (int v = 1; v <= n; v++) {
            xorAll ^= v;
        }

        int diffBit = xorAll & (-xorAll);

        int a = 0;
        int b = 0;
        for (int num : nums) {
            if ((num & diffBit) != 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        for (int v = 1; v <= n; v++) {
            if ((v & diffBit) != 0) {
                a ^= v;
            } else {
                b ^= v;
            }
        }

        for (int num : nums) {
            if (num == a) {
                return new int[] { a, b };
            }
        }
        return new int[] { b, a };
    }
}
