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
    // time: O(2^N), space: O(2^N)
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
      /**
       *  NOTE !!!   skip a duplicate at the same recursive level.
       *
       *   -> Key idea of the duplicate-skipping logic
       * 	   •	We do not skip all duplicates.
       * 	   •	We only skip a duplicate at the same recursive level.
       */
      /**
       *  Example :
       *
       *
       *  That's a fantastic follow-up — and exactly the kind of case that the duplicate-skipping logic is carefully crafted to handle.
       *
       * Let's walk through why [1,1,6] is valid, even though we skip duplicates in:
       *
       * if (i > startIdx && candidates[i] == candidates[i - 1]) {
       *     continue;
       * }
       *
       *

       * ✅ Input: [10, 1, 2, 7, 6, 1, 5], target = 8
       *
       * First step is to sort the array:
       *
       * [1, 1, 2, 5, 6, 7, 10]
       *
       * ⸻
       *
       * 🧠 Key idea of the duplicate-skipping logic
       * 	•	We do not skip all duplicates.
       * 	•	We only skip a duplicate at the same recursive level.
       *
       * ⸻
       *
       * 🔁 Let's simulate the recursion that finds [1,1,6]
       *
       * Initial call: startIdx = 0, cache = []
       *
       * Level 0: i = 0
       * 	•	pick 1, cache = [1], recurse with startIdx = 1
       *
       * Level 1: i = 1
       * 	•	pick 1 (again), cache = [1, 1], recurse with startIdx = 2
       *
       * Level 2: i = 4
       * 	•	pick 6, cache = [1, 1, 6], sum = 8 → ✅ add to result
       *
       * This is allowed because:
       * 	•	Even though candidates[1] == candidates[0] == 1, we are at a different level of recursion (startIdx = 1 vs 0).
       * 	•	At each level, the first occurrence of a duplicate is allowed, we only skip the second if it comes at the same level.
       *
       * ⸻
       *
       * 🛡️ That means:
       *
       * if (i > startIdx && candidates[i] == candidates[i - 1]) {
       *     continue;
       * }
       *
       * 	•	prevents picking the same value at the same level multiple times
       * 	•	but allows using the same value in deeper recursive levels
       *
       *   ->
       *      ✅ We can get [1,1,6] because the two 1s are picked in different recursive calls.
       *      ❌ We prevent duplicate [1,2,5] combinations where the same 1 appears multiple times at the same level.
       *
       */
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

    // V1-1
    // https://neetcode.io/problems/combination-target-sum-ii
    // IDEA: BRUTE FORCE
    // time: O(2^N), space: O(2^N)
    private Set<List<Integer>> res;

    public List<List<Integer>> combinationSum2_1_1(int[] candidates, int target) {
        res = new HashSet<>();
        Arrays.sort(candidates);
        generateSubsets(candidates, target, 0, new ArrayList<>(), 0);
        return new ArrayList<>(res);
    }

    private void generateSubsets(int[] candidates, int target, int i, List<Integer> cur, int total) {
        if (total == target) {
            res.add(new ArrayList<>(cur));
            return;
        }
        if (total > target || i == candidates.length) {
            return;
        }

        cur.add(candidates[i]);
        generateSubsets(candidates, target, i + 1, cur, total + candidates[i]);
        cur.remove(cur.size() - 1);

        generateSubsets(candidates, target, i + 1, cur, total);
    }

    // V1-2
    // https://neetcode.io/problems/combination-target-sum-ii
    // IDEA: BACKTRACK
    // time: O(2^N), space: O(2^N)
    private List<List<Integer>> res_1_2;

    public List<List<Integer>> combinationSum2_1_2(int[] candidates, int target) {
        res_1_2 = new ArrayList<>();
        Arrays.sort(candidates);
        dfs(candidates, target, 0, new ArrayList<>(), 0);
        return res_1_2;
    }

    private void dfs(int[] candidates, int target, int i, List<Integer> cur, int total) {
        if (total == target) {
            res_1_2.add(new ArrayList<>(cur));
            return;
        }
        if (total > target || i == candidates.length) {
            return;
        }

        cur.add(candidates[i]);
        dfs(candidates, target, i + 1, cur, total + candidates[i]);
        cur.remove(cur.size() - 1);

        while (i + 1 < candidates.length && candidates[i] == candidates[i + 1]) {
            i++;
        }
        dfs(candidates, target, i + 1, cur, total);
    }

    // V1-3
    // https://neetcode.io/problems/combination-target-sum-ii
    // IDEA: Backtracking (Hash Map)
    // time: O(2^N), space: O(2^N)
    List<List<Integer>> res_1_3 = new ArrayList<>();
    Map<Integer, Integer> count = new HashMap<>();

    public List<List<Integer>> combinationSum2_1_3(int[] nums, int target) {
        List<Integer> cur = new ArrayList<>();
        List<Integer> A = new ArrayList<>();

        for (int num : nums) {
            if (!count.containsKey(num)) {
                A.add(num);
            }
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        backtrack(A, target, cur, 0);
        return res_1_3;
    }

    private void backtrack(List<Integer> nums, int target, List<Integer> cur, int i) {
        if (target == 0) {
            res_1_3.add(new ArrayList<>(cur));
            return;
        }
        if (target < 0 || i >= nums.size()) {
            return;
        }

        if (count.get(nums.get(i)) > 0) {
            cur.add(nums.get(i));
            count.put(nums.get(i), count.get(nums.get(i)) - 1);
            backtrack(nums, target - nums.get(i), cur, i);
            count.put(nums.get(i), count.get(nums.get(i)) + 1);
            cur.remove(cur.size() - 1);
        }

        backtrack(nums, target, cur, i + 1);
    }

    // V1-4
    // https://neetcode.io/problems/combination-target-sum-ii
    // IDEA: Backtracking (Optimal)
    // time: O(2^N), space: O(2^N)
    private static List<List<Integer>> res_1_4 = new ArrayList<>();

    public List<List<Integer>> combinationSum2_1_4(int[] candidates, int target) {
        res_1_4.clear();
        Arrays.sort(candidates);
        dfs(0, new ArrayList<>(), 0, candidates, target);
        return res_1_4;
    }

    private static void dfs(int idx, List<Integer> path, int cur, int[] candidates, int target) {
        if (cur == target) {
            res_1_4.add(new ArrayList<>(path));
            return;
        }
        for (int i = idx; i < candidates.length; i++) {
            if (i > idx && candidates[i] == candidates[i - 1]) {
                continue;
            }
            if (cur + candidates[i] > target) {
                break;
            }

            path.add(candidates[i]);
            dfs(i + 1, path, cur + candidates[i], candidates, target);
            path.remove(path.size() - 1);
        }
    }


    // V2
    // IDEA : Backtracking with Counters
    // https://leetcode.com/problems/combination-sum-ii/editorial/
    /**
     * time = O(2^N)
     * space = O(2^N)
     */
        public List<List<Integer>> combinationSum2_2(int[] candidates, int target) {
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

    // V3
    // IDEA : Backtracking with Index
    // https://leetcode.com/problems/combination-sum-ii/editorial/
    /**
     * time = O(2^N)
     * space = O(2^N)
     */
        public List<List<Integer>> combinationSum2_3(int[] candidates, int target) {
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

    // V4
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    /**
     * time = O(2^N)
     * space = O(2^N)
     */
        public List<List<Integer>> combinationSum2_4(int[] nums, int target) {
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

    // V5
    // https://www.youtube.com/watch?v=rSA3t6BDDwg
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0040-combination-sum-ii.java
    /**
     * time = O(2^N)
     * space = O(2^N)
     */
        public List<List<Integer>> combinationSum2_5(int[] candidates, int target) {
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
