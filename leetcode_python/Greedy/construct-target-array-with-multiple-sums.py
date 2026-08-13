"""

1354. Construct Target Array With Multiple Sums
Hard

You are given an array target of n integers. From a starting array arr consisting of n 1's, you may perform the following procedure :

let x be the sum of all elements currently in your array.
choose index i, such that 0 <= i < n and set the value of arr at index i to x.
You may repeat this procedure as many times as needed.

Return true if it is possible to construct the target array from arr, otherwise, return false.


Example 1:

Input: target = [9,3,5]
Output: true
Explanation: Start with arr = [1, 1, 1]
[1, 1, 1], sum = 3 choose index 1
[1, 3, 1], sum = 5 choose index 2
[1, 3, 5], sum = 9 choose index 0
[9, 3, 5] Done

Example 2:

Input: target = [1,1,1,2]
Output: false
Explanation: Impossible to create target array from [1,1,1,1].

Example 3:

Input: target = [8,5]
Output: true


Constraints:

n == target.length
1 <= n <= 5 * 10^4
1 <= target[i] <= 10^9

"""

# V0
# IDEA : REVERSE SIMULATION + MAX HEAP
#
#  - Going forward is ambiguous (which index do we overwrite?),
#    but going BACKWARD is forced: the largest element must have been
#    the one just written, since it equals the sum of the whole array
#    at that moment (and every other element is smaller).
#
#  - So repeatedly pop the max `mx`, let `t` = sum of the rest.
#    Its previous value was `mx - t`. Doing that one step at a time is
#    too slow (e.g. [1, 10^9]), so we jump with a modulo:
#    prev = mx % t  (and if that is 0, prev = t, meaning t == 1 case /
#    exact division -> the last legal value before dropping below 1).
#
#  - Fail fast when t == 0 (only one element left and it is > 1) or
#    when mx - t < 1 (previous value would be non-positive).
#
# NOTE: python heapq is a MIN heap -> push negated values.
# time = O(n log n log m)
# space = O(n)
# n = len(target), m = max(target)
import heapq


class Solution(object):
    def isPossible(self, target):
        s = sum(target)
        pq = [-x for x in target]
        heapq.heapify(pq)

        while -pq[0] > 1:
            mx = -heapq.heappop(pq)
            t = s - mx
            # only one element left and it is not 1 -> impossible
            # or the previous value would be <= 0 -> impossible
            if t == 0 or mx - t < 1:
                return False
            x = (mx % t) or t
            heapq.heappush(pq, -x)
            s = s - mx + x

        return True
