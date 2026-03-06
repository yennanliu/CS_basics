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
    // IDEA: 2 POINTERS (gemini)
    public int removeElement(int[] nums, int val) {
        int l = 0;
        int r = nums.length - 1;

        while (l <= r) {
            if (nums[l] == val) {
                // If the left element is the one we want to remove,
                // replace it with whatever is at the right pointer.
                nums[l] = nums[r];
                // We don't move 'l' yet because the NEW nums[l] might also be 'val'.
                // We just shrink the right boundary.
                r--;
            } else {
                // Only if nums[l] is a "good" element do we move forward.
                l++;
            }
        }

        // When l > r, 'l' is exactly the number of non-val elements.
        return l;
    }

    // V0-0-1
    // IDEA : 2 POINTERS
    /**
     * time = O(N)
     * space = O(1)
     *
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
    public int removeElement_0_0_1(int[] nums, int val) {
        int s = 0;
        for (int f = 0; f < nums.length; f++){
            if (nums[f] != val){
                nums[s] = nums[f];
                s += 1;
            }
        }
        return s;
    }

    // V0-1
    // IDEA: 2 POINTERS (gemini)
    public int removeElement_0_1(int[] nums, int val) {
        int i = 0; // Pointer for the next position of a non-val element
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i; // i is exactly the count of elements not equal to val
    }

    // V0-2
    // IDEA: 2 POINTERS (gpt)
    public int removeElement_0_2(int[] nums, int val) {
        int l = 0;
        int r = nums.length - 1;

        while (l <= r) {
            if (nums[l] == val) {
                nums[l] = nums[r];
                r--;
            } else {
                l++;
            }
        }

        return l;
    }

    // V0-3
    // IDEA: 2 POINTERS (fixed by gpt)
    public int removeElement_0_3(int[] nums, int val) {
        if (nums == null || nums.length == 0)
            return 0;
        if (nums.length == 1)
            return nums[0] == val ? 0 : 1;

        int l = 0;
        int r = nums.length - 1;

        while (l <= r) {
            // move r left if nums[r] == val
            while (r >= l && nums[r] == val) {
                r--;
            }

            // now r >= l or loop ends
            if (l < r && nums[l] == val) {
                // swap nums[l] and nums[r]
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
                r--; // move r
            } else if (nums[l] != val) {
                l++; // only move l if nums[l] is valid
            } else {
                // l == r and nums[l] == val → finished
                break;
            }
        }

        return l;
    }


    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/remove-element/solutions/3670940/best-100-c-java-python-beginner-friendly/
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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