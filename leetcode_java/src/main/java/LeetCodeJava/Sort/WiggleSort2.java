package LeetCodeJava.Sort;

// https://leetcode.com/problems/wiggle-sort-ii/description/

import java.util.Arrays;

/**
 *
 *   324. Wiggle Sort II
 * Attempted
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an integer array nums, reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....
 *
 * You may assume the input array always has a valid answer.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,1,1,6,4]
 * Output: [1,6,1,5,1,4]
 * Explanation: [1,4,1,5,1,6] is also accepted.
 * Example 2:
 *
 * Input: nums = [1,3,2,2,3,1]
 * Output: [2,3,1,3,1,2]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 104
 * 0 <= nums[i] <= 5000
 * It is guaranteed that there will be an answer for the given input nums.
 *
 *
 * Follow Up: Can you do it in O(n) time and/or in-place with O(1) extra space?
 *
 */
public class WiggleSort2 {

    // V0
//    public void wiggleSort(int[] nums) {
//
//    }

    // V0-1
    // IDEA: sort in place (gpt)
    /**
     * 	1.	Sort the array.
     * 	2.	Split it into two halves — smaller and larger.
     * 	3.	Fill from both halves alternately in reverse order to ensure the wiggle pattern.
     *
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void wiggleSort_0_1(int[] nums) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int mid = (n + 1) / 2;
        int j = mid - 1;    // smaller half pointer
        int k = n - 1;      // larger half pointer

        // fill nums alternately: small, big, small, big...
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                nums[i] = sorted[j--]; // take from smaller half
            } else {
                nums[i] = sorted[k--]; // take from larger half
            }
        }
    }


    // V0-2
    // IDEA: Optimized sort in place (gpt)
    /**
     * 	•	Use Quickselect (nth_element) to find the median in O(n).
     * 	•	Apply Virtual Indexing to rearrange elements:
     * 	•	Left part < median
     * 	•	Middle part = median
     * 	•	Right part > median
     * 	•	Rearrange in-place so that the “greater half” goes to odd indices and the “smaller half” to even indices.
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void wiggleSort_0_2(int[] nums) {
        int n = nums.length;
        int median = findKthLargest(nums, (n + 1) / 2);

        int left = 0, i = 0, right = n - 1;

        // Virtual index mapping
        // newIndex(i) = (1 + 2*i) % (n | 1)
        while (i <= right) {
            int mapped = newIndex(i, n);
            if (nums[mapped] > median) {
                swap(nums, newIndex(left++, n), mapped);
                i++;
            } else if (nums[mapped] < median) {
                swap(nums, newIndex(right--, n), mapped);
            } else {
                i++;
            }
        }
    }

    private int newIndex(int i, int n) {
        return (1 + 2 * i) % (n | 1);
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // Quickselect: find kth largest
    private int findKthLargest(int[] nums, int k) {
        int left = 0, right = nums.length - 1;
        while (true) {
            int pos = partition(nums, left, right);
            if (pos + 1 == k) {
                return nums[pos];
            } else if (pos + 1 < k) {
                left = pos + 1;
            } else {
                right = pos - 1;
            }
        }
    }

    private int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        for (int j = left; j < right; j++) {
            if (nums[j] > pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, right);
        return i;
    }

    // V0-3
    // IDEA: SORT (gemini)
    // Helper function for the Wiggle Index Mapping
    private int newIndex_0_3(int i, int n) {
        // (1 + 2 * i) % (N | 1)
        // N | 1 ensures the divisor is always odd, correctly handling wrap-around for both even/odd N.
        return (1 + 2 * i) % (n | 1);
    }

    // Helper function to swap elements
    private void swap_0_3(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    // Quickselect (Median finding) is complex. For simplicity, we use the sorted approach
    // to find the median, which makes the overall complexity O(N log N).
    // To achieve O(N), one would replace this step with a full Quickselect implementation.
    private int findMedian(int[] nums) {
        // Using Arrays.sort() here makes the median step O(N log N)
        // For true O(N), use Quickselect.
        int n = nums.length;
        Arrays.sort(nums);
        return nums[n / 2];
    }

    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void wiggleSort_0_3(int[] nums) {
        int n = nums.length;

        // Step 1: Find the median (O(N log N) or O(N) using Quickselect)
        int median = findMedian(nums.clone()); // Clone to get median without modifying the input array yet

        // Step 2 & 3: Three-Way Partitioning and Wiggle Mapping (O(N))
        // We use virtual indexing based on the newIndex function.
        int i = 0; // Pointer for numbers LESS than median (target: even indices)
        int j = 0; // Current element pointer
        int k = n - 1; // Pointer for numbers GREATER than median (target: odd indices)

        while (j <= k) {
            int currentNum = nums[newIndex_0_3(j, n)];

            if (currentNum > median) {
                // Number > median should go to the front (virtually odd indices).
                // Swap it with the element at position 'i' and advance 'i'.
                swap_0_3(nums, newIndex_0_3(i++, n), newIndex_0_3(j++, n));
            } else if (currentNum < median) {
                // Number < median should go to the back (virtually even indices).
                // Swap it with the element at position 'k' and decrement 'k'.
                swap_0_3(nums, newIndex_0_3(j, n), newIndex_0_3(k--, n));
            } else {
                // Number == median, keep it in the middle segment.
                j++;
            }
        }

        // After this loop, the array is partitioned in the mapped indices:
        // [ < median ... < median | == median | > median ... > median ]
        // The mapping ensures that:
        // Indices 0, 2, 4, ... are filled with < median or == median
        // Indices 1, 3, 5, ... are filled with > median or == median
        // This placement satisfies the a < b > c ... wiggle property.
    }


    // V1
    // https://leetcode.ca/2016-10-19-324-Wiggle-Sort-II/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void wiggleSort_1(int[] nums) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int n = nums.length;
        int i = (n - 1) >> 1, j = n - 1;
        for (int k = 0; k < n; ++k) {
            if (k % 2 == 0) {
                nums[k] = arr[i--];
            } else {
                nums[k] = arr[j--];
            }
        }
    }

    // V2
    // IDEA: BRUTE FORCE
    // https://leetcode.com/problems/wiggle-sort-ii/solutions/6956714/simple-and-beginner-friendly-solution-be-np09/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void wiggleSort_2(int[] nums) {
        int n = nums.length-1;
        int[] arr = Arrays.copyOf(nums,nums.length);
        Arrays.sort(arr);
        for(int i=1;i<nums.length;i+=2){
            nums[i]=arr[n--];
        }
        for(int i=0;i<nums.length;i+=2){
            nums[i]=arr[n--];
        }
    }


    // V3


}
