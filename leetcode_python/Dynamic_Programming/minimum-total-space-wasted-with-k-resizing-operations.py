"""

1959. Minimum Total Space Wasted With K Resizing Operations
Medium

You are currently designing a dynamic array. You are given a 0-indexed integer array nums, where nums[i] is the number of elements that will be in the array at time i. In addition, you are given an integer k, the maximum number of times you can resize the array (to any size).

The size of the array at time t, size_t, must be at least nums[t] because there needs to be enough space in the array to hold all the elements. The space wasted at time t is defined as size_t - nums[t], and the total space wasted is the sum of the space wasted across every time t where 0 <= t < nums.length.

Return the minimum total space wasted if you can resize the array at most k times.

Note: The array can have any size at the start and does not count towards the number of resizing operations.


Example 1:

Input: nums = [10,20], k = 0
Output: 10
Explanation: size = [20,20].
We can set the initial size to be 20.
The total wasted space is (20 - 10) + (20 - 20) = 10.

Example 2:

Input: nums = [10,20,30], k = 1
Output: 10
Explanation: size = [20,20,30].
We can set the initial size to be 20 and resize to 30 at time 2.
The total wasted space is (20 - 10) + (20 - 20) + (30 - 30) = 10.

Example 3:

Input: nums = [10,20,15,30,20], k = 2
Output: 15
Explanation: size = [10,20,20,30,30].
We can set the initial size to 10, resize to 20 at time 1, and resize to 30 at time 3.
The total wasted space is (10 - 10) + (20 - 20) + (20 - 15) + (30 - 30) + (30 - 20) = 15.


Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= 10^6
0 <= k <= nums.length - 1

"""

# V0
# IDEA : PARTITION DP (k resizes == cut nums into k+1 segments)
#
#   within one segment the size never changes, so the best size is the
#   segment max -> waste(i..j) = max(nums[i..j]) * (j-i+1) - sum(nums[i..j]).
#
#   precompute g[i][j] = waste of segment nums[i..j]  (O(n^2) rolling max/sum)
#
#   f[i][j] = min waste of splitting the first i elements into j segments
#   f[i][j] = min over h < i of  f[h][j-1] + g[h][i-1]
#   answer  = f[n][k+1]
#
#   NOTE : k is the number of RESIZES, so the number of segments is k + 1.
#
# time = O(n^2 * k), space = O(n^2 + n*k)
class Solution(object):
    def minSpaceWastedKResizing(self, nums, k):
        n = len(nums)
        k += 1  # number of segments

        # g[i][j] = wasted space if nums[i..j] is served by a single size
        g = [[0] * n for _ in range(n)]
        for i in range(n):
            s = 0
            mx = 0
            for j in range(i, n):
                s += nums[j]
                mx = max(mx, nums[j])
                g[i][j] = mx * (j - i + 1) - s

        INF = float("inf")
        f = [[INF] * (k + 1) for _ in range(n + 1)]
        f[0][0] = 0
        for i in range(1, n + 1):
            for j in range(1, min(k, i) + 1):
                for h in range(i):
                    if f[h][j - 1] < INF:
                        cur = f[h][j - 1] + g[h][i - 1]
                        if cur < f[i][j]:
                            f[i][j] = cur
        return f[n][k]
