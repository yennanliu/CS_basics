package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/subsets/
/**
 * 78. Subsets
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums of unique elements, return all possible subsets (the power set).
 *
 * The solution set must not contain duplicate subsets. Return the solution in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * Example 2:
 *
 * Input: nums = [0]
 * Output: [[],[0]]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * All the numbers of nums are unique.
 *
 */
import java.util.*;

public class Subsets {

    List<List<Integer>> res = new ArrayList<>();
    HashSet<Integer> _set = new HashSet<>();

    // V0
    // IDEA : BACKTRACK
    // NOTE !!! ONLY permutation (全排列) DON'T NEED "start_idx"
    // NOTE !!! for subset, we need "!cur.contains(nums[i])" to NOT add duplicated element
    public List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0){
            return res;
        }
        // backtrack
        int start_idx = 0;
        List<Integer> cur = new ArrayList<>();
        //System.out.println("(before) res = " + res);
        this.getSubSet(start_idx, nums, cur, res);
        //System.out.println("(after) res = " + res);
        return res;
    }

    public void getSubSet(int start_idx, int[] nums, List<Integer> cur, List<List<Integer>> res){

        if (!res.contains(cur)){
            // NOTE !!! init new list via below
            res.add(new ArrayList<>(cur));
        }

        if (cur.size() > nums.length){
            return;
        }

        for (int i = start_idx; i < nums.length; i++){
            /**
             * NOTE !!!
             *
             *  for subset,
             *  we need "!cur.contains(nums[i])"
             *  -> to NOT add duplicated element
             */
            if (!cur.contains(nums[i])){
                cur.add(nums[i]);
                /**
                 *  NOTE !!!
                 *
                 *   at LC 78 subset, we need to use `i+1` idx
                 *   in recursive call
                 *
                 *   while at LC 39 Combination Sum,
                 *   we use `i` directly
                 *
                 *
                 *   e.g. next start_idx is ` i+1`
                 */
                this.getSubSet(i+1, nums, cur, res);
                // undo
                cur.remove(cur.size()-1);
            }
        }
    }

    // V0-1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> subsets_0_1(int[] nums) {
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

    // V1-1
    // https://neetcode.io/problems/subsets
    // IDEA: BACKTRACK
    public List<List<Integer>> subsets_1_1(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> subset = new ArrayList<>();
        dfs(nums, 0, subset, res);
        return res;
    }

    private void dfs(int[] nums, int i, List<Integer> subset, List<List<Integer>> res) {
        if (i >= nums.length) {
            res.add(new ArrayList<>(subset));
            return;
        }
        subset.add(nums[i]);
        dfs(nums, i + 1, subset, res);
        subset.remove(subset.size() - 1);
        dfs(nums, i + 1, subset, res);
    }

    // V1-2
    // https://neetcode.io/problems/subsets
    // IDEA: ITERATION
    public List<List<Integer>> subsets_1_2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());

        for (int num : nums) {
            int size = res.size();
            for (int i = 0; i < size; i++) {
                List<Integer> subset = new ArrayList<>(res.get(i));
                subset.add(num);
                res.add(subset);
            }
        }

        return res;
    }

    // V1-3
    // https://neetcode.io/problems/subsets
    // IDEA: BIT OP
    public List<List<Integer>> subsets_1_3(int[] nums) {
        int n = nums.length;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(nums[j]);
                }
            }
            res.add(subset);
        }
        return res;
    }
    

    // V2
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> subsets_2(int[] nums) {
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

    // V3
    // IDEA : BACKTRACK
    // https://www.youtube.com/watch?v=REOH22Xwdkk&t=4s
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0078-subsets.java
    public List<List<Integer>> subsets_3(int[] nums) {
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

    // V4
    // IDEA : Cascading
    // https://leetcode.com/problems/subsets/editorial/
    public List<List<Integer>> subsets_4(int[] nums) {
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

    // V5
    // IDEA : Lexicographic (Binary Sorted) Subsets
    // https://leetcode.com/problems/subsets/editorial/
    public List<List<Integer>> subsets_5(int[] nums) {
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
