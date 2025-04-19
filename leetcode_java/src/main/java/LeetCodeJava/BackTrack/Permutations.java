package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/permutations/
/**
 * 46. Permutations
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array nums of distinct integers, return all the possible permutations. You can return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * Example 2:
 *
 * Input: nums = [0,1]
 * Output: [[0,1],[1,0]]
 * Example 3:
 *
 * Input: nums = [1]
 * Output: [[1]]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * All the integers of nums are unique.
 *
 */
import java.util.*;

public class Permutations {

    // V0
    // IDEA : BACKTRACK
    /** NOTE !!!
     *
     *  for Permutations,
     *
     *  1) NO NEED "start_idx" since CAN'T get duplicated element
     *  2) via "!cur.contains(nums[i])" to NOT use duplicated element in recursive call within for loop
     */
    public List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();

        if (nums.length == 1){
            List<Integer> cur = new ArrayList<>();
            cur.add(nums[0]);
            //System.out.println("cur = " + cur);
            res.add(cur);
            //System.out.println("res = " + res);
            return res;
        }

        // backtrack
        List<Integer> cur = new ArrayList<>();
        //System.out.println("res = " + res);
        this.getPermutation(nums, cur, res);
        //System.out.println("(after) res = " + res);
        return res;
    }

    public void getPermutation(int[] nums, List<Integer> cur, List<List<Integer>> res){

        if (cur.size() > nums.length){
            return;
        }

        if (cur.size() == nums.length){
            if (!res.contains(cur)){
                res.add(new ArrayList<>(cur));
            }
        }

        /** NOTE !!! NO NEED to use "start_idx" */
        for (int i = 0; i < nums.length; i++){
            /** NOTE !!!
             *
             *
             * via "!cur.contains(nums[i])" to NOT use duplicated element in recursive call within for loop
             */
            if (!cur.contains(nums[i])){
                cur.add(nums[i]);
                this.getPermutation(nums, cur, res);
                // undo
                cur.remove(cur.size()-1);
            }
        }
    }

    List<List<Integer>> ans = new ArrayList<>();

    // V0-1
    // IDEA : BACKTRACK
    public List<List<Integer>> permute_0_1(int[] nums) {

        if (nums.length == 1){
            List<List<Integer>> _ans = new ArrayList<>();
            List<Integer> cur = new ArrayList<>();
            cur.add(nums[0]);
            _ans.add(cur);
            return _ans;
        }

        List<Integer> cur = new ArrayList<>();
        /** NOTE !!! we don't need to set idx param */
        helper(nums, cur);

        return this.ans;
    }

    private void helper(int[] nums, List<Integer> cur){

        if (cur.size() > nums.length){
            return;
        }

        if (!this.ans.contains(cur) && cur.size() == nums.length){

            /** NOTE !!! we use below to add current ArrayList instance to ans */
            this.ans.add(new ArrayList<>(cur));
        }


        for (int i = 0; i < nums.length; i++){
            int val = nums[i];
            // input nums is array with distinct integers
            /** NOTE !!! ONLY do recursive, backtrack when meet distinct element */
            if(!cur.contains(val)){
                cur.add(val);
                // recursive call
                helper(nums, cur);
                // undo last op
                cur.remove(cur.size()-1); // NOTE !!! remove last element
            }
        }
    }

    // V1-1
    // https://neetcode.io/problems/permutations
    // IDEA: RECURSION
    public List<List<Integer>> permute_1_1(int[] nums) {
        if (nums.length == 0) {
            return Arrays.asList(new ArrayList<>());
        }

        List<List<Integer>> perms = permute_1_1(Arrays.copyOfRange(nums, 1, nums.length));
        List<List<Integer>> res = new ArrayList<>();
        for (List<Integer> p : perms) {
            for (int i = 0; i <= p.size(); i++) {
                List<Integer> p_copy = new ArrayList<>(p);
                p_copy.add(i, nums[0]);
                res.add(p_copy);
            }
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/permutations
    // IDEA: Iteration
    public List<List<Integer>> permute_1_2(int[] nums) {
        List<List<Integer>> perms = new ArrayList<>();
        perms.add(new ArrayList<>());

        for (int num : nums) {
            List<List<Integer>> new_perms = new ArrayList<>();
            for (List<Integer> p : perms) {
                for (int i = 0; i <= p.size(); i++) {
                    List<Integer> p_copy = new ArrayList<>(p);
                    p_copy.add(i, num);
                    new_perms.add(p_copy);
                }
            }
            perms = new_perms;
        }
        return perms;
    }

    // V1-3
    // https://neetcode.io/problems/permutations
    // IDEA: Backtracking
    List<List<Integer>> res_1_3;
    public List<List<Integer>> permute_1_3(int[] nums) {
        res_1_3 = new ArrayList<>();
        backtrack(new ArrayList<>(), nums, new boolean[nums.length]);
        return res_1_3;
    }

    public void backtrack(List<Integer> perm, int[] nums, boolean[] pick) {
        if (perm.size() == nums.length) {
            res_1_3.add(new ArrayList<>(perm));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!pick[i]) {
                perm.add(nums[i]);
                pick[i] = true;
                backtrack(perm, nums, pick);
                perm.remove(perm.size() - 1);
                pick[i] = false;
            }
        }
    }


    // V1-4
    // https://neetcode.io/problems/permutations
    // IDEA: Backtracking (Bit Mask)
    List<List<Integer>> res_1_4 = new ArrayList<>();

    public List<List<Integer>> permute_1_4(int[] nums) {
        backtrack(new ArrayList<>(), nums, 0);
        return res_1_4;
    }

    private void backtrack(List<Integer> perm, int[] nums, int mask) {
        if (perm.size() == nums.length) {
            res_1_4.add(new ArrayList<>(perm));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if ((mask & (1 << i)) == 0) {
                perm.add(nums[i]);
                backtrack(perm, nums, mask | (1 << i));
                perm.remove(perm.size() - 1);
            }
        }
    }


    // V1-5
    // https://neetcode.io/problems/permutations
    // IDEA: Backtracking (Optimal)
    List<List<Integer>> res_1_5;
    public List<List<Integer>> permute_1_5(int[] nums) {
        res_1_5 = new ArrayList<>();
        backtrack(nums, 0);
        return res_1_5;
    }

    public void backtrack(int[] nums, int idx) {
        if (idx == nums.length) {
            List<Integer> perm = new ArrayList<>();
            for (int num : nums) perm.add(num);
            res_1_5.add(perm);
            return;
        }
        for (int i = idx; i < nums.length; i++) {
            swap(nums, idx, i);
            backtrack(nums, idx + 1);
            swap(nums, idx, i);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    
    // V2
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> permute_2(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        // Arrays.sort(nums); // not necessary
        backtrack_(list, new ArrayList<>(), nums);
        return list;
    }

    private void backtrack_(List<List<Integer>> list, List<Integer> tempList, int [] nums){
        if(tempList.size() == nums.length){
            list.add(new ArrayList<>(tempList));
        } else{
            for(int i = 0; i < nums.length; i++){
                if(tempList.contains(nums[i])) continue; // element already exists, skip
                tempList.add(nums[i]);
                backtrack_(list, tempList, nums);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    // V3
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/permutations/editorial/
    public List<List<Integer>> permute_3(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        backtrack(new ArrayList<>(), ans, nums);
        return ans;
    }

    public void backtrack(List<Integer> curr, List<List<Integer>> ans, int[] nums) {
        if (curr.size() == nums.length) {
            ans.add(new ArrayList<>(curr));
            return;
        }

        for (int num: nums) {
            if (!curr.contains(num)) {
                curr.add(num);
                backtrack(curr, ans, nums);
                curr.remove(curr.size() - 1);
            }
        }
    }

}
