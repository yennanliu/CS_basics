package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/split-array-largest-sum/description/

import java.util.Arrays;

public class SplitArrayLargestSum {

    // V0
    // TODO : implement
//    public int splitArray(int[] nums, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/split-array-largest-sum/solutions/1899033/java-simple-and-easy-solution-beats-100/
    int[] nums;
    public int splitArray_1(int[] nums, int m) {
        this.nums = nums;
        int low = 0, high = 0, min = Integer.MAX_VALUE;
        for(int i=0;i<nums.length;i++){
            low = Math.max(low, nums[i]);
            high += nums[i];
        }
        while(low <= high) {
            int mid = (low + high) / 2;
            if(required_no_of_chunks(mid, m)){
                min = Math.min(min, mid);
                high = mid - 1;
            }
            else low = mid + 1;
        }
        return min;
    }

    private boolean required_no_of_chunks(int mid, int m){
        int chunks = 0, i=0;
        while(i < nums.length){
            int val = 0;
            while(i < nums.length && nums[i] + val <= mid){
                val += nums[i++];
            };
            chunks++;
        }
        return chunks <= m;
    }

    // V2
    // IDEA : Top-down recursion + memoization
    // https://leetcode.com/problems/split-array-largest-sum/solutions/1084798/java-top-down-recursion-memoization-o-n-2-m-time/
    public int splitArray_2(int[] nums, int m) {
        int[][] memo = new int[nums.length][m+1];

        for (int i = 0; i < memo.length; i++) {
            Arrays.fill(memo[i], -1);
        }

        return walk(nums, memo, 0, m);
    }

    private int walk(int[] nums, int[][] memo, int start, int rem) {
        // base case
        if (rem == 0 && start == nums.length) {
            return 0;
        }
        if (rem == 0 || start == nums.length) {
            // if we reach the end and have not used up all patitions
            // or have used up all partitions and have not reached the end,
            // we do not want to count the current way of partitioning.
            // Return MAX_VALUE so that we don't contribute to the return value.
            return Integer.MAX_VALUE;
        }

        int n = nums.length;
        int ret = Integer.MAX_VALUE;
        int curSum = 0;

        if (memo[start][rem] != -1) {
            return memo[start][rem];
        }

        // try all positions to end the current partition.
        for (int i = start; i < n; i++) {
            curSum += nums[i];

            // answer for partitioning the subarray to the right of the current partition,
            // with one less partition number allowance, because we already used one
            // for the current partition. i.e. (rem - 1).
            int futureSum = walk(nums, memo, i + 1, rem - 1);

            // we want to minimum of the largest sum of the partitions.
            ret = Math.min(ret, Math.max(curSum, futureSum));
        }

        memo[start][rem] = ret;
        return ret;
    }

    // V3
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/split-array-largest-sum/solutions/1904499/java-0ms-100-clean-simplest-solution-with-comments/
    public int splitArray(int[] nums, int m) {

        if (nums == null || nums.length == 0 || m == 0 ) {
            return 0;
        }

        int max = 0;
        int sum = 0;

        // the lower boundary will be max and upper bounder will be sum for the binary search
        for ( int num : nums ) {
            sum = sum + num;
            max = Math.max(num, max);
        }

        // base checks where we do not need to apply binary search
        if ( m == nums.length ) {
            return max;
        } else if ( m == 1 ) {
            return sum;
        } else {

            int ans = 0;
            int lo = max;
            int hi = sum;

            while ( lo <= hi ) {
                //remember that we are not using the array index here, do not use (lo+hi)/2
                int mid = lo + ( hi-lo )/2;
                //check if it is possible to form m subarrays with the given mid
                if( isPossible(nums, mid, m) ) {
                    //if yes, store the answer and reduce the upper boundary
                    ans = mid;
                    hi = mid - 1;
                } else {
                    //if no, increase the lower boundary to get a higher mid
                    lo = mid + 1;
                }
            }

            return ans;

        }
    }

    public static boolean isPossible ( int[] nums, int mid, int m ) {
        int sum = 0;
        int requiredSubarrays = 1;

        for ( int i=0; i < nums.length; i++ ) {
            sum = sum+nums[i];
            if ( sum > mid ) {
                requiredSubarrays++;
                sum = nums[i];
            }
        }

        if ( requiredSubarrays <= m ) {
            return true;
        }

        return false;
    }


}
