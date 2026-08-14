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
