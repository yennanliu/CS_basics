package LeetCodeJava.Array;

// https://leetcode.com/problems/largest-time-for-given-digits/

/**
 *  949. Largest Time for Given Digits
 *  Medium
 *
 *  Given an array arr of 4 digits, find the latest 24-hour time that can be made
 *  using each digit exactly once.
 *
 *  24-hour times are formatted as "HH:MM", where HH is between 00 and 23, and MM
 *  is between 00 and 59. The earliest 24-hour time is 00:00, and the latest is 23:59.
 *
 *  Return the latest 24-hour time in "HH:MM" format. If no valid time can be made,
 *  return an empty string.
 *
 *
 *  Example 1:
 *
 *  Input: arr = [1,2,3,4]
 *  Output: "23:41"
 *  Explanation: The valid 24-hour times are "12:34", "12:43", "13:24", "13:42",
 *  "14:23", "14:32", "21:34", "21:43", "23:14", and "23:41". Of these times,
 *  "23:41" is the latest.
 *
 *  Example 2:
 *
 *  Input: arr = [5,5,5,5]
 *  Output: ""
 *  Explanation: There are no valid 24-hour times as "55:55" is not valid.
 *
 *
 *  Constraints:
 *
 *  arr.length == 4
 *  0 <= arr[i] <= 9
 */
public class LargestTimeForGivenDigits {

    // V0
    // IDEA: BRUTE FORCE over all 4*3*2 orderings (4 digits only), keep the max valid time
    /**
     * time = O(1) (at most 24 permutations)
     * space = O(1)
     */
    public String largestTimeFromDigits(int[] arr) {
        int best = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == i) {
                    continue;
                }
                for (int k = 0; k < 4; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    // the remaining index
                    int l = 6 - i - j - k;
                    int hour = arr[i] * 10 + arr[j];
                    int min = arr[k] * 10 + arr[l];
                    if (hour < 24 && min < 60) {
                        best = Math.max(best, hour * 60 + min);
                    }
                }
            }
        }
        if (best < 0) {
            return "";
        }
        return String.format("%02d:%02d", best / 60, best % 60);
    }

    // V1
    // IDEA: enumerate every legal clock time from 23:59 downwards, compare digit multiset
    /**
     * time = O(1) (1440 candidates)
     * space = O(1)
     */
    public String largestTimeFromDigits_1(int[] arr) {
        int[] want = new int[10];
        for (int x : arr) {
            want[x]++;
        }
        for (int t = 24 * 60 - 1; t >= 0; t--) {
            int h = t / 60;
            int m = t % 60;
            int[] have = new int[10];
            have[h / 10]++;
            have[h % 10]++;
            have[m / 10]++;
            have[m % 10]++;
            boolean same = true;
            for (int d = 0; d < 10; d++) {
                if (have[d] != want[d]) {
                    same = false;
                    break;
                }
            }
            if (same) {
                return String.format("%02d:%02d", h, m);
            }
        }
        return "";
    }
}
