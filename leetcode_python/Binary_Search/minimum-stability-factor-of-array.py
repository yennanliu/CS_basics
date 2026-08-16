"""

3605. Minimum Stability Factor of Array
Hard

You are given an integer array nums and an integer maxC.

A subarray is called stable if the highest common factor (HCF) of all its
elements is greater than or equal to 2.

The stability factor of an array is defined as the length of its longest
stable subarray.

You may modify at most maxC elements of the array to any integer.

Return the minimum possible stability factor of the array after at most
maxC modifications. If no stable subarray remains, return 0.

Note:

The highest common factor (HCF) of an array is the largest integer that
evenly divides all the array elements.
A subarray of length 1 is stable if its only element is greater than or
equal to 2, since HCF([x]) = x.


Example 1:

Input: nums = [3,5,10], maxC = 1
Output: 1
Explanation:
The stable subarray [5, 10] has HCF = 5, which has a stability factor of 2.
Since maxC = 1, one optimal strategy is to change nums[1] to 7, resulting in
nums = [3, 7, 10].
Now, no subarray of length greater than 1 has HCF >= 2. Thus, the minimum
possible stability factor is 1.

Example 2:

Input: nums = [2,6,8], maxC = 2
Output: 1
Explanation:
The subarray [2, 6, 8] has HCF = 2, which has a stability factor of 3.
Since maxC = 2, one optimal strategy is to change nums[1] to 3 and nums[2]
to 5, resulting in nums = [2, 3, 5].
Now, no subarray of length greater than 1 has HCF >= 2. Thus, the minimum
possible stability factor is 1.

Example 3:

Input: nums = [2,4,9,6], maxC = 1
Output: 2
Explanation:
The stable subarrays are:
[2, 4] with HCF = 2 and stability factor of 2.
[9, 6] with HCF = 3 and stability factor of 2.
Since maxC = 1, the stability factor of 2 cannot be reduced due to two
separate stable subarrays. Thus, the minimum possible stability factor is 2.


Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= 10^9
0 <= maxC <= n

"""

# V0
# IDEA : BINARY SEARCH THE ANSWER + GREEDY INTERVAL STABBING WITH GCD RUNS
#
#   first, a modification is only ever worth spending on turning an element
#   into 1. writing 1 makes every subarray through that index have hcf 1,
#   so the index becomes a wall; writing anything >= 2 leaves at least the
#   singleton stable and can only help the adversary. and since 1 is purely
#   destructive, no *new* stable subarray can appear — the surviving stable
#   subarrays are exactly the original ones that dodge every wall.
#
#   that turns the problem into a covering question. "the answer is at most
#   L" means no original stable subarray of length L + 1 survives, i.e.
#   every stable window [i - L, i] contains a wall. fewer walls are needed
#   as L grows, so the predicate is monotone and we can binary search L
#   over [0, n].
#
#   for a fixed L the cheapest wall placement is the classic stabbing
#   greedy: sweep left to right, and the first time we meet a stable window
#   that no existing wall pierces, put a wall at its rightmost index. that
#   position covers this window and reaches as far right as legally
#   possible, so it dominates every other choice for the same window.
#
#   what makes the sweep cheap is a gcd fact: as the left end of a subarray
#   ending at i moves leftwards the gcd only shrinks by divisibility, so it
#   crosses below 2 exactly once. hence for each i there is a single
#   frontier lo[i] — the earliest start whose gcd still reaches 2 — and the
#   window [i - L, i] is stable iff lo[i] <= i - L. those frontiers come
#   from carrying the list of distinct gcds of subarrays ending at i, which
#   stays O(log(max)) long because each distinct value at least halves.
#
# time = O(n * log(max(nums)) + n * log(n)), space = O(n)
class Solution(object):
    def minStable(self, nums, maxC):
        from math import gcd

        n = len(nums)

        # reach[i] = length - 1 of the longest stable subarray ending at i,
        #            or -1 when nums[i] == 1 and nothing is stable there
        reach = [0] * n
        cur = []  # (gcd, earliest start giving that gcd), gcd >= 2
        for i in range(n):
            x = nums[i]
            nxt = []
            for g, idx in cur:
                ng = gcd(g, x)
                if ng >= 2 and (not nxt or nxt[-1][0] != ng):
                    nxt.append((ng, idx))
            if x >= 2 and (not nxt or nxt[-1][0] != x):
                nxt.append((x, i))
            cur = nxt
            reach[i] = i - cur[0][1] if cur else -1

        def feasible(L):
            walls = 0
            last = -1
            for i in range(L, n):
                left = i - L
                if reach[i] >= L and last < left:
                    walls += 1
                    if walls > maxC:
                        return False
                    last = i
            return True

        lo, hi = 0, n
        while lo < hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
