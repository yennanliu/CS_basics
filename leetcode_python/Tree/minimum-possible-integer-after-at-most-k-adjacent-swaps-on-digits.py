"""

1505. Minimum Possible Integer After at Most K Adjacent Swaps On Digits
Hard

You are given a string num representing the digits of a very large integer
and an integer k. You are allowed to swap any two adjacent digits
of the integer at most k times.

Return the minimum integer you can obtain also as a string.


Example 1:

Input: num = "4321", k = 4
Output: "1342"
Explanation: The steps to obtain the minimum integer from 4321 with 4 adjacent swaps are shown.

Example 2:

Input: num = "100", k = 1
Output: "010"
Explanation: It's ok for the output to have leading zeros,
but the input is guaranteed not to have any leading zeros.

Example 3:

Input: num = "36789", k = 1000
Output: "36789"
Explanation: We can keep the number without any swaps.


Constraints:

1 <= num.length <= 3 * 10^4
num consists of only digits and does not contain leading zeros.
1 <= k <= 10^9

"""

# V0
# IDEA : GREEDY + BINARY INDEXED TREE (Fenwick tree)
#
#   greedy : fill the output left to right. for each output slot try the
#   digits 0..9 in order and take the SMALLEST one we can still afford.
#
#   the cost of pulling the digit at original index i to the front is
#   the number of digits BEFORE i that have NOT been taken out yet:
#
#       cost = (i) - (# of already-taken indices < i)
#
#   -> a BIT over "taken" flags gives that count in O(log n).
#   -> for each digit value we keep a queue of its original indices, so
#      the earliest untaken occurrence is just the queue head.
# time = O(n * 10 * log n)
# space = O(n)
from collections import deque
class BinaryIndexedTree(object):
    def __init__(self, n):
        self.n = n
        self.c = [0] * (n + 1)

    # 1-indexed
    def update(self, i, delta):
        while i <= self.n:
            self.c[i] += delta
            i += i & (-i)

    # prefix sum of [1 .. i]
    def query(self, i):
        s = 0
        while i > 0:
            s += self.c[i]
            i -= i & (-i)
        return s


class Solution(object):
    def minInteger(self, num, k):
        n = len(num)

        # pos[d] = queue of 1-indexed positions of digit d, ascending
        pos = [deque() for _ in range(10)]
        for i, ch in enumerate(num):
            pos[int(ch)].append(i + 1)

        bit = BinaryIndexedTree(n)
        res = []

        for _ in range(n):
            for d in range(10):
                if not pos[d]:
                    continue
                i = pos[d][0]
                """
                NOTE !!!
                    bit.query(i) counts the ALREADY TAKEN indices in [1..i];
                    index i itself is still untaken, so this is exactly the
                    taken count in [1..i-1].
                """
                cost = (i - 1) - bit.query(i)
                if cost <= k:
                    k -= cost
                    pos[d].popleft()
                    bit.update(i, 1)
                    res.append(str(d))
                    break

        return "".join(res)


# V1
# IDEA : GREEDY + BRUTE FORCE BUBBLE (O(n^2), fine for small inputs / as a reference)
#   repeatedly look at most k positions ahead, find the smallest digit there,
#   and bubble it to the current position.
# time = O(n^2)
# space = O(n)
class Solution(object):
    def minInteger(self, num, k):
        s = list(num)
        n = len(s)
        i = 0
        while i < n and k > 0:
            # smallest digit reachable within k swaps
            best = i
            for j in range(i + 1, min(n, i + k + 1)):
                if s[j] < s[best]:
                    best = j
            # bubble s[best] left to index i
            for j in range(best, i, -1):
                s[j], s[j - 1] = s[j - 1], s[j]
            k -= (best - i)
            i += 1
        return "".join(s)
