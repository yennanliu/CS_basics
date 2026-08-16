"""

3629. Minimum Jumps to Reach End via Prime Teleportation
Medium

You are given an integer array nums of length n.

You start at index 0, and your goal is to reach index n - 1.

From any index i, you may perform one of the following operations:

Adjacent Step: Jump to index i + 1 or i - 1, if the index is within bounds.

Prime Teleportation: If nums[i] is a prime number p, you may instantly jump
to any index j != i such that nums[j] % p == 0.

Return the minimum number of jumps required to reach index n - 1.

Example 1:

Input: nums = [1,2,4,6]
Output: 2
Explanation:
One optimal sequence of jumps is:
Start at index i = 0. Take an adjacent step to index 1.
At index i = 1, nums[1] = 2 is a prime number. Therefore, we teleport to
index i = 3 as nums[3] = 6 is divisible by 2.
Thus, the answer is 2.

Example 2:

Input: nums = [2,3,4,7,9]
Output: 2
Explanation:
One optimal sequence of jumps is:
Start at index i = 0. Take an adjacent step to index i = 1.
At index i = 1, nums[1] = 3 is a prime number. Therefore, we teleport to
index i = 4 since nums[4] = 9 is divisible by 3.
Thus, the answer is 2.

Example 3:

Input: nums = [4,6,5,8]
Output: 3
Explanation:
Since no teleportation is possible, we move through 0 → 1 → 2 → 3. Thus, the
answer is 3.

Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : BFS + SMALLEST-PRIME-FACTOR SIEVE, EACH PRIME BUCKET USED ONCE
#
#   the moves form an unweighted graph, so bfs gives the minimum number of
#   moves. the danger is the teleport edge: naively a prime p at index i
#   points at every multiple of p, which is quadratic.
#
#   the fix is that a bucket only ever needs to be expanded once. the first
#   time bfs stands on ANY index holding the prime p, it relaxes every index
#   whose value is divisible by p; every one of those gets its final (minimum)
#   distance right then, so the bucket can be emptied and never rebuilt.
#   total work over all buckets is bounded by sum of #distinct prime factors,
#   i.e. O(n log log max).
#
#   buckets are built from a smallest-prime-factor sieve: index j is filed
#   under each distinct prime dividing nums[j].
#
# time = O(max(nums) log log max(nums) + n log max(nums)), space = O(max(nums) + n log max(nums))
from collections import deque


class Solution(object):
    def minJumps(self, nums):
        n = len(nums)
        if n == 1:
            return 0
        hi = max(nums)
        # smallest prime factor sieve
        spf = list(range(hi + 1))
        i = 2
        while i * i <= hi:
            if spf[i] == i:
                for j in range(i * i, hi + 1, i):
                    if spf[j] == j:
                        spf[j] = i
            i += 1

        buckets = {}
        for idx in range(n):
            v = nums[idx]
            while v > 1:
                p = spf[v]
                if p in buckets:
                    buckets[p].append(idx)
                else:
                    buckets[p] = [idx]
                while v % p == 0:
                    v //= p

        dist = [-1] * n
        dist[0] = 0
        dq = deque([0])
        while dq:
            i = dq.popleft()
            d = dist[i]
            if i == n - 1:
                return d
            for j in (i - 1, i + 1):
                if 0 <= j < n and dist[j] == -1:
                    dist[j] = d + 1
                    dq.append(j)
            v = nums[i]
            # nums[i] is prime  <=>  v > 1 and spf[v] == v
            if v > 1 and spf[v] == v and v in buckets:
                for j in buckets.pop(v):
                    if dist[j] == -1:
                        dist[j] = d + 1
                        dq.append(j)
        return dist[n - 1]
