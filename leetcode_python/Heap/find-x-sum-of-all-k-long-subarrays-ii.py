"""

3321. Find X-Sum of All K-Long Subarrays II
Hard

You are given an array nums of n integers and two integers k and x.

The x-sum of an array is calculated by the following procedure:

Count the occurrences of all elements in the array.
Keep only the occurrences of the top x most frequent elements. If two elements have the same number of occurrences, the element with the bigger value is considered more frequent.
Calculate the sum of the resulting array.

Note that if an array has less than x distinct elements, its x-sum is the sum of the array.

Return an integer array answer of length n - k + 1 where answer[i] is the x-sum of the subarray nums[i..i + k - 1].


Example 1:

Input: nums = [1,1,2,2,3,4,2,3], k = 6, x = 2
Output: [6,10,12]
Explanation:
For subarray [1, 1, 2, 2, 3, 4], only elements 1 and 2 will be kept in the resulting array. Hence, answer[0] = 1 + 1 + 2 + 2.
For subarray [1, 2, 2, 3, 4, 2], only elements 2 and 4 will be kept in the resulting array. Hence, answer[1] = 2 + 2 + 2 + 4. Note that 4 is kept in the array since it is bigger than 3 and 1 which occur the same number of times.
For subarray [2, 2, 3, 4, 2, 3], only elements 2 and 3 are kept in the resulting array. Hence, answer[2] = 2 + 2 + 2 + 3 + 3.

Example 2:

Input: nums = [3,8,7,8,7,5], k = 2, x = 2
Output: [11,15,15,15,12]
Explanation:
Since k == x, answer[i] is equal to the sum of the subarray nums[i..i + k - 1].


Constraints:

nums.length == n
1 <= n <= 10^5
1 <= nums[i] <= 10^9
1 <= x <= k <= nums.length

"""

# V0
# IDEA : TWO HEAPS STRADDLING THE "TOP x" CUT, WITH LAZY DELETION
#
#   sliding the window changes exactly two counts, so the x-sum should be
#   maintained rather than recomputed. the ranking key is (count, value) —
#   more occurrences first, bigger value breaking ties — so keep
#
#       top  : the x best values, as a MIN-heap on that key
#              -> its root is the weakest member, the one that can be evicted
#       rest : everybody else, as a MAX-heap on the key
#              -> its root is the strongest challenger
#
#   a value whose count changes cannot be repositioned inside a heap, so its
#   old entry is left behind and skipped later : an entry is stale unless its
#   recorded count still matches the live count AND its membership flag still
#   agrees. that is what `prune` checks.
#
#   after each count change the two heaps are rebalanced so `top` holds
#   exactly min(x, distinct) values and no member of `rest` outranks a member
#   of `top`; `sum_top` is updated on every crossing.
#
# time = O(n log n), space = O(n)
import heapq


class Solution(object):
    def findXSum(self, nums, k, x):
        cnt = {}
        in_top = {}
        top = []          # min-heap of (count, value)
        rest = []         # max-heap of (-count, -value)
        state = {'sum': 0, 'ntop': 0}

        def prune_top():
            while top:
                c, v = top[0]
                if cnt.get(v, 0) == c and in_top.get(v):
                    return
                heapq.heappop(top)

        def prune_rest():
            while rest:
                nc, nv = rest[0]
                c, v = -nc, -nv
                if cnt.get(v, 0) == c and not in_top.get(v):
                    return
                heapq.heappop(rest)

        def push_top(v):
            in_top[v] = True
            state['sum'] += v * cnt[v]
            state['ntop'] += 1
            heapq.heappush(top, (cnt[v], v))

        def push_rest(v):
            in_top[v] = False
            heapq.heappush(rest, (-cnt[v], -v))

        def rebalance():
            while True:
                prune_top()
                prune_rest()
                if state['ntop'] < x and rest:
                    nc, nv = heapq.heappop(rest)
                    push_top(-nv)
                    continue
                if state['ntop'] > x:
                    c, v = heapq.heappop(top)
                    in_top[v] = False
                    state['sum'] -= v * cnt[v]
                    state['ntop'] -= 1
                    push_rest(v)
                    continue
                if top and rest and (-rest[0][0], -rest[0][1]) > top[0]:
                    c, v = heapq.heappop(top)
                    nc, nv = heapq.heappop(rest)
                    in_top[v] = False
                    state['sum'] -= v * cnt[v]
                    push_rest(v)
                    state['ntop'] -= 1
                    push_top(-nv)
                    continue
                break

        def change(v, delta):
            old = cnt.get(v, 0)
            if in_top.get(v) and old:
                state['sum'] -= v * old
                state['ntop'] -= 1
            in_top[v] = False
            new = old + delta
            if new:
                cnt[v] = new
                push_rest(v)
            else:
                cnt.pop(v, None)
            rebalance()

        res = []
        for i, v in enumerate(nums):
            change(v, 1)
            if i >= k:
                change(nums[i - k], -1)
            if i >= k - 1:
                res.append(state['sum'])
        return res
