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
