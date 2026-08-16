"""

3675. Minimum Operations to Transform String
Medium

You are given a string s consisting only of lowercase English letters.

You can perform the following operation any number of times (including zero):

Choose any character c in the string and replace every occurrence of c with
the next lowercase letter in the English alphabet.

Return the minimum number of operations required to transform s into a
string consisting of only 'a' characters.

Note: Consider the alphabet as circular, thus 'a' comes after 'z'.

Example 1:

Input: s = "yz"
Output: 2
Explanation:
Change 'y' to 'z' to get "zz".
Change 'z' to 'a' to get "aa".
Thus, the answer is 2.

Example 2:

Input: s = "a"
Output: 0
Explanation:
The string "a" only consists of 'a' characters. Thus, the answer is 0.

Constraints:

1 <= s.length <= 5 * 10^5
s consists only of lowercase English letters.

"""

# V0
# IDEA : THE ANSWER IS THE LONGEST DISTANCE TO 'a' AROUND THE CYCLE
#
#   a letter c needs (26 - (c - 'a')) increments to wrap around to 'a', and
#   'a' itself needs none.
#
#   operations are shared: while the "hardest" letter marches towards 'a' it
#   passes through every letter between it and 'z', so every other letter is
#   swept up on the way and costs nothing extra. that makes the total exactly
#   the maximum single-letter distance rather than a sum.
#
#   so scan the distinct letters and take the largest wrap distance.
#
# time = O(n), space = O(1)
class Solution(object):
    def minOperations(self, s):
        best = 0
        for ch in set(s):
            if ch != 'a':
                d = 26 - (ord(ch) - ord('a'))
                if d > best:
                    best = d
        return best
