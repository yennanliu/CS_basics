package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/description/

import java.util.Arrays;

public class MinimumSwapsToMakeSequencesIncreasing {

    // V0
    // TODO : implement
//    public int minSwap(int[] nums1, int[] nums2) {
//
//    }

    // V1
    // IDEA : DP
    // https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/solutions/3471328/solution/
    public int minSwap_1(int[] A, int[] B) {
        int swapRecord = 1, fixRecord = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i - 1] >= B[i] || B[i - 1] >= A[i]) {
                swapRecord++;
            } else if (A[i - 1] >= A[i] || B[i - 1] >= B[i]) {
                int temp = swapRecord;
                swapRecord = fixRecord + 1;
                fixRecord = temp;
            } else {
                int min = Math.min(swapRecord, fixRecord);
                swapRecord = min + 1;
                fixRecord = min;
            }
        }
        return Math.min(swapRecord, fixRecord);
    }

    // V2
    // IDEA : DFS + MEMORY
    // https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/solutions/3976863/java-with-explanation-dfs-memoization-sc-o-n-tc-o-n/
    Integer[][] memo;
    int SWAPPED = 0;
    int NOT_SWAPPED = 1;

    int NOT_VALID = (int) Math.pow(10, 6);
    public int minSwap_2(int[] nums1, int[] nums2) {
        this.memo = new Integer[nums1.length][2];
        return dfs(nums1, nums2, 0, NOT_SWAPPED);
    }

    private int dfs(int[] nums1, int[] nums2, int index, int swapped) {
        if (index == nums1.length) return 0;

        if (memo[index][swapped] != null) return memo[index][swapped];

        // we have 2 option: 1) swap our arr[index] or not
        int option1 = NOT_VALID;
        // before proceed any option check: is it valid?
        // Is arr[i] > arr[i-1]?
        if (isValidOption(nums1, nums2, index)) {
            // we didn't swapped our arr[i], amount of operations the same
            option1 = dfs(nums1, nums2, index + 1, NOT_SWAPPED);
        }

        // swap, dfs and swap again
        swap(nums1, nums2, index);
        int option2 = NOT_VALID;
        if (isValidOption(nums1, nums2, index) && nums1[index] != nums2[index]) {
            // we swapped our arr[i], amount of operations increased by 1
            option2 = dfs(nums1, nums2, index + 1, SWAPPED) + 1;
        }
        swap(nums1, nums2, index);

        // take the minimum amount of operations
        int result = Math.min(option1, option2);

        // we have 2 ways to save data:
        // if our PREVIOUS index was swapped hold 0, opposite 1
        memo[index][swapped] = result;
        return result;
    }

    private boolean isValidOption(int[] nums1, int[] nums2, int index) {
        if (index == 0) return true;
        return nums1[index] > nums1[index - 1] && nums2[index] > nums2[index - 1];
    }


    private void swap(int[] arr1, int[] arr2, int i) {
        int temp = arr1[i];
        arr1[i] = arr2[i];
        arr2[i] = temp;
    }

    // V3
    // https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/solutions/1849616/fully-explained-solution-for-beginner-recursion-memoization/
    public int minSwap_3(int[] nums1, int[] nums2) {
        int dp[] = new int[nums1.length];
        Arrays.fill(dp, -1);
        int ans = solve(nums1, nums2, 0, dp);
        return ans;
    }

    public int solve(int nums1[], int nums2[], int ind, int[] dp) {

        if (ind == nums1.length) return 0;

        // Condition 1

        if (ind > 0 && (nums1[ind - 1] >= nums1[ind] || nums2[ind - 1] >= nums2[ind])) {

            //	nums1[] = [3,2]
            //	nums2[] = [1,4]

            int t = nums1[ind];
            nums1[ind] = nums2[ind];
            nums2[ind] = t;

            int val = 1 + solve(nums1, nums2, ind + 1, dp);

            // Since after swapping array becomes
            // nums1[] = [3,4]
            // nums2[] = [1,2]
            // therefore we have to swap it back so that we can have our original array.

            t = nums1[ind];
            nums1[ind] = nums2[ind];
            nums2[ind] = t;

            return val;

        }
        // Condition 2

        else if (ind > 0 && (nums1[ind - 1] >= nums2[ind] || nums2[ind - 1] >= nums1[ind])) {
            return solve(nums1, nums2, ind + 1, dp);
        }
        // Condition 3

        else {
            if (dp[ind] != -1) return dp[ind];

            int tempAns1 = solve(nums1, nums2, ind + 1, dp);

            int t = nums1[ind];
            nums1[ind] = nums2[ind];
            nums2[ind] = t;

            int tempAns2 = 1 + solve(nums1, nums2, ind + 1, dp);

            t = nums1[ind];
            nums1[ind] = nums2[ind];
            nums2[ind] = t;

            return dp[ind] = Math.min(tempAns1, tempAns2);
        }
    }

    // V4
    // https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/solutions/1277478/from-recursion-to-dp-4-solutions-java/

}
