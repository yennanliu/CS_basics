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


# V0-1
# IDEA : GREEDY FROM THE TOP WITH A MAX-HEAP (MIRROR OF THE SORTED SCAN)
#
#   run the same exchange argument from the other end : the LARGEST remaining
#   value can never be an `original` (nothing bigger is left to be its double),
#   so it MUST be a double and has to consume one copy of half itself.
#   a max-heap (negated min-heap) hands the values back largest-first without
#   materialising a sorted copy.
#
#   NOTE : an odd largest value has no half -> impossible immediately.
#   NOTE : 0 is its own double; popping 0 decrements cnt[0] before asking for
#          cnt[0] again, so an odd number of zeros is rejected for free.
#
# time = O(n log n)
# space = O(n)
import heapq
from collections import Counter
class Solution(object):
    def findOriginalArray(self, changed):
        if len(changed) % 2 == 1:
            return []

        cnt = Counter(changed)
        heap = [-v for v in changed]
        heapq.heapify(heap)

        res = []
        while heap:
            x = -heapq.heappop(heap)
            if cnt[x] == 0:
                continue
            if x % 2 == 1:
                return []
            cnt[x] -= 1
            half = x // 2
            if cnt[half] == 0:
                return []
            cnt[half] -= 1
            res.append(half)

        return res


# V0-2
# IDEA : CHAIN DECOMPOSITION — SOLVE EACH DOUBLING CHAIN s, 2s, 4s, ... AT ONCE
#
#   values only ever pair up along a doubling chain rooted at an ODD number:
#   1,2,4,8...  /  3,6,12...  /  5,10,20...  Different chains never interact,
#   so the whole problem splits into independent 1-D problems.
#
#   walk one chain bottom-up carrying `pending` = how many doubles the previous
#   level still needs. the root is odd, so nothing at the root can be a double
#   -> every root copy is an original. at each level:
#       o = cnt[v] - pending      (leftovers are forced to be originals)
#   a negative o means the previous level cannot be paired -> impossible.
#   after the chain leaves the value range, `pending` must be 0.
#
#   NOTE : 0 is handled on its own — it is its own double, so the count of
#          zeros must be even and contributes cnt[0] // 2 zeros.
#
# time = O(n + M log M) with M = max(changed)
# space = O(n)
from collections import Counter
class Solution(object):
    def findOriginalArray(self, changed):
        if len(changed) % 2 == 1:
            return []

        cnt = Counter(changed)
        res = []

        if cnt[0] % 2 == 1:
            return []
        res.extend([0] * (cnt[0] // 2))

        top = max(changed)
        for s in range(1, top + 1, 2):
            pending = 0
            v = s
            while v <= top:
                o = cnt[v] - pending
                if o < 0:
                    return []
                res.extend([v] * o)
                pending = o
                v *= 2
            if pending != 0:
                return []

        return res
