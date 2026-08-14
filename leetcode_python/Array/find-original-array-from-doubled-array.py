"""

2007. Find Original Array From Doubled Array
Medium

An integer array original is transformed into a doubled array changed by appending twice the value of every element in original, and then randomly shuffling the resulting array.

Given an array changed, return original if changed is a doubled array. If changed is not a doubled array, return an empty array. The elements in original may be returned in any order.


Example 1:

Input: changed = [1,3,4,2,6,8]
Output: [1,3,4]
Explanation: One possible original array could be [1,3,4]:
- Twice the value of 1 is 1 * 2 = 2.
- Twice the value of 3 is 3 * 2 = 6.
- Twice the value of 4 is 4 * 2 = 8.
Other original arrays could be [4,3,1] or [3,1,4].

Example 2:

Input: changed = [6,3,0,1]
Output: []
Explanation: changed is not a doubled array.

Example 3:

Input: changed = [1]
Output: []
Explanation: changed is not a doubled array.


Constraints:

1 <= changed.length <= 10^5
0 <= changed[i] <= 10^5

"""

# V0
# IDEA : GREEDY MATCHING ON A SORTED ARRAY + COUNTER
#
#   the SMALLEST remaining value can never be somebody's double (nothing smaller
#   is left), so it must belong to `original` and has to consume one copy of
#   twice itself. pair them off, repeat.
#
#   NOTE : odd length -> impossible right away.
#   NOTE : 0 is its own double, so a run of zeros must have EVEN length; the
#          "decrement x first, then look for 2*x" order handles that for free
#          (cnt[0] is reduced before we ask for another 0).
#
# time = O(n log n), space = O(n)
from collections import Counter
class Solution(object):
    def findOriginalArray(self, changed):
        if len(changed) % 2 == 1:
            return []

        cnt = Counter(changed)
        res = []
        for x in sorted(changed):
            if cnt[x] == 0:
                continue
            cnt[x] -= 1
            if cnt[x * 2] == 0:
                return []
            cnt[x * 2] -= 1
            res.append(x)

        return res
