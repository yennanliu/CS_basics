package LeetCodeJava.HashTable;

// https://leetcode.com/problems/subarrays-with-k-different-integers/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 Code
 Testcase
 Testcase
 Test Result
 992. Subarrays with K Different Integers
 Solved
 Hard
 Topics
 premium lock icon
 Companies
 Hint
 Given an integer array nums and an integer k, return the number of good subarrays of nums.

 A good array is an array where the number of different integers in that array is exactly k.

 For example, [1,2,3,1,2] has 3 different integers: 1, 2, and 3.
 A subarray is a contiguous part of an array.



 Example 1:

 Input: nums = [1,2,1,2,3], k = 2
 Output: 7
 Explanation: Subarrays formed with exactly 2 different integers: [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
 Example 2:

 Input: nums = [1,2,1,3,4], k = 3
 Output: 3
 Explanation: Subarrays formed with exactly 3 different integers: [1,2,1,3], [2,1,3], [1,3,4].


 Constraints:

 1 <= nums.length <= 2 * 104
 1 <= nums[i], k <= nums.length
 *
 *
 */
public class SubarraysWithKDifferentIntegers {

    // V0
//    public int subarraysWithKDistinct(int[] nums, int k) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE (TLE) (gemini)
    public int subarraysWithKDistinct_0_1(int[] nums, int k) {
        int cnt = 0;
        int n = nums.length;

        for (int l = 0; l < n; l++) {
            Set<Integer> set = new HashSet<>();
            for (int r = l; r < n; r++) {
                set.add(nums[r]);

                int distinctCount = set.size();

                if (distinctCount == k) {
                    cnt++;
                } else if (distinctCount > k) {
                    // If we exceed K, no point in moving 'r' further for this 'l'
                    break;
                }
            }
        }
        return cnt;
    }

    // V0-2
    // IDEA: HASHMAP + PREFIX (gemini)
    /** IDEA: `Prefix` logic
     *
     * ### 🔍 How this "Prefix" logic works
     *
     * Imagine `nums = [1, 2, 1, 2, 3]` and `k = 2`.
     * When `right` is at index 3 (the second `2`):
     *
     * * The window is `[1, 2, 1, 2]`.
     * * The `while` loop sees that `nums[left]` (which is `1`) appears more than once in the map.
     * * It removes the first `1` and increments `prefix` to 1.
     * * Now the window is `[2, 1, 2]`.
     * * It checks the new `left` (`2`). It also appears more than once!
     * * It removes the `2` and increments `prefix` to 2.
     * * Now the window is `[1, 2]`.
     *
     * **Final Step**: Because `prefix` is 2, it means there were **2** extra elements we could have included without changing the distinct count. So we add `prefix + 1` (which is 3) to our total.
     * Those 3 subarrays are:
     *
     * 1. `[1, 2]`
     * 2. `[2, 1, 2]`
     * 3. `[1, 2, 1, 2]`
     *
     * ### 📊 Comparison
     *
     * * **Your Code**: Tries to count 1 per `right` move ( but logically incomplete).
     * * **Subtraction Trick**: Easy to understand, uses two passes ().
     * * **This One-Pass**: Very efficient but much harder to explain in an interview.
     *
     *
     */
    public int subarraysWithKDistinct_0_2(int[] nums, int k) {
        int n = nums.length;
        int left = 0, count = 0, prefix = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for (int right = 0; right < n; right++) {
            // 1. Add right element
            map.put(nums[right], map.getOrDefault(nums[right], 0) + 1);

            // 2. If we have too many distinct elements,
            // reset prefix and shrink from left until we have exactly k
            if (map.size() > k) {
                int leftVal = nums[left++];
                map.put(leftVal, map.get(leftVal) - 1);
                if (map.get(leftVal) == 0)
                    map.remove(leftVal);
                prefix = 0; // Reset prefix because the window changed distinct count
            }

            // 3. Key Optimization: Shrink left as much as possible
            // while maintaining exactly K distinct elements.
            // These "extra" elements form the 'prefix'.
            while (map.get(nums[left]) > 1) {
                /** NOTE !!!
                 *
                 *  the `prefix` trick. refer the explanation at top of this code.
                 *  (// V0-2)
                 */
                prefix++;
                int leftVal = nums[left++];
                map.put(leftVal, map.get(leftVal) - 1);
            }

            // 4. If we have exactly k, add the (current window + all prefix variations)
            if (map.size() == k) {
                count += (prefix + 1);
            }
        }

        return count;
    }



    // V0-3
    // IDEA: SLIDE WINDOW  + HASHMAP (gemini)
    /**
     *
     * ### 1. Why `count += (right - left + 1)`?
     *
     * When we are at a specific `right` index and our window `[left, right]` is valid (it has  distinct elements), we aren't just finding **one** valid subarray. We are finding **all valid subarrays that END at `right**`.
     *
     * **The Rule:** If a window is valid, then any subarray that starts inside that window and ends at `right` is also valid.
     *
     * **Example:** Suppose `k = 2` and our window is `[1, 2, 1]` (where `left = 0` and `right = 2`).
     * The length of this window is .
     * The subarrays ending at `right` (index 2) are:
     *
     * 1. `[1, 2, 1]` (The whole window)
     * 2. `[2, 1]` (Starting from index 1)
     * 3. `[1]` (Starting from index 2)
     *
     * All **3** of these have  distinct elements.
     * So, by adding the **window length** (`right - left + 1`), we are effectively counting every possible starting point that results in a valid subarray ending at our current position.
     *
     * ---
     *
     * ### 2. Why `atMost(k) - atMost(k - 1)`?
     *
     * This is a logic trick used because sliding windows are "greedy"—they are great at finding ranges (0 to ), but bad at finding "exactly."
     *
     * Think of the "Distinct Count" as a set of buckets:
     *
     * * **`atMost(k)`** counts subarrays with **1, 2, 3, ... up to ** distinct elements.
     * * **`atMost(k - 1)`** counts subarrays with **1, 2, 3, ... up to ** distinct elements.
     *
     * If you subtract the two:
     *
     *
     * **Visual Representation:**
     * Imagine :
     *
     * * `atMost(3)` gives you everything in the blue, green, and red zones.
     * * `atMost(2)` gives you everything in the blue and green zones.
     * * The difference is **only** the red zone (exactly 3).
     *
     * ---
     *
     * ### 3. Why can't we just count "Exactly K" in one go?
     *
     * In your original  code, you fixed `l` and moved `r`. That works because you are manually checking every single pair.
     *
     * In an  sliding window, the `left` pointer only moves forward. If we try to count "Exactly " directly:
     *
     * * As we move `right`, we hit  distinct elements.
     * * But as we move `right` further, we might *still* have  distinct elements (if we hit a duplicate).
     * * The window doesn't "tell" us how many valid starting positions there were behind us without doing a second inner loop (which would make it  again).
     *
     * By using the `atMost` helper, we turn a "Fixed Constraint" (Exactly ) into a "Range Constraint" (Up to ), which sliding windows are mathematically designed to solve in linear time.
     *
     * ---
     *
     * ### Summary for your interview:
     *
     * > "I use the **atMost** helper because a sliding window naturally calculates the number of subarrays within a range. By calculating the number of subarrays with **at most ** distinct elements and subtracting those with **at most **, the remainder is exactly the count of subarrays with **exactly ** elements. For each window, the number of valid subarrays ending at the current `right` pointer is simply the **window size**, because any smaller subarray within that valid window must also be valid."
     *
     * **Would you like to try applying this same `atMost(k) - atMost(k-1)` logic to LC 1248 (Nice Subarrays) to see if you've got the pattern down?**
     *
     */
    public int subarraysWithKDistinct_0_3(int[] nums, int k) {
        return atMost(nums, k) - atMost(nums, k - 1);
    }

    private int atMost(int[] nums, int k) {
        int left = 0, count = 0;

        // map : { val : cnt }
        Map<Integer, Integer> freq = new HashMap<>();

        for (int right = 0; right < nums.length; right++) {
            // 1. Add current number to window
            freq.put(nums[right], freq.getOrDefault(nums[right], 0) + 1);

            // 2. If distinct elements > k, shrink from left
            while (freq.size() > k) {
                freq.put(nums[left], freq.get(nums[left]) - 1);
                if (freq.get(nums[left]) == 0) {
                    freq.remove(nums[left]);
                }
                left++;
            }

            // 3. The number of subarrays ending at 'right' with AT MOST k distinct
            // elements is equal to the current window size (right - left + 1)
            count += (right - left + 1);
        }
        return count;
    }


    // V1-1
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/subarrays-with-k-different-integers/editorial/
    public int subarraysWithKDistinct_1_1(int[] nums, int k) {
        return slidingWindowAtMost(nums, k) - slidingWindowAtMost(nums, k - 1);
    }

    // Helper function to count the number of subarrays with at most k distinct elements.
    private int slidingWindowAtMost(int[] nums, int distinctK) {
        // To store the occurrences of each element.
        Map<Integer, Integer> freqMap = new HashMap<>();
        int left = 0, totalCount = 0;

        // Right pointer of the sliding window iterates through the array.
        for (int right = 0; right < nums.length; right++) {
            freqMap.put(nums[right], freqMap.getOrDefault(nums[right], 0) + 1);

            // If the number of distinct elements in the window exceeds k,
            // we shrink the window from the left until we have at most k distinct elements.
            while (freqMap.size() > distinctK) {
                freqMap.put(nums[left], freqMap.get(nums[left]) - 1);
                if (freqMap.get(nums[left]) == 0) {
                    freqMap.remove(nums[left]);
                }
                left++;
            }

            // Update the total count by adding the length of the current subarray.
            totalCount += (right - left + 1);
        }
        return totalCount;
    }


    // V1-2
    // IDEA: SLIDE WINDOW + ONE PASS
    // https://leetcode.com/problems/subarrays-with-k-different-integers/editorial/
    public int subarraysWithKDistinct_1_2(int[] nums, int k) {
        // Array to store the count of distinct values encountered
        int[] distinctCount = new int[nums.length + 1];

        int totalCount = 0;
        int left = 0;
        int right = 0;
        int currCount = 0;

        while (right < nums.length) {
            // Increment the count of the current element in the window
            if (distinctCount[nums[right++]]++ == 0) {
                // If encountering a new distinct element, decrement K
                k--;
            }

            // If K becomes negative, adjust the window from the left
            if (k < 0) {
                // Move the left pointer until the count of distinct elements becomes valid again
                --distinctCount[nums[left++]];
                k++;
                currCount = 0;
            }

            // If K becomes zero, calculate subarrays
            if (k == 0) {
                // While the count of left remains greater than 1, keep shrinking the window from the left
                while (distinctCount[nums[left]] > 1) {
                    --distinctCount[nums[left++]];
                    currCount++;
                }
                // Add the count of subarrays with K distinct elements to the total count
                totalCount += (currCount + 1);
            }
        }
        return totalCount;
    }



    // V2



}
