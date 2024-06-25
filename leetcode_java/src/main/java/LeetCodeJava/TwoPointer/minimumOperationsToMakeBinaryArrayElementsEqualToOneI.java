package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-i/description/

import java.util.Arrays;

public class minimumOperationsToMakeBinaryArrayElementsEqualToOneI {

    // V0
    // IDEA :  2 POINTERS
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
