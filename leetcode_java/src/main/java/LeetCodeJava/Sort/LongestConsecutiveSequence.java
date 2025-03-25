package LeetCodeJava.Sort;

// https://leetcode.com/problems/longest-consecutive-sequence/
/**
 *  128. Longest Consecutive Sequence
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an unsorted array of integers nums, return the length of the longest consecutive elements sequence.
 *
 * You must write an algorithm that runs in O(n) time.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [100,4,200,1,3,2]
 * Output: 4
 * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
 * Example 2:
 *
 * Input: nums = [0,3,7,2,5,8,4,6,0,1]
 * Output: 9
 *
 *
 * Constraints:
 *
 * 0 <= nums.length <= 105
 * -109 <= nums[i] <= 109
 */
import java.util.*;

public class LongestConsecutiveSequence {

    // V0
    // IDEA: SET + LIST SORT + 2 POINTERS
    /**
     *  DEMO
     *
     *  EXP 1)
     *
     *  step 1) sort:
     *     nums = [100,4,200,1,3,2]
     *     -> sorted = [1,2,3,4,100,200]
     *
     *
     *  step 2) 2 pointers
     *
     *    [1,2,3,4,100,200]
     *    i j            len = 1
     *    i   j          len = 2
     *    i    j         len = 3
     *    i      j       len = 4
     *            ij     len = 1
     *                ji len = 4
     *                   len = 4
     *
     *  EXP 2)
     *
     *   step 1)
     *    nums = [0,3,7,2,5,8,4,6,0,1]
     *    -> sorted = [0,0,1,2,3,4,5,6,7,8]
     *
     *   step 2) 2 pointers
     *
     *   [0,0,1,2,3,4,5,6,7,8]
     *   i  j                  len=1
     *      i j                len=2
     *      i   j              len=3
     *      i     j            len=4
     *      i      j           len=5
     *      i        j
     *      ....
     *
     *      i              j  len=9
     *
     */
    public int longestConsecutive(int[] nums) {

        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // use set, to only collect `non-duplicated` elements
        Set<Integer> set = new HashSet<>();
        for (int x : nums) {
            set.add(x);
        }

        List<Integer> numList = new ArrayList<>();
        for (int x : set) {
            numList.add(x);
        }

        // sort
        Collections.sort(numList);
        int res = 1;
        int i = 0;
        int j = 1;

        while (j < numList.size()) {
            if (numList.get(j) == numList.get(j - 1) + 1) {
                res = Math.max(res, j - i + 1);
            } else {
                i = j;
            }
            j += 1;
        }

        return res;
    }

    // V0-1
    // IDEA : SET + SORT + 2 POINTERS
    public int longestConsecutive_0_1(int[] nums) {

        if (nums.length <= 1){
            return nums.length;
        }

        Set<Integer> set = new HashSet<>();
        for (int x: nums){
            set.add(x);
        }
        Integer[] array = new Integer[set.size()];
        int j = 0;
        for(int y : set){
            array[j] = y;
            j += 1;
        }

        //System.out.println("nums before sort = " + Arrays.toString(array));
        Arrays.sort(array);
        //System.out.println("nums after sort = " + Arrays.toString(array));

        int res = 1;
        int l = 0;
        int r = 1;
        while (r < array.length){
            // if "not continuous, move l to r's idx (will move r 1 idx to right later)
            if (array[r-1] + 1 != array[r]){
                l = r;
            }
            /** NOTE !!!
             *
             *  get current max length first, then move r idx (r += 1)
             */
            res = Math.max(res, r-l+1);
            r += 1;
        }

        return res;
    }

    // V0-2
    // IDEA : SORT + SLIDING WINDOW (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/longest-consecutive-sequence.py#L32
    public int longestConsecutive_0_2(int[] nums) {

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

    // V0-3
    // IDEA : SET + SORT + for loop
    // step 1) set : only collect unique element
    // step 2) sort, so element is in ascending ordering
    // step 3) loop over final list, and maintain a max len of sequence
    public int longestConsecutive_0_3(int[] nums) {

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

    // V0-4
    // IDEA : SET + SORT + SHIFT ARRAY
    // Step 1) we get unique array as new one, since we get possible longest array from it
    // Step 2) we sort unique array from above
    // Step 3) we shift to with 1 index from above unique array as shift array
    // Step 4) we loop over unique array, shift array and calculate max length when diff = 1
    // Step 5) return amx length from above
    public int longestConsecutive_0_4(int[] nums) {

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

    // V1-1
    // https://neetcode.io/problems/longest-consecutive-sequence
    // IDEA: BRUTE FORCE
    public int longestConsecutive_1_1(int[] nums) {
        int res = 0;
        Set<Integer> store = new HashSet<>();
        for (int num : nums) {
            store.add(num);
        }

        for (int num : nums) {
            int streak = 0, curr = num;
            while (store.contains(curr)) {
                streak++;
                curr++;
            }
            res = Math.max(res, streak);
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/longest-consecutive-sequence
    // IDEA: SORTING
    public int longestConsecutive_1_2(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int res = 0, curr = nums[0], streak = 0, i = 0;

        while (i < nums.length) {
            if (curr != nums[i]) {
                curr = nums[i];
                streak = 0;
            }
            while (i < nums.length && nums[i] == curr) {
                i++;
            }
            streak++;
            curr++;
            res = Math.max(res, streak);
        }
        return res;
    }


    // V1-3
    // https://neetcode.io/problems/longest-consecutive-sequence
    // IDEA: HASHSET
    public int longestConsecutive_1_3(int[] nums) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        int longest = 0;

        for (int num : numSet) {
            if (!numSet.contains(num - 1)) {
                int length = 1;
                while (numSet.contains(num + length)) {
                    length++;
                }
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }

    // V1-4
    // https://neetcode.io/problems/longest-consecutive-sequence
    // IDEA: HASHMAP
    public int longestConsecutive_1_4(int[] nums) {
        Map<Integer, Integer> mp = new HashMap<>();
        int res = 0;

        for (int num : nums) {
            if (!mp.containsKey(num)) {
                mp.put(num, mp.getOrDefault(num - 1, 0) + mp.getOrDefault(num + 1, 0) + 1);
                mp.put(num - mp.getOrDefault(num - 1, 0), mp.get(num));
                mp.put(num + mp.getOrDefault(num + 1, 0), mp.get(num));
                res = Math.max(res, mp.get(num));
            }
        }
        return res;
    }
    

    // V2
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

    // V3
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
