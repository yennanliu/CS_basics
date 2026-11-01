package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/earliest-second-to-mark-indices-ii/description/

import java.util.PriorityQueue;

/**
 * 3049. Earliest Second to Mark Indices II
 * Hard
 *
 * You are given two 1-indexed integer arrays, nums and, changeIndices, having lengths n and m, respectively.
 *
 * Initially, all indices in nums are unmarked. Your task is to mark all indices in nums.
 *
 * In each second, s, in order from 1 to m (inclusive), you can perform one of the following operations:
 *
 * Choose an index i in the range [1, n] and decrement nums[i] by 1.
 * Set nums[changeIndices[s]] to any non-negative value.
 * Choose an index i in the range [1, n], where nums[i] is equal to 0, and mark index i.
 * Do nothing.
 *
 * Return an integer denoting the earliest second in the range [1, m] when all indices in nums can be marked by choosing operations optimally, or -1 if it is impossible.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,3], changeIndices = [1,3,2,2,2,2,3]
 * Output: 6
 * Explanation: In this example, we have 7 seconds. The following operations can be performed to mark all indices:
 * Second 1: Set nums[changeIndices[1]] to 0. nums becomes [0,2,3].
 * Second 2: Mark index 1. It can be marked since nums[1] is equal to 0.
 * Second 3: Set nums[changeIndices[3]] to 0. nums becomes [0,0,3].
 * Second 4: Mark index 2. It can be marked since nums[2] is equal to 0.
 * Second 5: Decrement nums[3] by one. nums becomes [0,0,2].
 * Second 6: Set nums[changeIndices[6]] to 0. nums becomes [0,0,0].
 * Now all indices have been marked.
 * It can be shown that it is not possible to mark all indices earlier than the 6th second.
 * Hence, the answer is 6.
 *
 * Example 2:
 *
 * Input: nums = [0,0,1,2], changeIndices = [1,2,1,2,1,2,1,2]
 * Output: 7
 * Explanation: In this example, we have 8 seconds. The following operations can be performed to mark all indices:
 * Second 1: Mark index 1. It can be marked since nums[1] is equal to 0.
 * Second 2: Mark index 2. It can be marked since nums[2] is equal to 0.
 * Second 3: Decrement index 4 by one. nums becomes [0,0,1,1].
 * Second 4: Decrement index 4 by one. nums becomes [0,0,1,0].
 * Second 5: Decrement index 3 by one. nums becomes [0,0,0,0].
 * Second 6: Mark index 3. It can be marked since nums[3] is equal to 0.
 * Second 7: Mark index 4. It can be marked since nums[4] is equal to 0.
 * Now all indices have been marked.
 * It can be shown that it is not possible to mark all indices earlier than the 7th second.
 * Hence, the answer is 7.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3], changeIndices = [1,2,3]
 * Output: -1
 * Explanation: In this example, it is impossible to mark all indices because we don't have enough seconds.
 * Hence, the answer is -1.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 5000
 * 0 <= nums[i] <= 10^9
 * 1 <= m == changeIndices.length <= 5000
 * 1 <= changeIndices[i] <= n
 *
 */
public class EarliestSecondToMarkIndices2 {

    // V0
    // IDEA: BINARY SEARCH THE DEADLINE + MIN-HEAP GREEDY FOR WHICH INDICES TO "SET TO 0"
    // every index is handled one of two ways:
    //   plain    : nums[i] decrement seconds + 1 mark second
    //   shortcut : 1 set-to-zero second (at a second holding that index) + 1 mark second
    // with no shortcut at all we need sum(nums) + n seconds; each shortcut saves nums[i] - 1.
    // A shortcut uses the index's EARLIEST occurrence (distinct per index, most room after).
    // Sorting chosen seconds t_1 < ... < t_K, marks fit iff  s - t_j >= 2 * (K - j) + 1.
    // Scanning candidates from the latest second backwards, K - j = how many are already
    // chosen, so the rule is a running test; a min-heap lets a bigger saving evict a smaller one.
    /**
     * time = O(M log M log M)
     * space = O(N + M)
     */
    public int earliestSecondToMarkIndices(int[] nums, int[] changeIndices) {
        int m = changeIndices.length;
        int left = 1;
        int right = m;
        int answer = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canMarkAll(nums, changeIndices, mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }

    /** Whether every index can be marked within the first `seconds` seconds. */
    private boolean canMarkAll(int[] nums, int[] changeIndices, int seconds) {
        int n = nums.length;
        // seconds needed with no shortcut at all
        long baseCost = n;
        for (int num : nums) {
            baseCost += num;
        }
        if (baseCost <= seconds) {
            return true;
        }

        // firstSecond[i] = earliest 1-based second holding index i (0 if none within the deadline)
        int[] firstSecond = new int[n];
        for (int t = 1; t <= seconds; t++) {
            int index = changeIndices[t - 1] - 1;
            if (firstSecond[index] == 0) {
                firstSecond[index] = t;
            }
        }
        // ownerOf[t] = the index whose earliest occurrence is second t, or -1
        int[] ownerOf = new int[seconds + 1];
        for (int t = 0; t <= seconds; t++) {
            ownerOf[t] = -1;
        }
        for (int i = 0; i < n; i++) {
            if (firstSecond[i] != 0) {
                ownerOf[firstSecond[i]] = i;
            }
        }

        // the values of the indices we shortcut; smallest saving on top so it can be evicted
        PriorityQueue<Integer> chosen = new PriorityQueue<>();
        long chosenSum = 0;
        for (int t = seconds; t >= 1; t--) {
            if (ownerOf[t] == -1) {
                continue;
            }
            int value = nums[ownerOf[t]];
            // a shortcut on a value <= 1 saves nothing
            if (value <= 1) {
                continue;
            }
            int alreadyChosen = chosen.size();
            if (seconds - t >= 2 * alreadyChosen + 1) {
                // room for this set-second plus every later shortcut's set and mark
                chosen.offer(value);
                chosenSum += value;
            } else if (!chosen.isEmpty() && chosen.peek() < value
                    && seconds - t >= 2 * (alreadyChosen - 1) + 1) {
                // swap out the thinnest saving for this fatter one
                chosenSum += value - chosen.poll();
                chosen.offer(value);
            }
        }

        long saving = chosenSum - chosen.size();
        return baseCost - saving <= seconds;
    }
}
