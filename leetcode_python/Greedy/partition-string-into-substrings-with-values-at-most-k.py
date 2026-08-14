"""

2522. Partition String Into Substrings With Values at Most K
Medium

You are given a string s consisting of digits from 1 to 9 and an integer k.

A partition of a string s is called good if:

Each digit of s is part of exactly one substring.
The value of each substring is less than or equal to k.

Return the minimum number of substrings in a good partition of s. If no good
partition of s exists, return -1.

Note that:

The value of a string is its result when interpreted as an integer. For example,
the value of "123" is 123 and the value of "1" is 1.
A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "165462", k = 60
Output: 4
Explanation: We can partition the string into substrings "16", "54", "6", and "2".
Each substring has a value less than or equal to k = 60.
It can be shown that we cannot partition the string into less than 4 substrings.

Example 2:

Input: s = "238182", k = 5
Output: -1
Explanation: There is no good partition for this string.


Constraints:

1 <= s.length <= 10^5
s[i] is a digit from '1' to '9'.
1 <= k <= 10^9

"""

# V0
# IDEA : GREEDY (extend the current piece as far as it can legally go)
#
#   scan left to right keeping the value `cur` of the piece being built. For
#   each digit, try to append it (cur * 10 + d). If the result still fits under
#   k, keep it in the same piece -- taking a longer piece here can never force
#   MORE pieces later, because any good partition that cuts earlier can be
#   rewritten to cut here without increasing the piece count (exchange
#   argument: moving a cut rightwards only shrinks the following piece).
#   If it does not fit, close the current piece and start a new one at d.
#
#   NOTE : a single digit larger than k can never sit inside any piece, so the
#          answer is -1. Since every char is '1'..'9' this is the ONLY
#          infeasible case -- there is no leading-zero subtlety to worry about.
#
#   NOTE : `res` starts at 1 (the first piece is already open) and each cut
#          bumps it, so the final still-open piece is counted without needing
#          an extra step after the loop.
#
#   NOTE : the equivalent memoized DP over cut positions is O(n) too, but this
#          one-pass greedy is O(1) space and avoids recursion depth issues at
#          n = 10^5.
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumPartition(self, s, k):
        res = 1
        cur = 0
        for ch in s:
            d = int(ch)
            # a lone digit that already exceeds k => no good partition exists
            if d > k:
                return -1
            if cur * 10 + d <= k:
                cur = cur * 10 + d
            else:
                # close the current piece, open a new one starting at d
                res += 1
                cur = d
        return res
