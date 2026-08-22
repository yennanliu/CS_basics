"""

3224. Minimum Array Changes to Make Differences Equal
Medium

You are given an integer array nums of size n where n is even, and an integer k.

You can perform some changes on the array, where in one change you can replace any element in the array with any integer in the range from 0 to k.

You need to perform some changes (possibly none) such that the final array satisfies the following condition:

There exists an integer X such that abs(a[i] - a[n - i - 1]) = X for all (0 <= i < n).

Return the minimum number of changes required to satisfy the above condition.


Example 1:

Input: nums = [1,0,1,2,4,3], k = 4
Output: 2
Explanation:
We can perform the following changes:
Replace nums[1] by 2. The resulting array is nums = [1,2,1,2,4,3].
Replace nums[3] by 3. The resulting array is nums = [1,2,1,3,4,3].
The integer X will be 2.

Example 2:

Input: nums = [0,1,2,3,3,6,5,4], k = 6
Output: 2
Explanation:
We can perform the following operations:
Replace nums[3] by 0. The resulting array is nums = [0,1,2,0,3,6,5,4].
Replace nums[4] by 4. The resulting array is nums = [0,1,2,0,4,6,5,4].
The integer X will be 4.


Constraints:

2 <= n == nums.length <= 10^5
n is even.
0 <= nums[i] <= k <= 10^5

"""

# V0
# IDEA : SCORE EVERY CANDIDATE X AT ONCE WITH A DIFFERENCE ARRAY
#
#   the pairs (nums[i], nums[n-1-i]) are independent, and for a target gap X
#   a pair costs
#       0  if its current gap already equals X
#       1  if ONE element can be rewritten to reach X
#       2  otherwise
#
#   changing a single element of the pair (a, b) can produce any gap up to
#       reach = max(max(a, b), k - min(a, b))
#   — push the larger one up to k, or drag the smaller one down to 0. so a
#   pair costs at most 1 for every X <= reach, and 2 beyond that.
#
#   so tally, over all pairs, how many have each exact gap (cnt) and how many
#   have each reach (a suffix count via a difference array). then for each X
#
#       cost(X) = (reachable - cnt[X]) * 1 + (pairs - reachable) * 2
#
#   and the answer is the smallest of them.
#
# time = O(n + k), space = O(k)
class Solution(object):
    def minChanges(self, nums, k):
        n = len(nums)
        pairs = n // 2
        cnt = [0] * (k + 2)              # pairs whose gap is exactly X
        reach_at_least = [0] * (k + 2)   # difference array over reach

        for i in range(pairs):
            a, b = nums[i], nums[n - 1 - i]
            gap = abs(a - b)
            reach = max(max(a, b), k - min(a, b))
            cnt[gap] += 1
            reach_at_least[0] += 1
            reach_at_least[reach + 1] -= 1

        best = float('inf')
        running = 0
        for X in range(k + 1):
            running += reach_at_least[X]
            cost = (running - cnt[X]) + 2 * (pairs - running)
            if cost < best:
                best = cost
        return best


# V0-1
# IDEA : BRUTE FORCE — TRY EVERY X, PRICE EVERY PAIR
#
#   the baseline the counting solution compresses. for a fixed target gap X,
#   walk all n/2 pairs and charge each one directly :
#       0  if abs(a - b) == X
#       1  if X <= max(max(a, b), k - min(a, b))   (one element can be moved)
#       2  otherwise                               (both must be rewritten)
#
#   correct and easy to trust, but re-reads every pair for each of the k + 1
#   candidates, so it only survives on small inputs.
#
# time = O(n * k)
# space = O(n)
class Solution(object):
    def minChanges(self, nums, k):
        n = len(nums)
        pairs = [(abs(nums[i] - nums[n - 1 - i]),
                  max(max(nums[i], nums[n - 1 - i]),
                      k - min(nums[i], nums[n - 1 - i])))
                 for i in range(n // 2)]

        best = float('inf')
        for X in range(k + 1):
            cost = 0
            for gap, reach in pairs:
                if gap == X:
                    continue
                cost += 1 if X <= reach else 2
            best = min(best, cost)
        return best


# V0-2
# IDEA : SORT THE REACHES + SWEEP X WITH A POINTER
#
#   same cost formula as V0, but the "how many pairs can still be fixed with a
#   single change at target X" question is answered by sorting the reaches
#   instead of by a difference array : as X grows the pointer only moves
#   forward over the reaches that have fallen below X.
#
#       dead     = pairs whose reach < X          -> cost 2 each
#       alive    = pairs - dead                   -> cost 1 each,
#                  except the cnt[X] pairs already at gap X, which cost 0
#
#   trades the O(k) difference array for an O(n log n) sort + O(n) counter,
#   which is the better shape when k is much larger than n.
#
# time = O(n log n + k)
# space = O(n)
import collections
class Solution(object):
    def minChanges(self, nums, k):
        n = len(nums)
        gaps = collections.Counter()
        reaches = []
        for i in range(n // 2):
            a, b = nums[i], nums[n - 1 - i]
            gaps[abs(a - b)] += 1
            reaches.append(max(max(a, b), k - min(a, b)))
        reaches.sort()

        total = n // 2
        best = float('inf')
        dead = 0
        for X in range(k + 1):
            while dead < total and reaches[dead] < X:
                dead += 1
            alive = total - dead
            cost = (alive - gaps[X]) + 2 * dead
            if cost < best:
                best = cost
        return best
