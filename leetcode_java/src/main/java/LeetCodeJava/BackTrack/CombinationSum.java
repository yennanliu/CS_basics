package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combination-sum/

import java.util.*;

public class CombinationSum {

    // attr
    List<List<Integer>> res = new ArrayList<>();
    HashSet<List<Integer>> set = new HashSet<>();

    // V0
    // IDEA : BACKTRACK
    public List<List<Integer>> combinationSum(int[] candidates, int target) {

        if (candidates == null || candidates.length == 0){
            return null;
        }

        // NOTE !!! we sore here
        Arrays.sort(candidates);

        List<List<Integer>> res = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        int idx = 0;

        backTrack(candidates, target, tmp, res, idx);
        return res;
    }

    private void backTrack(int[] candidates, int target, List<Integer> tmp, List<List<Integer>> res, int idx){

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
        for (int i = idx; i < candidates.length; i++){
            int cur = candidates[i];
            tmp.add(cur);
            /** NOTE !!!
             *
             *    use i, since we need to use start from current (i) index in recursion call
             *    (reuse current index)
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

    // V0'
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
    public List<List<Integer>> combinationSum_(int[] candidates, int target) {

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

    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> combinationSum_1(int[] nums, int target) {
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
