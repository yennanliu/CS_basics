package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-average-of-smallest-and-largest-elements/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class minimumAverageOfSmallestAndLargestElements {

    // V0
    // IDEA : MATH
    public double minimumAverage(int[] nums) {

        if (nums.length == 1){
            return nums[0];
        }

        List<Float> collected = new ArrayList<>();

        // sort, in increasing order
        System.out.println("before sort : " + Arrays.toString(nums));
        Arrays.sort(nums);
        System.out.println("after sort : " + Arrays.toString(nums));

        while(nums.length > 0){
            int max_ = nums[nums.length-1];
            int min_ = nums[0];
            nums = Arrays.copyOfRange(nums, 1, nums.length-1);
            float val = (float) (max_ + min_) / 2; // ?
            collected.add(val);
        }

        System.out.println("(before) collected  = " + collected);
        collected.sort((a,b) -> {
            if (a > b){
                return 1;
            }else if (a < b){
                return -1;
            }
            return 0;
        });

        System.out.println("(after) collected  = " + collected);
        // return collected.stream().min();
        return collected.get(0);
    }

}
