package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/circular-array-loop/

/**
 *  457. Circular Array Loop
 *  Medium
 *
 *  You are playing a game involving a circular array of non-zero integers nums.
 *  Each nums[i] denotes the number of indices forward/backward you must move if
 *  you are located at index i:
 *    - If nums[i] is positive, move nums[i] steps forward, and
 *    - If nums[i] is negative, move nums[i] steps backward.
 *
 *  Since the array is circular, you may assume that moving forward from the last
 *  element puts you on the first element, and moving backwards from the first
 *  element puts you on the last element.
 *
 *  A cycle in the array consists of a sequence of indices seq of length k where:
 *    - Following the movement rules above results in the repeating index sequence
 *      seq[0] -> seq[1] -> ... -> seq[k - 1] -> seq[0] -> ...
 *    - Every nums[seq[j]] is either all positive or all negative.
 *    - k > 1
 *
 *  Return true if there is a cycle in nums, or false otherwise.
 *
 *  Example 1:
 *    Input: nums = [2,-1,1,2,2]
 *    Output: true
 *
 *  Example 2:
 *    Input: nums = [-1,-2,-3,-4,-5,6]
 *    Output: false
 *
 *  Example 3:
 *    Input: nums = [1,-1,5,1,4]
 *    Output: true
 *
 *  Constraints:
 *    1 <= nums.length <= 5000
 *    -1000 <= nums[i] <= 1000
 *    nums[i] != 0
 *
 *  Follow up: Could you solve it in O(n) time complexity and O(1) extra space?
 */
public class CircularArrayLoop {

    // V0
    // IDEA: FAST & SLOW POINTERS (Floyd) per start index
    //       - a "next" step is invalid if it changes direction, or if it lands on
    //         the same index (k == 1 self loop)
    //       - after a failed start, mark the whole visited path with 0 so we never
    //         re-scan it  -> overall O(N)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean circularArrayLoop(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                continue; // already known to be a dead path
            }

            int slow = i;
            int fast = i;
            while (true) {
                slow = next(nums, slow);
                if (slow == -1) {
                    break;
                }
                fast = next(nums, fast);
                if (fast == -1) {
                    break;
                }
                fast = next(nums, fast);
                if (fast == -1) {
                    break;
                }
                if (slow == fast) {
                    return true;
                }
            }

            // mark the path starting at i as dead (same direction run)
            int j = i;
            int dir = nums[i];
            while (nums[j] != 0 && nums[j] * dir > 0) {
                int nxt = mod(j + nums[j], n);
                nums[j] = 0;
                j = nxt;
            }
        }
        return false;
    }

    /** next index, or -1 if the move breaks the "same direction, k > 1" rule */
    private int next(int[] nums, int i) {
        int n = nums.length;
        int nxt = mod(i + nums[i], n);

        if (nxt == i) {
            return -1;               // self loop -> k == 1, not allowed
        }
        if (nums[nxt] == 0) {
            return -1;               // already proven dead
        }
        if (nums[nxt] * nums[i] < 0) {
            return -1;               // direction flips
        }
        return nxt;
    }

    private int mod(int x, int n) {
        return ((x % n) + n) % n;
    }
}
