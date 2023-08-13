package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combination-sum/

import java.util.*;

public class CombinationSum {

    // attr
    List<List<Integer>> res = new ArrayList<>();
    HashSet<List<Integer>> set = new HashSet<>();


    // V0
    // IDEA : BACKTRACK
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
    public List<List<Integer>> combinationSum(int[] candidates, int target) {

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

        if (getSum(cur) > target){
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

    private Integer getSum(List<Integer> input){
        int sum = 0;
        for (Integer x : input){
            sum += x;
        }
        return sum;
    }

    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/combination-sum/editorial/
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

    public List<List<Integer>> combinationSum_2(int[] candidates, int target) {
        List<List<Integer>> results = new ArrayList<List<Integer>>();
        LinkedList<Integer> comb = new LinkedList<Integer>();

        this.backtrack(target, comb, 0, candidates, results);
        return results;
    }

    // V2
    // https://www.youtube.com/watch?v=GBKI9VSKdGg
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0039-combination-sum.java
    public List<List<Integer>> combinationSum_3(int[] candidates, int target) {
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

    // V3
    // https://leetcode.com/problems/combination-sum/solutions/3804814/java-easy-solution-using-backtracking/
    public List<List<Integer>> combinationSum_4(int[] candidates, int target) {
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
