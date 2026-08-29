package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-longest-increasing-subsequence/

/**
 *  673. Number of Longest Increasing Subsequence
 *  Medium
 *
 *  Given an integer array nums, return the number of longest increasing subsequences.
 *
 *  Notice that the sequence has to be strictly increasing.
 *
 *  Example 1:
 *    Input: nums = [1,3,5,4,7]
 *    Output: 2
 *    Explanation: The two longest increasing subsequences are [1,3,4,7] and [1,3,5,7].
 *
 *  Example 2:
 *    Input: nums = [2,2,2,2,2]
 *    Output: 5
 *    Explanation: The length of the longest increasing subsequence is 1,
 *                 and there are 5 increasing subsequences of length 1,
 *                 so output 5.
 *
 *  Constraints:
 *    - 1 <= nums.length <= 2000
 *    - -10^6 <= nums[i] <= 10^6
 *    - The answer is guaranteed to fit inside a 32-bit integer.
 */
public class NumberOfLongestIncreasingSubsequence {

    // V0
    // IDEA: PAIRED DP - len[i] = LIS length ending at i, cnt[i] = how many such LIS
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findNumberOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] len = new int[n];   // longest increasing subsequence ending at i
        int[] cnt = new int[n];   // number of such subsequences

        int best = 0;
        for (int i = 0; i < n; i++) {
            len[i] = 1;
            cnt[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (len[j] + 1 > len[i]) {
                        len[i] = len[j] + 1;
                        cnt[i] = cnt[j];
                    } else if (len[j] + 1 == len[i]) {
                        cnt[i] += cnt[j];
                    }
                }
            }
            best = Math.max(best, len[i]);
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            if (len[i] == best) {
                res += cnt[i];
            }
        }
        return res;
    }

    // V1
    // IDEA: FENWICK (BIT) over compressed values, every node keeping (bestLen, ways).
    //       A prefix query gives the best LIS among strictly smaller values in O(log N).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int findNumberOfLIS_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;

        // coordinate compression
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int sz = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[sz++] = sorted[i];
            }
        }

        int[] treeLen = new int[sz + 1];
        int[] treeCnt = new int[sz + 1];

        int bestLen = 0;
        int bestCnt = 0;
        for (int x : nums) {
            int pos = lowerBound_1(sorted, sz, x) + 1; // 1-indexed rank
            int[] q = query_1(treeLen, treeCnt, pos - 1); // strictly smaller values only
            int curLen = q[0] + 1;
            int curCnt = (q[0] == 0) ? 1 : q[1];
            update_1(treeLen, treeCnt, sz, pos, curLen, curCnt);

            if (curLen > bestLen) {
                bestLen = curLen;
                bestCnt = curCnt;
            } else if (curLen == bestLen) {
                bestCnt += curCnt;
            }
        }
        return bestCnt;
    }

    private int lowerBound_1(int[] arr, int sz, int target) {
        int lo = 0;
        int hi = sz;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    private void update_1(int[] treeLen, int[] treeCnt, int sz, int pos, int len, int cnt) {
        for (int i = pos; i <= sz; i += i & (-i)) {
            if (treeLen[i] < len) {
                treeLen[i] = len;
                treeCnt[i] = cnt;
            } else if (treeLen[i] == len) {
                treeCnt[i] += cnt;
            }
        }
    }

    private int[] query_1(int[] treeLen, int[] treeCnt, int pos) {
        int len = 0;
        int cnt = 0;
        for (int i = pos; i > 0; i -= i & (-i)) {
            if (treeLen[i] > len) {
                len = treeLen[i];
                cnt = treeCnt[i];
            } else if (treeLen[i] == len && len > 0) {
                cnt += treeCnt[i];
            }
        }
        return new int[] { len, cnt };
    }

    // V2
    // IDEA: TOP-DOWN MEMOIZATION - solve(i) returns the LIS length ending at i and
    //       caches, alongside it, how many such subsequences exist
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findNumberOfLIS_2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] memoLen = new int[n];
        int[] memoCnt = new int[n];

        int best = 0;
        for (int i = 0; i < n; i++) {
            best = Math.max(best, solve_2(nums, i, memoLen, memoCnt));
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            if (memoLen[i] == best) {
                res += memoCnt[i];
            }
        }
        return res;
    }

    private int solve_2(int[] nums, int i, int[] memoLen, int[] memoCnt) {
        if (memoLen[i] != 0) {
            return memoLen[i];
        }
        int len = 1;
        int cnt = 1;
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                int sub = solve_2(nums, j, memoLen, memoCnt);
                if (sub + 1 > len) {
                    len = sub + 1;
                    cnt = memoCnt[j];
                } else if (sub + 1 == len) {
                    cnt += memoCnt[j];
                }
            }
        }
        memoLen[i] = len;
        memoCnt[i] = cnt;
        return len;
    }
}
