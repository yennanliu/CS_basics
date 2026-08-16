"""

3655. XOR After Range Multiplication Queries II
Hard

You are given an integer array nums of length n and a 2D integer array
queries of size q, where queries[i] = [li, ri, ki, vi].

For each query, you must apply the following operations in order:

Set idx = li.
While idx <= ri:
  Update: nums[idx] = (nums[idx] * vi) % (10^9 + 7).
  Set idx += ki.

Return the bitwise XOR of all elements in nums after processing all
queries.


Example 1:

Input: nums = [1,1,1], queries = [[0,2,1,4]]
Output: 4
Explanation:
A single query [0, 2, 1, 4] multiplies every element from index 0 through
index 2 by 4.
The array changes from [1, 1, 1] to [4, 4, 4].
The XOR of all elements is 4 ^ 4 ^ 4 = 4.

Example 2:

Input: nums = [2,3,1,5,4], queries = [[1,4,2,3],[0,2,1,2]]
Output: 31
Explanation:
The first query [1, 4, 2, 3] multiplies the elements at indices 1 and 3 by
3, transforming the array to [2, 9, 1, 15, 4].
The second query [0, 2, 1, 2] multiplies the elements at indices 0, 1, and
2 by 2, resulting in [4, 18, 2, 15, 4].
Finally, the XOR of all elements is 4 ^ 18 ^ 2 ^ 15 ^ 4 = 31.


Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= q == queries.length <= 10^5
queries[i] = [li, ri, ki, vi]
0 <= li <= ri < n
1 <= ki <= n
1 <= vi <= 10^5

"""

# V0
# IDEA : HEAVY/LIGHT SPLIT ON THE STRIDE, WITH A MULTIPLICATIVE DIFFERENCE
#        ARRAY PER RESIDUE CLASS
#
#   a query touches ceil((r - l + 1) / k) cells, so a LARGE stride is cheap
#   to replay literally -- at most n / k cells. the pain is a small stride,
#   where one query can hit the whole array; that is the classic signal for
#   sqrt decomposition on k, with the cut at k ~ sqrt(n).
#
#   for a small stride, look at the indices l, l+k, l+2k, ... as their own
#   little array indexed by t = (idx - l) / k. the query is then a
#   contiguous RANGE multiply on that little array, and the standard prefix
#   trick applies -- except multiplicatively: post a factor v at the first
#   position and its modular inverse just past the last, then one prefix
#   PRODUCT sweep down the residue class replays every query at once. the
#   inverse exists because the modulus is prime and v is never a multiple of
#   it, and pow(v, MOD-2, MOD) computes it.
#
#   grouping by (k, residue) keeps the sweeps disjoint: each residue class
#   is swept once, starting only at its earliest event and stopping as soon
#   as the running factor is back to 1 with no events left.
#
# time = O((q + n) * sqrt(n)), space = O(n + q)
class Solution(object):
    def xorAfterQueries(self, nums, queries):
        MOD = 10 ** 9 + 7
        n = len(nums)
        arr = list(nums)
        B = int(n ** 0.5) + 1

        events = {}
        for l, r, k, v in queries:
            if k > B:
                for idx in range(l, r + 1, k):
                    arr[idx] = arr[idx] * v % MOD
                continue
            res = l % k
            t1 = (l - res) // k
            t2 = (r - res) // k
            key = k * (B + 1) + res
            ev = events.get(key)
            if ev is None:
                ev = events[key] = []
            ev.append((t1, v))
            if t2 + 1 <= (n - 1 - res) // k:
                ev.append((t2 + 1, pow(v, MOD - 2, MOD)))

        for key, ev in events.items():
            k, res = divmod(key, B + 1)
            ev.sort()
            p = 0
            cur = 1
            t = ev[0][0]
            idx = res + t * k
            size = len(ev)
            while idx < n:
                while p < size and ev[p][0] == t:
                    cur = cur * ev[p][1] % MOD
                    p += 1
                if cur != 1:
                    arr[idx] = arr[idx] * cur % MOD
                elif p >= size:
                    break
                idx += k
                t += 1

        out = 0
        for x in arr:
            out ^= x
        return out
