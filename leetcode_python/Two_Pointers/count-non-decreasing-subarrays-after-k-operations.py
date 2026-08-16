"""

3420. Count Non-Decreasing Subarrays After K Operations
Hard

You are given an array nums of n integers and an integer k.

For each subarray of nums, you can apply up to k operations on it. In each
operation, you increment any element of the subarray by 1.

Note that each subarray is considered independently, meaning changes made to one
subarray do not persist to another.

Return the number of subarrays that you can make non-decreasing ​​​​​after
performing at most k operations.

An array is said to be non-decreasing if each element is greater than or equal
to its previous element, if it exists.

Example 1:

Input: nums = [6,3,1,2,4,4], k = 7

Output: 17

Explanation:

Out of all 21 possible subarrays of nums, only the subarrays [6, 3, 1], [6, 3,
1, 2], [6, 3, 1, 2, 4] and [6, 3, 1, 2, 4, 4] cannot be made non-decreasing
after applying up to k = 7 operations. Thus, the number of non-decreasing
subarrays is 21 - 4 = 17.

Example 2:

Input: nums = [6,3,1,3,6], k = 4

Output: 12

Explanation:

The subarray [3, 1, 3, 6] along with all subarrays of nums with three or fewer
elements, except [6, 3, 1], can be made non-decreasing after k operations. There
are 5 subarrays of a single element, 4 subarrays of two elements, and 2
subarrays of three elements except [6, 3, 1], so there are 1 + 5 + 4 + 2 = 12
subarrays that can be made non-decreasing.

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= k <= 10^9

"""

# V0
# IDEA : SLIDING WINDOW OVER A RUNNING-MAXIMUM STAIRCASE
#
#   only increments are allowed, so the cheapest non-decreasing version of a
#   subarray is its running maximum: b[i] must be >= a[i] and >= b[i-1], and
#   prefix-max is the pointwise smallest array meeting both.  hence
#
#     cost(l, r) = sum_{i=l..r} ( max(a[l..i]) - a[i] ).
#
#   dropping the leftmost element can only lower every prefix maximum, so
#   cost(l+1, r) <= cost(l, r) — the predicate cost <= k is monotone in l and a
#   two pointer counts, for each r, all the l that work.
#
#   the bookkeeping trouble is that shrinking from the left *reveals* structure:
#   while a[l] was in the window it masked all the smaller records behind it.
#   so keep the window's left-to-right maxima explicitly as a deque of record
#   positions with strictly increasing values, plus T = sum of max(a[l..i]).
#
#     - extending r appends a[r] as a new record if it beats the current one,
#       otherwise the last record's stretch simply grows.  T += window max.
#     - retiring l removes the first record and then replays the region it was
#       hiding, hopping along precomputed "next strictly greater" pointers.
#
#   a position can be unmasked at most once and retired at most once, so the
#   replay is O(n) in total and the whole sweep is linear.
#
# time = O(n), space = O(n)
from collections import deque


class Solution(object):
    def countNonDecreasingSubarrays(self, nums, k):
        n = len(nums)
        # nxt[i] = first j > i with nums[j] > nums[i], else n
        nxt = [n] * n
        st = []
        for i in range(n - 1, -1, -1):
            v = nums[i]
            while st and nums[st[-1]] <= v:
                st.pop()
            nxt[i] = st[-1] if st else n
            st.append(i)

        dq = deque()
        T = 0            # sum of max(nums[l..i]) for i in [l, r]
        S = 0            # sum of nums[l..r]
        l = 0
        ans = 0
        for r in range(n):
            v = nums[r]
            if dq and nums[dq[-1]] >= v:
                T += nums[dq[-1]]
            else:
                dq.append(r)
                T += v
            S += v

            while T - S > k:
                p1 = dq[0]
                end = dq[1] if len(dq) > 1 else r + 1
                T -= nums[p1] * (end - p1)
                S -= nums[p1]
                dq.popleft()
                l += 1
                # replay the records that p1 was masking
                q = l
                add = []
                while q < end:
                    nq = nxt[q]
                    if nq > end:
                        nq = end
                    add.append(q)
                    T += nums[q] * (nq - q)
                    q = nq
                for q in reversed(add):
                    dq.appendleft(q)
            ans += r - l + 1
        return ans
