package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/subsets-ii/
/**
 * 90. Subsets II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums that may contain duplicates, return all possible subsets (the power set).
 *
 * The solution set must not contain duplicate subsets. Return the solution in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,2]
 * Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
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
 *
 *
 */

import java.util.*;

public class subsets2 {

    // V0
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/subsets/solutions/27281/a-general-approach-to-backtracking-questions-in-java-subsets-permutations-combination-sum-palindrome-partitioning/
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        /**
         *  NOTE !!!
         *
         *   in order to skip duplicates via `compare cur and prev val`,
         *   we need to sort array first
         */
        Arrays.sort(nums);
        backtrack(list, new ArrayList<>(), nums, 0);
        return list;
    }

    private void backtrack(List<List<Integer>> list, List<Integer> tempList, int [] nums, int start){
        list.add(new ArrayList<>(tempList));
        for(int i = start; i < nums.length; i++){
            // skip duplicates
            /**
             *  NOTE !!!
             *
             *   below is the key shows how to simply skip duplicates
             *   (instead of using hashmap counter)
             */
            if(i > start && nums[i] == nums[i-1]){
                continue;
            }
            tempList.add(nums[i]);
            backtrack(list, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    // V0-1
    // IDEA : Backtracking
    // https://leetcode.com/problems/subsets-ii/editorial/
    public List<List<Integer>> subsetsWithDup_0_1(int[] nums) {

        /**
         *  NOTE !!!
         *
         *   in order to skip duplicates via `compare cur and prev val`,
         *   we need to sort array first
         */
        Arrays.sort(nums);
        List<List<Integer>> subsets = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        _helper(subsets, currentSubset, nums, 0);
        return subsets;
    }

    private void _helper(List<List<Integer>> subsets, List<Integer> currentSubset, int[] nums, int index) {
        // Add the subset formed so far to the subsets list.
        subsets.add(new ArrayList<>(currentSubset));

        for (int i = index; i < nums.length; i++) {

            // If the current element is a duplicate, ignore.
            /**
             *  NOTE !!!
             *
             *   below is the key shows how to simply skip duplicates
             *   (instead of using hashmap counter)
             */
            if (i != index && nums[i] == nums[i - 1]) {
                continue;
            }
            currentSubset.add(nums[i]);
            subsetsWithDupHelper(subsets, currentSubset, nums, i + 1);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    // V0-2
    public List<List<Integer>> subsetsWithDup_0_2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) {
            return res;
        }

        // Sort the array to handle duplicates effectively
        Arrays.sort(nums);

        // Backtrack
        List<Integer> cur = new ArrayList<>();
        this.getSubSet(0, nums, cur, res);
        return res;
    }

    public void getSubSet(int start_idx, int[] nums, List<Integer> cur, List<List<Integer>> res) {
        // Add current subset to the result list (deep copy to avoid reference issues)
        res.add(new ArrayList<>(cur));

        // Backtracking loop
        for (int i = start_idx; i < nums.length; i++) {
            // Skip duplicates: only include the number if it is the first occurrence at
            // this level
            if (i > start_idx && nums[i] == nums[i - 1]) {
                continue;
            }

            cur.add(nums[i]);
            // Recursively call to explore the next element, moving start index forward
            this.getSubSet(i + 1, nums, cur, res);
            // Backtrack (remove the last element added)
            cur.remove(cur.size() - 1);
        }
    }



//    // V0'
//    // IDEA : BACKTRACK + LC 70
//    List<List<Integer>> output = new ArrayList();
//    int n, k;
//
//    public void backtrack(int first, ArrayList<Integer> curr, int[] nums) {
//        // if the combination is done
//        if (curr.size() == k) {
//            Collections.sort(curr);
//            if(!output.contains(curr)){
//                output.add(new ArrayList<>(curr));
//            }
//            return;
//        }
//
//        /** NOTE HERE !!!
//         *
//         *  ++i : i+1 first,  then do op
//         *  i++ : do op first, then i+1
//         *
//         *  -> i++ or ++i is both OK here
//         */
//        for (int i = first; i < n; i++) {
//            // add i into the current combination
//            curr.add(nums[i]);
//            // use next integers to complete the combination
//            backtrack(i + 1, curr, nums);
//            // backtrack
//            curr.remove(curr.size() - 1);
//        }
//    }
//
//    public List<List<Integer>> subsetsWithDup(int[] nums) {
//        n = nums.length;
//        /** NOTE HERE !!!
//         *
//         *  ++k : k+1 first,  then do op
//         *  k++ : do op first, then k+1
//         *
//         *  -> k++ or ++k is both OK here
//         */
//        for (k = 0; k < n + 1; k++) {
//            backtrack(0, new ArrayList<Integer>(), nums);
//        }
//        return output;
//    }

    // V1-1
    // https://neetcode.io/problems/subsets-ii
    // IDEA: BRUTE FOECE
    Set<List<Integer>> res = new HashSet<>();

    public List<List<Integer>> subsetsWithDup_1_1(int[] nums) {
        Arrays.sort(nums);
        backtrack(nums, 0, new ArrayList<>());
        return new ArrayList<>(res);
    }

    private void backtrack(int[] nums, int i, List<Integer> subset) {
        if (i == nums.length) {
            res.add(new ArrayList<>(subset));
            return;
        }

        subset.add(nums[i]);
        backtrack(nums, i + 1, subset);
        subset.remove(subset.size() - 1);
        backtrack(nums, i + 1, subset);
    }


    // V1-2
    // https://neetcode.io/problems/subsets-ii
    // IDEA: BACKTRACK I
    List<List<Integer>> res_1_2 = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup_1_2(int[] nums) {
        Arrays.sort(nums);
        backtrack(0, new ArrayList<>(), nums);
        return res_1_2;
    }

    private void backtrack(int i, List<Integer> subset, int[] nums) {
        if (i == nums.length) {
            res_1_2.add(new ArrayList<>(subset));
            return;
        }

        subset.add(nums[i]);
        backtrack(i + 1, subset, nums);
        subset.remove(subset.size() - 1);

        while (i + 1 < nums.length && nums[i] == nums[i + 1]) {
            i++;
        }
        backtrack(i + 1, subset, nums);
    }


    // V1-3
    // https://neetcode.io/problems/subsets-ii
    // IDEA: BACKTRACK II
    List<List<Integer>> res_1_3 = new ArrayList<>();

    public List<List<Integer>> subsetsWithDup_1_3(int[] nums) {
        Arrays.sort(nums);
        backtrack_1_3(0, new ArrayList<>(), nums);
        return res_1_3;
    }

    private void backtrack_1_3(int i, List<Integer> subset, int[] nums) {
        res_1_3.add(new ArrayList<>(subset));
        for (int j = i; j < nums.length; j++) {
            if (j > i && nums[j] == nums[j - 1]) {
                continue;
            }
            subset.add(nums[j]);
            backtrack_1_3(j + 1, subset, nums);
            subset.remove(subset.size() - 1);
        }
    }
    

    // V2
    // IDEA : Bitmasking
    // https://leetcode.com/problems/subsets-ii/editorial/
    public List<List<Integer>> subsetsWithDup_2(int[] nums) {
        List<List<Integer>> subsets = new ArrayList();
        int n = nums.length;

        // Sort the generated subset. This will help to identify duplicates.
        Arrays.sort(nums);

        int maxNumberOfSubsets = (int) Math.pow(2, n);
        // To store the previously seen sets.
        Set<String> seen = new HashSet<>();

        for (int subsetIndex = 0; subsetIndex < maxNumberOfSubsets; subsetIndex++) {
            // Append subset corresponding to that bitmask.
            List<Integer> currentSubset = new ArrayList();
            StringBuilder hashcode = new StringBuilder();
            for (int j = 0; j < n; j++) {
                // Generate the bitmask
                int mask = 1 << j;
                int isSet = mask & subsetIndex;
                if (isSet != 0) {
                    currentSubset.add(nums[j]);
                    // Generate the hashcode by creating a comma separated string of numbers in the currentSubset.
                    hashcode.append(nums[j]).append(",");
                }
            }
            if (!seen.contains(hashcode.toString())) {
                seen.add(hashcode.toString());
                subsets.add(currentSubset);
            }

        }

        return subsets;
    }


    // V3
    // IDEA : Cascading (Iterative)
    // https://leetcode.com/problems/subsets-ii/editorial/
    public List<List<Integer>> subsetsWithDup_3(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> subsets = new ArrayList<>();
        subsets.add(new ArrayList<Integer>());

        int subsetSize = 0;

        for (int i = 0; i < nums.length; i++) {
            int startingIndex = (i >= 1 && nums[i] == nums[i - 1]) ? subsetSize : 0;
            // subsetSize refers to the size of the subset in the previous step. This value also indicates the starting index of the subsets generated in this step.
            subsetSize = subsets.size();
            for (int j = startingIndex; j < subsetSize; j++) {
                List<Integer> currentSubset = new ArrayList<>(subsets.get(j));
                currentSubset.add(nums[i]);
                subsets.add(currentSubset);
            }
        }
        return subsets;
    }

    // V4
    // IDEA : Backtracking
    // https://leetcode.com/problems/subsets-ii/editorial/
    public List<List<Integer>> subsetsWithDup_4(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> subsets = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        subsetsWithDupHelper(subsets, currentSubset, nums, 0);
        return subsets;
    }

    private void subsetsWithDupHelper(List<List<Integer>> subsets, List<Integer> currentSubset, int[] nums, int index) {
        // Add the subset formed so far to the subsets list.
        subsets.add(new ArrayList<>(currentSubset));

        for (int i = index; i < nums.length; i++) {
            // If the current element is a duplicate, ignore.
            if (i != index && nums[i] == nums[i - 1]) {
                continue;
            }
            currentSubset.add(nums[i]);
            subsetsWithDupHelper(subsets, currentSubset, nums, i + 1);
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

}
