"""

3049. Earliest Second to Mark Indices II
Hard

You are given two 1-indexed integer arrays, nums and, changeIndices, having lengths n and m, respectively.

Initially, all indices in nums are unmarked. Your task is to mark all indices in nums.

In each second, s, in order from 1 to m (inclusive), you can perform one of the following operations:

Choose an index i in the range [1, n] and decrement nums[i] by 1.
Set nums[changeIndices[s]] to any non-negative value.
Choose an index i in the range [1, n], where nums[i] is equal to 0, and mark index i.
Do nothing.

Return an integer denoting the earliest second in the range [1, m] when all indices in nums can be marked by choosing operations optimally, or -1 if it is impossible.


Example 1:

Input: nums = [3,2,3], changeIndices = [1,3,2,2,2,2,3]
Output: 6
Explanation: In this example, we have 7 seconds. The following operations can be performed to mark all indices:
Second 1: Set nums[changeIndices[1]] to 0. nums becomes [0,2,3].
Second 2: Mark index 1. It can be marked since nums[1] is equal to 0.
Second 3: Set nums[changeIndices[3]] to 0. nums becomes [0,0,3].
Second 4: Mark index 2. It can be marked since nums[2] is equal to 0.
Second 5: Decrement nums[3] by one. nums becomes [0,0,2].
Second 6: Set nums[changeIndices[6]] to 0. nums becomes [0,0,0].
Now all indices have been marked.
It can be shown that it is not possible to mark all indices earlier than the 6th second.
Hence, the answer is 6.

Example 2:

Input: nums = [0,0,1,2], changeIndices = [1,2,1,2,1,2,1,2]
Output: 7
Explanation: In this example, we have 8 seconds. The following operations can be performed to mark all indices:
Second 1: Mark index 1. It can be marked since nums[1] is equal to 0.
Second 2: Mark index 2. It can be marked since nums[2] is equal to 0.
Second 3: Decrement index 4 by one. nums becomes [0,0,1,1].
Second 4: Decrement index 4 by one. nums becomes [0,0,1,0].
Second 5: Decrement index 3 by one. nums becomes [0,0,0,0].
Second 6: Mark index 3. It can be marked since nums[3] is equal to 0.
Second 7: Mark index 4. It can be marked since nums[4] is equal to 0.
Now all indices have been marked.
It can be shown that it is not possible to mark all indices earlier than the 7th second.
Hence, the answer is 7.

Example 3:

Input: nums = [1,2,3], changeIndices = [1,2,3]
Output: -1
Explanation: In this example, it is impossible to mark all indices because we don't have enough seconds.
Hence, the answer is -1.


Constraints:

1 <= n == nums.length <= 5000
0 <= nums[i] <= 10^9
1 <= m == changeIndices.length <= 5000
1 <= changeIndices[i] <= n

"""

# V0
# IDEA : BINARY SEARCH THE DEADLINE + A HEAP GREEDY FOR "WHICH INDICES TO ZERO"
#
#   unlike LC 3048 the MARK operation is now free-form (any zero index, any
#   second), and there is a new weapon : at second t you may set
#   nums[changeIndices[t]] to 0 outright.
#
#   so every index is handled one of two ways :
#       plain     : nums[i] decrement-seconds + 1 mark-second
#       shortcut  : 1 second for the set + 1 mark-second
#   picking a set S of shortcut indices costs
#       sum(nums[i] for i not in S) + |S| + n   seconds
#   which means S saves  sum(nums[i] - 1 for i in S).  so we want the largest
#   possible saving that still fits a valid schedule.
#
#   two scheduling facts pin it down :
#     * each shortcut needs its OWN second holding that index — take the
#       EARLIEST occurrence, which is automatically distinct across indices
#       and leaves the most room afterwards.
#     * the mark of a shortcut index must come after its set. sorting the
#       chosen seconds ascending as t_1 < ... < t_K, the marks fit iff
#           s - t_j >= 2*(K - j) + 1     for every j
#       (each later shortcut still needs its own set-second and mark-second).
#
#   scanning the candidates from the LATEST second backwards keeps K - j
#   equal to "how many are already chosen", so the rule becomes a running
#   test, and a min-heap lets a fatter saving evict a thinner one.
#
# time = O(m log m log m), space = O(n + m)
import heapq


class Solution(object):
    def earliestSecondToMarkIndices(self, nums, changeIndices):
        n, m = len(nums), len(changeIndices)
        base = sum(nums) + n              # seconds needed with no shortcut at all

        def feasible(s):
            if base <= s:
                return True

            first = {}
            for t in range(1, s + 1):
                i = changeIndices[t - 1]
                if i not in first:
                    first[i] = t

            # candidates, latest second first
            cand = sorted(((t, nums[i - 1]) for i, t in first.items()),
                          key=lambda p: -p[0])

            heap = []                     # savings-bearing values we keep
            total = 0                     # sum of nums[i] over the chosen set
            for t, v in cand:
                if v <= 1:                # a shortcut here would save nothing
                    continue
                k = len(heap)
                if s - t >= 2 * k + 1:
                    heapq.heappush(heap, v)
                    total += v
                elif heap and heap[0] < v and s - t >= 2 * (k - 1) + 1:
                    total += v - heapq.heappop(heap)
                    heapq.heappush(heap, v)

            saving = total - len(heap)
            return base - saving <= s

        lo, hi = 1, m
        res = -1
        while lo <= hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                res = mid
                hi = mid - 1
            else:
                lo = mid + 1
        return res
