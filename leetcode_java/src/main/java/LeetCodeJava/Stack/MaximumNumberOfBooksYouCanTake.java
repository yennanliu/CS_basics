package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximum-number-of-books-you-can-take/

/**
 *  2355. Maximum Number of Books You Can Take
 *  Hard
 *
 *  You are given a 0-indexed integer array books of length n where books[i]
 *  denotes the number of books on the ith shelf of a bookshelf.
 *
 *  You are going to take books from a contiguous section of the bookshelf
 *  spanning from l to r where 0 <= l <= r < n. For each index i in the range
 *  l <= i < r, you must take strictly fewer books from shelf i than shelf i+1.
 *
 *  Return the maximum number of books you can take from the bookshelf.
 *
 *  Example 1:
 *    Input: books = [8,5,2,7,9]
 *    Output: 19
 *    Explanation: take 1 + 2 + 7 + 9 from shelves 1..4.
 *
 *  Example 2:
 *    Input: books = [7,0,3,4,5]
 *    Output: 12
 *    Explanation: take 3 + 4 + 5 from shelves 2..4.
 *
 *  Constraints:
 *    1 <= books.length <= 10^5
 *    0 <= books[i] <= 10^5
 */
public class MaximumNumberOfBooksYouCanTake {

    // V0
    // IDEA: MONOTONIC STACK + DP (the optimal section ending at i is a
    //       one-step staircase)
    //       In an optimal answer whose right end is i, taking books[i] from
    //       shelf i is always best, and going left we take one fewer each step
    //       (books[i]-1, books[i]-2, ...) until the shelf itself has fewer
    //       books than that.
    //       Rewrite nums[i] = books[i] - i. The staircase can extend left past
    //       j iff nums[j] >= nums[i], so the breaking point is
    //           left[i] = nearest index j < i with nums[j] < nums[i]
    //       dp[i] = (arithmetic sum books[i], books[i]-1, ... over
    //                i - left[i] terms) + dp[left[i]]
    //       When left[i] == -1 the run is capped by min(books[i], i+1).
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maximumBooks(int[] books) {
        int n = books.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = books[i] - i;
        }

        int[] left = new int[n];
        int[] stk = new int[n];
        int top = -1;
        for (int i = 0; i < n; i++) {
            while (top >= 0 && nums[stk[top]] >= nums[i]) {
                top--;
            }
            left[i] = (top >= 0) ? stk[top] : -1;
            stk[++top] = i;
        }

        long res = 0;
        long[] dp = new long[n];
        for (int i = 0; i < n; i++) {
            long v = books[i];
            int j = left[i];
            long cnt = Math.min(v, (long) i - j); // shelves the staircase covers
            long lo = v - cnt + 1;
            long s = (lo + v) * cnt / 2;
            dp[i] = s + ((j != -1) ? dp[j] : 0L);
            if (dp[i] > res) {
                res = dp[i];
            }
        }
        return res;
    }
}
