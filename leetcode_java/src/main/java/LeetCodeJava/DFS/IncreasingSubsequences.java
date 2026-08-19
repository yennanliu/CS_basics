package LeetCodeJava.DFS;

// https://leetcode.com/problems/increasing-subsequences/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  491. Non-decreasing Subsequences (a.k.a Increasing Subsequences)
 *  Medium
 *
 *  Given an integer array nums, return all the different possible non-decreasing subsequences
 *  of the given array with at least two elements. You may return the answer in any order.
 *
 *  Example 1:
 *  Input: nums = [4,6,7,7]
 *  Output: [[4,6],[4,6,7],[4,6,7,7],[4,7],[4,7,7],[6,7],[6,7,7],[7,7]]
 *
 *  Example 2:
 *  Input: nums = [4,4,3,2,1]
 *  Output: [[4,4]]
 *
 *  Constraints:
 *  1 <= nums.length <= 15
 *  -100 <= nums[i] <= 100
 */
public class IncreasingSubsequences {

    // V0
    // IDEA: BACKTRACK + per-level `used` set to skip duplicated picks at the same depth
    //       (can NOT sort the array, order must be preserved)
    /**
     * time = O(2^n * n)
     * space = O(2^n * n)
     */
    public List<List<Integer>> findSubsequences(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 2) {
            return res;
        }
        backtrack(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private void backtrack(int[] nums, int start, List<Integer> path, List<List<Integer>> res) {
        if (path.size() >= 2) {
            res.add(new ArrayList<>(path));
        }
        // values are in [-100, 100] -> a set per recursion level kills duplicates
        Set<Integer> usedAtThisLevel = new HashSet<>();
        for (int i = start; i < nums.length; i++) {
            if (!path.isEmpty() && nums[i] < path.get(path.size() - 1)) {
                continue;
            }
            if (usedAtThisLevel.contains(nums[i])) {
                continue;
            }
            usedAtThisLevel.add(nums[i]);
            path.add(nums[i]);
            backtrack(nums, i + 1, path, res);
            path.remove(path.size() - 1);
        }
    }

    // V1
    // IDEA: DFS + global HashSet dedup (simpler, but stores every candidate)
    /**
     * time = O(2^n * n)
     * space = O(2^n * n)
     */
    public List<List<Integer>> findSubsequences_1(int[] nums) {
        Set<List<Integer>> seen = new HashSet<>();
        if (nums == null || nums.length < 2) {
            return new ArrayList<>();
        }
        dfs(nums, 0, new ArrayList<>(), seen);
        return new ArrayList<>(seen);
    }

    private void dfs(int[] nums, int start, List<Integer> path, Set<List<Integer>> seen) {
        if (path.size() >= 2) {
            seen.add(new ArrayList<>(path));
        }
        for (int i = start; i < nums.length; i++) {
            if (!path.isEmpty() && nums[i] < path.get(path.size() - 1)) {
                continue;
            }
            path.add(nums[i]);
            dfs(nums, i + 1, path, seen);
            path.remove(path.size() - 1);
        }
    }

    // V2
    // IDEA: BITMASK enumeration - n <= 15, so just walk all 2^n subsets, keep the ones that
    //       are non-decreasing and have >= 2 elements, dedup with a HashSet. No recursion.
    /**
     * time = O(2^n * n)
     * space = O(2^n * n)
     */
    public List<List<Integer>> findSubsequences_2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 2) {
            return res;
        }
        int n = nums.length;
        Set<List<Integer>> seen = new HashSet<>();
        for (int mask = 0; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) < 2) {
                continue;
            }
            List<Integer> cand = new ArrayList<>();
            boolean nonDecreasing = true;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    continue;
                }
                if (!cand.isEmpty() && nums[i] < cand.get(cand.size() - 1)) {
                    nonDecreasing = false;
                    break;
                }
                cand.add(nums[i]);
            }
            if (nonDecreasing && seen.add(cand)) {
                res.add(cand);
            }
        }
        return res;
    }
}
