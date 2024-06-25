package LeetCodeJava.Set;

// https://leetcode.com/problems/make-array-zero-by-subtracting-equal-amounts/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
