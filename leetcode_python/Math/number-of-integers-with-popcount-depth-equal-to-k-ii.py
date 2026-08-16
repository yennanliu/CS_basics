"""

3624. Number of Integers With Popcount-Depth Equal to K II
Hard

You are given an integer array nums.

For any positive integer x, define the following sequence:

p0 = x
p(i+1) = popcount(pi) for all i >= 0, where popcount(y) is the number of set
bits (1's) in the binary representation of y.

This sequence will eventually reach the value 1.

The popcount-depth of x is defined as the smallest integer d >= 0 such that
pd = 1.

For example, if x = 7 (binary representation "111"). Then, the sequence is:
7 -> 3 -> 2 -> 1, so the popcount-depth of 7 is 3.

You are also given a 2D integer array queries, where each queries[i] is either:

[1, l, r, k] - Determine the number of indices j such that l <= j <= r and the
popcount-depth of nums[j] is equal to k.
[2, idx, val] - Update nums[idx] to val.

Return an integer array answer, where answer[i] is the number of indices for
the ith query of type [1, l, r, k].


Example 1:

Input: nums = [2,4], queries = [[1,0,1,1],[2,1,1],[1,0,1,0]]
Output: [2,1]
Explanation:
Query [1,0,1,1] on nums = [2,4] (binary [10, 100], depths [1, 1]) counts both
indices 0 and 1, so the answer is 2.
Query [2,1,1] updates nums to [2,1].
Query [1,0,1,0] on nums = [2,1] (binary [10, 1], depths [1, 0]) counts only
index 1, so the answer is 1.
Thus, the final answer is [2, 1].

Example 2:

Input: nums = [3,5,6], queries = [[1,0,2,2],[2,1,4],[1,1,2,1],[1,0,1,0]]
Output: [3,1,0]
Explanation:
Query [1,0,2,2] on nums = [3,5,6] (binary [11, 101, 110], depths [2, 2, 2])
counts indices 0, 1 and 2, so the answer is 3.
Query [2,1,4] updates nums to [3,4,6].
Query [1,1,2,1] on nums = [3,4,6] (binary [11, 100, 110], depths [2, 1, 2])
counts only index 1, so the answer is 1.
Query [1,0,1,0] on nums = [3,4,6] counts nothing, so the answer is 0.
Thus, the final answer is [3, 1, 0].

Example 3:

Input: nums = [1,2], queries = [[1,0,1,1],[2,0,3],[1,0,0,1],[1,0,0,2]]
Output: [1,0,1]
Explanation:
Query [1,0,1,1] on nums = [1,2] (binary [1, 10], depths [0, 1]) counts only
index 1, so the answer is 1.
Query [2,0,3] updates nums to [3,2].
Query [1,0,0,1] on nums = [3,2] (binary [11, 10], depths [2, 1]) counts
nothing in [0, 0], so the answer is 0.
Query [1,0,0,2] on nums = [3,2] counts index 0, so the answer is 1.
Thus, the final answer is [1, 0, 1].


Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= 10^15
1 <= queries.length <= 10^5
queries[i].length == 3 or 4
queries[i] == [1, l, r, k] or,
queries[i] == [2, idx, val]
0 <= l <= r <= n - 1
0 <= k <= 5
0 <= idx <= n - 1
1 <= val <= 10^15

"""

# V0
# IDEA : DEPTH IS TINY, SO KEEP ONE FENWICK TREE PER DEPTH
#
#   the key observation is how few distinct depths exist. values go up to
#   10^15 < 2^50, so one popcount step already lands in [1, 50], and iterating
#   from there collapses fast — no value in range has depth above 4. with only
#   a handful of possible answers, "count depth == k on [l, r]" is really six
#   independent 0/1 arrays, one per depth.
#
#   each of those arrays needs prefix sums that survive point updates, which
#   is exactly a fenwick tree. an update moves a single index from its old
#   depth's tree to its new one — two O(log n) touches — and a query is one
#   prefix-sum difference in the tree for k. keeping k separate is what makes
#   this work: a single tree could not answer "how many equal k" without
#   rescanning.
#
#   depth itself is O(1) to evaluate: depth(1) = 0 and otherwise
#   depth(x) = 1 + depth(popcount(x)), and popcount(x) <= 50 is precomputed.
#
# time = O((n + q) * log n), space = O(n)
class Solution(object):
    def popcountDepth(self, nums, queries):
        MAX_BITS = 50          # 10^15 < 2^50
        MAX_DEPTH = 5          # k is capped at 5 by the constraints

        # depth of every value a popcount can produce
        small = [0] * (MAX_BITS + 1)
        for i in range(2, MAX_BITS + 1):
            small[i] = small[bin(i).count('1')] + 1

        def depth(x):
            if x == 1:
                return 0
            return small[bin(x).count('1')] + 1

        n = len(nums)

        # trees[d] is a 1-indexed fenwick tree over the indices of nums
        trees = [[0] * (n + 1) for _ in range(MAX_DEPTH + 1)]

        def add(d, i, delta):
            tree = trees[d]
            i += 1
            while i <= n:
                tree[i] += delta
                i += i & (-i)

        def prefix(d, i):
            # sum over indices [0, i]
            tree = trees[d]
            i += 1
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & (-i)
            return s

        cur = [0] * n
        for i, x in enumerate(nums):
            cur[i] = depth(x)
            add(cur[i], i, 1)

        ans = []
        for q in queries:
            if q[0] == 1:
                _, l, r, k = q
                if k > MAX_DEPTH:
                    ans.append(0)
                else:
                    ans.append(prefix(k, r) - (prefix(k, l - 1) if l else 0))
            else:
                _, idx, val = q
                d = depth(val)
                if d != cur[idx]:
                    add(cur[idx], idx, -1)
                    add(d, idx, 1)
                    cur[idx] = d
        return ans
