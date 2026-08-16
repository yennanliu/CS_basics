"""

3645. Maximum Total from Optimal Activation Order
Medium

You are given two integer arrays value and limit, both of length n.

Initially, all elements are inactive. You may activate them in any order.

To activate an inactive element at index i, the number of currently active
elements must be strictly less than limit[i].
When you activate the element at index i, it adds value[i] to the total
activation value (i.e., the sum of value[i] for all elements that have
undergone activation operations).
After each activation, if the number of currently active elements becomes
x, then all elements j with limit[j] <= x become permanently inactive, even
if they are already active.

Return the maximum total you can obtain by choosing the activation order
optimally.


Example 1:

Input: value = [3,5,8], limit = [2,1,3]
Output: 16
Explanation:
One optimal activation order is:
Step 1: activate i = 1 (value 5). Active count goes 0 -> 1, so j = 1 dies
        because limit[1] = 1. Total = 5.
Step 2: activate i = 0 (value 3). Active count goes 0 -> 1, nothing new
        dies. Total = 8.
Step 3: activate i = 2 (value 8). Active count goes 1 -> 2, so j = 0 dies
        because limit[0] = 2. Total = 16.
Thus, the maximum possible total is 16.

Example 2:

Input: value = [4,2,6], limit = [1,1,1]
Output: 6
Explanation:
One optimal activation order is:
Step 1: activate i = 2 (value 6). Active count goes 0 -> 1, so j = 0, 1, 2
        all die because limit[j] = 1. Total = 6.
Thus, the maximum possible total is 6.

Example 3:

Input: value = [4,1,5,2], limit = [3,3,2,3]
Output: 12
Explanation:
One optimal activation order is:
Step 1: activate i = 2 (value 5). Active 0 -> 1, nothing dies. Total = 5.
Step 2: activate i = 0 (value 4). Active 1 -> 2, so j = 2 dies because
        limit[2] = 2. Total = 9.
Step 3: activate i = 1 (value 1). Active 1 -> 2, nothing dies. Total = 10.
Step 4: activate i = 3 (value 2). Active 2 -> 3, so j = 0, 1, 3 die
        because limit[j] = 3. Total = 12.
Thus, the maximum possible total is 12.


Constraints:

1 <= n == value.length == limit.length <= 10^5
1 <= value[i] <= 10^5
1 <= limit[i] <= n

"""

# V0
# IDEA : AT MOST L ELEMENTS OF limit == L CAN EVER BE ACTIVATED
#
#   track H, the high-water mark of the active count. everything with
#   limit <= H is dead forever, so H only ever grows, and it grows by
#   exactly 1 at a time (the count itself only ever moves up by one per
#   activation). call "phase h" the stretch of time while H == h; every
#   element with limit == L must be activated during phases 0..L-1.
#
#   how many activations fit in phase h? when H first reached h the count
#   was h, then every activated element with limit == h died at once, so the
#   count dropped to h - a_h, writing a_h for how many elements of limit
#   exactly h we chose to activate. from there each activation adds one, and
#   the count crossing h is what ends the phase -- so phase h holds exactly
#   a_h + 1 activations.
#
#   summing, phases 0..L-1 supply L + a_1 + ... + a_{L-1} slots, while the
#   elements that must fit in them number a_1 + ... + a_L. the demand fits
#   iff a_L <= L, and that is one independent cap per limit value: nothing
#   couples different L's. (the slot sets are prefixes 0..L-1, so hall's
#   condition collapses to exactly these prefix inequalities.)
#
#   with the feasible region being "pick at most min(cnt_L, L) elements of
#   each limit L", and all values positive, the greedy is immediate: bucket
#   by limit and keep the L largest values in each bucket.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxTotal(self, value, limit):
        buckets = {}
        for v, lim in zip(value, limit):
            buckets.setdefault(lim, []).append(v)
        total = 0
        for lim, vals in buckets.items():
            vals.sort()
            total += sum(vals[-lim:])
        return total
