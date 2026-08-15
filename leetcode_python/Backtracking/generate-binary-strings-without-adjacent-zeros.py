"""

3211. Generate Binary Strings Without Adjacent Zeros
Medium

You are given a positive integer n.

A binary string x is valid if all substrings of x of length 2 contain at least one "1".

Return all valid strings with length n, in any order.


Example 1:

Input: n = 3
Output: ["010","011","101","110","111"]
Explanation:
The valid strings of length 3 are: "010", "011", "101", "110", and "111".

Example 2:

Input: n = 1
Output: ["0","1"]
Explanation:
The valid strings of length 1 are: "0" and "1".


Constraints:

1 <= n <= 18

"""

# V0
# IDEA : BACKTRACK, REFUSING TO PLACE A '0' AFTER A '0'
#
#   "every length-2 substring holds a 1" is the same as "no two adjacent
#   zeros", so the only rule while building left to right is : a '0' may
#   follow anything except another '0'.
#
#   the count of such strings is a Fibonacci number, so n = 18 yields a few
#   thousand results — small enough to list them all.
#
# time = O(n * number of valid strings), space = O(n) recursion
class Solution(object):
    def validStrings(self, n):
        res = []
        cur = []

        def build(i):
            if i == n:
                res.append(''.join(cur))
                return
            if not cur or cur[-1] == '1':
                cur.append('0')
                build(i + 1)
                cur.pop()
            cur.append('1')
            build(i + 1)
            cur.pop()

        build(0)
        return res
