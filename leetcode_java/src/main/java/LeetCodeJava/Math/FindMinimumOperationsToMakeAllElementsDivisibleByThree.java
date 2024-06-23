package LeetCodeJava.Math;

// https://leetcode.com/problems/find-minimum-operations-to-make-all-elements-divisible-by-three/

public class FindMinimumOperationsToMakeAllElementsDivisibleByThree {

    // V0
    // IDEA : MATH
    public int minimumOperations(int[] nums) {

        int res = 0;
        for (int x : nums){
            int diff = (x % 3);
            int diff_ = diff;
            if (diff == 2){
                diff_ = 1;
            }
           // System.out.println("x = " + x + ", diff = " + diff + " diff_ = " + diff_);
            res += Math.abs(diff_);
        }

        return res;
    }


    // V1
    // IDEA : MATH
    // https://leetcode.com/problems/find-minimum-operations-to-make-all-elements-divisible-by-three/solutions/5352966/simple-approach-if-num-3-0-count-explained/
    public int minimumOperations_1(int[] nums) {
        int count = 0;
        for (int num : nums) {
            if (num % 3 != 0) {
                count++;
            }
        }
        return count;
    }

}
