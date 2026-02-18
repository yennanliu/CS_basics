package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/single-number-ii/description/

import java.util.HashMap;
import java.util.Map;

/**
 *
 Code
 Testcase
 Testcase
 Test Result
 137. Single Number II
 Solved
 Medium
 Topics
 premium lock icon
 Companies
 Given an integer array nums where every element appears three times except for one, which appears exactly once. Find the single element and return it.

 You must implement a solution with a linear runtime complexity and use only constant extra space.



 Example 1:

 Input: nums = [2,2,3,2]
 Output: 3
 Example 2:

 Input: nums = [0,1,0,1,0,1,99]
 Output: 99


 Constraints:

 1 <= nums.length <= 3 * 104
 -231 <= nums[i] <= 231 - 1
 Each element in nums appears exactly three times except for one element which appears once.
 *
 *
 */
public class SingleNumber2 {

    // V0
//    public int singleNumber(int[] nums) {
//
//    }

    // V1-1
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/single-number-ii/solutions/3714928/bit-manipulation-c-java-python-beginner-l7m9l/
    public int singleNumber_1_1(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int x : nums) {
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }

        return -1;
    }


    // V1-2
    // IDEA: BIT OP
    // https://leetcode.com/problems/single-number-ii/solutions/3714928/bit-manipulation-c-java-python-beginner-l7m9l/
    public int singleNumber_1_2(int[] nums) {
        int ans = 0;

        for (int i = 0; i < 32; ++i) {
            int sum = 0;
            for (final int num : nums)
                sum += num >> i & 1;
            sum %= 3;
            ans |= sum << i;
        }

        return ans;
    }


    // V1-3
    // IDEA: MAGIC
    // https://leetcode.com/problems/single-number-ii/solutions/3714928/bit-manipulation-c-java-python-beginner-l7m9l/
    public int singleNumber_1_3(int[] nums) {
        int ones = 0;
        int twos = 0;

        for (final int num : nums) {
            ones ^= (num & ~twos);
            twos ^= (num & ~ones);
        }

        return ones;
    }


    // V2
    // IDEA: BIT OP
    // https://leetcode.com/problems/single-number-ii/solutions/43297/java-on-easy-to-understand-solution-easi-qoxr/
    public int singleNumber_2(int[] nums) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int sum = 0;
            for (int j = 0; j < nums.length; j++) {
                if (((nums[j] >> i) & 1) == 1) {
                    sum++;
                    sum %= 3;
                }
            }
            if (sum != 0) {
                ans |= sum << i;
            }
        }
        return ans;
    }



}
