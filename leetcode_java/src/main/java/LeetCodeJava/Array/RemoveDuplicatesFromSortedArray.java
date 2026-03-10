package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-duplicates-from-sorted-array/
/**
 * 26. Remove Duplicates from Sorted Array
 * Solved
 * Easy
 * Topics
 * Companies
 * Hint
 * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once. The relative order of the elements should be kept the same. Then return the number of unique elements in nums.
 *
 * Consider the number of unique elements of nums to be k, to get accepted, you need to do the following things:
 *
 * Change the array nums such that the first k elements of nums contain the unique elements in the order they were present in nums initially. The remaining elements of nums are not important as well as the size of nums.
 * Return k.
 * Custom Judge:
 *
 * The judge will test your solution with the following code:
 *
 * int[] nums = [...]; // Input array
 * int[] expectedNums = [...]; // The expected answer with correct length
 *
 * int k = removeDuplicates(nums); // Calls your implementation
 *
 * assert k == expectedNums.length;
 * for (int i = 0; i < k; i++) {
 *     assert nums[i] == expectedNums[i];
 * }
 * If all assertions pass, then your solution will be accepted.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,2]
 * Output: 2, nums = [1,2,_]
 * Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 * Example 2:
 *
 * Input: nums = [0,0,1,1,1,2,2,3,3,4]
 * Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
 * Explanation: Your function should return k = 5, with the first five elements of nums being 0, 1, 2, 3, and 4 respectively.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 104
 * -100 <= nums[i] <= 100
 * nums is sorted in non-decreasing order.
 *
 *
 */
import java.util.LinkedHashSet;

public class RemoveDuplicatesFromSortedArray {

    // V0
    // IDEA : 2 POINTERS
    // time: O(N), space: O(1)
    /**
     *  //--------------------------------
     *  Example 1
     *  //--------------------------------
     *
     *  nums = [1,1,2]
     *
     *  [1,1,2]
     *   s f
     *
     *  [1,2, 1]     if nums[f] != nums[s], move s, then swap f, s
     *   s s  f
     *
     *
     *   //--------------------------------
     *   Example 2
     *   //--------------------------------
     *
     *   nums = [0,0,1,1,1,2,2,3,3,4]
     *
     *   [0,0,1,1,1,2,2,3,3,4]
     *    s f
     *
     *   [0,1,0,1,1,2,2,3,3,4]   if nums[f] != nums[s], move s, then swap f, s
     *    s s f
     *
     *   [0,1,0,1,1,2,2,3,3,4]
     *      s   f
     *
     *   [0,1,0,1,1,2,2,3,3,4]
     *      s     f
     *
     *   [0,1,2,1,1,0,2,3,3,4]   if nums[f] != nums[s], move s, then swap f, s
     *      s s     f
     *
     *   [0,1,2,1,1,0,2,3,3,4]
     *        s       f
     *
     *   [0,1,2,3,1,0,2,1,3,4]  if nums[f] != nums[s], move s, then swap f, s
     *        s s       f
     *
     *   [0,1,2,3,1,0,2,1,3,4]
     *          s         f
     *
     *   [0,1,2,3,4,0,2,1,3,1]   if nums[f] != nums[s], move s, then swap f, s
     *          s s         f
     *
     */
    public int removeDuplicates(int[] nums) {
        /**
         *  NOTE !!!  we init slow, fast pointers as s, f
         *            and use for loop go through over fast pointer (f)
         */
        int s = 0;
        for (int f = 1; f < nums.length; f++){
            /**
             *  NOTE !!!
             *
             *   we do swap when f NOT EQUALS s
             */
            if (nums[f] != nums[s]){
                /**
                 *  NOTE !!!
                 *
                 *   we move slow pointer first,
                 *   then do swap
                 */
                s += 1;
                int tmp = nums[f];
                nums[f] = nums[s];
                nums[s] = tmp;
            }
        }
        /**
         *  NOTE !!!
         *
         *   we return s+1 as non-duplicated array length, since length = index + 1
         */
        return s+1;
    }

    // V0-0-1
    // IDEA: 2 POINTERS (fixed by gemini)
    public int removeDuplicates_0_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }

        // 2 pointers (fast - slow ???)
        int l = 0;
        int r = 1;

        /** NOTE: we can go with `for loop` below as well (V0-0-1) */
        while (r < nums.length) {
            // case 1) nums[r] == nums[l]
            if (nums[r] == nums[l]) {
                r += 1;
            }
            // case 2) nums[r] != nums[l]
            else {
                // note !!! WE MOVE left pointer first
                l += 1;
                /**  NOTE !!
                 *
                 *  NO need to `swap`
                 *
                 * Overwriting vs. Swapping:
                 * You don't actually need to swap.
                 * Since the array is sorted,
                 * you just need to copy the next unique element to the position
                 * right after the current unique element.
                 *
                 */
//                int tmp = nums[r];
//                //nums[r] = l;
//                //nums[l] = tmp;
//                //l += 1;
                nums[l] = nums[r]; // ???
            }
        }

        // note !!! return `l+1` instead
        return l + 1;
        //return l;
    }


    // V0-1
    // IDEA: 2 POINTERS (gemini)
    public int removeDuplicates_0_1(int[] nums) {
        // 1. Edge Case: If empty or 1 element, it's already "unique"
        if (nums == null || nums.length == 0)
            return 0;
        if (nums.length == 1)
            return 1;

        // 2. Two Pointers
        // 'l' is the index of the last known unique element
        int l = 0;

        // 'r' scans the rest of the array
        for (int r = 1; r < nums.length; r++) {
            // If we find a value that is NOT a duplicate of our current unique element...
            if (nums[r] != nums[l]) {
                l++; // Move to the next "empty" slot for a unique value
                nums[l] = nums[r]; // Copy the new unique value into that slot
            }
        }

        // 3. Return the COUNT of unique elements (index + 1)
        return l + 1;
    }


    // V0-2
    // IDEA: 2 POINTERS (GPT)
    public int removeDuplicates_0_2(int[] nums) {

        if (nums == null || nums.length == 0) {
            return 0;
        }

        int l = 0;

        for (int r = 1; r < nums.length; r++) {
            if (nums[r] != nums[l]) {
                l++;
                nums[l] = nums[r];
            }
        }

        return l + 1;
    }



    // V1
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3416595/c-java-python-javascript-fully-explained-two-pointer/
    /**
     * time = O(N)
     * space = O(1)
     */
        public int removeDuplicates_1(int[] nums) {
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[i] != nums[j]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    // V2
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3342081/100-faster-java-2-approaches-step-by-step-explained/
    /**
     * time = O(N)
     * space = O(1)
     */
        public int removeDuplicates_2(int[] nums) {

        int newIndex = 1; // Start with index 1 because the first element is already in place

        for (int i = 0; i < nums.length - 1; i++) {

            if (nums[i] < nums[i + 1]) { // If the current element is less than the next element

                nums[newIndex] = nums[i + 1]; // Move the next element to the new index
                newIndex++; // Increment the new index
            }
        }
        return newIndex; // Return the length of the new subarray
    }

    // V2'
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3342081/100-faster-java-2-approaches-step-by-step-explained/
    /**
     * time = O(N)
     * space = O(N)
     */
        public int removeDuplicates_3(int[] nums) {

        //Insert all array element in the Set.
        //Set does not allow duplicates and sets like LinkedHashSet maintains the order of insertion so it will remove duplicates and elements will be printed in the same order in which it is inserted

        LinkedHashSet<Integer> set = new LinkedHashSet<>();

        for(int i = 0; i < nums.length; i++){
            set.add(nums[i]);
        }
        //copy unique element back to array
        int i = 0;

        for(int ele:set){
            nums[i++] = ele;
        }
        return set.size();
    }




}
