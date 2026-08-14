"""

1291. Sequential Digits
Medium

An integer has sequential digits if and only if each digit in the number
is one more than the previous digit.

Return a sorted list of all the integers in the range [low, high] inclusive
that have sequential digits.


Example 1:

Input: low = 100, high = 300
Output: [123,234]

Example 2:

Input: low = 1000, high = 13000
Output: [1234,2345,3456,4567,5678,6789,12345]


Constraints:

10 <= low <= high <= 10^9

"""

# V0
# IDEA: ENUMERATE all sequential numbers (there are only 36 of them)
#
#  -> a sequential number is fully decided by its FIRST digit (1..9)
#     and its length. build it by repeatedly doing x = x * 10 + next_digit.
#     total candidates = 8 + 7 + ... + 1 = 36, so just filter by range.
#
# time = O(1), at most 36 candidates
# space = O(1)
class Solution(object):
    def sequentialDigits(self, low, high):
        res = []
        for start in range(1, 10):
            x = start
            for d in range(start + 1, 10):
                x = x * 10 + d
                if low <= x <= high:
                    res.append(x)
        return sorted(res)


# V1
# IDEA: BFS (level = number of digits)
#
#  -> start the queue with the 1-digit seeds 1..9,
#     each pop `cur` expands to `cur * 10 + (last_digit + 1)`.
#     BFS naturally visits shorter numbers first.
#
# time = O(1), at most 45 states
# space = O(1)
from collections import deque
class Solution(object):
    def sequentialDigits(self, low, high):
        res = []
        q = deque(range(1, 10))
        while q:
            cur = q.popleft()
            if low <= cur <= high:
                res.append(cur)
            last = cur % 10
            """
            NOTE !!!

            we can only append `last + 1`, and only when last < 9
            """
            if last < 9:
                nxt = cur * 10 + last + 1
                if nxt <= high:
                    q.append(nxt)
        return sorted(res)
