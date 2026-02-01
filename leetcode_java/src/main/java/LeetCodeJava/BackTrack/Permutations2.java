package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/permutations-ii/description/

import java.util.*;

/**
 * 47. Permutations II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2]
 * Output:
 * [[1,1,2],
 *  [1,2,1],
 *  [2,1,1]]
 * Example 2:
 *
 * Input: nums = [1,2,3]
 * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 *
 */
public class Permutations2 {

    // V0
    // IDEA: BACKTRACK + `not visit duplicated val in same layer` (fixed by gpt) + `visited` array
    // https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/backtrack.md#1-2-2-avoid-add-duplicated-element-in-same-level-same-recursion-call
    // time: O(N! * N), space: O(N! * N)
    List<List<Integer>> permuteRes2 = new ArrayList<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
        //permuteRes2.clear(); // Reset result list
        if (nums == null || nums.length == 0)
            return new ArrayList<>();

        Arrays.sort(nums); // Sort to handle duplicates
        boolean[] used = new boolean[nums.length];
        permuteHelper2(nums, new ArrayList<>(), used);
        return permuteRes2;
    }

    public void permuteHelper2(int[] nums, List<Integer> cur, boolean[] used) {
        if (cur.size() == nums.length) {
            permuteRes2.add(new ArrayList<>(cur));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            // Skip used elements
            if (used[i])
                continue;

      // Skip duplicates in the same recursion layer
      /**
       *  NOTE !!!
       *
       *  - If nums[i] is the same as the previous value nums[i - 1]…
       *
       *  - And we haven't used nums[i - 1] in this recursive path
       *    (i.e., we've already branched on it in this level)…
       *
       *  - Then skip nums[i] to avoid generating the same permutation again.
       *
       */
      /**
       *  We only skip a duplicate number (e.g. second 1)
       *  if the first one (previous index) was not used in this path.
       *
       *  Because:
       * 	•	If we don't use the first 1, but try to use the second 1,
       *      	it leads to the `same` permutation as using the first.
       *
       * 	•	So we skip the second in this path to avoid duplication.
       *
       *
       *   But if the first 1 was used, then using the second one now is a valid, different position, and we're good to proceed.
       *
       */
      /**  Example:
       *
       * 📘 Real example walk-through:
       *
       * Imagine this call stack path:
       *
       * permuteUnique([1,1,2])
       *
       * We sort to [1,1,2]. Then we build permutations using backtracking.
       *
       * Step-by-step:
       *
       * Index	Used[]	cur	Decision
       * 0	false	[]	Add 1
       * 1	true	[1]	Add 1 again ✅
       * 2	true	[1,1]	Add 2 → [1,1,2] ✅
       *
       * This is valid. But what if we skip the first 1 and use the second one?
       *
       * Index	Used[]	cur	Decision
       * 0	false	[]	Skip
       * 1	false	[]	Try using second 1 ❌ Skip it
       *
       * Because:
       *
       * nums[1] == nums[0] && !used[0]
       *
       * This means we're in a situation like:
       *
       * "Hey, the value is the same as previous, but we didn't use the previous one — so using this would duplicate the earlier case."
       *
       * 🔁 This condition:
       *
       * if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;
       *
       * ==> "Skip duplicate values that haven't had their first occurrence used yet in this recursion layer."
       *
       * ⸻
       *
       * 🧠 Intuition in simpler terms:
       *
       * If two values are equal, and you didn't use the first one yet — don't start with the second one.
       * Only use a duplicate after the earlier copy has been used.
       *
       */
      if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
          continue;
      }

            used[i] = true;
            cur.add(nums[i]);

            permuteHelper2(nums, cur, used);

            // Backtrack
            used[i] = false;
            cur.remove(cur.size() - 1);
        }
    }

    // V1-1
    // IDEA: BACKTRACK + `VISITED ARRAY` (NO NEED start_idx)
    // https://www.youtube.com/watch?v=qhBVWf0YafA
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0047-permutations-ii.java
    /**
     * time = O(N! * N)
     * space = O(N! * N)
     */
        public List<List<Integer>> permuteUnique_1_1(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();

        backtrack(res, nums, new ArrayList<>(), new boolean[nums.length]);
        return res;
    }

    public void backtrack(List<List<Integer>> res,
                          int[] nums,
                          List<Integer> path,
                          boolean[] visited) {
        if (path.size() == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i] ||
                    (i > 0 && nums[i - 1] == nums[i] && visited[i - 1]))
                continue;

            visited[i] = true;
            path.add(nums[i]);

            backtrack(res, nums, path, visited);

            visited[i] = false;
            path.remove(path.size() - 1);
        }
    }

    // V2-1
    // https://leetcode.com/problems/permutations-ii/editorial/
    // IDEA: Backtracking with Groups of Numbers (counter)
    /**
     * time = O(N! * N)
     * space = O(N! * N)
     */
        public List<List<Integer>> permuteUnique_2_1(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();

        // count the occurrence of each number
        HashMap<Integer, Integer> counter = new HashMap<>();
        for (int num : nums) {
            if (!counter.containsKey(num))
                counter.put(num, 0);
            counter.put(num, counter.get(num) + 1);
        }

        LinkedList<Integer> comb = new LinkedList<>();
        this.backtrack(comb, nums.length, counter, results);
        return results;
    }

    protected void backtrack(
            LinkedList<Integer> comb,
            Integer N,
            HashMap<Integer, Integer> counter,
            List<List<Integer>> results) {
        if (comb.size() == N) {
            // make a deep copy of the resulting permutation,
            // since the permutation would be backtracked later.
            results.add(new ArrayList<Integer>(comb));
            return;
        }

        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            Integer num = entry.getKey();
            Integer count = entry.getValue();
            if (count == 0)
                continue;
            // add this number into the current combination
            comb.addLast(num);
            counter.put(num, count - 1);

            // continue the exploration
            backtrack(comb, N, counter, results);

            // revert the choice for the next exploration
            comb.removeLast();
            counter.put(num, count);
        }
    }


    // V3
    // https://leetcode.com/problems/permutations-ii/solutions/18601/short-iterative-java-solution-by-shpolsk-r4tm/
    /**
     * time = O(N! * N)
     * space = O(N! * N)
     */
        public List<List<Integer>> permuteUnique_3(int[] num) {
        LinkedList<List<Integer>> res = new LinkedList<>();
        res.add(new ArrayList<>());
        for (int i = 0; i < num.length; i++) {
            Set<String> cache = new HashSet<>();
            while (res.peekFirst().size() == i) {
                List<Integer> l = res.removeFirst();
                for (int j = 0; j <= l.size(); j++) {
                    List<Integer> newL = new ArrayList<>(l.subList(0, j));
                    newL.add(num[i]);
                    newL.addAll(l.subList(j, l.size()));
                    if (cache.add(newL.toString()))
                        res.add(newL);
                }
            }
        }
        return res;
    }

}
