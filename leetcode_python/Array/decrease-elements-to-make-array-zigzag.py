"""

1144. Decrease Elements To Make Array Zigzag
Medium

Given an array nums of integers, a move consists of choosing any element
and decreasing it by 1.

An array A is a zigzag array if either:

Every even-indexed element is greater than adjacent elements,
ie. A[0] > A[1] < A[2] > A[3] < A[4] > ...
OR, every odd-indexed element is greater than adjacent elements,
ie. A[0] < A[1] > A[2] < A[3] > A[4] < ...

Return the minimum number of moves to transform the given array nums into a zigzag array.


Example 1:

Input: nums = [1,2,3]
Output: 2
Explanation: We can decrease 2 to 0 or 3 to 1.

Example 2:

Input: nums = [9,6,1,6,2]
Output: 4


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : GREEDY + ENUMERATE THE 2 PARITIES
#        we may only DECREASE, so it never helps to touch the "peak" positions.
#        -> just pick which parity becomes the "valley" (smaller than neighbours),
#           and for every valley index j push nums[j] down to
#           min(neighbours) - 1 (cost = max(0, nums[j] - min(nb) + 1)).
#        -> valleys are non adjacent, so their costs are independent.
#        answer = min(cost when even indices are valleys,
#                     cost when odd  indices are valleys)
# time = O(n)
# space = O(1)
class Solution(object):
    def movesToMakeZigzag(self, nums):
        n = len(nums)
        res = [0, 0]
        for i in range(2):
            for j in range(i, n, 2):
                nb = float('inf')
                if j - 1 >= 0:
                    nb = min(nb, nums[j - 1])
                if j + 1 < n:
                    nb = min(nb, nums[j + 1])
                if nb != float('inf') and nums[j] >= nb:
                    res[i] += nums[j] - nb + 1
        return min(res)


# V0-1
# IDEA : DP OVER THE VALUE DOMAIN (no greedy insight needed)
#
#   fix which parity carries the peaks, then let
#       dp[i][v] = min moves so that nums[0..i] is a valid zigzag prefix
#                  AND nums[i] ends up equal to v
#   dp[i][v] = (nums[i] - v) + min over allowed u of dp[i-1][u], where
#       "i is a peak"   -> u < v   (running PREFIX min of dp[i-1])
#       "i is a valley" -> u > v   (running SUFFIX min of dp[i-1])
#   only v <= nums[i] is reachable because moves may only DECREASE.
#
#   the running prefix / suffix minimum keeps each row linear in the value
#   range, so this stays O(n * max(nums)) instead of O(n * max(nums)^2).
#
# time = O(n * max(nums))
# space = O(max(nums))
class Solution(object):
    def movesToMakeZigzag(self, nums):
        n = len(nums)
        if n == 1:
            return 0

        INF = float('inf')
        V = max(nums) + 1
        best = INF
        for pat in range(2):        # pat = parity of the PEAK indices
            dp = [INF] * V
            for v in range(nums[0] + 1):
                dp[v] = nums[0] - v

            for i in range(1, n):
                nxt = [INF] * V
                run = INF
                if i % 2 == pat:            # nums[i] must beat nums[i-1]
                    for v in range(V):
                        if v > 0 and dp[v - 1] < run:
                            run = dp[v - 1]
                        if v <= nums[i] and run < INF:
                            nxt[v] = run + nums[i] - v
                else:                       # nums[i] must be below nums[i-1]
                    for v in range(V - 1, -1, -1):
                        if v + 1 < V and dp[v + 1] < run:
                            run = dp[v + 1]
                        if v <= nums[i] and run < INF:
                            nxt[v] = run + nums[i] - v
                dp = nxt

            best = min(best, min(dp))
        return best


# V0-2
# IDEA : BRUTE FORCE - ACTUALLY PERFORM THE MOVES, ONE DECREMENT AT A TIME
#
#   pick the parity that becomes the valleys, copy the array, then for every
#   valley keep subtracting 1 and counting the move until it is strictly
#   below both of its (never touched) neighbours.
#
#   no closed-form cost is used, the moves are simulated literally, which is
#   a handy way to sanity check the greedy formula of V0.
#
# time = O(n * max(nums))
# space = O(n) for the working copy
class Solution(object):
    def movesToMakeZigzag(self, nums):
        n = len(nums)
        res = float('inf')
        for start in range(2):          # start = parity of the VALLEYS
            arr = list(nums)
            moves = 0
            for j in range(start, n, 2):
                while True:
                    left_ok = (j - 1 < 0) or (arr[j] < arr[j - 1])
                    right_ok = (j + 1 >= n) or (arr[j] < arr[j + 1])
                    if left_ok and right_ok:
                        break
                    arr[j] -= 1
                    moves += 1
            res = min(res, moves)
        return res
