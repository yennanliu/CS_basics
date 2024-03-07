package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-product-subarray/description/

public class MaximumProductSubarray {

    // V0
    // IDEA : DP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/maximum-product-subarray.py#L69
    // IDEA : cur max = max (cur, cur * dp[k-1])
    //        But, also needs to consider "minus number"
    //        -> e.g.  (-1) * (-3) = 3
    //        -> so we NEED to track maxSoFar, and minSoFar
    public int maxProduct(int[] nums) {

        // null check
        if (nums.length == 0){
            return 0;
        }
        // init
        int maxSoFar = nums[0];
        int minSoFar = nums[0];
        int res = maxSoFar;

        for (int i = 1; i < nums.length; i++){

            int cur = nums[i];
            /**
             *  or, can use below trick to get max in 3 numbers
             *
             *   max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
             *   min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
             *
             */
            int tmpMax = findMax(cur, maxSoFar * cur, minSoFar * cur);
            minSoFar = findMin(cur, maxSoFar * cur, minSoFar * cur);
            maxSoFar = tmpMax;

            res = Math.max(maxSoFar, res);
        }

        return res;
    }

    private int findMax(int a, int b, int c){
        if (a >= b && a >= c){
            return a;
        }
        else if (b >= a && b >= c){
            return b;
        }else{
            return c;
        }
    }

    private int findMin(int a, int b, int c){
        if (a <= b && a <= c){
            return a;
        }
        else if (b <= a && b <= c){
            return b;
        }else{
            return c;
        }
    }

    // V1
    // IDEA : For each index i keep updating the max and min. We are also keeping min because on multiplying with any negative number your min will become max and max will become min. So for every index i we will take max of (i-th element, prevMax * i-th element, prevMin * i-th element).
    // -> max of (i-th element, prevMax * i-th element, prevMin * i-th element)
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    public int maxProduct_1(int[] nums) {

        int max = nums[0], min = nums[0], ans = nums[0];

        for (int i = 1; i < nums.length; i++) {

            int temp = max;  // store the max because before updating min your max will already be updated

            max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
            min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);

            if (max > ans) {
                ans = max;
            }
        }

        return ans;
    }

    // V2
    // IDEA : Just the slight modification of previous approach. As we know that on multiplying with negative number max will become min and min will become max, so why not as soon as we encounter negative element, we swap the max and min already.
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    public int maxProduct_2(int[] nums) {

        int max = nums[0], min = nums[0], ans = nums[0];
        int n = nums.length;

        for (int i = 1; i < n; i++) {

            // Swapping min and max
            if (nums[i] < 0){
                int temp = max;
                max = min;
                min = temp;
            }

            max = Math.max(nums[i], max * nums[i]);
            min = Math.min(nums[i], min * nums[i]);

            ans = Math.max(ans, max);
        }

        return ans;
    }

    // V3
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/maximum-product-subarray/solutions/1608862/java-3-solutions-detailed-explanation-using-image/
    public int maxProduct_3(int[] nums) {

        int n = nums.length;
        int l=1,r=1;
        int ans=nums[0];

        for(int i=0;i<n;i++){

            //if any of l or r become 0 then update it to 1
            l = l==0 ? 1 : l;
            r = r==0 ? 1 : r;

            l *= nums[i];   //prefix product
            r *= nums[n-1-i];    //suffix product

            ans = Math.max(ans,Math.max(l,r));

        }

        return ans;
    }

}
