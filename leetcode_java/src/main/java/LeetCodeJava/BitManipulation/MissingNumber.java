package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/missing-number/?envType=list&envId=xoqag3yj

import java.util.Arrays;

public class MissingNumber {

    // V0
    // IDEA : MATH
    public int missingNumber(int[] nums) {
        if (nums.length == 0 || nums == null){
            return 0;
        }
        int _len = nums.length;
        int sum1 = Arrays.stream(nums).sum();
        int sum2 = ((0 + _len) * (_len+1)) / 2;
        return sum2 - sum1;
    }

    // V1
    // IDEA : MATH
    // https://leetcode.com/problems/missing-number/solutions/4461518/beats-100-with-java-86-c-easiest-logic-6-lines-code-java-100-runtime/?envType=list&envId=xoqag3yj
    public int missingNumber_1(int[]nums) {
        //Step1: Find the length of array.
        int n = nums.length;

        //Step2: Calculate the actual sum using Mathematical formulae
        long actualSum = (n*(n+1))/2;

        //Step3: Calculate the given sum.
        long sum = 0;
        for(int element: nums)
        {
            sum += element;
        }

        //Step 4: return actualSum-givenSum.
        return (int)(actualSum-sum);
    }

}
