package LeetCodeJava.Greedy;

// https://leetcode.com/problems/video-stitching/

/**
 *  1024. Video Stitching
 *  Medium
 *
 *  You are given a series of video clips from a sporting event that lasted `time` seconds.
 *  These video clips can be overlapping and have varying lengths.
 *
 *  Each video clip is described by an array clips where clips[i] = [starti, endi] means
 *  the ith clip started at starti and ended at endi.
 *
 *  We can cut these clips into segments freely. Return the minimum number of clips needed
 *  so that we can cut the clips into segments that cover the entire sporting event
 *  [0, time]. If the task is impossible, return -1.
 *
 *  Example 1:
 *    Input: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
 *    Output: 3    ([0,2] + [1,9] + [8,10])
 *
 *  Example 2:
 *    Input: clips = [[0,1],[1,2]], time = 5
 *    Output: -1
 *
 *  Constraints:
 *    1 <= clips.length <= 100
 *    0 <= starti <= endi <= 100
 *    1 <= time <= 100
 */
public class VideoStitching {

    // V0
    // IDEA: jump-game greedy. maxReach[s] = furthest end among clips starting at s;
    //       sweep left to right keeping the furthest reachable point, and take a new
    //       clip each time we hit the end of the current one.
    /**
     * time = O(n + time)
     * space = O(time)
     */
    public int videoStitching(int[][] clips, int time) {
        int[] maxReach = new int[time];
        for (int[] c : clips) {
            if (c[0] < time) {
                maxReach[c[0]] = Math.max(maxReach[c[0]], c[1]);
            }
        }

        int res = 0;
        int curEnd = 0;
        int farthest = 0;

        for (int i = 0; i < time; i++) {
            farthest = Math.max(farthest, maxReach[i]);
            if (farthest <= i) {
                return -1; // stuck, cannot pass second i
            }
            if (i == curEnd) {
                res++;
                curEnd = farthest;
            }
        }
        return res;
    }

    // V1
    // IDEA: DP. dp[t] = min clips to cover [0, t].
    /**
     * time = O(n * time)
     * space = O(time)
     */
    public int videoStitching_1(int[][] clips, int time) {
        final int INF = Integer.MAX_VALUE / 2;
        int[] dp = new int[time + 1];
        for (int i = 1; i <= time; i++) {
            dp[i] = INF;
        }

        for (int t = 1; t <= time; t++) {
            for (int[] c : clips) {
                if (c[0] < t && t <= c[1]) {
                    dp[t] = Math.min(dp[t], dp[c[0]] + 1);
                }
            }
        }
        return dp[time] >= INF ? -1 : dp[time];
    }
}
