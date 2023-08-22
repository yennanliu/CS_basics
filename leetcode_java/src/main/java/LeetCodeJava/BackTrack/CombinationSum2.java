package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/combination-sum-ii/

import java.util.*;

public class CombinationSum2 {

    // V0
    // IDEA : BACKTRACK
    // TODO : fix with hashmap avoid duplicates
//    List<List<Integer>> ans = new ArrayList<>();
//    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
//
//        if (candidates.length == 0 || Arrays.stream(candidates).sum() <  target){
//            //ans.add(new ArrayList<>());
//            return ans;
//        }
//
//        Arrays.sort(candidates);
//
//        HashMap<Integer, Integer> counter = new HashMap<>();
//        for (int candidate : candidates) {
//            if (counter.containsKey(candidate))
//                counter.put(candidate, counter.get(candidate) + 1);
//            else
//                counter.put(candidate, 1);
//        }
//
//        ArrayList<Integer> cur = new ArrayList<>();
//        int[] _candidates = candidates;
//        _help(candidates, target, cur, 0, counter);
//        return this.ans;
//    }
//
//    private void _help(int[] candidates, int target, ArrayList cur, int idx, HashMap counter){
//
//        int _sum = _getSum(cur);
//
//        if (cur.size() > candidates.length ||_sum > target){
//            return;
//        }
//
//        if (_sum == target){
//            Collections.sort(cur);
//            if(!this.ans.contains(cur)){
//                this.ans.add(new ArrayList<>(cur));
//                return;
//            }
//        }
//
//        for (int i = idx; i < candidates.length; i++){
//            int val = candidates[i];
//            if (val > target){
//                return;
//            }
//            cur.add(val);
//            int _cnt = (int) counter.get(val);
//            counter.put(val, _cnt - 1);
//            // recursive
//            _help(candidates, target, cur, idx+1, counter);
//            // undo
//            counter.put(val, _cnt + 1);
//            cur.remove(cur.size()-1);
//        }
//
//    }
//
//    private int _getSum(ArrayList<Integer> cur){
//        int res = 0;
//        for(int x : cur){
//            res += x;
//        }
//        return res;
//    }

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
