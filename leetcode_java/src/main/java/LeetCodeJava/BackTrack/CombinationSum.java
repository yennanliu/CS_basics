package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combination-sum/
/**
 *  39. Combination Sum
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.
 *
 * The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the
 * frequency
 *  of at least one of the chosen numbers is different.
 *
 * The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.
 *
 *
 *
 * Example 1:
 *
 * Input: candidates = [2,3,6,7], target = 7
 * Output: [[2,2,3],[7]]
 * Explanation:
 * 2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
 * 7 is a candidate, and 7 = 7.
 * These are the only two combinations.
 * Example 2:
 *
 * Input: candidates = [2,3,5], target = 8
 * Output: [[2,2,2,2],[2,3,3],[3,5]]
 * Example 3:
 *
 * Input: candidates = [2], target = 1
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= candidates.length <= 30
 * 2 <= candidates[i] <= 40
 * All elements of candidates are distinct.
 * 1 <= target <= 40
 *
 */
import java.util.*;

public class CombinationSum {

    // attr
    List<List<Integer>> res = new ArrayList<>();
    HashSet<List<Integer>> set = new HashSet<>();

    // V0
    // IDEA : BACKTRACK + START_IDX
    // time: O(N^(T/M)), space: O(T/M)
    public List<List<Integer>> combinationSum(int[] candidates, int target) {

        if (candidates == null || candidates.length == 0){
            return null;
        }

        // NOTE !!! NEED to sort here (or the result is WRONG)
        Arrays.sort(candidates);

        List<List<Integer>> res = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        int start_idx = 0;

        backTrack(candidates, target, tmp, res, start_idx);
        return res;
    }

    private void backTrack(int[] candidates, int target, List<Integer> tmp, List<List<Integer>> res, int start_idx){

        if (getSum(tmp) == target){
            tmp.sort(Comparator.comparing(x -> x));
            if (!res.contains(tmp)){
                res.add(new ArrayList<>(tmp));
            }
            return;
        }

        if (getSum(tmp) > target){
            return;
        }

        // backtrack logic
        /**
         *
         *  NOTE !!! have to start from "start" idx
         *
         */
        for (int i = start_idx; i < candidates.length; i++){
            int cur = candidates[i];
            tmp.add(cur);
            /** NOTE !!!
             *
             *    use i, since we need to use start from current (i) index in recursion call
             *    (reuse current index)
             */
            /** NOTE !!!
             *
             *   we have to set i starts from "start" idx
             *   since we have to reuse same element in recursion call
             *
             *   ONLY 全排列 (Permutations) can go without "start" idx
             */
            /**
             *  NOTE !!!
             *
             *   at LC 39 Combination Sum,
             *   we use `i` directly
             *
             *   (while at LC 78, we use `i+1`)
             *
             *
             *   e.g. next start_idx is ` i`
             */
            /**
             *
             *  NOTE !!!  pass `i` as `next start_idx`
             *
             *  (but NOT pass current `start_idx`)
             *
             */
            backTrack(candidates, target, tmp, res, i);
            // undo
            tmp.remove(tmp.size()-1);
        }

    }

    private int getSum(List<Integer> input){

        if (input == null || input.size() == 0){
            return 0;
        }

        int res = 0;
        for (Integer x : input){
            res += x;
        }
        return res;
    }

    // V0-1
    // IDEA : BACKTRACK + START_IDX (GPT)
    // time: O(N^(T/M)), space: O(T/M)
    List<List<Integer>> res_0 = new ArrayList<>();

    public List<List<Integer>> combinationSum_0_1(int[] candidates, int target) {
        List<Integer> cur = new ArrayList<>();
        //Arrays.sort(candidates); // Optional: sort to improve efficiency
        backtrack_0(candidates, target, cur, res_0, 0);
        return res_0;
    }

    public void backtrack_0(int[] candidates, int target, List<Integer> cur, List<List<Integer>> res, int start) {
        if (getSum_0(cur) == target) {
            res.add(new ArrayList<>(cur)); // Add a copy of the current list
            return;
        }

        if (getSum(cur) > target) {
            return;
        }

        // NOTE !!! have to start from "start" idx
        for (int i = start; i < candidates.length; i++) {
            cur.add(candidates[i]);
            /** NOTE !!!
             *
             *   we have to set i starts from "start" idx
             *   since we have to reuse same element in recursion call
             *
             *   ONLY 全排列 (Permutations) can go without "start" idx
             */
            backtrack_0(candidates, target, cur, res, i); // Not i+1 because we can reuse the same elements
            cur.remove(cur.size() - 1); // Undo the last addition
        }
    }

    public int getSum_0(List<Integer> cur) {
        int res = 0;
        for (int x : cur) {
            res += x;
        }
        return res;
    }

    // V0-2
    // IDEA : BACKTRACK + START_IDX
    // time: O(N^(T/M)), space: O(T/M)
    /**
     *  Example output :
     *   candidates = [2, 3, 5]
     *
     * -> output:
     *
     * i = 0 cur = []
     * i = 0 cur = [2]
     * i = 0 cur = [2, 2]
     * i = 0 cur = [2, 2, 2]
     * i = 1 cur = [2, 2, 2]
     * i = 2 cur = [2, 2, 2]
     * i = 1 cur = [2, 2]
     * i = 1 cur = [2, 2, 3]
     * i = 2 cur = [2, 2, 3]
     * i = 2 cur = [2, 2]
     * i = 1 cur = [2]
     * i = 1 cur = [2, 3]
     * i = 2 cur = [2, 3]
     * i = 2 cur = [2]
     * i = 2 cur = [2, 5]
     * i = 1 cur = []
     * i = 1 cur = [3]
     * i = 1 cur = [3, 3]
     * i = 2 cur = [3, 3]
     * i = 2 cur = [3]
     * i = 2 cur = []
     * i = 2 cur = [5]
     *
     */
    // time: O(N^(T/M)), space: O(T/M)
    public List<List<Integer>> combinationSum_0_2(int[] candidates, int target) {

        // sort
        Arrays.sort(candidates);
        //Arrays.stream(candidates).forEach(System.out::println);

        if (candidates[0] > target){
            return res;
        }

        List<Integer> cur = new ArrayList<>();
        help(candidates, target, cur, 0);

        return res;
    }

    private void help(int[] candidates, int target, List<Integer> cur, int idx){

        if (getSum(cur) == target){
            Collections.sort(cur);
            if (!set.contains(cur)){
                set.add(cur);
                // NOTE : make a deep copy
                res.add(new ArrayList<>(cur));
                return;
            }
        }

        if (getSum_(cur) > target){
            return;
        }

        for (int i = idx; i < candidates.length; i++){
            //System.out.println("i = " + i + " cur = " + cur);
            int _val = candidates[i];
            cur.add(_val);
            // NOTE here : use i, instead of idx + 1
            help(candidates, target, cur, i);
            // NOTE : we remove last element, instead of first one
            cur.remove(cur.size()-1);
        }

    }

    private Integer getSum_(List<Integer> input){
        int sum = 0;
        for (Integer x : input){
            sum += x;
        }
        return sum;
    }

    // V0-4
    // IDEA : BACKTRACK + START_IDX (fixed by gpt)
    // time: O(N^(T/M)), space: O(T/M)
    List<List<Integer>> combineSumRes = new ArrayList<>();

    public List<List<Integer>> combinationSum_0_4(int[] candidates, int target) {
        // edge
        if (candidates == null || candidates.length == 0) {
            return null;
        }

        /** NOTE !!! NEED to sort here */
        Arrays.sort(candidates);

        List<Integer> cur = new ArrayList<>();
        getCombineSumHelper(candidates, target, 0, cur);
        return combineSumRes;
    }

    /** NOTE !!! NEED start_idx */
    public void getCombineSumHelper(int[] candidates, int target, int start_idx, List<Integer> cur) {
        if (getArraySum(cur) > target) {
            return;
        }

        if (getArraySum(cur) == target) {
            // sort
            Collections.sort(cur);
            if (!combineSumRes.contains(cur)) {
                combineSumRes.add(new ArrayList<>(cur));
            }
        }

        /** NOTE !!! i START from start_idx */
        for (int i = start_idx; i < candidates.length; i++) {
            cur.add(candidates[i]);
            /** NOTE !!! pass i as "next start_idx" in the recursive call */
            getCombineSumHelper(candidates, target, i, cur);
            // undo
            cur.remove(cur.size() - 1);
        }

    }

    public int getArraySum(List<Integer> input) {
        int res = 0;
        for (int x : input) {
            res += x;
        }
        return res;
    }

    // V1-1
    // https://neetcode.io/problems/combination-target-sum
    // IDEA: BACKTRACK
    // time: O(N^(T/M)), space: O(T/M)
    List<List<Integer>> res_1_1;
    public List<List<Integer>> combinationSum_1_1(int[] nums, int target) {
        res_1_1 = new ArrayList<List<Integer>>();
        List<Integer> cur = new ArrayList();
        backtrack(nums, target, cur, 0);
        return res_1_1;
    }

    public void backtrack(int[] nums, int target, List<Integer> cur, int i) {
        if (target == 0) {
            res_1_1.add(new ArrayList(cur));
            return;
        }
        if (target < 0 || i >= nums.length) {
            return;
        }

        cur.add(nums[i]);
        backtrack(nums, target - nums[i], cur, i);
        cur.remove(cur.size() - 1);
        backtrack(nums, target, cur, i + 1);
    }

    // V1-2
    // https://neetcode.io/problems/combination-target-sum
    // IDEA: Backtracking (Optimal)
    // time: O(N^(T/M)), space: O(T/M)
    List<List<Integer>> res_1_2;
    public List<List<Integer>> combinationSum_1_2(int[] nums, int target) {
        res_1_2 = new ArrayList<>();
        Arrays.sort(nums);

        dfs(0, new ArrayList<>(), 0, nums, target);
        return res_1_2;
    }

    private void dfs(int i, List<Integer> cur, int total, int[] nums, int target) {
        if (total == target) {
            res_1_2.add(new ArrayList<>(cur));
            return;
        }

        for (int j = i; j < nums.length; j++) {
            if (total + nums[j] > target) {
                return;
            }
            cur.add(nums[j]);
            dfs(j, cur, total + nums[j], nums, target);
            cur.remove(cur.size() - 1);
        }
    }


    // V2
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    // time: O(N^(T/M)), space: O(T/M)
    public List<List<Integer>> combinationSum_2(int[] nums, int target) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums);
        _backtrack(list, new ArrayList<>(), nums, target, 0);
        return list;
    }

    private void _backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int remain, int start){
        if(remain < 0) return;
        else if(remain == 0) list.add(new ArrayList<>(tempList));
        else{
            for(int i = start; i < nums.length; i++){
                tempList.add(nums[i]);
                _backtrack(list, tempList, nums, remain - nums[i], i); // not i + 1 because we can reuse same elements
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    // V3
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/combination-sum/editorial/
    // time: O(N^(T/M)), space: O(T/M)
    protected void backtrack(
            int remain,
            LinkedList<Integer> comb,
            int start, int[] candidates,
            List<List<Integer>> results) {

        if (remain == 0) {
            // make a deep copy of the current combination
            results.add(new ArrayList<Integer>(comb));
            return;
        } else if (remain < 0) {
            // exceed the scope, stop exploration.
            return;
        }

        for (int i = start; i < candidates.length; ++i) {
            // add the number into the combination
            comb.add(candidates[i]);
            this.backtrack(remain - candidates[i], comb, i, candidates, results);
            // backtrack, remove the number from the combination
            comb.removeLast();
        }
    }

    // time: O(N^(T/M)), space: O(T/M)
    public List<List<Integer>> combinationSum_3(int[] candidates, int target) {
        List<List<Integer>> results = new ArrayList<List<Integer>>();
        LinkedList<Integer> comb = new LinkedList<Integer>();

        this.backtrack(target, comb, 0, candidates, results);
        return results;
    }

    // V4
    // https://www.youtube.com/watch?v=GBKI9VSKdGg
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0039-combination-sum.java
    // time: O(N^(T/M)), space: O(T/M)
    public List<List<Integer>> combinationSum_4(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        List<Integer> cur = new ArrayList();
        backtrack(candidates, target, ans, cur, 0);
        return ans;
    }

    public void backtrack(
            int[] candidates,
            int target,
            List<List<Integer>> ans,
            List<Integer> cur,
            int index
    ) {
        if (target == 0) {
            ans.add(new ArrayList(cur));
        } else if (target < 0 || index >= candidates.length) {
            return;
        } else {
            cur.add(candidates[index]);
            backtrack(candidates, target - candidates[index], ans, cur, index);

            cur.remove(cur.get(cur.size() - 1));
            backtrack(candidates, target, ans, cur, index + 1);
        }
    }

    // V5
    // https://leetcode.com/problems/combination-sum/solutions/3804814/java-easy-solution-using-backtracking/
    // time: O(N^(T/M)), space: O(T/M)
    public List<List<Integer>> combinationSum_5(int[] candidates, int target) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(candidates);
        backtrack(list, new ArrayList<>(), candidates, target, 0);
        return list;
    }
    public void backtrack(List<List<Integer>> list, List<Integer> temp, int[] nums, int remain, int start) {
        if(remain < 0) return;
        else if (remain == 0) list.add(new ArrayList<>(temp));
        else {
            for(int i = start; i < nums.length; i ++) {
                temp.add(nums[i]);
                backtrack(list, temp, nums, remain - nums[i], i);
                temp.remove(temp.size() - 1);
            }
        }
    }

}
