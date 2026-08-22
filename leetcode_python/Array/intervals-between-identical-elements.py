"""

2121. Intervals Between Identical Elements
Medium

You are given a 0-indexed array of n integers arr.

The interval between two elements in arr is defined as the absolute difference between their indices. More formally, the interval between arr[i] and arr[j] is |i - j|.

Return an array intervals of length n where intervals[i] is the sum of intervals between arr[i] and each element in arr with the same value as arr[i].

Note: |x| is the absolute value of x.


Example 1:

Input: arr = [2,1,3,1,2,3,3]
Output: [4,2,7,2,4,4,5]
Explanation:
- Index 0: Another 2 is found at index 4. |0 - 4| = 4
- Index 1: Another 1 is found at index 3. |1 - 3| = 2
- Index 2: Two more 3s are found at indices 5 and 6. |2 - 5| + |2 - 6| = 7
- Index 3: Another 1 is found at index 1. |3 - 1| = 2
- Index 4: Another 2 is found at index 0. |4 - 0| = 4
- Index 5: Two more 3s are found at indices 2 and 6. |5 - 2| + |5 - 6| = 4
- Index 6: Two more 3s are found at indices 2 and 5. |6 - 2| + |6 - 5| = 5

Example 2:

Input: arr = [10,5,10,10]
Output: [5,0,3,4]
Explanation:
- Index 0: Two more 10s are found at indices 2 and 3. |0 - 2| + |0 - 3| = 5
- Index 1: There is only one 5 in the array, so its sum of intervals to identical elements is 0.
- Index 2: Two more 10s are found at indices 0 and 3. |2 - 0| + |2 - 3| = 3
- Index 3: Two more 10s are found at indices 0 and 2. |3 - 0| + |3 - 2| = 4


Constraints:

n == arr.length
1 <= n <= 10^5
1 <= arr[i] <= 10^5

"""

# V0
# IDEA : GROUP THE INDICES BY VALUE, THEN ROLL THE SUM ALONG EACH GROUP
#
#   inside one group idx[0] < idx[1] < ... < idx[m-1] :
#       total(0) = sum(idx) - m * idx[0]
#   and stepping from position t to t+1 changes it by a constant :
#       total(t+1) = total(t) + (t + 1) * gap - (m - t - 1) * gap
#   with gap = idx[t+1] - idx[t] : the t+1 elements on the left each get one
#   `gap` further away, the m-t-1 on the right each get one `gap` closer.
#
#   that turns the naive O(m^2) pairwise sum into a single O(m) sweep.
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def getDistances(self, arr):
        pos = defaultdict(list)
        for i, x in enumerate(arr):
            pos[x].append(i)

        res = [0] * len(arr)
        for idx in pos.values():
            m = len(idx)
            cur = sum(idx) - m * idx[0]
            res[idx[0]] = cur
            for t in range(m - 1):
                gap = idx[t + 1] - idx[t]
                cur += (t + 1) * gap - (m - t - 1) * gap
                res[idx[t + 1]] = cur
        return res


# V0-1
# IDEA : PREFIX SUMS INSIDE EACH VALUE GROUP
#
#   for one group of (already sorted) indices idx[0 .. m-1] and a position t :
#       left  part = sum_{j<t} (idx[t] - idx[j]) = t * idx[t] - pre[t]
#       right part = sum_{j>t} (idx[j] - idx[t]) = (tot - pre[t+1])
#                                                 - (m - 1 - t) * idx[t]
#   with pre[t] = idx[0] + ... + idx[t-1] and tot = pre[m].
#
#   so every answer is read off the prefix-sum table directly, instead of
#   being carried forward incrementally as in V0.
#
# time = O(n)
# space = O(n)
from collections import defaultdict


class Solution(object):
    def getDistances(self, arr):
        pos = defaultdict(list)
        for i, x in enumerate(arr):
            pos[x].append(i)

        res = [0] * len(arr)
        for idx in pos.values():
            m = len(idx)
            pre = [0] * (m + 1)
            for t in range(m):
                pre[t + 1] = pre[t] + idx[t]
            tot = pre[m]
            for t in range(m):
                left = t * idx[t] - pre[t]
                right = (tot - pre[t + 1]) - (m - 1 - t) * idx[t]
                res[idx[t]] = left + right
        return res


# V0-2
# IDEA : TWO STREAMING SWEEPS, ONLY (COUNT, SUM OF INDICES) PER VALUE
#
#   left -> right : every already seen index j of the same value contributes
#                   (i - j), i.e. cnt[v] * i - tot[v]
#   right -> left : every already seen index j of the same value contributes
#                   (j - i), i.e. tot[v] - cnt[v] * i
#
#   no per-value index list is ever materialised - two integers per distinct
#   value are enough, so this is the low-memory variant.
#
# time = O(n)
# space = O(u), u = number of distinct values
from collections import defaultdict


class Solution(object):
    def getDistances(self, arr):
        n = len(arr)
        res = [0] * n

        cnt = defaultdict(int)
        tot = defaultdict(int)
        for i in range(n):
            v = arr[i]
            res[i] += cnt[v] * i - tot[v]
            cnt[v] += 1
            tot[v] += i

        cnt.clear()
        tot.clear()
        for i in range(n - 1, -1, -1):
            v = arr[i]
            res[i] += tot[v] - cnt[v] * i
            cnt[v] += 1
            tot[v] += i

        return res
