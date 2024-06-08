package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/permutations/

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

    // V0'
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

    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> permute_1(int[] nums) {
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


    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/permutations/editorial/
    public List<List<Integer>> permute_2(int[] nums) {
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
