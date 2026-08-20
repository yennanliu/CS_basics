package LeetCodeJava.Sort;

// https://leetcode.com/problems/arithmetic-subarrays/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  1630. Arithmetic Subarrays
 *  Medium
 *
 *  A sequence of numbers is called arithmetic if it consists of at least two
 *  elements, and the difference between every two consecutive elements is the
 *  same. More formally, a sequence s is arithmetic if and only if
 *  s[i+1] - s[i] == s[1] - s[0] for all valid i.
 *
 *  You are given an array of n integers, nums, and two arrays of m integers each,
 *  l and r, representing the m range queries, where the ith query is the range
 *  [l[i], r[i]]. All the arrays are 0-indexed.
 *
 *  Return a list of boolean elements answer, where answer[i] is true if the
 *  subarray nums[l[i]], nums[l[i]+1], ... , nums[r[i]] can be rearranged to form
 *  an arithmetic sequence, and false otherwise.
 *
 *  Example 1:
 *    Input: nums = [4,6,5,9,3,7], l = [0,0,2], r = [2,3,5]
 *    Output: [true,false,true]
 *    Explanation: [4,6,5] -> [4,5,6] is arithmetic; [4,6,5,9] is not;
 *                 [5,9,3,7] -> [3,5,7,9] is arithmetic.
 *
 *  Example 2:
 *    Input: nums = [-12,-9,-3,-12,-6,15,20,-25,-20,-15,-10],
 *           l = [0,1,6,4,8,7], r = [4,4,9,7,9,10]
 *    Output: [false,true,false,false,true,true]
 *
 *  Constraints:
 *    n == nums.length
 *    m == l.length
 *    m == r.length
 *    2 <= n <= 500
 *    1 <= m <= 500
 *    0 <= l[i] < r[i] < n
 *    -10^5 <= nums[i] <= 10^5
 */
public class ArithmeticSubarrays {

    // V0
    // IDEA: MIN / MAX / SET CHECK PER QUERY (no sorting needed)
    //       a multiset of k numbers can be rearranged into an arithmetic
    //       sequence iff, with lo = min and hi = max:
    //         - (hi - lo) is divisible by (k - 1) -> gives the step d
    //         - every term lo, lo + d, ..., hi is present
    //       a set silently rejects duplicates when d != 0, because a duplicate
    //       means some required term is missing. d == 0 (all equal) is legal.
    /**
     * time = O(m * n)
     * space = O(n)
     */
    public List<Boolean> checkArithmeticSubarrays(int[] nums, int[] l, int[] r) {
        List<Boolean> res = new ArrayList<>();
        for (int q = 0; q < l.length; q++) {
            res.add(ok(nums, l[q], r[q]));
        }
        return res;
    }

    private boolean ok(int[] nums, int from, int to) {
        int k = to - from + 1;
        int lo = Integer.MAX_VALUE;
        int hi = Integer.MIN_VALUE;
        Set<Integer> seen = new HashSet<>();
        for (int i = from; i <= to; i++) {
            lo = Math.min(lo, nums[i]);
            hi = Math.max(hi, nums[i]);
            seen.add(nums[i]);
        }

        int span = hi - lo;
        if (span % (k - 1) != 0) {
            return false;
        }
        int d = span / (k - 1);
        if (d == 0) {
            return seen.size() == 1;
        }
        for (int t = 0; t < k; t++) {
            if (!seen.contains(lo + t * d)) {
                return false;
            }
        }
        return true;
    }
}
