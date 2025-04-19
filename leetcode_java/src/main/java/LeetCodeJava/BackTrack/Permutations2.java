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
//    public List<List<Integer>> permuteUnique(int[] nums) {
//
//    }

    // V1-1
    // https://www.youtube.com/watch?v=qhBVWf0YafA
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0047-permutations-ii.java
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
    // IDEA: Backtracking with Groups of Numbers
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
