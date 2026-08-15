"""

3258. Count Substrings That Satisfy K-Constraint I
Easy

You are given a binary string s and an integer k.

A binary string satisfies the k-constraint if either of the following conditions holds:

The number of 0's in the string is at most k.
The number of 1's in the string is at most k.

Return an integer denoting the number of substrings of s that satisfy the k-constraint.


Example 1:

Input: s = "10101", k = 1
Output: 12
Explanation:
Every substring of s except the substrings "1010", "10101", and "0101" satisfies the k-constraint.

Example 2:

Input: s = "1010101", k = 2
Output: 25
Explanation:
Every substring of s except the substrings with a length greater than 5 satisfies the k-constraint.

Example 3:

Input: s = "11111", k = 1
Output: 15
Explanation:
All substrings of s satisfy the k-constraint.


Constraints:

1 <= s.length <= 50
1 <= k <= s.length
s[i] is either '0' or '1'.

"""

# V0
# IDEA : SLIDING WINDOW — THE CONSTRAINT IS MONOTONE IN THE WINDOW SIZE
#
#   growing a substring can only raise both counts, so once a window breaks
#   the rule every longer one with the same left end does too. that makes a
#   two-pointer sweep exact : extend the right end, and shrink from the left
#   until min(zeros, ones) <= k again.
#
#   each right end then contributes (right - left + 1) valid substrings.
#
#   n is only 50 here, but this scales as is to the sequel's 10^5.
#
# time = O(n), space = O(1)
class Solution(object):
    def countKConstraintSubstrings(self, s, k):
        zeros = ones = 0
        left = 0
        res = 0
        for right, ch in enumerate(s):
            if ch == '0':
                zeros += 1
            else:
                ones += 1
            while zeros > k and ones > k:
                if s[left] == '0':
                    zeros -= 1
                else:
                    ones -= 1
                left += 1
            res += right - left + 1
        return res
