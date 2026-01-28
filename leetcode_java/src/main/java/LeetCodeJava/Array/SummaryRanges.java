package LeetCodeJava.Array;

// https://leetcode.com/problems/summary-ranges/
/**
 *  228. Summary Ranges
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given a sorted unique integer array nums.
 *
 * A range [a,b] is the set of all integers from a to b (inclusive).
 *
 * Return the smallest sorted list of ranges that cover all the numbers in the array exactly. That is, each element of nums is covered by exactly one of the ranges, and there is no integer x such that x is in one of the ranges but not in nums.
 *
 * Each range [a,b] in the list should be output as:
 *
 * "a->b" if a != b
 * "a" if a == b
 * Example 1:
 *
 * Input: nums = [0,1,2,4,5,7]
 * Output: ["0->2","4->5","7"]
 * Explanation: The ranges are:
 * [0,2] --> "0->2"
 * [4,5] --> "4->5"
 * [7,7] --> "7"
 * Example 2:
 *
 * Input: nums = [0,2,3,4,6,8,9]
 * Output: ["0","2->4","6","8->9"]
 * Explanation: The ranges are:
 * [0,0] --> "0"
 * [2,4] --> "2->4"
 * [6,6] --> "6"
 * [8,9] --> "8->9"
 *
 *
 * Constraints:
 *
 * 0 <= nums.length <= 20
 * -231 <= nums[i] <= 231 - 1
 * All the values of nums are unique.
 * nums is sorted in ascending order.
 *
 *
 */
import java.util.ArrayList;
import java.util.List;

public class SummaryRanges {

    // V0
    // IDEA: INTERVAL (fixed by gemini)
    public List<String> summaryRanges(int[] nums) {
        List<String> res = new ArrayList<>();
        if (nums == null || nums.length == 0)
            return res;

        int l = 0; // The start of the current range

        // We loop through the array
        for (int r = 0; r < nums.length; r++) {
            /**  NOTE !!!
             *
             *  we use `r + 1 == nums.length`
             *  to handle `last element` op as well,
             *  so via this, we don't need the extra
             *  to paste `last interval` to our answer.
             *  (e.g. as what V0-1 does)
             *
             *
             *  ```
             * // final range
             *    int last = nums.length - 1;
             *    if (l == last) {
             *    res.add(String.valueOf(nums[l]));
             *    } else {
             *    res.add(nums[l] + "->" + nums[last]);
             *   }
             * ```
             *
             */
            // We only trigger a "range end" if:
            // 1. We are at the last element of the array
            // 2. OR the next element is not consecutive (gap detected)
            if (r + 1 == nums.length || nums[r + 1] != nums[r] + 1) {

                // Format the range based on whether it's a single number or a span
                if (l == r) {
                    res.add(String.valueOf(nums[l]));
                } else {
                    res.add(nums[l] + "->" + nums[r]);
                }

                // Move the left pointer to the start of the next potential range
                l = r + 1;
            }
        }

        return res;
    }

    // V0-1
    // IDEA: INTERVAL (GPT)
    public List<String> summaryRanges_0_1(int[] nums) {

        List<String> res = new ArrayList<>();
        if (nums == null || nums.length == 0)
            return res;

        int l = 0;

        for (int r = 1; r < nums.length; r++) {
            /** NOTE !!!
             *
             *   we use below to avoid `overflow` error.
             *
             *   e.g. below is correct but cause `overflow` error.
             *
             *     //...
             *     if((r-1) - l + 1 == 1){
             *         // ...
             *     }
             *     //...
             */
            if (nums[r] != nums[r - 1] + 1) {

                if (l == r - 1) {
                    res.add(String.valueOf(nums[l]));
                } else {
                    res.add(nums[l] + "->" + nums[r - 1]);
                }

                l = r;
            }
        }

        // final range
        int last = nums.length - 1;
        if (l == last) {
            res.add(String.valueOf(nums[l]));
        } else {
            res.add(nums[l] + "->" + nums[last]);
        }

        return res;
    }


    // V1
    // https://leetcode.com/problems/summary-ranges/solutions/1805391/concise-solution-in-0-n-with-approach-explained-in-detail/
    // time: O(N), space: O(1)
    public List<String> summaryRanges_1(int[] nums) {
        ArrayList<String> al = new ArrayList<>();

        for(int i=0;i<nums.length;i++){
            int start=nums[i];
            while(i+1<nums.length && nums[i]+1==nums[i+1]){
                i++;
            }

            if(start!=nums[i]){
                al.add(""+start+"->"+nums[i]);
            }
            else{
                al.add(""+start);
            }
        }
        return al;
    }

    // V2
    // https://leetcode.com/problems/summary-ranges/solutions/2813052/java-100-faster-solution/
    // time: O(N), space: O(1)
    public List<String> summaryRanges_2(int[] nums) {
        List<String> list = new ArrayList<>();
        if (nums.length == 0) return list;
        int start = nums[0], end = nums[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] + 1 != nums[i + 1]) {
                if (start == end) {
                    sb.append(start);
                    list.add(sb.toString());
                } else {
                    sb.append(start).append("->").append(end);
                    list.add(sb.toString());
                }
                sb.setLength(0);
                start = nums[i + 1];
                end = nums[i + 1];
            } else {
                end = nums[i + 1];
            }
        }
        if (start == end) {
            sb.append(start);
            list.add(sb.toString());
        } else {
            sb.append(start).append("->").append(end);
            list.add(sb.toString());
        }
        return list;
    }



}
