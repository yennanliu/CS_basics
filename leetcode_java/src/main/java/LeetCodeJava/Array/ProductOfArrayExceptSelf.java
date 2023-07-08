package LeetCodeJava.Array;

// https://leetcode.com/problems/product-of-array-except-self/

public class ProductOfArrayExceptSelf {

    // VO
    // IDEA : ARRAY PRODCUT
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
