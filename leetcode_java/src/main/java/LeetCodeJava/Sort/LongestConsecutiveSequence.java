package LeetCodeJava.Sort;

// https://leetcode.com/problems/longest-consecutive-sequence/

import java.util.*;

public class LongestConsecutiveSequence {

    // V0
    // IDEA : SORT + SLIDING WINDOW (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/longest-consecutive-sequence.py#L32
    public int longestConsecutive(int[] nums) {

        // Edge case
        if (nums.length == 0) {
            return 0;
        }

        /**
         *  step 1) Remove duplicates and sort the array
         *
         *  NOTE !!! need to remove duplicates from array (via set)
         */
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        nums = new int[set.size()];
        int index = 0;
        for (int num : set) {
            nums[index++] = num;
        }
        /**
         *  step 2) sort
         *
         *  NOTE !!! sort array via Arrays.sort()
         *
         *   https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/java_trick.md#1-4-3-sort-list-vs-array
         */
        Arrays.sort(nums);

        int res = 0;
        int l = 0;
        int r = 1;

        /**
         *  step 3) Sliding window
         *
         *   1. keep comparing r and r - 1
         *   2. if r != r-1,  make l = r, r = r + 1
         *   3. NOTE !!! l here is ONLY for continuous sub-array calculation
         */
        /*
         * Sliding window here :
         * condition : l, r are still in the array (r < nums.length and l < nums.length)
         *
         * 2 cases
         *
         * case 1) nums[r] != nums[r - 1] + 1
         *
         *      -> means not continuous,
         *      -> so we need to move r to right (1 index)
         *      -> and MOVE l to r - 1, since it's NOT possible to have any continuous
         *      subarray within [l, r] anymore
         *
         * case 2) nums[r] == nums[r - 1] + 1
         *
         *      -> means there is a continuous subarray currently, so we keep moving r to
         *      right (r += 1) and get the current max
         *      subarray length (res = max(res, r - l + 1))
         */
        /**
         * NOTE !!!
         *
         *  keep comparing nums[r], nums[r-1]
         *
         *  "l idx" is just as left boundary that for sub array length calculation
         *
         */
        while (r < nums.length && l < nums.length) {
            // case 1)  nums[r] != nums[r - 1] + 1
            if (nums[r] != nums[r - 1] + 1) {
                r += 1;
                l = (r - 1);
            }
            // case 2) nums[r] == nums[r - 1] + 1
            else {
                res = Math.max(res, r - l + 1);
                r += 1;
            }
        }

        // Edge case: if res == 0, means no continuous array (with length > 1), so we return 1
        // (a single element can be recognized as a "continuous array", and its length = 1)
        return res > 1 ? res : 1;
    }

    // V0'
    // IDEA : SET + SORT + for loop
    // step 1) set : only collect unique element
    // step 2) sort, so element is in ascending ordering
    // step 3) loop over final list, and maintain a max len of sequence
    public int longestConsecutive_0(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return 0;
        }

        // get set
        Set<Integer> set = new HashSet<>();
        List<Integer> uniqueNums = new ArrayList<>();

        for (int i : nums){
            if (!set.contains(i)){
                set.add(i);
                //cnt += 1;
                uniqueNums.add(i);
            }
        }
        // sort
        uniqueNums.sort((a, b) -> Integer.compare(a, b));
        int ans = 0;
        int tmp = 0;

        // NOTE !!! start from idx = 1
        for (int i = 1; i < uniqueNums.size(); i++){
            // compare with cur, and last element
            // check cur = last + 1 (e.g. consecutive)
            if (uniqueNums.get(i) == uniqueNums.get(i-1) + 1){
                tmp += 1;
                ans = Math.max(ans, tmp);
            }
            else{
                tmp = 0;
            }
        }

        // NOTE : ans + 1, since we start from idx = 1
        return ans+1;
    }

    // V0''
    // IDEA : SET + SORT + SHIFT ARRAY
    // Step 1) we get unique array as new one, since we get possible longest array from it
    // Step 2) we sort unique array from above
    // Step 3) we shift to with 1 index from above unique array as shift array
    // Step 4) we loop over unique array, shift array and calculate max length when diff = 1
    // Step 5) return amx length from above
    public int longestConsecutive_0_1(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return 0;
        }

        // get set
        Set<Integer> set = new HashSet<>();
        List<Integer> uniqueNums = new ArrayList<>();

        for (int i : nums){
            if (!set.contains(i)){
                set.add(i);
                //cnt += 1;
                uniqueNums.add(i);
            }
        }
        // sort
        uniqueNums.sort((a, b) -> Integer.compare(a, b));
        int ans = 0;
        int tmp = 0;

        // shift to right with 1 idx
        int [] numsShift = new int[uniqueNums.size()+1];
        for (int i = 0; i < uniqueNums.size()-1; i++){
            numsShift[i+1] = uniqueNums.get(i);
        }

        for (int j = 1; j < uniqueNums.size(); j++){
            int diff = uniqueNums.get(j) - numsShift[j];
            if (diff == 1){
                tmp += 1;
                ans = Math.max(ans, tmp);
            }else{
                tmp = 0;
            }
        }

        return ans+1;
    }

    // V1
    // IDEA : BRUTE FORCE
    // https://leetcode.com/problems/longest-consecutive-sequence/editorial/
    private boolean arrayContains(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    public int longestConsecutive_2(int[] nums) {
        int longestStreak = 0;

        for (int num : nums) {
            int currentNum = num;
            int currentStreak = 1;

            while (arrayContains(nums, currentNum + 1)) {
                currentNum += 1;
                currentStreak += 1;
            }

            longestStreak = Math.max(longestStreak, currentStreak);
        }

        return longestStreak;
    }

    // V2
    // IDEA : SORTING
    // https://leetcode.com/problems/longest-consecutive-sequence/editorial/
    public int longestConsecutive_3(int[] nums) {

        if (nums.length == 0) {
            return 0;
        }

        Arrays.sort(nums);

        int longestStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[i-1]) {
                if (nums[i] == nums[i-1]+1) {
                    currentStreak += 1;
                }
                else {
                    longestStreak = Math.max(longestStreak, currentStreak);
                    currentStreak = 1;
                }
            }
        }

        return Math.max(longestStreak, currentStreak);
    }

}
