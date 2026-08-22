"""

1534. Count Good Triplets
Easy

Given an array of integers arr, and three integers a, b and c. You need to find the number of good triplets.

A triplet (arr[i], arr[j], arr[k]) is good if the following conditions are true:

0 <= i < j < k < arr.length
|arr[i] - arr[j]| <= a
|arr[j] - arr[k]| <= b
|arr[i] - arr[k]| <= c

Where |x| denotes the absolute value of x.

Return the number of good triplets.


Example 1:

Input: arr = [3,0,1,1,9,7], a = 7, b = 2, c = 3
Output: 4
Explanation: There are 4 good triplets: [(3,0,1), (3,0,1), (3,1,1), (0,1,1)].

Example 2:

Input: arr = [1,1,2,2,3], a = 0, b = 0, c = 1
Output: 0
Explanation: No triplet satisfies all conditions.


Constraints:

3 <= arr.length <= 100
0 <= arr[i] <= 1000
0 <= a, b, c <= 1000

"""

# V0
# IDEA : BRUTE FORCE ENUMERATION WITH EARLY PRUNING
#
#   n <= 100, so at most C(100, 3) ~= 161700 triplets — plain triple loop
#   is the intended answer here.
#
#   NOTE : test |arr[i] - arr[j]| <= a in the OUTER pair loop and skip the
#          whole inner k loop when it fails; that prunes most of the work
#          without changing the asymptotics.
#
# time = O(n^3), space = O(1)
class Solution(object):
    def countGoodTriplets(self, arr, a, b, c):
        n = len(arr)
        res = 0
        for i in range(n):
            for j in range(i + 1, n):
                if abs(arr[i] - arr[j]) > a:
                    continue
                for k in range(j + 1, n):
                    if abs(arr[j] - arr[k]) <= b and abs(arr[i] - arr[k]) <= c:
                        res += 1
        return res


# V0-1
# IDEA : 2D PREFIX COUNT TABLE OVER (PREFIX LENGTH, VALUE)
#
#   fix the OUTER pair (i, k) — that is where the |arr[i] - arr[k]| <= c
#   condition lives. the middle index j then only has to satisfy i < j < k and
#       arr[j] in [max(arr[i] - a, arr[k] - b), min(arr[i] + a, arr[k] + b)]
#   i.e. a COUNT of values in a range inside the window arr[i+1 .. k-1].
#   pre[p][v] = how many of arr[0 .. p-1] are <= v answers that in O(1) :
#       (pre[k][hi] - pre[i+1][hi]) - (pre[k][lo-1] - pre[i+1][lo-1])
#   so the cubic loop collapses to a quadratic one plus an O(n * V) table.
#
#   NOTE : clamp lo to 0 and hi to the value ceiling before indexing, and
#          treat lo == 0 as "no lower cut" (there is no column -1).
#
# time = O(n * V + n^2) with V = 1001 possible values
# space = O(n * V)
class Solution(object):
    def countGoodTriplets(self, arr, a, b, c):
        n = len(arr)
        V = 1000
        pre = [[0] * (V + 1) for _ in range(n + 1)]
        for p in range(n):
            prev, row = pre[p], pre[p + 1]
            row[:] = prev
            for v in range(arr[p], V + 1):
                row[v] += 1

        res = 0
        for i in range(n):
            for k in range(i + 2, n):
                if abs(arr[i] - arr[k]) > c:
                    continue
                lo = max(0, arr[i] - a, arr[k] - b)
                hi = min(V, arr[i] + a, arr[k] + b)
                if lo > hi:
                    continue
                cnt = pre[k][hi] - pre[i + 1][hi]
                if lo:
                    cnt -= pre[k][lo - 1] - pre[i + 1][lo - 1]
                res += cnt
        return res


# V0-2
# IDEA : FENWICK TREE (BIT) OVER THE VALUE DOMAIN
#
#   sweep the MIDDLE index j left to right while a BIT holds the values of
#   arr[0 .. j-1] — exactly the legal choices for i. for every k > j with
#   |arr[j] - arr[k]| <= b, the good i's are the ones whose value lies in
#       [max(arr[j] - a, arr[k] - c), min(arr[j] + a, arr[k] + c)]
#   which is a single prefix-sum difference on the tree.
#
#   same quadratic pair loop as V0-1, but the counting structure is built
#   INCREMENTALLY (one insert per j) instead of precomputed, trading a log
#   factor for O(V) memory instead of O(n * V).
#
# time = O(n^2 log V)
# space = O(V)
class Solution(object):
    def countGoodTriplets(self, arr, a, b, c):
        V = 1001                      # values 0..1000, BIT is 1-indexed
        tree = [0] * (V + 1)

        def add(v):
            i = v + 1
            while i <= V:
                tree[i] += 1
                i += i & (-i)

        def pref(v):
            # how many inserted values are <= v
            i = min(v, V - 1) + 1
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & (-i)
            return s

        n = len(arr)
        res = 0
        for j in range(n):
            for k in range(j + 1, n):
                if abs(arr[j] - arr[k]) > b:
                    continue
                lo = max(0, arr[j] - a, arr[k] - c)
                hi = min(V - 1, arr[j] + a, arr[k] + c)
                if lo > hi:
                    continue
                res += pref(hi) - (pref(lo - 1) if lo else 0)
            add(arr[j])
        return res
