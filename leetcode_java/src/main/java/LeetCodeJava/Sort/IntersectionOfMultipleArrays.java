package LeetCodeJava.Sort;

// https://leetcode.com/problems/intersection-of-multiple-arrays/

import java.util.ArrayList;
import java.util.List;

/**
 *  2248. Intersection of Multiple Arrays
 *  Easy
 *
 *  Given a 2D integer array nums where nums[i] is a non-empty array of distinct
 *  positive integers, return the list of integers that are present in each array
 *  of nums sorted in ascending order.
 *
 *  Example 1:
 *    Input: nums = [[3,1,2,4,5],[1,2,3,4],[3,4,5,6]]
 *    Output: [3,4]
 *    Explanation: The only integers present in each of nums[0] = [3,1,2,4,5],
 *                 nums[1] = [1,2,3,4], and nums[2] = [3,4,5,6] are 3 and 4.
 *
 *  Example 2:
 *    Input: nums = [[1,2,3],[4,5,6]]
 *    Output: []
 *    Explanation: There does not exist any integer present both in nums[0] and nums[1].
 *
 *  Constraints:
 *    1 <= nums.length <= 1000
 *    1 <= sum(nums[i].length) <= 1000
 *    1 <= nums[i][j] <= 1000
 *    The elements in nums[i] are distinct.
 */
public class IntersectionOfMultipleArrays {

    // V0
    // IDEA: COUNTING (values bounded by 1000, rows hold DISTINCT values)
    //       since a row never repeats a value, a value present in every row is
    //       exactly a value whose total occurrence count equals nums.length.
    //       scanning the value range 1..1000 in order gives the ascending output
    //       for free (no sort needed).
    /**
     * time = O(TOTAL + M)   // TOTAL = sum of row lengths, M = 1000
     * space = O(M)
     */
    public List<Integer> intersection(int[][] nums) {
        int[] cnt = new int[1001];
        for (int[] row : nums) {
            for (int v : row) {
                cnt[v]++;
            }
        }

        List<Integer> res = new ArrayList<>();
        for (int v = 1; v <= 1000; v++) {
            if (cnt[v] == nums.length) {
                res.add(v);
            }
        }
        return res;
    }
}
