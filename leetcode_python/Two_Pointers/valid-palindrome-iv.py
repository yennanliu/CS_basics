"""

2330. Valid Palindrome IV
Medium
(premium / locked problem)

You are given a 0-indexed string s consisting of only lowercase English letters. In one operation, you can change any character of s to any other character.

Return true if you can make s a palindrome after performing exactly one or two operations, or return false otherwise.


Example 1:

Input: s = "abcdba"
Output: true
Explanation: One way to make s a palindrome using 1 operation is:
- Change s[2] to 'd'. Now, s = "abddba".
One operation could be performed to make s a palindrome so return true.

Example 2:

Input: s = "aa"
Output: true
Explanation: One way to make s a palindrome using 2 operations is:
- Change s[0] to 'b'. Now, s = "ba".
- Change s[1] to 'b'. Now, s = "bb".
Two operations could be performed to make s a palindrome so return true.

Example 3:

Input: s = "abcdef"
Output: false
Explanation: It is not possible to make s a palindrome using one or two operations so return false.


Constraints:

1 <= s.length <= 10^5
s consists only of lowercase English letters.

"""

# V0
# IDEA : COUNT THE MISMATCHED MIRROR PAIRS — THE ANSWER IS "AT MOST 2"
#
#   let d be the number of positions i < n/2 with s[i] != s[n-1-i]. fixing one
#   mismatched pair costs exactly one operation, so d <= 2 is clearly needed.
#
#   the subtle case is d == 0 (already a palindrome), where the problem still
#   demands EXACTLY one or two operations. two operations always work : pick
#   a mirror pair and rewrite BOTH to some other letter, keeping the symmetry.
#   for an odd-length string the middle character can be changed twice.
#   either way d == 0 is achievable.
#
#   so the whole problem reduces to  d <= 2.
#
# time = O(n), space = O(1)
class Solution(object):
    def makePalindrome(self, s):
        n = len(s)
        d = sum(1 for i in range(n // 2) if s[i] != s[n - 1 - i])
        return d <= 2
