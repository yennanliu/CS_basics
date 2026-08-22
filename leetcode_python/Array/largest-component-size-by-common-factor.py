"""

952. Largest Component Size by Common Factor
Hard

You are given an integer array of unique positive integers nums. Consider the following graph:

There are nums.length nodes, labeled nums[0] to nums[nums.length - 1],
There is an undirected edge between nums[i] and nums[j] if nums[i] and nums[j] share a common factor greater than 1.

Return the size of the largest connected component in the graph.

Example 1:

Input: nums = [4,6,15,35]
Output: 4

Example 2:

Input: nums = [20,50,9,63]
Output: 2

Example 3:

Input: nums = [2,3,6,7,4,12,21,39]
Output: 8

Constraints:

1 <= nums.length <= 2 * 10^4
1 <= nums[i] <= 10^5
All the values of nums are unique.

"""

# V0
# IDEA : UNION FIND over PRIME FACTORS
#
#  - Building edges pairwise is O(n^2) -> too slow.
#  - Instead, union every number with each of its prime factors.
#    Two numbers sharing a prime factor then land in the same set
#    (transitively through that prime's node).
#  - Numbers and primes share one label space (both <= max(nums)); the
#    collision is harmless because "number v" and "prime v" belong together
#    anyway.
#  - Finally, count how many of the ORIGINAL numbers fall under each root.
#
# time = O(n * sqrt(M) * a(M)), n = len(nums), M = max(nums)
# space = O(M)
from collections import Counter
class Solution(object):
    def largestComponentSize(self, nums):
        m = max(nums)
        parent = list(range(m + 1))

        def find(x):
            # iterative find with path halving
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb

        for v in nums:
            x = v
            f = 2
            while f * f <= x:
                if x % f == 0:
                    union(v, f)
                    while x % f == 0:
                        x //= f
                f += 1
            if x > 1:
                # leftover prime factor (bigger than sqrt(v))
                union(v, x)

        # only the actual numbers count towards component size, not the prime nodes
        cnt = Counter(find(v) for v in nums)
        return max(cnt.values())


# V0-1
# IDEA : SMALLEST-PRIME-FACTOR SIEVE + UNION FIND OVER THE INDICES
#
#  - sieve spf[x] = smallest prime factor of x once, up to max(nums).
#    factorising a number is then O(log v) divisions instead of O(sqrt(v))
#    trial divisions.
#  - the DSU nodes are the INDICES of nums (not the values), so no shared
#    label space with the primes is needed. For each prime p we remember the
#    first index that owned it and union every later owner with that index.
#  - component sizes come straight out of the DSU size array.
#
# time = O(M log log M + n log M), n = len(nums), M = max(nums)
# space = O(M)
class Solution(object):
    def largestComponentSize(self, nums):
        M = max(nums)
        spf = list(range(M + 1))
        i = 2
        while i * i <= M:
            if spf[i] == i:
                for j in range(i * i, M + 1, i):
                    if spf[j] == j:
                        spf[j] = i
            i += 1

        n = len(nums)
        parent = list(range(n))
        size = [1] * n

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return
            if size[ra] < size[rb]:
                ra, rb = rb, ra
            parent[rb] = ra
            size[ra] += size[rb]

        owner = {}          # prime -> index of the first number carrying it
        for k, v in enumerate(nums):
            x = v
            while x > 1:
                p = spf[x]
                if p in owner:
                    union(k, owner[p])
                else:
                    owner[p] = k
                while x % p == 0:
                    x //= p

        return max(size[find(k)] for k in range(n))


# V0-2
# IDEA : PRIME BUCKETS -> EXPLICIT GRAPH -> ITERATIVE DFS (no union find)
#
#  - bucket[p] = every index whose number is divisible by prime p, and
#    primes_of[k] = the prime factors of nums[k]. Together they encode the
#    graph without ever writing down its O(n^2) edges.
#  - flood fill from each unvisited index : from index k hop to every index
#    in bucket[p] for each p in primes_of[k].
#  - each bucket is expanded at most ONCE globally (a prime lives in exactly
#    one component), which is what keeps the traversal linear in the total
#    bucket size instead of quadratic.
#
# time = O(n * sqrt(M) + n log M), space = O(n log M)
from collections import defaultdict
class Solution(object):
    def largestComponentSize(self, nums):
        n = len(nums)
        bucket = defaultdict(list)
        primes_of = [[] for _ in range(n)]

        for k, v in enumerate(nums):
            x = v
            f = 2
            while f * f <= x:
                if x % f == 0:
                    bucket[f].append(k)
                    primes_of[k].append(f)
                    while x % f == 0:
                        x //= f
                f += 1
            if x > 1:
                bucket[x].append(k)
                primes_of[k].append(x)

        seen = [False] * n
        used_prime = set()
        res = 0
        for start in range(n):
            if seen[start]:
                continue
            seen[start] = True
            stack = [start]
            cnt = 0
            while stack:
                k = stack.pop()
                cnt += 1
                for p in primes_of[k]:
                    if p in used_prime:
                        continue
                    used_prime.add(p)
                    for nb in bucket[p]:
                        if not seen[nb]:
                            seen[nb] = True
                            stack.append(nb)
            res = max(res, cnt)
        return res
