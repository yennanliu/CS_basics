package LeetCodeJava.Array;

// https://leetcode.com/problems/product-of-array-except-self/
/**
 * 238. Product of Array Except Self
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].
 *
 * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
 *
 * You must write an algorithm that runs in O(n) time and without using the division operation.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: [24,12,8,6]
 * Example 2:
 *
 * Input: nums = [-1,1,0,-3,3]
 * Output: [0,0,9,0,0]
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 105
 * -30 <= nums[i] <= 30
 * The input is generated such that answer[i] is guaranteed to fit in a 32-bit integer.
 *
 *
 * Follow up: Can you solve the problem in O(1) extra space complexity? (The output array does not count as extra space for space complexity analysis.)
 *
 *
 */
public class ProductOfArrayExceptSelf {

    // VO
    // IDEA : ARRAY PRODUCT
    public int[] productExceptSelf(int[] nums) {

        if (nums.length == 0 || nums.equals(null)){
            return nums;
        }

        /** NOTE !!! how we init array here */
        int[] ans = new int[nums.length];

        int defaultProd = getProduct(nums);

        for (int i = 0; i < nums.length; i++){
            int cur = nums[i];
            int val = 0;
            if (cur != 0){
                val = defaultProd / cur ;
            }else{
                int[] tmpNums = nums.clone();
                tmpNums[i] = 1;
                val = getProduct(tmpNums);
            }
            ans[i] = val;
        }

        return ans;
    }

    private int getProduct(int[] nums) {

        if (nums.length == 0 || nums.equals(null)) {
            return 0;
        }
        int product = 1;
        for (int i : nums) {
            product = product * i;
        }
        return product;
    }

    // V0-1
    // LC 238
    public int[] productExceptSelf_0_1(int[] nums) {

        // edge
        if(nums == null || nums.length == 0){
            return new int[]{};
        }

        int[] res = new int[nums.length];
        // get all product
        int product = 1;
        for(int x: nums){
            product = product * x;
        }

        for(int i = 0; i < nums.length; i++){
            if (nums[i] != 0){
                res[i] = product / nums[i];
            }else{
                res[i] = getProductExceptIdx(nums, i);
            }
        }

        return res;
    }

    private int getProductExceptIdx(int[] nums, int idx){
        int res = 1;
        for(int i = 0; i < nums.length; i++){
            if(i != idx){
                res = res * nums[i];
            }
        }

        return res;
    }

    // V1
    // IDEA : Left and Right product lists
    // https://leetcode.com/problems/product-of-array-except-self/editorial/
    public int[] productExceptSelf_2(int[] nums) {

        // The length of the input array
        int length = nums.length;

        // The left and right arrays as described in the algorithm
        int[] L = new int[length];
        int[] R = new int[length];

        // Final answer array to be returned
        int[] answer = new int[length];

        // L[i] contains the product of all the elements to the left
        // Note: for the element at index '0', there are no elements to the left,
        // so L[0] would be 1
        L[0] = 1;
        for (int i = 1; i < length; i++) {

            // L[i - 1] already contains the product of elements to the left of 'i - 1'
            // Simply multiplying it with nums[i - 1] would give the product of all
            // elements to the left of index 'i'
            L[i] = nums[i - 1] * L[i - 1];
        }

        // R[i] contains the product of all the elements to the right
        // Note: for the element at index 'length - 1', there are no elements to the right,
        // so the R[length - 1] would be 1
        R[length - 1] = 1;
        for (int i = length - 2; i >= 0; i--) {

            // R[i + 1] already contains the product of elements to the right of 'i + 1'
            // Simply multiplying it with nums[i + 1] would give the product of all
            // elements to the right of index 'i'
            R[i] = nums[i + 1] * R[i + 1];
        }

        // Constructing the answer array
        for (int i = 0; i < length; i++) {
            // For the first element, R[i] would be product except self
            // For the last element of the array, product except self would be L[i]
            // Else, multiple product of all elements to the left and to the right
            answer[i] = L[i] * R[i];
        }

        return answer;
    }

    // V2
    // IDEA : O(1) space approach
    // https://leetcode.com/problems/product-of-array-except-self/editorial/
    public int[] productExceptSelf_3(int[] nums) {

        // The length of the input array
        int length = nums.length;

        // Final answer array to be returned
        int[] answer = new int[length];

        // answer[i] contains the product of all the elements to the left
        // Note: for the element at index '0', there are no elements to the left,
        // so the answer[0] would be 1
        answer[0] = 1;
        for (int i = 1; i < length; i++) {

            // answer[i - 1] already contains the product of elements to the left of 'i - 1'
            // Simply multiplying it with nums[i - 1] would give the product of all
            // elements to the left of index 'i'
            answer[i] = nums[i - 1] * answer[i - 1];
        }

        // R contains the product of all the elements to the right
        // Note: for the element at index 'length - 1', there are no elements to the right,
        // so the R would be 1
        int R = 1;
        for (int i = length - 1; i >= 0; i--) {

            // For the index 'i', R would contain the
            // product of all elements to the right. We update R accordingly
            answer[i] = answer[i] * R;
            R *= nums[i];
        }

        return answer;
    }

}
