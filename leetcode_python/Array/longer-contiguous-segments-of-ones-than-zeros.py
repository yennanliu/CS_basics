"""

1869. Longer Contiguous Segments of Ones than Zeros
Easy

Given a binary string s, return true if the longest contiguous segment of 1's is strictly longer than the longest contiguous segment of 0's in s, or return false otherwise.

For example, in s = "110100010" the longest continuous segment of 1s has length 2, and the longest continuous segment of 0s has length 3.

Note that if there are no 0's, then the longest continuous segment of 0's is considered to have a length 0. The same applies if there is no 1's.


Example 1:

Input: s = "1101"
Output: true
Explanation:
The longest contiguous segment of 1s has length 2: "1101"
The longest contiguous segment of 0s has length 1: "1101"
The segment of 1s is longer, so return true.

Example 2:

Input: s = "111000"
Output: false
Explanation:
The longest contiguous segment of 1s has length 3: "111000"
The longest contiguous segment of 0s has length 3: "111000"
The segment of 1s is not longer, so return false.

Example 3:

Input: s = "110100010"
Output: false
Explanation:
The longest contiguous segment of 1s has length 2: "110100010"
The longest contiguous segment of 0s has length 3: "110100010"
The segment of 1s is not longer, so return false.


Constraints:

1 <= s.length <= 100
s[i] is either '0' or '1'.

"""

# V0
# IDEA : SINGLE PASS RUN LENGTH (track both runs at once)
#
#   walk the string keeping `run` = length of the current streak.
#   whenever the character changes, the streak restarts at 1.
#   after each step update best[c] where c is the current character.
#
#   NOTE : a character that never appears keeps best = 0, which matches
#          the problem's "no 0's -> longest 0 segment is 0" rule.
#
# time = O(n), space = O(1)
class Solution(object):
    def checkZeroOnes(self, s):
        best = {'0': 0, '1': 0}
        run = 0

        for i in range(len(s)):
            if i > 0 and s[i] == s[i - 1]:
                run += 1
            else:
                run = 1
            if run > best[s[i]]:
                best[s[i]] = run

        return best['1'] > best['0']
