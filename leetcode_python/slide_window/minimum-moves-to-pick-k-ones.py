"""

3086. Minimum Moves to Pick K Ones
Hard

You are given a binary array nums of length n, a positive integer k and a non-negative integer maxChanges.

Alice plays a game, where the goal is for Alice to pick up k ones from nums using the minimum number of moves. When the game starts, Alice picks up any index aliceIndex in the range [0, n - 1] and stands there. If nums[aliceIndex] == 1 , Alice picks up the one and nums[aliceIndex] becomes 0 (this does not count as a move). After this, Alice can make any number of moves (including zero) where in each move Alice must perform exactly one of the following actions:

Select any index j != aliceIndex such that nums[j] == 0 and set nums[j] = 1. This action can be performed at most maxChanges times.
Select any two adjacent indices x and y (|x - y| == 1) such that nums[x] == 1, nums[y] == 0, then swap their values (set nums[y] = 1 and nums[x] = 0). If y == aliceIndex, Alice picks up the one after this move and nums[y] becomes 0.

Return the minimum number of moves required by Alice to pick exactly k ones.


Example 1:

Input: nums = [1,1,0,0,0,1,1,0,0,1], k = 3, maxChanges = 1
Output: 3
Explanation: Alice can pick up 3 ones in 3 moves, if Alice performs the following actions in each move when standing at aliceIndex == 1:
At the start Alice picks up the one and nums[1] becomes 0. nums becomes [1,0,0,0,0,1,1,0,0,1].
Select j == 2 and perform an action of the first type. nums becomes [1,0,1,0,0,1,1,0,0,1]
Select x == 2 and y == 1, and perform an action of the second type. nums becomes [1,1,0,0,0,1,1,0,0,1]. As y == aliceIndex, Alice picks up the one and nums becomes [1,0,0,0,0,1,1,0,0,1].
Select x == 0 and y == 1, and perform an action of the second type. nums becomes [0,1,0,0,0,1,1,0,0,1]. As y == aliceIndex, Alice picks up the one and nums becomes [0,0,0,0,0,1,1,0,0,1].
Note that it may be possible for Alice to pick up 3 ones using some other sequence of 3 moves.

Example 2:

Input: nums = [0,0,0,0], k = 2, maxChanges = 3
Output: 4
Explanation: Alice can pick up 2 ones in 4 moves, if Alice performs the following actions in each move when standing at aliceIndex == 0:
Select j == 1 and perform an action of the first type. nums becomes [0,1,0,0].
Select x == 1 and y == 0, and perform an action of the second type. nums becomes [1,0,0,0]. As y == aliceIndex, Alice picks up the one and nums becomes [0,0,0,0].
Select j == 1 again and perform an action of the first type. nums becomes [0,1,0,0].
Select x == 1 and y == 0, and perform an action of the second type. nums becomes [1,0,0,0]. As y == aliceIndex, Alice picks up the one and nums becomes [0,0,0,0].


Constraints:

2 <= n <= 10^5
0 <= nums[i] <= 1
1 <= k <= 10^5
0 <= maxChanges <= 10^5
maxChanges + sum(nums) >= k

"""

# V0
# IDEA : PRICE EVERY ONE, THEN NOTICE ONLY ~4 SPLITS BETWEEN REAL/CREATED MATTER
#
#   costs, from Alice's standing index :
#       a real one AT her index      -> 0 moves (free pickup)
#       a real one at distance d     -> d moves (swap it along, one per step)
#       a created one (maxChanges)   -> 2 moves (set it adjacent, swap it in)
#
#   so if she collects r real ones and creates x = k - r, the total is
#       2 * x  +  (sum of distances from her index to those r ones)
#   and for a fixed r the best real ones are a CONTIGUOUS block of ones with
#   Alice standing at its median — the classic minimum-sum-of-distances
#   arrangement, computed for every window with prefix sums.
#
#   the search over r collapses : a created one costs 2, so it matches or
#   beats any real one at distance >= 2. only three real ones can be cheaper
#   than that — the one under her feet (0) and its two neighbours (1 each) —
#   so beyond max(0, k - maxChanges) real ones there is no point taking more
#   than 3 extra. that leaves at most four values of r to try.
#
# time = O(n), space = O(n)
class Solution(object):
    def minimumMoves(self, nums, k, maxChanges):
        pos = [i for i, v in enumerate(nums) if v == 1]
        m = len(pos)

        pre = [0] * (m + 1)
        for i, p in enumerate(pos):
            pre[i + 1] = pre[i] + p

        def window_cost(r):
            """min total distance to gather r ones, standing at their median."""
            if r == 0:
                return 0
            best = float('inf')
            for lo in range(m - r + 1):
                hi = lo + r - 1
                mid = lo + r // 2
                med = pos[mid]
                # right part minus left part, each measured against the median
                right = (pre[hi + 1] - pre[mid]) - med * (hi - mid + 1)
                left = med * (mid - lo) - (pre[mid] - pre[lo])
                best = min(best, left + right)
            return best

        base = max(0, k - maxChanges)          # real ones we are forced to take
        res = float('inf')
        for r in range(base, min(m, base + 3) + 1):
            if r > k:
                break
            res = min(res, 2 * (k - r) + window_cost(r))
        return res
