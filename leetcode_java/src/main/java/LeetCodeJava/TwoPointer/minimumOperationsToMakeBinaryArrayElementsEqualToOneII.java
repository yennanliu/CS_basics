package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-ii/description/

/**
 * 3192. Minimum Operations to Make Binary Array Elements Equal to One II
 * Medium
 *
 * You are given a binary array nums.
 *
 * You can do the following operation on the array any number of times (possibly zero):
 *
 * Choose any index i from the array and flip all the elements from index i to the end of the array.
 *
 * Flipping an element means changing its value from 0 to 1, and from 1 to 0.
 *
 * Return the minimum number of operations required to make all elements in nums equal to 1.
 *
 * Example 1:
 *
 * Input: nums = [0,1,1,0,1]
 * Output: 4
 * Explanation:
 *
 * We can do the following operations:
 *
 * Choose the index i = 1. The resulting array will be nums = [0,0,0,1,0].
 * Choose the index i = 0. The resulting array will be nums = [1,1,1,0,1].
 * Choose the index i = 4. The resulting array will be nums = [1,1,1,0,0].
 * Choose the index i = 3. The resulting array will be nums = [1,1,1,1,1].
 *
 * Example 2:
 *
 * Input: nums = [1,0,0,0]
 * Output: 1
 * Explanation:
 *
 * We can do the following operation:
 *
 * Choose the index i = 1. The resulting array will be nums = [1,1,1,1].
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 1
 *
 */
public class minimumOperationsToMakeBinaryArrayElementsEqualToOneII {

    // V0
    // IDEA : 2 pointer, brute force
    // TLE (TODO : fix)
//    public int minOperations(int[] nums) {
//
//        if (Arrays.stream(nums).sum() == nums.length){
//            return 0;
//        }
//
//        int res = 0;
//        int i = 0;
//        while (i < nums.length){
//            if (nums[i] == 0){
//                for (int j = i; j < nums.length; j++){
//                    // if 0, do op
//                    if (nums[j] == 0){
//                        nums[j] = 1;
//                    }else{
//                        nums[j] = 0;
//                    }
//                }
//                res += 1;
//            }
//            //System.out.println("nums =  " + Arrays.toString(nums));
//            i += 1;
//        }
//
//        for (int k = 0; k < nums.length; k++){
//            if (nums[k] == 0){
//                return -1;
//            }
//        }
//
//        return res;
//    }

    // V1
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-ii/solutions/5352964/clean-solution-must-check/
    /**
     *  Idea :
     *
     *
     *  Step 1 :
     *      First find a 1, Flip all the elements till the end
     *
     *  Step 2:
     *      Go to the left most 0 of that 1 and Flip all
     *      the elements till the end, now all the elements till that 1 have become 1
     *
     *  Now after step 1 and 2, the elements to right of
     *  that 1 will be same as before step 1,
     *  so we can avoid both the flips (in step 1 and 2), which saves us time
     *
     *  Now repeat step 1 and 2 for the rest of the array
     *
     *  Edge case:
     *      If 1 not found in the end, we can flip
     *      the trailing zeros in one flip
     *
     */
    class Solution {
        /**
         * time = O(N)
         * space = O(1)
         */
        public int minOperations_1(int[] nums) {
            int res = 0;
            boolean zero_found = false;

            for(int i=0; i<nums.length; i++){
                if(nums[i] == 0 && zero_found == false){
                    zero_found = true;
                }else if(nums[i] == 1 && zero_found == true){
                    res += 2;
                    zero_found = false;
                }
            }

            if(nums[nums.length-1] == 0){
                res++;
            }

            return res;
        }
    }

    // V2
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-ii/solutions/5353910/beats-100-time-100-space-java-solution/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations_2(int[] nums) {
        int flipCount =1-nums[nums.length-1];
        for (int i = 0; i < nums.length-1; i++) {
            if(nums[i]==0 && nums[i+1]==1){
                flipCount+=2;
            }
        }
        return flipCount;
    }

    // V3
    // https://leetcode.com/problems/minimum-operations-to-make-binary-array-elements-equal-to-one-ii/solutions/5353296/easy-java-code-single-pass-linear-time/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations_3(int[] nums) {
        int ans=0;
        int t=0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]!=t) continue;
            ans++;
            t=1-t;
        }
        return ans;
    }

}
