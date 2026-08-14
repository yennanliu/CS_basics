"""

1784. Check if Binary String Has at Most One Segment of Ones
Easy

Given a binary string s without leading zeros, return true if s contains at most one contiguous segment of ones. Otherwise, return false.

Example 1:

Input: s = "1001"
Output: false
Explanation: The string has two segments of size 1.

Example 2:

Input: s = "110"
Output: true

Constraints:

1 <= s.length <= 100
s[i] is either '0' or '1'.
s[0] is '1'.

"""

# V0
# IDEA : LOOK FOR THE PATTERN "01"
#
#   s starts with '1', so the string looks like 1...1 0...0 1... .
#   a second segment of ones can only begin right after a zero, i.e. exactly
#   when the substring "01" occurs. so "at most one segment" <=> "01" absent.
#
# time = O(n), space = O(1)
class Solution(object):
    def checkOnesSegment(self, s):
        return "01" not in s
