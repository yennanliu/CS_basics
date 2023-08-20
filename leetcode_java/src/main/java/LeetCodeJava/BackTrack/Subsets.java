package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/subsets/

import java.util.*;

public class Subsets {

    List<List<Integer>> res = new ArrayList<>();
    HashSet<Integer> _set = new HashSet<>();

    // V0
    // IDEA : BACKTRACK
//    List<List<Integer>> ans = new ArrayList<>();
//    int _k;
//    public List<List<Integer>> subsets(int[] nums) {
//
//        // k : sub array length
//        for (int _k=0; _k < nums.length+1; _k++){
//            List<Integer> cur = new ArrayList<>();
//            this._helper(0, cur, nums);
//        }
//
//        return this.ans;
//    }
//
//    // first : start idx
//    private void _helper(int first, List<Integer> cur, int[] nums){
//
//        if (cur.size() == _k){
//            ans.add(new ArrayList<>(cur));
//            return;
//        }
//
//        for(int i = first; i < nums.length; i++){
//            int val = nums[i];
////            if(!cur.contains(val)){
////                cur.add(val);
////                // recursive
////                _helper(i+1, cur, nums);
////                // undo
////                cur.remove(cur.size()-1);
////            }
//            cur.add(val);
//            // recursive
//            _helper(i+1, cur, nums);
//            // undo
//            cur.remove(cur.size()-1);
//        }
//    }


    // V0
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums); // optional
        _backtrack(list, new ArrayList<>(), nums, 0);
        return list;
    }

    private void _backtrack(List<List<Integer>> list , List<Integer> tempList, int [] nums, int start){
        list.add(new ArrayList<>(tempList));
        for(int i = start; i < nums.length; i++){
            tempList.add(nums[i]);
            _backtrack(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }


    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> subsets_2_(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        Arrays.sort(nums);
        backtrack(list, new ArrayList<>(), nums, 0);
        return list;
    }

    private void backtrack(List<List<Integer>> list , List<Integer> tempList, int [] nums, int start){
        list.add(new ArrayList<>(tempList));
        for(int i = start; i < nums.length; i++){
            tempList.add(nums[i]);
            backtrack(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    // V1
    // IDEA : BACKTRACK
    // https://www.youtube.com/watch?v=REOH22Xwdkk&t=4s
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0078-subsets.java
    public List<List<Integer>> subsets_1_2(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        helper(ans, 0, nums, list);
        return ans;
    }

    public void helper(List<List<Integer>> ans, int start, int[] nums, List<Integer> list) {

            if (start >= nums.length) {
                ans.add(new ArrayList<>(list));
            } else {
                // decision tree :  add the element and start the  recursive call
                list.add(nums[start]);
                helper(ans, start + 1, nums, list);

                // decision tree :  remove the element and do the backtracking call.
                list.remove(list.size() - 1);
                helper(ans, start + 1, nums, list);
            }
    }

    // V1
    // IDEA : Cascading
    // https://leetcode.com/problems/subsets/editorial/
    public List<List<Integer>> subsets_3(int[] nums) {
        List<List<Integer>> output = new ArrayList();
        output.add(new ArrayList<Integer>());

        for (int num : nums) {
            List<List<Integer>> newSubsets = new ArrayList();
            for (List<Integer> curr : output) {
                newSubsets.add(new ArrayList<Integer>(curr){{add(num);}});
            }
            for (List<Integer> curr : newSubsets) {
                output.add(curr);
            }
        }
        return output;
    }

    // V3
    // IDEA : Lexicographic (Binary Sorted) Subsets
    // https://leetcode.com/problems/subsets/editorial/
    public List<List<Integer>> subsets_4(int[] nums) {
        List<List<Integer>> output = new ArrayList();
        int n = nums.length;

        for (int i = (int)Math.pow(2, n); i < (int)Math.pow(2, n + 1); ++i) {
            // generate bitmask, from 0..00 to 1..11
            String bitmask = Integer.toBinaryString(i).substring(1);

            // append subset corresponding to that bitmask
            List<Integer> curr = new ArrayList();
            for (int j = 0; j < n; ++j) {
                if (bitmask.charAt(j) == '1') curr.add(nums[j]);
            }
            output.add(curr);
        }
        return output;
    }

}
