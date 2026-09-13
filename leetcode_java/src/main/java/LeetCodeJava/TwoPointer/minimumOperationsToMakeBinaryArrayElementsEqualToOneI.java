package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/description/

import java.util.Arrays;

/**
 * 3191. Minimum Operations to Make Binary Array Elements Equal to One I
 * Medium
 *
 * You are given a binary array nums.
 *
 * You can do the following operation on the array any number of times (possibly zero):
 *
 * Choose any 3 consecutive elements from the array and flip all of them.
 *
 * Flipping an element means changing its value from 0 to 1, and from 1 to 0.
 *
 * Return the minimum number of operations required to make all elements in nums equal to 1. If it is impossible, return -1.
 *
 * Example 1:
 *
 * Input: nums = [0,1,1,1,0,0]
 * Output: 3
 * Explanation:
 *
 * We can do the following operations:
 *
 * Choose the elements at indices 0, 1 and 2. The resulting array is nums = [1,0,0,1,0,0].
 * Choose the elements at indices 1, 2 and 3. The resulting array is nums = [1,1,1,0,0,0].
 * Choose the elements at indices 3, 4 and 5. The resulting array is nums = [1,1,1,1,1,1].
 *
 * Example 2:
 *
 * Input: nums = [0,1,1,1]
 * Output: -1
 * Explanation:
 *
 * It is impossible to make all elements equal to 1.
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * 0 <= nums[i] <= 1
 *
 */
public class minimumOperationsToMakeBinaryArrayElementsEqualToOneI {

    // V0
    // IDEA :  2 POINTERS
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations(int[] nums) {

        int res = 0;

        /**
         *  NOTE !!!
         *
         *  here we set up l, r as 2 pointers
         *
         *  l : index check if element is 0
         *  r : check if pointer reach the end of array
         */
        int l = 0;
        int r = 2;
        while (r < nums.length){
            if (nums[l] == 0){
                /**
                 *  NOTE !!!
                 *
                 *  here we use a tmp pointer j
                 *  to check sub array value
                 *  and do "flip" op
                 */
                for (int j = l; j <= l+2; j++){
                    if (nums[j] == 0){
                        nums[j] = 1;
                    }else{
                        nums[j] = 0;
                    }
                }
                //System.out.println("nums =  " + Arrays.toString(nums));
                res += 1;
            }
            r += 1;
            l += 1;
        }

        // check if still has 0
        for (int i = 0; i < nums.length; i++){
            if (nums[i] == 0){
                return -1;
            }
        }

        return res;
    }

    // V1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/submissions/1297671097/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations_1(int[] nums) {
        int n = nums.length;
        int i = 0;
        /** NOTE !!! j init as 2 */
        int j = 2;
        int ans = 0;

        while (j < n) {
            /** NOTE !!!
             *
             *  if  element == 0, flip it to 1 (idx ~ idx +3)
             *  then i to next idx (idx+=1), check again if element == 0, if so, repeat above flip
             *  ... and continue same op
             */
            if (nums[i] == 0) {
                ans++;
                for (int k = i; k < i + 3; k++) {
                    nums[k] = (nums[k] == 0) ? 1 : 0;
                }
            }
            /** NOTE !!!
             *
             *  keep adding i and j in every while loop
             *  and since j is ahead i with 2 idx
             *  so at last iteration of  "j < n", i should already be able to visit whole array (for (int k = i; k < i + 3; k++))
             */
            i++;
            j++;
        }

        for (int m = 0; m < n; m++) {
            if (nums[m] == 0) {
                return -1;
            }
        }

        return ans;
    }

    // V2
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/solutions/5352828/easy-approach/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations_2(int[] nums) {
        int n = nums.length;
        int cnt = 0;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0 && i + 2 < n) {
                nums[i] = 1;
                if (nums[i + 1] == 1) {
                    nums[i + 1] = 0;
                } else {
                    nums[i + 1] = 1;
                }
                if (nums[i + 2] == 1) {
                    nums[i + 2] = 0;
                } else {
                    nums[i + 2] = 1;
                }
                cnt++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                return -1;
            }
        }

        return cnt;
    }

    // V3
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/solutions/5353478/basic-ternary-operator-easy-beginner/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations_3(int[] nums) {
        int ans = 0;

        // Iterate through the array up to the third-last element
        for (int i = 0; i < nums.length - 2; i++) {
            // If the current element is 0, perform an operation
            if (nums[i] == 0) {
                ans++;
                // Flip the current element and the next two elements
                nums[i] = 1;
                nums[i + 1] = nums[i + 1] == 0 ? 1 : 0;
                nums[i + 2] = nums[i + 2] == 0 ? 1 : 0;
            }
        }

        // Check the last two elements if they are 0, return -1 as they cannot be flipped
        for (int i = nums.length - 2; i < nums.length; i++) {
            if (nums[i] == 0) {
                return -1;
            }
        }

        return ans;
    }

}
