package LeetCodeJava.Math;

// https://leetcode.com/problems/find-unique-binary-string/description/

/**
 * 1980. Find Unique Binary String
 * Medium
 *
 * Given an array of strings nums containing n unique binary strings each of length n, return a binary string of length n that does not appear in nums. If there are multiple answers, you may return any of them.
 *
 *
 * Example 1:
 *
 * Input: nums = ["01","10"]
 * Output: "11"
 * Explanation: "11" does not appear in nums. "00" would also be correct.
 *
 * Example 2:
 *
 * Input: nums = ["00","01"]
 * Output: "11"
 * Explanation: "11" does not appear in nums. "10" would also be correct.
 *
 * Example 3:
 *
 * Input: nums = ["111","011","001"]
 * Output: "101"
 * Explanation: "101" does not appear in nums. "000", "010", "100", and "110" would also be correct.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 16
 * nums[i].length == n
 * nums[i] is either '0' or '1'.
 *
 */
public class FindUniqueBinaryString {

    // V0
    // IDEA: CANTOR DIAGONAL - flip the i-th bit of the i-th string
    //  the result differs from nums[i] at position i for every i, so it is in none of them.
    /**
     * time = O(N)
     * space = O(N) for the answer
     */
    public String findDifferentBinaryString(String[] nums) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < nums.length; i++) {
            char diagonal = nums[i].charAt(i);
            if (diagonal == '0') {
                result.append('1');
            } else {
                result.append('0');
            }
        }
        return result.toString();
    }
}
