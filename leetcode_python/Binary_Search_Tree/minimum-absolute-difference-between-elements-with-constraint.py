"""

2817. Minimum Absolute Difference Between Elements With Constraint
Medium

You are given a 0-indexed integer array nums and an integer x.

Find the minimum absolute difference between two elements in the array that are at least x indices apart.

In other words, find two indices i and j such that abs(i - j) >= x and abs(nums[i] - nums[j]) is minimized.

Return an integer denoting the minimum absolute difference between two elements that are at least x indices apart.


Example 1:

Input: nums = [4,3,2,4], x = 2
Output: 0
Explanation: We can select nums[0] = 4 and nums[3] = 4.
They are at least 2 indices apart, and their absolute difference is the minimum, 0.
It can be shown that 0 is the optimal answer.

Example 2:

Input: nums = [5,3,2,10,15], x = 1
Output: 1
Explanation: We can select nums[1] = 3 and nums[2] = 2.
They are at least 1 index apart, and their absolute difference is the minimum, 1.
It can be shown that 1 is the optimal answer.

Example 3:

Input: nums = [1,2,3,4], x = 3
Output: 3
Explanation: We can select nums[0] = 1 and nums[3] = 4.
They are at least 3 indices apart, and their absolute difference is the minimum, 3.
It can be shown that 3 is the optimal answer.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
0 <= x < nums.length

"""

# V0
# IDEA : SLIDING "ORDERED SET" OF ELIGIBLE PARTNERS (BST-STYLE PREDECESSOR /
#        SUCCESSOR QUERIES, IMPLEMENTED ON A BIT WITH BINARY LIFTING)
#
#   Sweep j = x, x+1, ... and just before handling j insert nums[j - x] into
#   an ordered multiset S. By construction S holds exactly the values whose
#   index is <= j - x, i.e. every partner that is >= x indices away from j
#   (pairs with i > j are covered symmetrically when the roles swap, so one
#   forward sweep is enough).
#
#   The best partner for nums[j] inside S is its PREDECESSOR (largest value
#   <= nums[j]) or its SUCCESSOR (smallest value >= nums[j]) - anything
#   further away in value is strictly worse. Two ordered-set queries per step.
#
#   NOTE : x == 0 is legal and means "any two indices, including i == j", so
#          the sweep must insert nums[j] itself before querying j -> answer 0.
#          Starting the loop at j = x with the insert-then-query order gets
#          this right automatically.
#
#   NOTE : Python has no built-in balanced BST. Instead of an O(n) per-insert
#          `bisect.insort` (which degrades to ~O(n^2) memmove at n = 10^5),
#          the ordered set here is a Fenwick tree over COMPRESSED values:
#          add() is O(log n), and predecessor / successor are answered by
#          "count how many are <= v" plus a binary-lifting select of the k-th
#          smallest, also O(log n).
#
# time = O(n * log n), space = O(n)
class Solution(object):
    def minAbsoluteDifference(self, nums, x):
        n = len(nums)

        # coordinate compression: value -> rank in 1..m
        vals = sorted(set(nums))
        m = len(vals)
        rank = {}
        for i, v in enumerate(vals):
            rank[v] = i + 1

        tree = [0] * (m + 1)
        size = 0

        # highest power of two <= m, used by the binary-lifting select
        log = 1
        while (log << 1) <= m:
            log <<= 1

        def add(i):
            while i <= m:
                tree[i] += 1
                i += i & (-i)

        def count_le(i):
            # how many inserted values have rank <= i
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & (-i)
            return s

        def select(k):
            # rank of the k-th smallest inserted value (1 <= k <= size)
            pos, rest = 0, k
            step = log
            while step:
                nxt = pos + step
                if nxt <= m and tree[nxt] < rest:
                    pos = nxt
                    rest -= tree[pos]
                step >>= 1
            return pos + 1

        ans = float('inf')
        for j in range(x, n):
            add(rank[nums[j - x]])
            size += 1

            r = rank[nums[j]]
            c = count_le(r)
            if c > 0:                          # predecessor (value <= nums[j])
                d = nums[j] - vals[select(c) - 1]
                if d < ans:
                    ans = d
            if c < size:                       # successor (value > nums[j])
                d = vals[select(c + 1) - 1] - nums[j]
                if d < ans:
                    ans = d
            if ans == 0:
                return 0
        return ans
