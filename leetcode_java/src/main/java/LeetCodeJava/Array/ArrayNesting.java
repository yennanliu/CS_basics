package LeetCodeJava.Array;

// https://leetcode.com/problems/array-nesting/

/**
 *  565. Array Nesting
 *  Medium
 *
 *  You are given an integer array nums of length n where nums is a permutation
 *  of the numbers in the range [0, n - 1].
 *
 *  You should build a set s[k] = {nums[k], nums[nums[k]], nums[nums[nums[k]]], ... }
 *  subjected to the following rule:
 *    - The first element in s[k] starts with the selection of nums[k] of index = k.
 *    - The next element is nums[nums[k]], and then nums[nums[nums[k]]], and so on.
 *    - We stop adding right before a duplicate element occurs in s[k].
 *
 *  Return the longest length of a set s[k].
 *
 *  Example 1:
 *  Input: nums = [5,4,0,3,1,6,2]
 *  Output: 4
 *  Explanation: s[0] = {nums[0], nums[5], nums[6], nums[2]} = {5, 6, 2, 0}
 *
 *  Example 2:
 *  Input: nums = [0,1,2]
 *  Output: 1
 *
 *  Constraints:
 *  1 <= nums.length <= 10^5
 *  0 <= nums[i] < nums.length
 *  All the values of nums are unique.
 */
public class ArrayNesting {

    // V0
    // IDEA: the permutation decomposes into disjoint cycles; answer = longest cycle.
    //       Mark visited indices so each element is walked at most once.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int arrayNesting(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        boolean[] visited = new boolean[nums.length];
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }
            int cnt = 0;
            int idx = i;
            while (!visited[idx]) {
                visited[idx] = true;
                idx = nums[idx];
                cnt++;
            }
            res = Math.max(res, cnt);
        }
        return res;
    }

    // V1
    // IDEA: same cycle walk, but mark visited in-place (destroys input, O(1) space)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int arrayNesting_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 0) {
                continue;
            }
            int cnt = 0;
            int idx = i;
            while (nums[idx] >= 0) {
                int next = nums[idx];
                nums[idx] = -1;
                idx = next;
                cnt++;
            }
            res = Math.max(res, cnt);
        }
        return res;
    }
}
