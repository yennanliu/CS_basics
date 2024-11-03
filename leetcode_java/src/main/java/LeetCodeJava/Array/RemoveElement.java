package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-element/description/
/**
 * 27. Remove Element
 Solved
 Easy
 Topics
 Companies
 Hint
 Given an integer array nums and an integer val, remove all occurrences of val in nums in-place. The order of the elements may be changed. Then return the number of elements in nums which are not equal to val.

 Consider the number of elements in nums which are not equal to val be k, to get accepted, you need to do the following things:

 Change the array nums such that the first k elements of nums contain the elements which are not equal to val. The remaining elements of nums are not important as well as the size of nums.
 Return k.
 Custom Judge:

 The judge will test your solution with the following code:

 int[] nums = [...]; // Input array
 int val = ...; // Value to remove
 int[] expectedNums = [...]; // The expected answer with correct length.
 // It is sorted with no values equaling val.

 int k = removeElement(nums, val); // Calls your implementation

 assert k == expectedNums.length;
 sort(nums, 0, k); // Sort the first k elements of nums
 for (int i = 0; i < actualLength; i++) {
 assert nums[i] == expectedNums[i];
 }
 If all assertions pass, then your solution will be accepted.



 Example 1:

 Input: nums = [3,2,2,3], val = 3
 Output: 2, nums = [2,2,_,_]
 Explanation: Your function should return k = 2, with the first two elements of nums being 2.
 It does not matter what you leave beyond the returned k (hence they are underscores).
 Example 2:

 Input: nums = [0,1,2,2,3,0,4,2], val = 2
 Output: 5, nums = [0,1,4,0,3,_,_,_]
 Explanation: Your function should return k = 5, with the first five elements of nums containing 0, 0, 1, 3, and 4.
 Note that the five elements can be returned in any order.
 It does not matter what you leave beyond the returned k (hence they are underscores).


 Constraints:

 0 <= nums.length <= 100
 0 <= nums[i] <= 50
 0 <= val <= 100

 *
 */
class RemoveElement {

    // V0
    // IDEA : 2 POINTERS
    /**
     *  //--------------------
     *  Example 1
     *  //--------------------
     *
     *  nums = [3,2,2,3], val = 3
     *
     *  [3,2,2,3]
     *   s
     *   f
     *
     *  [2,3,2,3]    if nums[f] != val, swap, move s
     *   s s
     *     f
     *
     *  [2,2,3,3]   if nums[f] != val, swap, move s
     *     s s
     *       f
     *
     * [2,2,3,3]
     *      s
     *        f
     *
     *
     *  //--------------------
     *  Example 2
     *  //--------------------
     *
     *  nums = [0,1,2,2,3,0,4,2], val = 2
     *
     *
     *  [0,1,2,2,3,0,4,2]   if nums[f] != val, swap, move s
     *   s s
     *   f
     *
     *  [0,1,2,2,3,0,4,2]     if nums[f] != val, swap, move s
     *     s s
     *     f
     *
     *  [0,1,2,2,3,0,4,2]
     *       s
     *       f
     *
     * [0,1,2,2,3,0,4,2]
     *      s
     *        f
     *
     * [0,1,3,2,2,0,4,2]   if nums[f] != val, swap, move s
     *      s s
     *          f
     *
     * [0,1,3,0,2,2,4,2]   if nums[f] != val, swap, move s
     *        s s
     *            f
     *
     * [0,1,3,0,4,2,2,2]    if nums[f] != val, swap, move s
     *          s s
     *              f
     *
     *  [0,1,3,0,4,2,2,2]
     *             s
     *                 f
     */
    public int removeElement(int[] nums, int val) {
        int s = 0;
        for (int f = 0; f < nums.length; f++){
            if (nums[f] != val){
                nums[s] = nums[f];
                s += 1;
            }
        }
        return s;
    }

    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/remove-element/solutions/3670940/best-100-c-java-python-beginner-friendly/
    public int removeElement_1(int[] nums, int val) {
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[index] = nums[i];
                index++;
            }
        }
        return index;
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/remove-element/solutions/3102906/java-best-solution-o-n-time-complexity/
    public int removeElement_2(int[] nums, int val) {
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
                i++;
            }
        }
        return i;
    }
}