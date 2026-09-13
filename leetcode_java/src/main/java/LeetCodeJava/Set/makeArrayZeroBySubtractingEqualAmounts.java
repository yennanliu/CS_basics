package LeetCodeJava.Set;

// https://leetcode.com/problems/make-array-zero-by-subtracting-equal-amounts/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 2357. Make Array Zero by Subtracting Equal Amounts
 * Easy
 *
 * You are given a non-negative integer array nums. In one operation, you must:
 *
 * Choose a positive integer x such that x is less than or equal to the smallest non-zero element in nums.
 * Subtract x from every positive element in nums.
 *
 * Return the minimum number of operations to make every element in nums equal to 0.
 *
 * Example 1:
 *
 * Input: nums = [1,5,0,3,5]
 * Output: 3
 * Explanation:
 * In the first operation, choose x = 1. Now, nums = [0,4,0,2,4].
 * In the second operation, choose x = 2. Now, nums = [0,2,0,0,2].
 * In the third operation, choose x = 2. Now, nums = [0,0,0,0,0].
 *
 * Example 2:
 *
 * Input: nums = [0]
 * Output: 0
 * Explanation: Each element in nums is already 0 so no operations are needed.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 100
 *
 */
public class makeArrayZeroBySubtractingEqualAmounts {

    // V0
    // TODO : fix
//    public int minimumOperations_1(int[] nums) {
//
//        if (nums.length == 0){
//            if (nums[0] == 0){
//                return 0;
//            }
//            return 1;
//        }
//
//        int res = 0;
//        //int i = 0;
//        for (int i = 0; i < nums.length; i++){
//            if (nums[i] != 0){
//                int[] sub = Arrays.copyOfRange(nums, i, nums.length-1);
//                int minVal = Arrays.stream(sub).min().getAsInt();
//                for (int j = i; j < nums.length; j++){
//                    if (nums[j] != 0){
//                        nums[j] -= minVal;
//                    }
//                }
//                res += 1;
//            }
//        }
//
//        return res;
//    }

    // V1
    // IDEA : HASHSET
    // https://leetcode.com/problems/make-array-zero-by-subtracting-equal-amounts/solutions/2357691/java-c-python-number-of-different-positives/
    // Same elements, are always same
    //  -> Deduplicate
    //
    // Different elements, are always different until 0
    //  -> Counts unique elements
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minimumOperations_1(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int a: nums)
            if (a > 0)
                set.add(a);
        return set.size();
    }

    // V2
    // IDEA : HASHSET
    // https://leetcode.com/problems/make-array-zero-by-subtracting-equal-amounts/solutions/4789123/hashset-method/
    // Number of unique element determine the number of operations because if
    // the numbers present in the array are same would become zero in a single operation.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minimumOperations_2(int[] nums) {
        HashSet<Integer> a=new HashSet<Integer>();
        for(int i=0;i<nums.length;i++){
            if(nums[i]==0)
                continue;
            a.add(nums[i]);
        }
        return a.size();
    }

}
