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

    // V1
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/solutions/3416595/c-java-python-javascript-fully-explained-two-pointer/
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
