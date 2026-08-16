"""

3675. Minimum Operations to Transform String
Medium

You are given a string s consisting of lowercase English letters.

You can perform the following operation any number of times:

Choose any one of the 26 lowercase letters c, and replace every occurrence of c in s with the next letter of the alphabet, cyclically, so 'a' becomes 'b', 'b' becomes 'c', ..., and 'z' becomes 'a'.

Return the minimum number of operations needed to transform s so that it contains only the character 'a'.


Example 1:

Input: s = "yz"
Output: 2
Explanation:
Turn every 'z' into 'a', giving "ya", then turn every 'y' into 'z' and that 'z' into 'a'; two operations suffice because 'z' needs one step and 'y' needs two, and the step for 'z' is shared.

Example 2:

Input: s = "a"
Output: 0
Explanation:
The string already contains only 'a', so no operation is needed.


Constraints:

1 <= s.length <= 10^5
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
