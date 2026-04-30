package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/description/
// https://leetcode.ca/all/154.html
/**
 *  154. Find Minimum in Rotated Sorted Array II
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,4,4,5,6,7] might become:
 *
 * [4,5,6,7,0,1,4] if it was rotated 4 times.
 * [0,1,4,4,5,6,7] if it was rotated 7 times.
 * Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
 *
 * Given the sorted rotated array nums that may contain duplicates, return the minimum element of this array.
 *
 * You must decrease the overall operation steps as much as possible.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5]
 * Output: 1
 * Example 2:
 *
 * Input: nums = [2,2,2,0,1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 5000
 * -5000 <= nums[i] <= 5000
 * nums is sorted and rotated between 1 and n times.
 *
 *
 */
public class FindMinimumInRotatedSortedArray2 {

    // V0
//    public int findMin(int[] nums) {
//    }


    // V0-1
    // IDEA: BRUTE FORCE
    public int findMin_0_1(int[] nums) {
        // edge

        int ans = 5001;
        for(int x: nums){
            ans = Math.min(x, ans);
        }

        return ans;
    }


    // V0-5
    // IDEA: BINARY SEARCH (l <= r) (gpt)
    /**  CORE IDEA:
     *
     *
     * Compare with right pointer (nums[r]), not left.  !!!!!
     *
     *
     *
     *
     * There are 3 cases:
     *
     *   1. nums[mid] < nums[r] → min is in left (including mid)
     *   2. nums[mid] > nums[r] → min is in right
     *   3. nums[mid] == nums[r] → shrink search space (r--)
     *
     *
     */
    /**  Q: why we check `nums[r]` but NOT nums[l]
     *      for finding `moving directions` ?
     *
     *
     * -> Ans:
     *
     * Short answer:
     *
     * **because the right boundary gives you a stable
     * reference even with duplicates**,
     * while the left boundary often doesn’t.
     *
     * ---
     *
     * ## The key property we exploit
     *
     * In **Find Minimum in Rotated Sorted Array II**, the array is:
     *
     * * originally sorted
     * * then rotated
     * * may contain duplicates
     *
     * That means **at least one side (left or right of `mid`)
     * is sorted**, but duplicates can blur which one.
     *
     * ---
     *
     * ## Why compare with `nums[r]` (right side)?
     *
     * ### Case 1: `nums[mid] < nums[r]`
     *
     * 👉 The **right side is sorted**, and `mid` is inside that sorted region.
     *
     * Example:
     *
     * ```
     * [4,5,6,7,0,1,2]
     *          ↑   ↑
     *         mid  r
     * ```
     *
     * * `nums[mid] < nums[r]`
     * * So min **cannot be to the right of mid**
     * * → move `r = mid`
     *
     * ---
     *
     * ### Case 2: `nums[mid] > nums[r]`
     *
     * 👉 The **rotation point is to the right**
     *
     * Example:
     *
     * ```
     * [4,5,6,7,0,1,2]
     *      ↑       ↑
     *     mid      r
     * ```
     *
     * * `nums[mid] > nums[r]`
     * * So min is **strictly in right half**
     * * → `l = mid + 1`
     *
     * ---
     *
     * ### Case 3: `nums[mid] == nums[r]`
     *
     * 👉 **Ambiguous due to duplicates**
     *
     * Example:
     *
     * ```
     * [2,2,2,0,1,2]
     *         ↑   ↑
     *        mid  r
     * ```
     *
     * * You **cannot tell** which side contains the min
     * * But you *can safely shrink*:
     *   → `r--`
     *
     * ---
     *
     * ## Why NOT compare with `nums[l]`?
     *
     * Because duplicates break your logic.
     *
     * ### Counterexample:
     *
     * ```
     * [2,2,2,0,1,2]
     *  ↑
     *  l
     * ```
     *
     * If you try:
     *
     * ```java
     * if (nums[mid] >= nums[l])
     * ```
     *
     * You might think:
     *
     * * "left side is sorted" → go right
     *
     * ❌ But that’s wrong:
     *
     * * `nums[mid] == nums[l]` gives **no information**
     * * You might skip the minimum
     *
     * ---
     *
     * ## Intuition (this is the real takeaway)
     *
     * * `nums[r]` helps you answer:
     *   👉 “Is `mid` in the **sorted tail**?”
     *
     * * That’s reliable because:
     *
     *   * the **rightmost value anchors the sorted suffix**
     *   * even with duplicates, you can still **shrink safely**
     *
     * ---
     *
     * ## Mental model
     *
     * Think of it like:
     *
     * > “Is `mid` already in the clean, sorted ending segment?”
     *
     * * YES → go left
     * * NO → go right
     * * NOT SURE → shrink right
     *
     * ---
     *
     * ## Bottom line
     *
     * * Comparing with `r` → **stable, safe with duplicates**
     * * Comparing with `l` → **ambiguous, error-prone**
     *
     *
     */
    public int findMin_0_5(int[] nums) {
        int l = 0, r = nums.length - 1;
        int ans = nums[0];

        while (l <= r) {
            int mid = l + (r - l) / 2;

            ans = Math.min(ans, nums[mid]);

            // if right part is sorted or mid could be min
            if (nums[mid] < nums[r]) {
                r = mid - 1;
            }
            // min is in right part
            else if (nums[mid] > nums[r]) {
                l = mid + 1;
            }
            // duplicates, cannot decide
            else {
                r--;
            }
        }

        return ans;
    }



    // V0-6
    // IDEA: BINARY SEARCH (gpt)
    public int findMin_0_6(int[] nums) {
        int l = 0, r = nums.length - 1;

        while (l < r) {
            int mid = l + (r - l) / 2;

            if (nums[mid] < nums[r]) {
                r = mid; // min in left incl. mid
            } else if (nums[mid] > nums[r]) {
                l = mid + 1; // min in right
            } else {
                r--; // handle duplicates
            }
        }

        return nums[l];
    }


    // V0-7
    // IDEA: BINARY SEARCH (gpt)
    public int findMin_0_7(int[] nums) {
        int l = 0;
        int r = nums.length - 1;

        while (l < r) {
            // Optimization: If the segment is already sorted, nums[l] is the min
            if (nums[l] < nums[r]) {
                return nums[l];
            }

            int mid = l + (r - l) / 2;

            if (nums[mid] > nums[r]) {
                // Case 1: Left part is sorted, pivot is to the right
                // Example: [3, 4, 5, 1, 2] -> mid is 5, r is 2. 5 > 2.
                l = mid + 1;
            } else if (nums[mid] < nums[r]) {
                // Case 2: Right part is sorted, mid could be the min or left of it
                // Example: [5, 1, 2, 3, 4] -> mid is 2, r is 4. 2 < 4.
                r = mid;
            } else {
                // Case 3: nums[mid] == nums[r] (The Duplicate Case)
                // We don't know where the min is, so we shrink the search space
                // Example: [1, 0, 1, 1, 1] vs [1, 1, 1, 0, 1]
                r--;
            }
        }

        return nums[l];
    }




    // V1
    // https://leetcode.ca/2016-05-02-154-Find-Minimum-in-Rotated-Sorted-Array-II/
    /**
     * time = O(log N)
     * space = O(1)
     */

    public int findMin_1(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) >> 1;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else if (nums[mid] < nums[right]) {
                right = mid;
            } else {
                --right;
            }
        }
        return nums[left];
    }


    // V2
    // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/solutions/7157901/easy-code-for-solve-this-problem-by-4_k4-5p13/
    /**
     * time = O(N)
     * space = O(1)
     */

    public int findMin_2(int[] nums) {
        int min = Integer.MAX_VALUE;
        for (int i : nums) {
            if (i < min) {
                min = i;
            }
        }
        return min;

    }


    // V3
    // https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/solutions/7135635/java-solution-beats-100-proof-concise-co-2vpe/
    /**
     * time = O(log N)
     * space = O(1)
     */

    public int findMin_3(int[] nums) {
        int min = Integer.MAX_VALUE;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            while (left < right && nums[left] == nums[left + 1])
                left++;
            while (left < right && nums[right] == nums[right - 1])
                right--;
            int mid = (left + right) / 2;
            if (nums[mid] > nums[right])
                left = mid + 1; // means minimum will be in the next half
            else {
                min = Math.min(min, nums[mid]);
                right = mid - 1;
            }

        }
        return min;
    }





}
