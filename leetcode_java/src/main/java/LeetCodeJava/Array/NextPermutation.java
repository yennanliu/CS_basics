package LeetCodeJava.Array;

// https://leetcode.com/problems/next-permutation/description/
/**
 * 31. Next Permutation
 * Solved
 * Medium
 * Topics
 * Companies
 * A permutation of an array of integers is an arrangement of its members into a sequence or linear order.
 *
 * For example, for arr = [1,2,3], the following are all the permutations of arr: [1,2,3], [1,3,2], [2, 1, 3], [2, 3, 1], [3,1,2], [3,2,1].
 * The next permutation of an array of integers is the next lexicographically greater permutation of its integer. More formally, if all the permutations of the array are sorted in one container according to their lexicographical order, then the next permutation of that array is the permutation that follows it in the sorted container. If such arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).
 *
 * For example, the next permutation of arr = [1,2,3] is [1,3,2].
 * Similarly, the next permutation of arr = [2,3,1] is [3,1,2].
 * While the next permutation of arr = [3,2,1] is [1,2,3] because [3,2,1] does not have a lexicographical larger rearrangement.
 * Given an array of integers nums, find the next permutation of nums.
 *
 * The replacement must be in place and use only constant extra memory.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: [1,3,2]
 * Example 2:
 *
 * Input: nums = [3,2,1]
 * Output: [1,2,3]
 * Example 3:
 *
 * Input: nums = [1,1,5]
 * Output: [1,5,1]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 100
 *
 */
public class NextPermutation {

    // V0
    // IDEA : 2 POINTERS (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/next-permutation.py#L43
    public void nextPermutation(int[] nums) {

        int i = nums.length - 1;
        int j = nums.length - 1;

        // Find the first decreasing element from the end
        while (i > 0 && nums[i - 1] >= nums[i]) {
            i--;
        }

        // If the entire array is in descending order, reverse it
        if (i == 0) {
            reverse_(nums, 0, nums.length - 1);
            return;
        }

        int k = i - 1; // Find the last "ascending" position

        // Find the element just larger than nums[k]
        while (nums[j] <= nums[k]) {
            j--;
        }

        // Swap the elements at positions k and j
        swap_(nums, k, j);

        // Reverse the sequence from k+1 to the end
        reverse_(nums, k + 1, nums.length - 1);
    }

    // Helper method to swap elements at positions i and j
    private void swap_(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // Helper method to reverse elements from start to end
    private void reverse_(int[] nums, int start, int end) {
        while (start < end) {
            swap_(nums, start, end);
            start++;
            end--;
        }
    }

    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/next-permutation/solutions/3473399/beats-100-full-explanation-in-steps/
    public void nextPermutation_1(int[] nums) {
        int ind1=-1;
        int ind2=-1;
        // step 1 find breaking point
        for(int i=nums.length-2;i>=0;i--){
            if(nums[i]<nums[i+1]){
                ind1=i;
                break;
            }
        }
        // if there is no breaking  point
        if(ind1==-1){
            reverse(nums,0);
        }

        else{
            // step 2 find next greater element and swap with ind2
            for(int i=nums.length-1;i>=0;i--){
                if(nums[i]>nums[ind1]){
                    ind2=i;
                    break;
                }
            }

            swap(nums,ind1,ind2);
            // step 3 reverse the rest right half
            reverse(nums,ind1+1);
        }
    }
    void swap(int[] nums,int i,int j){
        int temp=nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }
    void reverse(int[] nums,int start){
        int i=start;
        int j=nums.length-1;
        while(i<j){
            swap(nums,i,j);
            i++;
            j--;
        }
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/next-permutation/solutions/5205798/java-solution-0ms-solution-100-faster-solution-full-explanation-code/
    public void nextPermutation_2(int[] nums) {
        // Traverse form the end and find the element which is smaller
        // than its next element. Keep that index as ind

        // Traverse the array from the back and find the element which is
        // nearest element grater that the element present in index ind.
        // Swap them.

        // Reverse the array after the index the index ind

        int ind = -1;

        int n = nums.length;

        for(int i = n - 1; i >= 1; i--) {
            if(nums[i] > nums[i - 1]) {
                ind = i - 1;
                break;
            }
        }

        if(ind == -1) {
            int i = 0, j = n - 1;

            while(i < j) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;

                i++;
                j--;
            }

            return;
        }
        else {
            for(int i = n - 1; i > ind; i--) {
                if(nums[i] > nums[ind]) {
                    int temp = nums[ind];
                    nums[ind] = nums[i];
                    nums[i] = temp;
                    break;
                }
            }
        }

        int i = ind + 1, j = n - 1;

        while(i < j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;

            i++;
            j--;
        }

        return;
    }

    // V3
    // IDEA : 2 POINTERS (gpt)
    /**
     *  IDEA :
     *
     *  Explanation of the Algorithm:
     *
     * 	1.	Identify the first decreasing element from the end:
     * 	    •	Traverse the array from the right and find the first index i such that nums[i] < nums[i + 1].
     * 	    •	This is the pivot point where the next permutation is possible.
     *
     * 	2.	Find the smallest element larger than nums[i] to the right of i:
     * 	    •	From the end of the array, find the first index j such that nums[j] > nums[i].
     *
     * 	3.	Swap nums[i] and nums[j]:
     * 	    •	Swap these elements to ensure the next permutation is lexicographically greater.
     *
     * 	4.	Reverse the suffix starting from i + 1:
     * 	    •	Reverse the elements to the right of i to ensure they are in ascending order, giving the smallest lexicographical order for the suffix.
     *
     * 	5.	If no i is found (i.e., the array is in descending order):
     * 	    •	Reverse the entire array to reset it to the smallest permutation.
     *
     */
    public void nextPermutation_3(int[] nums) {
        int n = nums.length;
        /**
         *  why set i = n - 2 ?
         *
         *  The initialization of int i = n - 2; in the next permutation algorithm is essential for the logic of finding the first decreasing element when traversing the array from right to left. Here’s why it starts at n - 2:
         *
         * Understanding the Reason:
         *
         * 	1.	i Represents the Pivot Point:
         * 	    •	The algorithm tries to find the largest index i where nums[i] < nums[i + 1].
         * 	    •	This index i is called the pivot because it is where the current permutation can be adjusted to create the next lexicographically greater permutation.
         * 	2.	Start from the Second-to-Last Element (n - 2):
         * 	    •	The last element (nums[n - 1]) has no element to its right, so it cannot be compared with anything.
         * 	    •	To compare nums[i] with nums[i + 1], i must start from n - 2, the second-to-last index.
         * 	3.	Traverse from Right to Left:
         * 	    •	By moving from right to left, the algorithm ensures that we find the last occurrence of nums[i] < nums[i + 1], which is the correct pivot point for generating the next permutation.
         * 	    •	If no such i is found (i.e., the array is in descending order), the array is reversed to reset it to the smallest permutation.
         *
         */
        int i = n - 2;

        // Step 1: Find the first decreasing element from the end
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }

        if (i >= 0) { // If such an element is found
            // Step 2: Find the smallest element larger than nums[i]
            int j = n - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }

            // Step 3: Swap nums[i] and nums[j]
            swap_3(nums, i, j);
        }

        // Step 4: Reverse the suffix starting at i + 1
        reverse_3(nums, i + 1, n - 1);
    }

    // Helper method to swap two elements
    private void swap_3(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // Helper method to reverse part of the array
    private void reverse_3(int[] nums, int start, int end) {
        while (start < end) {
            swap_3(nums, start, end);
            start++;
            end--;
        }
    }

}
