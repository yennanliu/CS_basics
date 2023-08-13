package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/subsets/

import java.util.*;

public class Subsets {

    List<List<Integer>> res = new ArrayList<>();
    HashSet<Integer> _set = new HashSet<>();

    // V0
    // IDEA :
    // TODO : fix this
//    public List<List<Integer>> subsets(int[] nums) {
//
//        if (nums.length == 1){
//            List<List<Integer>> _res = new ArrayList<>();
//            List<Integer> tmp = new ArrayList<>();
//            tmp.add(null);
//            tmp.add(nums[0]);
//            _res.add(tmp);
//            return _res;
//        }
//
//        // help func
//        List<Integer> cur = new ArrayList();
//        int layer = 0;
//        int idx = 0;
//        help(nums, cur, layer, idx);
//        //res.add(new ArrayList<>());
//        return res;
//    }
//
//    private void help(int[] nums, List<Integer> cur, int layer, int idx){
//
//        // add to list
//        Collections.sort(cur);
//        if (!_set.contains(cur)){
//            res.add(cur);
//        }
//
////        if (cur.size() >= nums.length){
////            return;
////        }
//
//        if (layer >= nums.length){
//            return;
//        }
//
//        for (int i=idx; i < nums.length; i++){
//            int _val = nums[i];
//            cur.add(_val);
//            // recursive call
//            help(nums, cur, layer+1, i+1);
////            // undo
////            i -= 1;
////            cur.remove(0);
//        }
//
//    }

    // V0'
    // IDEA : Backtracking
    // https://leetcode.com/problems/subsets/editorial/
    List<List<Integer>> output = new ArrayList();
    int n, k;

    public void backtrack(int first, ArrayList<Integer> curr, int[] nums) {
        // if the combination is done
        if (curr.size() == k) {
            output.add(new ArrayList(curr));
            return;
        }
        /** NOTE HERE !!!
         *
         *  ++i : i+1 first,  then do op
         *  i++ : do op first, then i+1
         *
         *  -> i++ or ++i is both OK here
         */
        for (int i = first; i < n; i++) {
            // add i into the current combination
            curr.add(nums[i]);
            // use next integers to complete the combination
            backtrack(i + 1, curr, nums);
            // backtrack
            curr.remove(curr.size() - 1);
        }
    }

    public List<List<Integer>> subsets(int[] nums) {
        n = nums.length;
        /** NOTE HERE !!!
         *
         *  ++k : k+1 first,  then do op
         *  k++ : do op first, then k+1
         *
         *  -> k++ or ++k is both OK here
         */
        for (k = 0; k < n + 1; k++) {
            backtrack(0, new ArrayList<Integer>(), nums);
        }
        return output;
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

    public void helper(
            List<List<Integer>> ans,
            int start,
            int[] nums,
            List<Integer> list
    ) {
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
    public List<List<Integer>> subsets_2(int[] nums) {
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
