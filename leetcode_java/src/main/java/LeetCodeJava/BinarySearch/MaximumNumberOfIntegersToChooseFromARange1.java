package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *   2554. Maximum Number of Integers to Choose From a Range I
 *
 * You are given an integer array banned and two integers n and maxSum. You are choosing some number of integers following the below rules:
 *
 * The chosen integers have to be in the range [1, n].
 * Each integer can be chosen at most once.
 * The chosen integers should not be in the array banned.
 * The sum of the chosen integers should not exceed maxSum.
 *
 *
 * Return the maximum number of integers you can choose following the mentioned rules.
 *
 *
 * Example 1:
 *
 * Input: banned = [1,6,5], n = 5, maxSum = 6
 * Output: 2
 * Explanation: You can choose the integers 2 and 4.
 * 2 and 4 are from the range [1, 5], both did not appear in banned, and their sum is 6, which did not exceed maxSum.
 * Example 2:
 *
 * Input: banned = [1,2,3,4,5,6,7], n = 8, maxSum = 1
 * Output: 0
 * Explanation: You cannot choose any integer while following the mentioned conditions.
 * Example 3:
 *
 * Input: banned = [11], n = 7, maxSum = 50
 * Output: 7
 * Explanation: You can choose the integers 1, 2, 3, 4, 5, 6, and 7.
 * They are from the range [1, 7], all did not appear in banned, and their sum is 28, which did not exceed maxSum.
 *
 *
 * Constraints:
 *
 * 1 <= banned.length <= 104
 * 1 <= banned[i], n <= 104
 * 1 <= maxSum <= 109
 *
 */
public class MaximumNumberOfIntegersToChooseFromARange1 {

    // V0
//    public int maxCount(int[] banned, int n, int maxSum) {
//
//    }

    // V0-1
    // IDEA: GREEDY (fixed by gpt)
    public int maxCount_0_1(int[] banned, int n, int maxSum) {
        // Edge cases
        if (n <= 0 || maxSum <= 0) {
            return 0;
        }

        Set<Integer> bannedSet = new HashSet<>();
        for (int x : banned) {
            bannedSet.add(x);
        }

        /** NOTE !!!  cnt as number cnt, cumsum as cumulated sum */
        int cnt = 0;
        int cumSum = 0;

        // Loop from 1 to n
        for (int i = 1; i <= n; i++) {
            if (bannedSet.contains(i)) {
                continue;
            }

            /** NOTE !!!
             *
             *  1. cumSum + i > maxSum  condition
             *
             *  2. below is WRONG !!!!
             *     -> we SHOULD check if `cumSum + i ` is <=  maxSum
             *        BEFORE pick the `new i`
             *
             *
             *
             *             if(cumSum > maxSum){
             *                 break;
             *             }
             *
             *             //ans = Math.max(ans, i + 1); // ??
             *             cnt += 1;
             *             cumSum += i;
             *
             *
             */
            // Check if adding this number exceeds maxSum
            if (cumSum + i > maxSum) {
                break;
            }

            cumSum += i;
            cnt++;
        }

        return cnt;
    }

    // V0-2
    // IDEA: GREEDY (gpt)
    public int maxCount_0_2(int[] banned, int n, int maxSum) {
        // Use a set for fast banned lookups
        Set<Integer> bannedSet = new HashSet<>();
        for (int x : banned) {
            bannedSet.add(x);
        }

        int count = 0;
        int sum = 0;

        // Iterate through 1..n
        for (int i = 1; i <= n; i++) {
            if (bannedSet.contains(i)) {
                continue; // skip banned numbers
            }

            if (sum + i <= maxSum) {
                sum += i;
                count++;
            } else {
                break; // once we exceed maxSum, stop
            }
        }

        return count;
    }

    // V1-1
    // IDEA:  BINARY SEARCH
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/editorial/
    public int maxCount_1_1(int[] banned, int n, int maxSum) {
        // Sort banned array to enable binary search
        Arrays.sort(banned);

        int count = 0;

        // Try each number from 1 to n
        for (int num = 1; num <= n; num++) {
            // Skip if number is in banned array
            if (customBinarySearch(banned, num))
                continue;

            maxSum -= num;

            // Break if sum exceeds our limit
            if (maxSum < 0)
                break;

            count++;
        }
        return count;
    }

    // Helper method to check if value exists in sorted array
    private boolean customBinarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target)
                return true;

            if (arr[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }

    // V1-2
    // IDEA: SWEEP
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/editorial/
    public int maxCount_1_2(int[] banned, int n, int maxSum) {
        Arrays.sort(banned);

        int bannedIdx = 0, count = 0;

        // Check each number from 1 to n while sum is valid
        for (int num = 1; num <= n && maxSum >= 0; num++) {
            // Skip if current number is in banned array
            if (bannedIdx < banned.length && banned[bannedIdx] == num) {
                // Handle duplicate banned numbers
                while (bannedIdx < banned.length && banned[bannedIdx] == num) {
                    bannedIdx++;
                }
            } else {
                // Include current number if possible
                maxSum -= num;
                if (maxSum >= 0) {
                    count++;
                }
            }
        }
        return count;
    }


    // V1-3
    // IDEA: HASH SET
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/editorial/
    public int maxCount_1_3(int[] banned, int n, int maxSum) {
        // Store banned numbers in HashSet
        Set<Integer> bannedSet = new HashSet<>();
        for (int num : banned) {
            bannedSet.add(num);
        }

        // Track count of valid numbers we can choose
        int count = 0;

        // Try each number from 1 to n
        for (int num = 1; num <= n; num++) {
            // Skip banned numbers
            if (bannedSet.contains(num))
                continue;

            // Return if adding current number exceeds maxSum
            if (maxSum - num < 0)
                return count;

            // Include current number
            maxSum -= num;
            count++;
        }
        return count;
    }


    // V2
    // IDEA: ARRAY
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/solutions/6117769/beats-100-array-solution-for-leetcode255-fvz6/
    public int maxCount_2(int[] banned, int n, int maxSum) {
        int[] arr = new int[10001];
        for (int i = 0; i < banned.length; i++) {
            arr[banned[i]] = 1;
        }

        long sum = 0;
        int cnt = 0;
        for (int i = 1; i <= n; i++) {
            if (arr[i] == 1) {
                continue;
            }
            sum += i;
            if (sum > maxSum) {
                break;
            }
            cnt++;
        }

        return cnt;
    }


    // V3-1
    // IDEA: HASH MAP
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/solutions/6118408/mastering-maximum-count-of-integers-map-pznsh/
    public int maxCount_3_1(int[] banned, int n, int maxSum) {
        HashMap<Integer, Integer> mp = new HashMap<>();
        for (int i : banned) {
            mp.put(i, 1);
        }
        int sum = 0, res = 0;
        for (int i = 1; i <= n; i++) {
            if (!mp.containsKey(i)) {
                sum += i;
                if (sum <= maxSum) {
                    res++;
                } else {
                    return res;
                }
            }
        }
        return res;
    }

    // V3-2
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/maximum-number-of-integers-to-choose-from-a-range-i/solutions/6118408/mastering-maximum-count-of-integers-map-pznsh/
    public int maxCount_3_2(int[] banned, int n, int maxSum) {
        Arrays.sort(banned);
        int total = 0, count = 0;
        for (int i = 1; i <= n; i++) {
            if (!binarySearch(banned, i)) {
                total += i;
                if (total <= maxSum)
                    count++;
                else
                    break;
            }
        }
        return count;
    }

    private boolean binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target)
                return true;
            if (arr[mid] < target)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return false;
    }



}
