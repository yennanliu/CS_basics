"""

3216. Lexicographically Smallest String After a Swap
Easy

Given a string s containing only digits, return the lexicographically smallest string that can be obtained after swapping adjacent digits in s with the same parity at most once.

Digits have the same parity if both are odd or both are even. For example, 5 and 9, as well as 2 and 4, have the same parity, while 6 and 9 do not.


Example 1:

Input: s = "45320"
Output: "43520"
Explanation:
s[1] == '5' and s[2] == '3' both have the same parity, and swapping them results in the lexicographically smallest string.

Example 2:

Input: s = "001"
Output: "001"
Explanation:
There is no need to perform a swap because s is already the lexicographically smallest.


Constraints:

2 <= s.length <= 100
s consists only of digits.

"""

# V0
# IDEA : ONE SWAP, SO TAKE THE EARLIEST ONE THAT ACTUALLY HELPS
#
#   a swap only helps when it puts a smaller digit earlier, i.e. when the
#   pair is out of order (s[i] > s[i+1]) and shares a parity.
#
#   the earliest such position dominates every later one — improving an
#   earlier character beats any later change — so return immediately on the
#   first hit, and leave the string alone if there is none.
#
# time = O(n), space = O(n)
class Solution(object):
    def getSmallestString(self, s):
        t = list(s)
        for i in range(len(t) - 1):
            a, b = int(t[i]), int(t[i + 1])
            if a % 2 == b % 2 and a > b:
                t[i], t[i + 1] = t[i + 1], t[i]
                break
        return ''.join(t)
