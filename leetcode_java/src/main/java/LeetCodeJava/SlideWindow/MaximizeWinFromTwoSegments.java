package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximize-win-from-two-segments/description/
/**
 *  2555. Maximize Win From Two Segments
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There are some prizes on the X-axis. You are given an integer array prizePositions that is sorted in non-decreasing order, where prizePositions[i] is the position of the ith prize. There could be different prizes at the same position on the line. You are also given an integer k.
 *
 * You are allowed to select two segments with integer endpoints. The length of each segment must be k. You will collect all prizes whose position falls within at least one of the two selected segments (including the endpoints of the segments). The two selected segments may intersect.
 *
 * For example if k = 2, you can choose segments [1, 3] and [2, 4], and you will win any prize i that satisfies 1 <= prizePositions[i] <= 3 or 2 <= prizePositions[i] <= 4.
 * Return the maximum number of prizes you can win if you choose the two segments optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: prizePositions = [1,1,2,2,3,3,5], k = 2
 * Output: 7
 * Explanation: In this example, you can win all 7 prizes by selecting two segments [1, 3] and [3, 5].
 * Example 2:
 *
 * Input: prizePositions = [1,2,3,4], k = 0
 * Output: 2
 * Explanation: For this example, one choice for the segments is [3, 3] and [4, 4], and you will be able to get 2 prizes.
 *
 *
 * Constraints:
 *
 * 1 <= prizePositions.length <= 105
 * 1 <= prizePositions[i] <= 109
 * 0 <= k <= 109
 * prizePositions is sorted in non-decreasing order.
 *
 */
public class MaximizeWinFromTwoSegments {

    // V0
//    public int maximizeWin(int[] prizePositions, int k) {
//
//    }

    // V0-1
    // IDEA: DP + SLIDE WINDOW (fixed by gpt)
    public int maximizeWin_0_1(int[] prizePositions, int k) {
        int n = prizePositions.length;
        if (n == 0)
            return 0;

        // best[i] = max number of prizes we can collect using ONE segment up to index i
        int[] best = new int[n];
        int ans = 0;

        // sliding window [left..right]
        int left = 0;
        for (int right = 0; right < n; right++) {
            // move left until segment length <= k
            while (prizePositions[right] - prizePositions[left] > k) {
                left++;
            }
            int windowSize = right - left + 1;

            // best so far up to index right
            best[right] = (right == 0) ? windowSize : Math.max(best[right - 1], windowSize);
        }

        // Second pass: choose second segment starting at i, combine with best from before i
        left = 0;
        for (int right = 0; right < n; right++) {
            while (prizePositions[right] - prizePositions[left] > k) {
                left++;
            }
            int windowSize = right - left + 1;

            int prevBest = (left > 0) ? best[left - 1] : 0;
            ans = Math.max(ans, prevBest + windowSize);
        }

        return ans;
    }

    // V0-5
    // IDEA:  SLIDE WINDOW + PREFIX SUM
    // https://leetcode.com/problems/maximize-win-from-two-segments/solutions/7026737/on-sliding-window-prefix-sum-by-wbcnskdh-f2zv/
    public int maximizeWin_0_5(int[] prizePositions, int k) {
        int n = prizePositions.length;

        // segments[i] is the right side index of the interval if the interval starts with index i
        // so segments[i] is non-decreasing
        // [i, segments[i]] is like [0, 3], [1, 3], [2, 5], [3, 6], [4, 9], [5, 9], ...
        int[] segments = new int[n];
        int r = 0;
        for (int i = 0; i < n; i++) {
            while (r < n && prizePositions[r] - prizePositions[i] <= k) {
                r++;
            }
            segments[i] = r - 1;
        }

        int[] suffixMax = new int[n + 1];
        suffixMax[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            suffixMax[i] = Math.max(suffixMax[i + 1], segments[i] - i + 1);
        }

        // if interval starting with index i is selected, select the max one from range [segments[i] + 1, n), pre computed in suffixMax
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, segments[i] - i + 1 + suffixMax[segments[i] + 1]);
        }
        return ans;
    }

    // V1
    // IDEA: DP
    // https://leetcode.com/problems/maximize-win-from-two-segments/solutions/3141449/javacpython-dp-sliding-segment-on-by-lee-bum4/
    public int maximizeWin_1(int[] A, int k) {
        int res = 0, n = A.length, j = 0, dp[] = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            while (A[j] < A[i] - k)
                ++j;
            dp[i + 1] = Math.max(dp[i], i - j + 1);
            res = Math.max(res, i - j + 1 + dp[j]);
        }
        return res;
    }

    // V2
    // IDEA: DP: leftMax + rightMax
    // https://leetcode.com/problems/maximize-win-from-two-segments/solutions/3141758/dp-leftmax-rightmax-by-hardcracker-0ov1/
    public int maximizeWin_2(int[] prizePositions, int k) {
        int n = prizePositions.length;
        int[] leftMax = new int[n], rightMax = new int[n];
        // leftMax[i]: max on the left of i (including i)
        // rightMax[i]: max on the right of i (including i)

        int j = 0;
        leftMax[0] = 1;
        for(int i = 1; i < n; i++) {
            while (prizePositions[i] - prizePositions[j] > k) j++;
            leftMax[i] = Math.max(leftMax[i - 1], i - j + 1);
            // leftMax[i]: the max between leftMax[i-1], and
            // the number in the window of size k from i to the left
        }

        j = n - 1;
        rightMax[n - 1] = 1;
        for(int i = n - 2; i >= 0; i--) {
            while (prizePositions[j] - prizePositions[i] > k) j--;
            rightMax[i] = Math.max(rightMax[i + 1], j - i + 1);
            // rightMax[i]: the max between rightMax[i+1], and
            // the number in the window of size k from i to the right
        }

        int result = 0;

        for(int i = 0; i <= n; i++) {
            // XXXXXXXXXX i-1 i XXXXXXXXXXX for each i to get leftMax[i - 1] + rightMax[i]
            // edge cases: when i = 0 no leftMax, when i = n - 1 no rightMax
            result = Math.max(result, (i == 0 ? 0 : leftMax[i - 1]) + (i == n ? 0 : rightMax[i]));
        }

        return result;
    }


    // V3
    // IDEA: 2 POINTERS
    // https://leetcode.com/problems/maximize-win-from-two-segments/solutions/4649586/java-two-pointer-solution-simple-easy-so-4snp/
    public int maximizeWin_3(int[] prizePositions, int k) {

        int n = prizePositions.length;
        int leftMax[] = new int[n];
        int rightMax[] = new int[n];

        leftMax[0] = 1;
        for (int i = 1, j = 0; i < n; i++) {
            while (prizePositions[i] - prizePositions[j] > k) {
                j++;
            }
            leftMax[i] = Math.max(leftMax[i - 1], i - j + 1);
        }

        rightMax[n - 1] = 1;
        for (int i = n - 2, j = n - 1; i >= 0; i--) {
            while (prizePositions[j] - prizePositions[i] > k) {
                j--;
            }
            rightMax[i] = Math.max(rightMax[i + 1], j - i + 1);
        }

        int res = 0;
        for (int i = 0; i <= n; i++) {
            res = Math.max(res, (i == 0 ? 0 : leftMax[i - 1]) + (i == n ? 0 : rightMax[i]));
        }
        return res;
    }

    // V4
    // IDEA: DP + SLIDE WINDOW
    // https://leetcode.com/problems/maximize-win-from-two-segments/solutions/3142203/java-dp-sliding-window-solution-by-opraj-begp/
    public int maximizeWin_4(int[] prizePositions, int k) {
        int n = prizePositions.length;
        //dp to store the maximum number of prizes that can be won at each position
        int dp[][] = new int[3][n + 1];

        for (int i = 1; i < 3; i++) {
            int start = 1;
            for (int j = 1; j <= n; j++) {
                //if segment becomes greater than k, increase start to make it valid again.
                while (prizePositions[j - 1] - prizePositions[start - 1] > k) {
                    start++;
                }
                //update the dp by taking the maximum between the maximum number of prizes at the previous position and the maximum number of prizes in the current valid segment plus the length of the valid segment.
                dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][start - 1] + j - start + 1);
            }
        }
        return dp[2][n];
    }



}
