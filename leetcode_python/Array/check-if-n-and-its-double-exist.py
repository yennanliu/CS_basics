"""

1346. Check If N and Its Double Exist
Easy

Given an array arr of integers, check if there exist two indices i and j
such that :

- i != j
- 0 <= i, j < arr.length
- arr[i] == 2 * arr[j]


Example 1:

Input: arr = [10,2,5,3]
Output: true
Explanation: For i = 0 and j = 2, arr[i] == 10 == 2 * 5 == 2 * arr[j]

Example 2:

Input: arr = [3,1,7,11]
Output: false
Explanation: There is no i and j that satisfy the conditions.


Constraints:

2 <= arr.length <= 500
-10^3 <= arr[i] <= 10^3

"""

# V0
# IDEA: HASH SET (one pass)
#
#  for the current x, the partner is either 2*x or x/2,
#  and it must have been SEEN already (or will see x later),
#  so checking both directions against the "seen" set is enough.
#
#  NOTE:
#   - x = 0 works out naturally: the 2nd zero sees 2*0 == 0 in the set,
#     so [0,0] -> True, while a single 0 -> False (i != j is respected).
#   - use `x % 2 == 0` before halving, so 5 does not match 2 (5//2 == 2).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def checkIfExist(self, arr):
        seen = set()
        for x in arr:
            if (x * 2) in seen:
                return True
            if x % 2 == 0 and (x // 2) in seen:
                return True
            seen.add(x)
        return False


# V0-1
# IDEA : BRUTE FORCE (test every ordered pair)
#
#   the definition read literally : look at all pairs (i, j) with i != j and
#   ask whether arr[i] == 2 * arr[j]. n <= 500 so 250k checks is nothing, and
#   it needs no extra memory at all - the baseline the hash set improves on.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def checkIfExist(self, arr):
        n = len(arr)
        for i in range(n):
            for j in range(n):
                if i != j and arr[i] == 2 * arr[j]:
                    return True
        return False


# V0-2
# IDEA : SORT + BINARY SEARCH
#
#   sort a copy once, then for each value x binary-search its double 2*x.
#   bisect_left / bisect_right bracket the whole run of equal values, so the
#   only delicate case is x == 0 (where 2*x == x): there the run must hold at
#   least TWO copies, otherwise we would be pairing the element with itself
#   and break the i != j rule.
#
#   NOTE : negatives are fine - sorting is on the values, and 2*x simply lands
#          to the LEFT of x when x < 0.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def checkIfExist(self, arr):
        import bisect
        s = sorted(arr)
        for x in s:
            t = 2 * x
            lo = bisect.bisect_left(s, t)
            hi = bisect.bisect_right(s, t)
            if lo < hi and (t != x or hi - lo >= 2):
                return True
        return False
