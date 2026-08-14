"""

2355. Maximum Number of Books You Can Take
Hard

You are given a 0-indexed integer array books of length n where books[i] denotes the number of books on the ith shelf of a bookshelf.

You are going to take books from a contiguous section of the bookshelf spanning from l to r where 0 <= l <= r < n. For each index i in the range l <= i < r, you must take strictly fewer books from shelf i than shelf i + 1.

Return the maximum number of books you can take from the bookshelf.


Example 1:

Input: books = [8,5,2,7,9]
Output: 19
Explanation:
- Take 1 book from shelf 1.
- Take 2 books from shelf 2.
- Take 7 books from shelf 3.
- Take 9 books from shelf 4.
You have taken 19 books, so return 19.
It can be proven that 19 is the maximum number of books you can take.

Example 2:

Input: books = [7,0,3,4,5]
Output: 12
Explanation:
- Take 3 books from shelf 2.
- Take 4 books from shelf 3.
- Take 5 books from shelf 4.
You have taken 12 books so return 12.
It can be proven that 12 is the maximum number of books you can take.

Example 3:

Input: books = [8,2,3,7,3,4,0,1,4,3]
Output: 13
Explanation:
- Take 1 book from shelf 0.
- Take 2 books from shelf 1.
- Take 3 books from shelf 2.
- Take 7 books from shelf 3.
You have taken 13 books so return 13.
It can be proven that 13 is the maximum number of books you can take.


Constraints:

1 <= books.length <= 10^5
0 <= books[i] <= 10^5

"""

# V0
# IDEA : MONOTONIC STACK + DP (optimal section ending at i is a 1-step staircase)
#
#   In an optimal answer whose right end is i, taking books[i] from shelf i is
#   always best, and going left we take one fewer each step (i.e. books[i]-1,
#   books[i]-2, ...) until we are forced to stop because the shelf itself has
#   fewer books than that.
#
#   Rewrite nums[i] = books[i] - i. The staircase can extend left past j iff
#   nums[j] >= nums[i]. So the breaking point is
#       left[i] = nearest index j < i with nums[j] < nums[i]   (monotonic stack)
#
#   dp[i] = best total for a section ending exactly at i
#         = (arithmetic sum books[i], books[i]-1, ... over i-left[i] terms)
#           + dp[left[i]]
#
#   NOTE : when left[i] != -1 we always have books[i] >= i-left[i], because
#          books[left[i]] - left[i] < books[i] - i and books[] is non-negative.
#          When left[i] == -1 the run is capped by min(books[i], i+1).
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumBooks(self, books):
        n = len(books)
        nums = [books[i] - i for i in range(n)]

        left = [-1] * n
        stk = []
        for i in range(n):
            while stk and nums[stk[-1]] >= nums[i]:
                stk.pop()
            if stk:
                left[i] = stk[-1]
            stk.append(i)

        res = 0
        dp = [0] * n
        for i in range(n):
            v = books[i]
            j = left[i]
            cnt = min(v, i - j)          # how many shelves the staircase covers
            lo = v - cnt + 1
            s = (lo + v) * cnt // 2
            dp[i] = s + (dp[j] if j != -1 else 0)
            if dp[i] > res:
                res = dp[i]
        return res
