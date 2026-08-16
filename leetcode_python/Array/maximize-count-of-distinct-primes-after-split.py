"""

3569. Maximize Count of Distinct Primes After Split
Hard

You are given an integer array nums having length n and a 2D integer array
queries where queries[i] = [idx, val].

For each query:

1. Update nums[idx] = val.
2. Choose an integer k with 1 <= k < n to split the array into the non-empty
   prefix nums[0..k-1] and suffix nums[k..n-1] such that the sum of the
   counts of distinct prime values in each part is maximum.

Note: The changes made to the array in one query persist into the next
query.

Return an array containing the result for each query, in the order they are
given.


Example 1:

Input: nums = [2,1,3,1,2], queries = [[1,2],[3,3]]
Output: [3,4]
Explanation:
- Initially nums = [2, 1, 3, 1, 2].
- After 1st query, nums = [2, 2, 3, 1, 2]. Split nums into [2] and
  [2, 3, 1, 2]. [2] consists of 1 distinct prime and [2, 3, 1, 2] consists
  of 2 distinct primes. Hence, the answer for this query is 1 + 2 = 3.
- After 2nd query, nums = [2, 2, 3, 3, 2]. Split nums into [2, 2, 3] and
  [3, 2] with an answer of 2 + 2 = 4.
- The output is [3, 4].

Example 2:

Input: nums = [2,1,4], queries = [[0,1]]
Output: [0]
Explanation:
- Initially nums = [2, 1, 4].
- After 1st query, nums = [1, 1, 4]. There are no prime numbers in nums,
  hence the answer for this query is 0.
- The output is [0].


Constraints:

2 <= n == nums.length <= 5 * 10^4
1 <= queries.length <= 5 * 10^4
1 <= nums[i] <= 10^5
0 <= queries[i][0] < nums.length
1 <= queries[i][1] <= 10^5

"""

# V0
# IDEA : REWRITE THE SPLIT VALUE AS "TOTAL PRIMES + INTERVAL COVERAGE",
#        THEN A RANGE-ADD / GLOBAL-MAX SEGMENT TREE
#
#   a distinct prime p only cares about where it first and last occurs.
#   for a split at k it is counted in the prefix iff first[p] < k, and in
#   the suffix iff last[p] >= k. since first[p] <= last[p], at least one of
#   the two always holds — so every prime present contributes at least 1,
#   independent of k, and contributes a *second* point exactly when
#   first[p] < k <= last[p].
#
#   so answer = P + max over k in [1, n-1] of "how many primes have k inside
#   (first[p], last[p]]", where P is the number of distinct primes present.
#   the search over k collapses into a classic stabbing problem: each prime
#   occurring at two or more indices lays down the interval
#   [first[p] + 1, last[p]] and we want the point covered most often.
#
#   an update touches at most two prime values (the one leaving the cell and
#   the one entering it), and each of those shifts a single interval — so
#   keep every prime's occurrence indices in a set with lazily-cleaned min /
#   max heaps, retract the old interval with a range -1 and lay down the new
#   one with a range +1. a bottom-up lazy segment tree holds the coverage
#   profile and its root is the running maximum, read off in O(1).
#
# time = O((n + q) * log n + V log log V), space = O(n + V)
class Solution(object):
    def maximumCount(self, nums, queries):
        import heapq

        LIMIT = 100001
        sieve = bytearray([1]) * LIMIT
        sieve[0] = sieve[1] = 0
        i = 2
        while i * i < LIMIT:
            if sieve[i]:
                sieve[i * i::i] = bytearray(len(range(i * i, LIMIT, i)))
            i += 1

        n = len(nums)

        # segment tree over the split points k = 1 .. n - 1, stored at
        # positions 0 .. n - 2; supports range add and O(1) global max
        size = 1
        while size < n - 1:
            size <<= 1
        tree = [0] * (2 * size)
        lazy = [0] * size

        def modify(lo, hi, delta):
            # add delta to every position in [lo, hi]
            l = lo + size
            r = hi + size + 1
            left, right = l, r - 1
            while l < r:
                if l & 1:
                    tree[l] += delta
                    if l < size:
                        lazy[l] += delta
                    l += 1
                if r & 1:
                    r -= 1
                    tree[r] += delta
                    if r < size:
                        lazy[r] += delta
                l >>= 1
                r >>= 1
            for x in (left, right):
                while x > 1:
                    x >>= 1
                    a, b = tree[x << 1], tree[x << 1 | 1]
                    tree[x] = (a if a > b else b) + lazy[x]

        where = {}       # prime -> set of indices currently holding it
        lows = {}        # prime -> min-heap of candidate indices
        highs = {}       # prime -> max-heap (negated) of candidate indices
        distinct = [0]   # number of distinct primes present

        def span(p):
            # current [first + 1, last] as segment-tree positions, or None
            s = where.get(p)
            if not s:
                return None
            h = lows[p]
            while h[0] not in s:
                heapq.heappop(h)
            first = h[0]
            g = highs[p]
            while -g[0] not in s:
                heapq.heappop(g)
            last = -g[0]
            return (first, last - 1) if first < last else None

        def relay(p, before, after):
            if before == after:
                return
            if before is not None:
                modify(before[0], before[1], -1)
            if after is not None:
                modify(after[0], after[1], 1)

        def attach(p, idx):
            s = where.get(p)
            if s is None:
                s = where[p] = set()
                lows[p] = []
                highs[p] = []
            before = span(p)
            if not s:
                distinct[0] += 1
            s.add(idx)
            heapq.heappush(lows[p], idx)
            heapq.heappush(highs[p], -idx)
            relay(p, before, span(p))

        def detach(p, idx):
            s = where[p]
            before = span(p)
            s.discard(idx)
            if not s:
                distinct[0] -= 1
            relay(p, before, span(p))

        cur = list(nums)
        for idx, v in enumerate(cur):
            if sieve[v]:
                attach(v, idx)

        res = []
        for idx, val in queries:
            old = cur[idx]
            if old != val:
                if sieve[old]:
                    detach(old, idx)
                cur[idx] = val
                if sieve[val]:
                    attach(val, idx)
            res.append(distinct[0] + tree[1])
        return res
