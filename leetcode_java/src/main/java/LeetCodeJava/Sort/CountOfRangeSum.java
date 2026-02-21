package LeetCodeJava.Sort;

// https://leetcode.com/problems/count-of-range-sum/description/
/**
 * 327. Count of Range Sum
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums and two integers lower and upper, return the number of range sums that lie in [lower, upper] inclusive.
 *
 * Range sum S(i, j) is defined as the sum of the elements in nums between indices i and j inclusive, where i <= j.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [-2,5,-1], lower = -2, upper = 2
 * Output: 3
 * Explanation: The three ranges are: [0,0], [2,2], and [0,2] and their respective sums are: -2, -1, 2.
 * Example 2:
 *
 * Input: nums = [0], lower = 0, upper = 0
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * -231 <= nums[i] <= 231 - 1
 * -105 <= lower <= upper <= 105
 * The answer is guaranteed to fit in a 32-bit integer.
 *
 *
 *
 */
public class CountOfRangeSum {

    // V0
//    public int countRangeSum(int[] nums, int lower, int upper) {
//
//    }

    // V1-1
    // IDEA: BRUTE FORCE (TLE)
    // https://leetcode.com/problems/count-of-range-sum/solutions/77990/share-my-solution-by-dietpepsi-1g5d/
    public int countRangeSum_1_1(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; ++i)
            sums[i + 1] = sums[i] + nums[i];
        int ans = 0;
        for (int i = 0; i < n; ++i)
            for (int j = i + 1; j <= n; ++j)
                if (sums[j] - sums[i] >= lower && sums[j] - sums[i] <= upper)
                    ans++;
        return ans;
    }

    // V1-1
    // IDEA: MERGE SORT
    // https://leetcode.com/problems/count-of-range-sum/solutions/77990/share-my-solution-by-dietpepsi-1g5d/
    public int countRangeSum_1_2(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; ++i)
            sums[i + 1] = sums[i] + nums[i];
        return countWhileMergeSort(sums, 0, n + 1, lower, upper);
    }

    private int countWhileMergeSort(long[] sums, int start, int end, int lower, int upper) {
        if (end - start <= 1)
            return 0;
        int mid = (start + end) / 2;
        int count = countWhileMergeSort(sums, start, mid, lower, upper)
                + countWhileMergeSort(sums, mid, end, lower, upper);
        int j = mid, k = mid, t = mid;
        long[] cache = new long[end - start];
        for (int i = start, r = 0; i < mid; ++i, ++r) {
            while (k < end && sums[k] - sums[i] < lower)
                k++;
            while (j < end && sums[j] - sums[i] <= upper)
                j++;
            while (t < end && sums[t] < sums[i])
                cache[r++] = sums[t++];
            cache[r] = sums[i];
            count += j - k;
        }
        System.arraycopy(cache, 0, sums, start, t - start);
        return count;
    }



    // V2
    // IDEA: MERGE SORT
    // https://leetcode.com/problems/count-of-range-sum/solutions/1178174/java-clean-merge-sort-on-logn-solution-w-qg4i/
    private int lower;
    private int upper;
    private int count = 0;
    private long[] pfxSum;

    public int countRangeSum_2_2(int[] nums, int lower, int upper) {
        int n = nums.length;
        this.lower = lower;
        this.upper = upper;

        this.pfxSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            pfxSum[i + 1] = pfxSum[i] + nums[i];
        }

        mergeSort(0, n);
        return count;
    }

    private void mergeSort(int low, int high) {
        if (low >= high)
            return;
        int mid = low + (high - low) / 2;

        mergeSort(low, mid);
        mergeSort(mid + 1, high);

        int i = mid + 1, j = mid + 1;
        for (int k = low; k <= mid; k++) {
            while (i <= high && pfxSum[i] - pfxSum[k] < lower)
                i++;
            while (j <= high && pfxSum[j] - pfxSum[k] <= upper)
                j++;

            count += j - i;
        }

        merge(low, mid, high);
    }

    private void merge(int low, int mid, int high) {
        long[] helper = new long[high - low + 1];
        for (int i = low; i <= high; i++) {
            helper[i - low] = pfxSum[i];
        }

        int i = low, j = mid + 1;
        int idx = low;

        while (i <= mid && j <= high) {
            if (helper[i - low] < helper[j - low]) {
                pfxSum[idx++] = helper[i++ - low];
            } else {
                pfxSum[idx++] = helper[j++ - low];
            }
        }

        while (i <= mid) {
            pfxSum[idx++] = helper[i++ - low];
        }
    }




}
