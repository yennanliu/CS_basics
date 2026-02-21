package LeetCodeJava.Sort;

// https://leetcode.com/problems/maximum-gap/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 164. Maximum Gap
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, return the maximum difference between two successive elements in its sorted form. If the array contains less than two elements, return 0.
 *
 * You must write an algorithm that runs in linear time and uses linear extra space.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,6,9,1]
 * Output: 3
 * Explanation: The sorted form of the array is [1,3,6,9], either (3,6) or (6,9) has the maximum difference 3.
 * Example 2:
 *
 * Input: nums = [10]
 * Output: 0
 * Explanation: The array contains less than 2 elements, therefore return 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 *
 *
 */
public class MaximumGap {

    // V0
    // IDEA: SORT ( but time complexity is O(N log N), rather than O (N) )
    public int maximumGap(int[] nums) {
        // edge
        if (nums.length < 2) {
            return 0;
        }

        //System.out.println(">>> (before sort) nums = " + Arrays.toString(nums));
        Arrays.sort(nums); // /??
        //System.out.println(">>> (after sort) nums = " + Arrays.toString(nums));

        int biggestGap = 0;
        for (int i = 1; i < nums.length; i++) {
            biggestGap = Math.max(biggestGap, nums[i] - nums[i - 1]);
        }

        return biggestGap;
    }

    // V0-1
    // IDEA: BUCKET SORT (gemini)
    // time:  O (N), space: O (N)
    public int maximumGap_0_1(int[] nums) {
        if (nums == null || nums.length < 2)
            return 0;

        int n = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        // If all elements are the same
        if (min == max)
            return 0;

        // 1. Calculate bucket size and count
        // We want bucket size to be at least 1
        int bucketSize = Math.max(1, (max - min) / (n - 1));
        int bucketCount = (max - min) / bucketSize + 1;

        // 2. Initialize buckets (only need to store min and max for each)
        int[] bucketMin = new int[bucketCount];
        int[] bucketMax = new int[bucketCount];
        Arrays.fill(bucketMin, Integer.MAX_VALUE);
        Arrays.fill(bucketMax, Integer.MIN_VALUE);

        // 3. Put numbers into buckets
        for (int num : nums) {
            int idx = (num - min) / bucketSize;
            bucketMin[idx] = Math.min(bucketMin[idx], num);
            bucketMax[idx] = Math.max(bucketMax[idx], num);
        }

        // 4. Scan buckets to find the max gap
        int maxGap = 0;
        int previousMax = min; // Start with the global min

        for (int i = 0; i < bucketCount; i++) {
            // Skip empty buckets
            if (bucketMin[i] == Integer.MAX_VALUE)
                continue;

            // Gap is current bucket's min minus previous bucket's max
            maxGap = Math.max(maxGap, bucketMin[i] - previousMax);
            previousMax = bucketMax[i];
        }

        return maxGap;
    }


    // V1

    // V2
    // https://leetcode.com/problems/maximum-gap/solutions/1240838/js-python-java-c-simple-bucket-sort-solu-ws5s/
    public int maximumGap_2(int[] nums) {
        if (nums.length < 2)
            return 0;
        int hi = 0, lo = Integer.MAX_VALUE, ans = 0;
        for (int n : nums) {
            hi = Math.max(hi, n);
            lo = Math.min(lo, n);
        }
        int bsize = Math.max((hi - lo) / (nums.length - 1), 1);
        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = (hi - lo) / bsize; i >= 0; i--)
            buckets.add(new ArrayList<>());
        for (int n : nums)
            buckets.get((n - lo) / bsize).add(n);
        int currhi = 0;
        for (List<Integer> b : buckets) {
            if (b.isEmpty())
                continue;
            int prevhi = currhi > 0 ? currhi : b.get(0), currlo = b.get(0);
            for (int n : b) {
                currhi = Math.max(currhi, n);
                currlo = Math.min(currlo, n);
            }
            ans = Math.max(ans, currlo - prevhi);
        }
        return ans;
    }


    // V3
    // https://leetcode.com/problems/maximum-gap/solutions/50642/radix-sort-solution-in-java-with-explana-m003/
    public int maximumGap_3(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        // m is the maximal number in nums
        int m = nums[0];
        for (int i = 1; i < nums.length; i++) {
            m = Math.max(m, nums[i]);
        }

        int exp = 1; // 1, 10, 100, 1000 ...
        int R = 10; // 10 digits

        int[] aux = new int[nums.length];

        while (m / exp > 0) { // Go through all digits from LSB to MSB
            int[] count = new int[R];

            for (int i = 0; i < nums.length; i++) {
                count[(nums[i] / exp) % 10]++;
            }

            for (int i = 1; i < count.length; i++) {
                count[i] += count[i - 1];
            }

            for (int i = nums.length - 1; i >= 0; i--) {
                aux[--count[(nums[i] / exp) % 10]] = nums[i];
            }

            for (int i = 0; i < nums.length; i++) {
                nums[i] = aux[i];
            }
            exp *= 10;
        }

        int max = 0;
        for (int i = 1; i < aux.length; i++) {
            max = Math.max(max, aux[i] - aux[i - 1]);
        }

        return max;
    }


    // V4



}
