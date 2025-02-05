package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combination-sum-ii/
/**
 * 40. Combination Sum II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target.
 *
 * Each number in candidates may only be used once in the combination.
 *
 * Note: The solution set must not contain duplicate combinations.
 *
 *
 *
 * Example 1:
 *
 * Input: candidates = [10,1,2,7,6,1,5], target = 8
 * Output:
 * [
 * [1,1,6],
 * [1,2,5],
 * [1,7],
 * [2,6]
 * ]
 * Example 2:
 *
 * Input: candidates = [2,5,2,1,2], target = 5
 * Output:
 * [
 * [1,2,2],
 * [5]
 * ]
 *
 *
 * Constraints:
 *
 * 1 <= candidates.length <= 100
 * 1 <= candidates[i] <= 50
 * 1 <= target <= 30
 *
 *
 */
import java.util.*;

public class CombinationSum2 {

    // V0
    // IDEA : BACKTRACK
    // TODO : fix below TLE error
//    List<List<Integer>> comSumRes = new ArrayList<>();
//
//    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
//        // Edge case check for empty array or null input
//        if (candidates == null || candidates.length == 0) {
//            return new ArrayList<>();
//        }
//
//        // Sort candidates to handle duplicates efficiently
//        Arrays.sort(candidates);
//
//        HashSet<Integer> usedIdx = new HashSet<>();
//        // Backtracking step
//        backTrack(candidates, target, new ArrayList<>(), 0, usedIdx);
//        return comSumRes;
//    }
//
//    private void backTrack(int[] candidates, int target, List<Integer> cache, int startIdx, HashSet<Integer> usedIdx) {
//        int sum = getSum(cache);
//
//        // If the sum of the current combination equals target, add it to the result
//        if (sum == target) {
//            // ???
//            Collections.sort(cache);
//            if(!comSumRes.contains(cache)){
//                comSumRes.add(new ArrayList<>(cache)); // Store a copy to avoid reference issues
//            }
//            return;
//        }
//
//        // If the sum exceeds target, stop the recursion
//        if (sum > target) {
//            return;
//        }
//
//        // Loop over the candidates starting from the current index
//        for (int i = startIdx; i < candidates.length; i++) {
//            // Skip duplicates: if the current element is the same as the previous, skip it
////            if (i > startIdx && candidates[i] == candidates[i - 1]) {
////                continue;
////            }
//
//            if (usedIdx.contains(i)) {
//                continue;
//            }
//
//            usedIdx.add(i);
//            cache.add(candidates[i]); // Choose current candidate
//            backTrack(candidates, target, cache, i + 1, usedIdx); // Move to the next index (no repetition of the same element)
//            usedIdx.remove(i);
//            cache.remove(cache.size() - 1); // Undo the choice (backtrack)
//        }
//    }
//
//    private int getSum(List<Integer> cache) {
//        int res = 0;
//        for (int x : cache) {
//            res += x;
//        }
//        return res;
//    }

    // V0-1
    // IDEA: LC 39 + check duplicated (GPT)
    List<List<Integer>> comSumRes = new ArrayList<>();

    public List<List<Integer>> combinationSum2_0_1(int[] candidates, int target) {
        // Edge case check for empty array or null input
        if (candidates == null || candidates.length == 0) {
            return new ArrayList<>();
        }

        // Sort candidates to handle duplicates efficiently
        Arrays.sort(candidates);

        // Backtracking step
        backTrack(candidates, target, new ArrayList<>(), 0);
        return comSumRes;
    }

    private void backTrack(int[] candidates, int target, List<Integer> cache, int startIdx) {
        int sum = getSum(cache);

        // If the sum of the current combination equals target, add it to the result
        if (sum == target) {
            comSumRes.add(new ArrayList<>(cache)); // Store a copy to avoid reference issues
            return;
        }

        // If the sum exceeds target, stop the recursion
        if (sum > target) {
            return;
        }

        // Loop over the candidates starting from the current index
        for (int i = startIdx; i < candidates.length; i++) {
            // Skip duplicates: if the current element is the same as the previous, skip it
            if (i > startIdx && candidates[i] == candidates[i - 1]) {
                continue;
            }

            cache.add(candidates[i]); // Choose current candidate
            backTrack(candidates, target, cache, i + 1); // Move to the next index (no repetition of the same element)
            cache.remove(cache.size() - 1); // Undo the choice (backtrack)
        }
    }

    private int getSum(List<Integer> cache) {
        int res = 0;
        for (int x : cache) {
            res += x;
        }
        return res;
    }

    // V1
    // IDEA : Backtracking with Counters
    // https://leetcode.com/problems/combination-sum-ii/editorial/
    public List<List<Integer>> combinationSum2_1(int[] candidates, int target) {
        // container to hold the final combinations
        List<List<Integer>> results = new ArrayList<>();
        LinkedList<Integer> comb = new LinkedList<>();

        HashMap<Integer, Integer> counter = new HashMap<>();
        for (int candidate : candidates) {
            if (counter.containsKey(candidate))
                counter.put(candidate, counter.get(candidate) + 1);
            else
                counter.put(candidate, 1);
        }

        // convert the counter table to a list of (num, count) tuples
        List<int[]> counterList = new ArrayList<>();
        counter.forEach((key, value) -> {
            counterList.add(new int[]{key, value});
        });

        backtrack(comb, target, 0, counterList, results);
        return results;
    }

    private void backtrack(LinkedList<Integer> comb,
                           int remain, int curr,
                           List<int[]> counter,
                           List<List<Integer>> results) {

        if (remain <= 0) {
            if (remain == 0) {
                // make a deep copy of the current combination.
                results.add(new ArrayList<Integer>(comb));
            }
            return;
        }

        for (int nextCurr = curr; nextCurr < counter.size(); ++nextCurr) {
            int[] entry = counter.get(nextCurr);
            Integer candidate = entry[0], freq = entry[1];

            if (freq <= 0)
                continue;

            // add a new element to the current combination
            comb.addLast(candidate);
            counter.set(nextCurr, new int[]{candidate, freq - 1});

            // continue the exploration with the updated combination
            backtrack(comb, remain - candidate, nextCurr, counter, results);

            // backtrack the changes, so that we can try another candidate
            counter.set(nextCurr, new int[]{candidate, freq});
            comb.removeLast();
        }
    }

    // V2
    // IDEA : Backtracking with Index
    // https://leetcode.com/problems/combination-sum-ii/editorial/
    public List<List<Integer>> combinationSum2_2(int[] candidates, int target) {
        List<List<Integer>> results = new ArrayList<>();
        LinkedList<Integer> comb = new LinkedList<>();

        Arrays.sort(candidates);

        backtrack(candidates, comb, target, 0, results);
        return results;
    }

    private void backtrack(int[] candidates, LinkedList<Integer> comb,
                           Integer remain, Integer curr,
                           List<List<Integer>> results) {
        if (remain == 0) {
            // copy the current combination to the final list.
            results.add(new ArrayList<Integer>(comb));
            return;
        }

        for (int nextCurr = curr; nextCurr < candidates.length; ++nextCurr) {
            if (nextCurr > curr && candidates[nextCurr] == candidates[nextCurr - 1])
                continue;

            Integer pick = candidates[nextCurr];
            // optimization: early stopping
            if (remain - pick < 0)
                break;

            comb.addLast(pick);
            backtrack(candidates, comb, remain - pick, nextCurr + 1, results);
            comb.removeLast();
        }
    }

    // V3
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> combinationSum2_3(int[] nums, int target) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(list, new ArrayList<>(), nums, target, 0);
        return list;

    }

    private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int remain, int start) {
        if (remain < 0) return;
        else if (remain == 0) list.add(new ArrayList<>(tempList));
        else {
            for (int i = start; i < nums.length; i++) {
                if (i > start && nums[i] == nums[i - 1]) continue; // skip duplicates
                tempList.add(nums[i]);
                backtrack(list, tempList, nums, remain - nums[i], i + 1);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    // V4
    // https://www.youtube.com/watch?v=rSA3t6BDDwg
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0040-combination-sum-ii.java
    public List<List<Integer>> combinationSum2_4(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        List<Integer> ls = new ArrayList<Integer>();
        comb(candidates, target, ans, ls, 0);
        return ans;
    }

    public void comb(
            int[] candidates,
            int target,
            List<List<Integer>> ans,
            List<Integer> ls,
            int index
    ) {
        if (target == 0) {
            ans.add(new ArrayList(ls));
        } else if (target < 0) return; else {
            for (int i = index; i < candidates.length; i++) {
                if (i > index && candidates[i] == candidates[i - 1]) continue;
                ls.add(candidates[i]);
                comb(candidates, target - candidates[i], ans, ls, i + 1);
                ls.remove(ls.get(ls.size() - 1));
            }
        }
    }

}
