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


# V0-1
# IDEA : BRUTE FORCE - RECOMPUTE PREFIX / SUFFIX DISTINCT PRIME COUNTS PER QUERY
#
#   no reformulation and no data structure : after applying the update, walk
#   left to right holding a set of the primes already seen to get
#   pre[k] = # distinct primes in nums[0..k-1], walk right to left the same
#   way for suf[k] = # distinct primes in nums[k..n-1], then take
#   max(pre[k] + suf[k]) over k = 1 .. n-1.
#   O(n) per query, so this is the reference implementation to check the
#   clever versions against rather than a submission for the real limits.
#
# time = O(q * n + V log log V), space = O(n + V)
class Solution(object):
    def maximumCount(self, nums, queries):
        LIMIT = 100001
        sieve = bytearray([1]) * LIMIT
        sieve[0] = sieve[1] = 0
        i = 2
        while i * i < LIMIT:
            if sieve[i]:
                sieve[i * i::i] = bytearray(len(range(i * i, LIMIT, i)))
            i += 1

        n = len(nums)
        cur = list(nums)
        res = []
        for idx, val in queries:
            cur[idx] = val

            pre = [0] * (n + 1)
            seen = set()
            for i in range(n):
                if sieve[cur[i]]:
                    seen.add(cur[i])
                pre[i + 1] = len(seen)

            suf = [0] * (n + 1)
            seen = set()
            for i in range(n - 1, -1, -1):
                if sieve[cur[i]]:
                    seen.add(cur[i])
                suf[i] = len(seen)

            best = 0
            for k in range(1, n):
                if pre[k] + suf[k] > best:
                    best = pre[k] + suf[k]
            res.append(best)
        return res


# V0-2
# IDEA : SAME "TOTAL PRIMES + INTERVAL STABBING" REWRITE, BUT SQRT DECOMPOSITION
#
#   as in V0 : every distinct prime present scores 1 for free, and scores a
#   second point exactly for the split points k in [first[p] + 1, last[p]],
#   so answer = distinct + (max coverage of those intervals over one point).
#
#   the difference is the structure holding the coverage profile. instead of a
#   lazy segment tree the array is cut into blocks of ~sqrt(n) :
#     * range add - the whole blocks strictly inside take a lazy += delta in
#       O(1) each, the two partial blocks are patched element-wise and their
#       block maximum recomputed, so O(sqrt n) per interval
#     * global max - max over blocks of (block_max + block_lazy), O(sqrt n)
#   occurrence indices of a prime live in one bisect-sorted list, so first and
#   last are s[0] / s[-1] and an update is a single insort / pop.
#   flatter constants and much less code than the segment tree, at the price
#   of sqrt(n) instead of log(n) per operation.
#
# time = O((n + q) * sqrt(n) + V log log V), space = O(n + V)
class Solution(object):
    def maximumCount(self, nums, queries):
        import bisect

        LIMIT = 100001
        sieve = bytearray([1]) * LIMIT
        sieve[0] = sieve[1] = 0
        i = 2
        while i * i < LIMIT:
            if sieve[i]:
                sieve[i * i::i] = bytearray(len(range(i * i, LIMIT, i)))
            i += 1

        n = len(nums)
        m = n - 1                    # split points k = 1..n-1 -> slots 0..m-1
        B = int(m ** 0.5) + 1
        nb = (m + B - 1) // B
        cov = [0] * m
        blk_max = [0] * nb
        blk_add = [0] * nb

        def add(lo, hi, delta):
            b_lo, b_hi = lo // B, hi // B
            if b_lo == b_hi:
                for j in range(lo, hi + 1):
                    cov[j] += delta
                s, e = b_lo * B, min(b_lo * B + B, m)
                blk_max[b_lo] = max(cov[s:e])
                return
            s, e = b_lo * B, min(b_lo * B + B, m)
            for j in range(lo, e):
                cov[j] += delta
            blk_max[b_lo] = max(cov[s:e])
            for b in range(b_lo + 1, b_hi):
                blk_add[b] += delta
            s, e = b_hi * B, min(b_hi * B + B, m)
            for j in range(s, hi + 1):
                cov[j] += delta
            blk_max[b_hi] = max(cov[s:e])

        occ = {}         # prime -> sorted list of indices holding it
        distinct = [0]

        def span(p):
            s = occ.get(p)
            if not s or s[0] == s[-1]:
                return None
            return (s[0], s[-1] - 1)

        def relay(before, after):
            if before == after:
                return
            if before is not None:
                add(before[0], before[1], -1)
            if after is not None:
                add(after[0], after[1], 1)

        def attach(p, idx):
            s = occ.get(p)
            if s is None:
                s = occ[p] = []
            before = span(p)
            if not s:
                distinct[0] += 1
            bisect.insort(s, idx)
            relay(before, span(p))

        def detach(p, idx):
            s = occ[p]
            before = span(p)
            s.pop(bisect.bisect_left(s, idx))
            if not s:
                distinct[0] -= 1
            relay(before, span(p))

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
            top = max(blk_max[b] + blk_add[b] for b in range(nb))
            res.append(distinct[0] + top)
        return res
