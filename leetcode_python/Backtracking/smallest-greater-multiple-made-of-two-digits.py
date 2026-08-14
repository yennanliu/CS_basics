"""

1999. Smallest Greater Multiple Made of Two Digits
Medium

Given three integers, k, digit1, and digit2, you want to find the smallest integer that is:

Larger than k,
A multiple of k, and
Comprised of only the digits digit1 and/or digit2.

Return the smallest such integer. If no such integer exists or the integer exceeds the limit of a signed 32-bit integer (2^31 - 1), return -1.


Example 1:

Input: k = 2, digit1 = 0, digit2 = 2
Output: 20
Explanation:
20 is the first integer larger than 2, a multiple of 2, and comprised of only the digits 0 and/or 2.

Example 2:

Input: k = 3, digit1 = 4, digit2 = 2
Output: 24
Explanation:
24 is the first integer larger than 3, a multiple of 3, and comprised of only the digits 4 and/or 2.

Example 3:

Input: k = 2, digit1 = 0, digit2 = 0
Output: -1
Explanation:
No integer meets the requirements so return -1.


Constraints:

1 <= k <= 1000
0 <= digit1 <= 9
0 <= digit2 <= 9

"""

# V0
# IDEA : BFS OVER THE DIGIT TREE (generates candidates in increasing order)
#
#   every candidate is built by repeatedly appending digit1 or digit2 to a
#   shorter candidate : x -> x*10 + d.
#
#   push the smaller digit first, so a FIFO queue emits candidates in
#   non-decreasing numeric order - the first one that is > k and divisible
#   by k is the answer, no sorting needed.
#
#   NOTE : with digit1 == digit2 push only one child, otherwise the queue
#          duplicates every value and blows up.
#   NOTE : the search is bounded - stop and return -1 as soon as the front of
#          the queue passes 2^31 - 1, since everything behind it is larger.
#   NOTE : digit1 == digit2 == 0 generates only 0 -> -1.
#
# time = O(2^L) with L <= 10 digits before the 2^31 cutoff, space = O(2^L)
from collections import deque
class Solution(object):
    def findInteger(self, k, digit1, digit2):
        if digit1 == 0 and digit2 == 0:
            return -1
        lo, hi = min(digit1, digit2), max(digit1, digit2)

        LIMIT = 2 ** 31 - 1
        q = deque([0])
        while q:
            x = q.popleft()
            if x > LIMIT:
                return -1
            if x > k and x % k == 0:
                return x
            q.append(x * 10 + lo)
            if lo != hi:
                q.append(x * 10 + hi)
        return -1
