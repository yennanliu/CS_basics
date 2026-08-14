"""

1849. Splitting a String Into Descending Consecutive Values
Medium

You are given a string s that consists of only digits.

Check if we can split s into two or more non-empty substrings such that the numerical values of the substrings are in descending order and the difference between numerical values of every two adjacent substrings is equal to 1.

For example, the string s = "0090089" can be split into ["0090", "089"] with numerical values [90,89]. The values are in descending order and adjacent values differ by 1, so this way is valid.

Another example, the string s = "001" can be split into ["0", "01"], ["00", "1"], or ["0", "0", "1"]. However all the ways are invalid because they have numerical values [0,1], [0,1], and [0,0,1] respectively, all of which are not in descending order.

Return true if it is possible to split s as described above, or false otherwise.

A substring is a contiguous sequence of characters in a string.


Example 1:

Input: s = "1234"
Output: false
Explanation: There is no valid way to split s.

Example 2:

Input: s = "050043"
Output: true
Explanation: s can be split into ["05", "004", "3"] with numerical values [5,4,3].
The values are in descending order with adjacent values differing by 1.

Example 3:

Input: s = "9080701"
Output: false
Explanation: There is no valid way to split s.


Constraints:

1 <= s.length <= 20
s only consists of digits.

"""

# V0
# IDEA : BACKTRACKING -- only the FIRST piece really branches
#
#   dfs(i, prev) : can s[i:] be split so the first piece is prev - 1 and the
#   chain keeps descending by 1?
#     - i == len(s) -> the split closed cleanly -> True
#     - otherwise grow a candidate y digit by digit from position i and recurse
#       whenever y == prev - 1.
#
#   for the very first piece (prev = -1) every prefix is allowed, EXCEPT the
#   whole string -- the split must have at least two parts. that is the
#   `r = len(s) - 1` cap below.
#
#   NOTE : leading zeros are fine ("004" == 4), so we just accumulate
#          y = y * 10 + digit without any special casing.
#   NOTE : once prev is fixed there is at most ONE valid next value, so the
#          search collapses to a linear walk after the first choice -- no
#          memoisation needed with n <= 20.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def splitString(self, s):
        n = len(s)

        def dfs(i, prev):
            if i == n:
                return True
            y = 0
            end = n - 1 if prev < 0 else n
            for j in range(i, end):
                y = y * 10 + int(s[j])
                if prev < 0 or prev - y == 1:
                    if dfs(j + 1, y):
                        return True
            return False

        return dfs(0, -1)
