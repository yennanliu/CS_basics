package LeetCodeJava.HashTable;

// https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/

/**
 *  1010. Pairs of Songs With Total Durations Divisible by 60
 *  Medium
 *
 *  You are given a list of songs where the ith song has a duration of time[i]
 *  seconds.
 *
 *  Return the number of pairs of songs for which their total duration in seconds
 *  is divisible by 60. Formally, we want the number of indices i, j such that
 *  i < j with (time[i] + time[j]) % 60 == 0.
 *
 *  Example 1:
 *  Input: time = [30,20,150,100,40]
 *  Output: 3
 *  Explanation: (30,150), (20,100), (20,40)
 *
 *  Example 2:
 *  Input: time = [60,60,60]
 *  Output: 3
 *
 *  Constraints:
 *  1 <= time.length <= 6 * 10^4
 *  1 <= time[i] <= 500
 */
public class PairsOfSongsWithTotalDurationsDivisibleBy60 {

    // V0
    // IDEA: REMAINDER COUNTING - for each t, the complement remainder is
    //       (60 - t % 60) % 60; count how many earlier songs already have it.
    /**
     * time = O(n)
     * space = O(1)  (fixed 60 buckets)
     */
    public int numPairsDivisibleBy60(int[] time) {

        // edge
        if (time == null || time.length < 2) {
            return 0;
        }

        int[] cnt = new int[60];
        int res = 0;

        for (int t : time) {
            int r = t % 60;
            int need = (60 - r) % 60;
            res += cnt[need];
            cnt[r]++;
        }

        return res;
    }
}
