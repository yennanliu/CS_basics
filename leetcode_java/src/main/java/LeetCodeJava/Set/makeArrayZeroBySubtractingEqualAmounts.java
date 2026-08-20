package LeetCodeJava.Set;

// https://leetcode.com/problems/make-array-zero-by-subtracting-equal-amounts/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class makeArrayZeroBySubtractingEqualAmounts {

    // V0
    // IDEA: HASH SET (count of `distinct positive` values)
    /**  NOTE !!!
     *
     *   1. every op picks the CURRENT smallest positive value x,
     *      and subtracts x from every positive element
     *
     *      -> so all elements that are `equal` become 0 at the SAME time
     *      -> and elements that are `different` stay different (until 0)
     *
     *   2. therefore the answer is simply
     *      `the number of distinct NON-ZERO values` in nums
     *
     *      (0 needs no op at all)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minimumOperations(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        Set<Integer> distinctPositive = new HashSet<>();
        for (int x : nums) {
            /** NOTE !!! we ONLY collect `positive` val (0 needs no op) */
            if (x > 0) {
                distinctPositive.add(x);
            }
        }

        return distinctPositive.size();
    }

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
