"""

2202. Maximize the Topmost Element After K Moves
Medium

You are given a 0-indexed integer array nums representing the contents of a pile, where nums[0] is the topmost element of the pile.

In one move, you can perform either of the following:

If the pile is not empty, remove the topmost element of the pile.
If there are one or more removed elements, add any one of them back onto the pile. This element becomes the new topmost element.

You are also given an integer k, which denotes the total number of moves to be made.

Return the maximum value of the topmost element of the pile possible after exactly k moves. In case it is not possible to obtain a non-empty pile after k moves, return -1.


Example 1:

Input: nums = [5,2,2,4,0,6], k = 4
Output: 5
Explanation:
One of the ways we can end with 5 at the top of the pile after 4 moves is as follows:
- Step 1: Remove the topmost element = 5. The pile becomes [2,2,4,0,6].
- Step 2: Remove the topmost element = 2. The pile becomes [2,4,0,6].
- Step 3: Remove the topmost element = 2. The pile becomes [4,0,6].
- Step 4: Add 5 back onto the pile. The pile becomes [5,4,0,6].
Note that this is not the only way to end with 5 at the top of the pile. It can be shown that 5 is the largest answer possible after 4 moves.

Example 2:

Input: nums = [2], k = 1
Output: -1
Explanation:
In the first move, our only option is to pop the topmost element of the pile.
Since it is not possible to obtain a non-empty pile after one move, we return -1.


Constraints:

1 <= n == nums.length <= 10^5
0 <= nums[i], k <= 10^9

"""

# V0
# IDEA : CASE ANALYSIS ON WHAT k MOVES CAN REACH
#
#   after exactly k moves the top can only be :
#     * some nums[i] with i <= k - 2 — pop the first i+1 elements, waste any
#       leftover moves popping/pushing, then push nums[i] back last. so the
#       best of nums[0 .. k-2] is available.
#     * nums[k] — pop the first k elements, which leaves nums[k] on top
#       (needs k < n).
#
#   nums[k-1] is NOT reachable : popping k-1 elements leaves a move over, and
#   spending it either pops nums[k-1] away or pushes something else on top.
#
#   special cases :
#     k == 0 -> nothing changes, answer nums[0]
#     k == 1 -> only nums[1] (if it exists), else -1
#     k >= n and n == 1 with odd/even parity is covered by the general rule,
#     since nums[0..k-2] is non-empty whenever k >= 2.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumTop(self, nums, k):
        n = len(nums)
        if k == 0:
            return nums[0]
        if n == 1:
            # only reachable state alternates between [nums[0]] and empty
            return nums[0] if k % 2 == 0 else -1
        if k == 1:
            return nums[1]

        best = max(nums[:min(k - 1, n)])      # nums[0 .. k-2], clipped to n
        if k < n:
            best = max(best, nums[k])
        return best


# V0-1
# IDEA : REACHABILITY PREDICATE + SORT THE (VALUE, INDEX) PAIRS
#
#   which index can end up on top does not depend on the values at all, so
#   turn the problem into "pick the largest value sitting on a reachable
#   index". for k >= 2 on a pile of 2+ elements index i is reachable iff
#     i <= k - 2   (pop the prefix, burn the spare moves, push nums[i] last)
#     or i == k    (pop exactly k elements and stop)
#   so sort the pairs by value descending and return the first one whose
#   index passes the test - no case analysis on where the maximum lives.
#   the degenerate k = 0 / k = 1 / single-element piles are still special.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maximumTop(self, nums, k):
        n = len(nums)
        if k == 0:
            return nums[0]
        if n == 1:
            # the pile just flips between [nums[0]] and empty
            return nums[0] if k % 2 == 0 else -1
        if k == 1:
            return nums[1]

        for v, i in sorted(((v, i) for i, v in enumerate(nums)), reverse=True):
            if i <= k - 2 or i == k:
                return v
        return -1


# V0-2
# IDEA : MAX-HEAP, POP UNTIL A REACHABLE INDEX SURFACES
#
#   same reachability test as V0-1, but the candidates are ordered lazily : a
#   heap of (-value, index) is built in O(n) and only popped until the top is
#   a usable index. at most (n - number of reachable indices) + 1 pops ever
#   happen, so when a large value already sits in the reachable prefix - the
#   common case, since the prefix is nums[0 .. k-2] - the sort of V0-1 is
#   never paid for.
#
# time = O(n) to heapify + O(t log n) for the t pops actually taken
# space = O(n)
class Solution(object):
    def maximumTop(self, nums, k):
        import heapq

        n = len(nums)
        if k == 0:
            return nums[0]
        if n == 1:
            return nums[0] if k % 2 == 0 else -1
        if k == 1:
            return nums[1]

        heap = [(-v, i) for i, v in enumerate(nums)]
        heapq.heapify(heap)
        while heap:
            v, i = heapq.heappop(heap)
            if i <= k - 2 or i == k:
                return -v
        return -1
