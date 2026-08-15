"""

3013. Divide an Array Into Subarrays With Minimum Cost II
Hard

You are given a 0-indexed array of integers nums of length n, and two positive integers k and dist.

The cost of an array is the value of its first element. For example, the cost of [1,2,3] is 1 while the cost of [3,4,1] is 3.

You need to divide nums into k disjoint contiguous subarrays, such that the difference between the starting index of the second subarray and the starting index of the kth subarray should be less than or equal to dist. In other words, if you divide nums into the subarrays nums[0..(i1 - 1)], nums[i1..(i2 - 1)], ..., nums[ik-1..(n - 1)], then ik-1 - i1 <= dist.

Return the minimum possible sum of the cost of these subarrays.


Example 1:

Input: nums = [1,3,2,6,4,2], k = 3, dist = 3
Output: 5
Explanation: The best possible way to divide nums into 3 subarrays is: [1,3], [2], and [6,4,2]. This choice is valid because ik-1 - i1 equals 3 - 2 which is equal to dist. The total cost is nums[0] + nums[2] + nums[3] which is 1 + 2 + 2 = 5.
It can be shown that there is no possible way to divide nums into 3 subarrays at a cost lower than 5.

Example 2:

Input: nums = [10,1,2,2,2,1], k = 4, dist = 3
Output: 15
Explanation: The best possible way to divide nums into 4 subarrays is: [10], [1], [2], and [2,2,1]. This choice is valid because ik-1 - i1 equals 3 - 1 which is less than dist. The total cost is nums[0] + nums[1] + nums[2] + nums[3] which is 10 + 1 + 2 + 2 = 15.
The division [10], [1], [2,2,2], and [1] is not valid, because the difference between ik-1 and i1 is 5 - 1 = 4, which is greater than dist.
It can be shown that there is no possible way to divide nums into 4 subarrays at a cost lower than 15.

Example 3:

Input: nums = [10,8,18,9], k = 3, dist = 1
Output: 36
Explanation: The best possible way to divide nums into 4 subarrays is: [10], [8], and [18,9]. This choice is valid because ik-1 - i1 equals 2 - 1 which is equal to dist. The total cost is nums[0] + nums[1] + nums[2] which is 10 + 8 + 18 = 36.
The division [10], [8,18], and [9] is not valid, because the difference between ik-1 and i1 is 3 - 1 = 2, which is greater than dist.
It can be shown that there is no possible way to divide nums into 3 subarrays at a cost lower than 36.


Constraints:

3 <= n <= 10^5
1 <= nums[i] <= 10^9
3 <= k <= n
k - 2 <= dist <= n - 2

"""

# V0
# IDEA : SLIDING WINDOW OF WIDTH dist+1, KEEPING THE k-1 SMALLEST STARTS
#
#   nums[0] is always paid. the other k-1 subarray starts are distinct
#   indices in 1..n-1 whose spread is at most dist — i.e. they all fit in
#   some window of dist+1 consecutive indices. so
#
#       answer = nums[0] + min over windows of (sum of the k-1 smallest)
#
#   any window works : if the chosen starts sit inside [l, l+dist], the
#   window anchored at their own minimum contains them too.
#
#   maintaining "sum of the k-1 smallest" under insert AND delete is the real
#   work. two heaps straddle the cut :
#       small : max-heap holding exactly the k-1 smallest, with their sum
#       large : min-heap holding everything else
#   entries carry their INDEX, so leaving the window is a lazy deletion —
#   mark the index dead and skip it when it surfaces at a heap top.
#
#   after every insert/delete, rebalance : refill or trim `small` to k-1
#   elements, then swap while its max exceeds `large`'s min.
#
# time = O(n log n), space = O(n)
import heapq


class Solution(object):
    def minimumCost(self, nums, k, dist):
        n = len(nums)
        need = k - 1

        small = []                    # max-heap: (-value, index)
        large = []                    # min-heap: (value, index)
        alive = [False] * n
        in_small = [False] * n
        state = {'sum': 0, 'ns': 0, 'nl': 0}

        def prune_small():
            while small and not (alive[small[0][1]] and in_small[small[0][1]]):
                heapq.heappop(small)

        def prune_large():
            while large and not (alive[large[0][1]] and not in_small[large[0][1]]):
                heapq.heappop(large)

        def rebalance():
            # top up `small` until it holds k-1 elements
            while state['ns'] < need and state['nl'] > 0:
                prune_large()
                v, idx = heapq.heappop(large)
                state['nl'] -= 1
                in_small[idx] = True
                state['sum'] += v
                state['ns'] += 1
                heapq.heappush(small, (-v, idx))
            # trim any excess back into `large`
            while state['ns'] > need:
                prune_small()
                nv, idx = heapq.heappop(small)
                v = -nv
                state['ns'] -= 1
                state['sum'] -= v
                in_small[idx] = False
                state['nl'] += 1
                heapq.heappush(large, (v, idx))
            # enforce max(small) <= min(large)
            while True:
                prune_small()
                prune_large()
                if not small or not large or -small[0][0] <= large[0][0]:
                    break
                nv, i_s = heapq.heappop(small)
                v_s = -nv
                v_l, i_l = heapq.heappop(large)
                state['sum'] += v_l - v_s
                in_small[i_s] = False
                in_small[i_l] = True
                heapq.heappush(large, (v_s, i_s))
                heapq.heappush(small, (-v_l, i_l))

        def add(idx):
            alive[idx] = True
            in_small[idx] = False
            state['nl'] += 1
            heapq.heappush(large, (nums[idx], idx))
            rebalance()

        def remove(idx):
            alive[idx] = False
            if in_small[idx]:
                state['sum'] -= nums[idx]
                state['ns'] -= 1
            else:
                state['nl'] -= 1
            rebalance()

        res = float('inf')
        for r in range(1, n):
            add(r)
            l = max(1, r - dist)
            if l - 1 >= 1 and alive[l - 1]:
                remove(l - 1)
            if state['ns'] == need:
                res = min(res, state['sum'])
        return nums[0] + res
