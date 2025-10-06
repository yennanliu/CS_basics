package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/frequency-of-the-most-frequent-element/description/

import java.util.Arrays;

/**
 *  1838. Frequency of the Most Frequent Element
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * The frequency of an element is the number of times it occurs in an array.
 *
 * You are given an integer array nums and an integer k. In one operation, you can choose an index of nums and increment the element at that index by 1.
 *
 * Return the maximum possible frequency of an element after performing at most k operations.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,4], k = 5
 * Output: 3
 * Explanation: Increment the first element three times and the second element two times to make nums = [4,4,4].
 * 4 has a frequency of 3.
 * Example 2:
 *
 * Input: nums = [1,4,8,13], k = 5
 * Output: 2
 * Explanation: There are multiple optimal solutions:
 * - Increment the first element three times to make nums = [4,4,8,13]. 4 has a frequency of 2.
 * - Increment the second element four times to make nums = [1,8,8,13]. 8 has a frequency of 2.
 * - Increment the third element five times to make nums = [1,4,13,13]. 13 has a frequency of 2.
 * Example 3:
 *
 * Input: nums = [3,9,6], k = 2
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 105
 * 1 <= k <= 105
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 */
public class FrequencyOfTheMostFrequentElement {

    // V0
    // IDEA: SLIDE WINDOW + cumSum
    public int maxFrequency(int[] nums, int k) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }

        Arrays.sort(nums);

        int res = 0;
        /** NOTE !!
         *
         *  we use `long` for dealing with `overflow` issue
         */
        long windowSum = 0L;
        int l = 0;

        for (int r = 0; r < nums.length; r++) {
            // cur window sum
            windowSum += nums[r];
            /** NOTE !!
             *
             *  we use `long` for dealing with `overflow` issue
             */
            while ((long) (r - l + 1) * nums[r] - windowSum > k) {
                windowSum -= nums[l];
                l += 1;
            }
            res = Math.max(res, r - l + 1);
        }

        return res;
    }

    // V0-1
    // IDEA: SLIDE WINDOW (fixed by gpt)
    /**  IDEA:
     *
     * 🧠 Intuition
     * 	•	Sort first, so that when we “upgrade” elements to match nums[right], it’s always non-decreasing.
     * 	•	Maintain the current window sum.
     * 	•	Compute cost = (target * windowSize) - sumOfWindow.
     * 	•	If cost ≤ k → valid window → update max frequency.
     * 	•	If cost > k → shrink window by moving left.
     */
    public int maxFrequency_0_1(int[] nums, int k) {
        /**
         *
         * Sort nums in non-decreasing order.
         * Sorting is crucial — when array is sorted,
         * if we want to raise several elements
         * to match some target value,
         * the cheapest target to aim for is
         * always the current rightmost value in a sorted window.
         */
        Arrays.sort(nums); // sort ascending

        /**
         * Maintain the `sum of the current sliding window. `
         * Use long to prevent integer overflow
         * if nums values and window size are large.
         *
         *
         * -> windowSum: sum of cur slide window
         */
        long windowSum = 0; // use long to avoid overflow
        // left is the left index of the sliding window. res stores the best (maximum) window size found so far.
        // Initialize res = 1 because at least one element always fits.
        int left = 0;
        int res = 1;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];

            /**
             *  NOTE !!! core logic:
             *
             *  This computes the cost to make every element in window [left..right] equal to nums[right]:
             *
             * 	 - nums[right] * windowSize is the total value
             * 	   if every element became nums[right].
             * 	 - Subtract windowSum (the current total) to
             * 	   get how much increment is needed.
             *
             * If this cost exceeds k, the current window is
             *  not achievable with at most k increments,
             *  so we must shrink it from the left.
             *
             *
             * Important: cast nums[right] to long (done by (long)) to avoid overflow in product.
             *
             */
            // cost to make all numbers in window [left..right] equal to nums[right]:
            // cost = nums[right] * windowSize - windowSum
            // if cost > k, shrink from the left
            /**  NOTE !!
             *
             *  nums[right] * (right - left + 1):  how much increment needed
             *
             */
            while ((long) nums[right] * (right - left + 1) - windowSum > k) {
                /**
                 * Remove the leftmost element from the window sum because we are going to increment left.
                 */
                windowSum -= nums[left];
                left++;
            }

            // update max length
            res = Math.max(res, right - left + 1);
        }

        return res;
    }

    // V1-1
    // IDEA: Sliding Window
    // https://leetcode.com/problems/frequency-of-the-most-frequent-element/editorial/
    public int maxFrequency_1_1(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int ans = 0;
        long curr = 0;

        for (int right = 0; right < nums.length; right++) {
            long target = nums[right];
            curr += target;

            while ((right - left + 1) * target - curr > k) {
                curr -= nums[left];
                left++;
            }

            ans = Math.max(ans, right - left + 1);
        }

        return ans;
    }

    // V1-2
    // IDEA:  Advanced Sliding Window
    // https://leetcode.com/problems/frequency-of-the-most-frequent-element/editorial/
    public int maxFrequency_1_2(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        long curr = 0;

        for (int right = 0; right < nums.length; right++) {
            long target = nums[right];
            curr += target;

            if ((right - left + 1) * target - curr > k) {
                curr -= nums[left];
                left++;
            }
        }

        return nums.length - left;
    }

    // V1-3
    // IDEA: Binary Search
    // https://leetcode.com/problems/frequency-of-the-most-frequent-element/editorial/
    public int check(int i, int k, int[] nums, long[] prefix) {
        int target = nums[i];
        int left = 0;
        int right = i;
        int best = i;

        while (left <= right) {
            int mid = (left + right) / 2;
            long count = i - mid + 1;
            long finalSum = count * target;
            long originalSum = prefix[i] - prefix[mid] + nums[mid];
            long operationsRequired = finalSum - originalSum;

            if (operationsRequired > k) {
                left = mid + 1;
            } else {
                best = mid;
                right = mid - 1;
            }
        }

        return i - best + 1;
    }

    public int maxFrequency_1_3(int[] nums, int k) {
        Arrays.sort(nums);
        long[] prefix = new long[nums.length];
        prefix[0] = nums[0];

        for (int i = 1; i < nums.length; i++) {
            prefix[i] = nums[i] + prefix[i - 1];
        }

        int ans = 0;
        for (int i = 0; i < nums.length; i++) {
            ans = Math.max(ans, check(i, k, nums, prefix));
        }

        return ans;

    }


    // V2
    // https://leetcode.com/problems/frequency-of-the-most-frequent-element/solutions/5513173/video-sliding-window-solution-by-niits-j4zy/
    public int maxFrequency_2(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0, right = 0;
        long res = 0, total = 0;

        while (right < nums.length) {
            total += nums[right];

            while (nums[right] * (right - left + 1L) > total + k) {
                total -= nums[left];
                left += 1;
            }

            res = Math.max(res, right - left + 1L);
            right += 1;
        }

        return (int) res;
    }


    // V3
    // https://leetcode.com/problems/frequency-of-the-most-frequent-element/solutions/4300738/more-than-one-waydetail-explanation-java-rg2k/
    public int maxFrequency_3(int[] nums, int k) {
        int maxFrequency = 0; // Initialize the maximum frequency
        long currentSum = 0; // Initialize the current sum of the subarray elements

        Arrays.sort(nums); // Sort the array in ascending order

        for (int left = 0, right = 0; right < nums.length; ++right) {
            currentSum += nums[right]; // Include the current element in the subarray sum

            // Check if the current subarray violates the condition (sum + k < nums[right] * length)
            while (currentSum + k < (long) nums[right] * (right - left + 1)) {
                currentSum -= nums[left]; // Adjust the subarray sum by removing the leftmost element
                left++; // Move the left pointer to the right
            }

            // Update the maximum frequency based on the current subarray length
            maxFrequency = Math.max(maxFrequency, right - left + 1);
        }

        return maxFrequency;
    }



}
