package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-non-decreasing-subarrays-after-k-operations/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 3420. Count Non-Decreasing Subarrays After K Operations
 * Hard
 *
 * You are given an array nums of n integers and an integer k.
 *
 * For each subarray of nums, you can apply up to k operations on it. In each operation, you
 * increment any element of the subarray by 1.
 *
 * Note that each subarray is considered independently, meaning changes made to one subarray do
 * not persist to another.
 *
 * Return the number of subarrays that you can make non-decreasing after performing at most k
 * operations.
 *
 * An array is said to be non-decreasing if each element is greater than or equal to its previous
 * element, if it exists.
 *
 *
 * Example 1:
 *
 * Input: nums = [6,3,1,2,4,4], k = 7
 * Output: 17
 * Explanation:
 * Out of all 21 possible subarrays of nums, only the subarrays [6, 3, 1], [6, 3, 1, 2],
 * [6, 3, 1, 2, 4] and [6, 3, 1, 2, 4, 4] cannot be made non-decreasing after applying up to
 * k = 7 operations. Thus, the number of non-decreasing subarrays is 21 - 4 = 17.
 *
 * Example 2:
 *
 * Input: nums = [6,3,1,3,6], k = 4
 * Output: 12
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= 10^9
 *
 */
public class CountNonDecreasingSubarraysAfterKOperations {

    // V0
    // IDEA: LEFT-TO-RIGHT SLIDING WINDOW OVER THE RUNNING-MAXIMUM STAIRCASE
    //  only increments are allowed, so the cheapest fix of a[l..r] is its running maximum and
    //      cost(l, r) = sum_{i=l..r} ( max(a[l..i]) - a[i] ).
    //  cost is monotone in l, so a two pointer works. The deque holds the window's record
    //  positions (strictly increasing values). Retiring l removes the first record and replays
    //  the smaller records it was hiding, hopping along precomputed "next greater" pointers;
    //  each position is replayed at most once, so the sweep stays linear.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long countNonDecreasingSubarrays(int[] nums, int k) {
        int n = nums.length;

        // nextGreater[i] = first j > i with nums[j] > nums[i], else n
        int[] nextGreater = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                stack.pop();
            }
            nextGreater[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        // record positions of the window, left to right
        Deque<Integer> records = new ArrayDeque<>();
        long sumOfRunningMax = 0; // sum of max(a[left..i]) for i in [left, right]
        long sumOfValues = 0;     // sum of a[left..right]
        int left = 0;
        long count = 0;

        for (int right = 0; right < n; right++) {
            int value = nums[right];
            if (!records.isEmpty() && nums[records.peekLast()] >= value) {
                // the current record's stretch grows by one
                sumOfRunningMax += nums[records.peekLast()];
            } else {
                // nums[right] is a new record
                records.addLast(right);
                sumOfRunningMax += value;
            }
            sumOfValues += value;

            // too expensive: retire `left`, which is always the first record
            while (sumOfRunningMax - sumOfValues > k) {
                int firstRecord = records.pollFirst();
                int stretchEnd = records.isEmpty() ? right + 1 : records.peekFirst();
                sumOfRunningMax -= (long) nums[firstRecord] * (stretchEnd - firstRecord);
                sumOfValues -= nums[firstRecord];
                left++;

                // replay the records firstRecord was masking inside [left, stretchEnd)
                List<Integer> revealed = new ArrayList<>();
                int pos = left;
                while (pos < stretchEnd) {
                    int next = Math.min(nextGreater[pos], stretchEnd);
                    revealed.add(pos);
                    sumOfRunningMax += (long) nums[pos] * (next - pos);
                    pos = next;
                }
                for (int i = revealed.size() - 1; i >= 0; i--) {
                    records.addFirst(revealed.get(i));
                }
            }
            count += right - left + 1;
        }
        return count;
    }

    // V1
    // IDEA: RIGHT-TO-LEFT SLIDING WINDOW + DEQUE OF (VALUE, COUNT) STAIRCASE SEGMENTS
    //  sweep the left end l from n - 1 down to 0. Adding a[l] in front makes it the running max
    //  of every following element up to the next bigger one, so it swallows the leading
    //  segments with value <= a[l]. While the cost exceeds k, drop the rightmost element: it
    //  belongs to the last segment, and its cost was (segment value - a[right]).
    //  This is the easier one to explain in an interview.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long countNonDecreasingSubarrays_1(int[] nums, int k) {
        int n = nums.length;
        // each segment is {running max value, number of elements it covers}; front = leftmost
        Deque<long[]> segments = new ArrayDeque<>();
        long cost = 0;
        int right = n - 1;
        long count = 0;

        for (int left = n - 1; left >= 0; left--) {
            long value = nums[left];
            long covered = 1;
            // a[left] raises every segment it swallows up to its own value
            while (!segments.isEmpty() && segments.peekFirst()[0] <= value) {
                long[] segment = segments.pollFirst();
                cost += (value - segment[0]) * segment[1];
                covered += segment[1];
            }
            segments.addFirst(new long[] {value, covered});

            // shrink from the right until the window is affordable
            while (cost > k) {
                long[] last = segments.peekLast();
                cost -= last[0] - nums[right];
                last[1]--;
                if (last[1] == 0) {
                    segments.pollLast();
                }
                right--;
            }
            count += right - left + 1;
        }
        return count;
    }
}
