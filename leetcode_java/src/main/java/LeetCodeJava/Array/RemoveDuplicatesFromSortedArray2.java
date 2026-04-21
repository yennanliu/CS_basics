package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/description/
/**
 * 80. Remove Duplicates from Sorted Array II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums sorted in non-decreasing order, remove some duplicates in-place such that each unique element appears at most twice. The relative order of the elements should be kept the same.
 *
 * Since it is impossible to change the length of the array in some languages, you must instead have the result be placed in the first part of the array nums. More formally, if there are k elements after removing the duplicates, then the first k elements of nums should hold the final result. It does not matter what you leave beyond the first k elements.
 *
 * Return k after placing the final result in the first k slots of nums.
 *
 * Do not allocate extra space for another array. You must do this by modifying the input array in-place with O(1) extra memory.
 *
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
 * Input: nums = [1,1,1,2,2,3]
 * Output: 5, nums = [1,1,2,2,3,_]
 * Explanation: Your function should return k = 5, with the first five elements of nums being 1, 1, 2, 2 and 3 respectively.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 * Example 2:
 *
 * Input: nums = [0,0,1,1,1,1,2,3,3]
 * Output: 7, nums = [0,0,1,1,2,3,3,_,_]
 * Explanation: Your function should return k = 7, with the first seven elements of nums being 0, 0, 1, 1, 2, 3 and 3 respectively.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 104
 * -104 <= nums[i] <= 104
 * nums is sorted in non-decreasing order.
 *
 *
 */
public class RemoveDuplicatesFromSortedArray2 {


    // V0
    // IDEA : 2 POINTERS ( gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/remove-duplicates-from-sorted-array-ii.py
    /**
     * time = O(N)
     * space = O(1)
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length < 3) {
            return nums.length;
        }

        int slow = 1; // slow starts from 1
        for (int fast = 2; fast < nums.length; fast++) { // fast starts from 2
            // If the condition is met, update the array and increment slow pointer
            if (nums[slow] != nums[fast] || nums[slow] != nums[slow - 1]) {
                nums[++slow] = nums[fast];
            }
        }
        return slow + 1; // Return the length of the modified array
    }


    // V1
    // IDEA : 2 POINTERS
    // The approach employs a two-pointer strategy. The variable j is used to keep track of the current position in the modified array where elements are being stored without violating the constraint. The loop iterates through the array, and for each element, it checks whether it is the same as the element two positions behind the current j. If it is, it means there are already two occurrences of this element in the modified array, and we should skip adding another one to adhere to the constraint. Otherwise, the element is added to the modified array at position j, and j is incremented.
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/solutions/4804983/beats-100-0ms-advanced-two-pointer-approach-java-c-python-rust/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int removeDuplicates_1(int[] nums) {
        int j = 1;
        for (int i = 1; i < nums.length; i++) {
            if (j == 1 || nums[i] != nums[j - 2]) {
                nums[j++] = nums[i];
            }
        }
        return j;
    }


    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/solutions/4511964/easy-o-n-python-java-go-c-beginner-friendly/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int removeDuplicates_2(int[] nums) {

        int index = 1;
        int occurance = 1;

        for(int i=1; i < nums.length; i++){
            if (nums[i] == nums[i-1]){
                occurance++;
            }else{
                occurance = 1;
            }

            if (occurance <= 2){
                nums[index] = nums[i];
                index++;
            }
        }
        return index;
    }




}
