"""

2375. Construct Smallest Number From DI String
Medium

You are given a 0-indexed string pattern of length n consisting of the characters 'I' meaning increasing and 'D' meaning decreasing.

A 0-indexed string num of length n + 1 is created using the following conditions:

num consists of the digits '1' to '9', where each digit is used at most once.
If pattern[i] == 'I', then num[i] < num[i + 1].
If pattern[i] == 'D', then num[i] > num[i + 1].

Return the lexicographically smallest possible string num that meets the conditions.


Example 1:

Input: pattern = "IIIDIDDD"
Output: "123549876"
Explanation:
At indices 0, 1, 2, and 4 we must have that num[i] < num[i+1].
At indices 3, 5, 6, and 7 we must have that num[i] > num[i+1].
Some possible values of num are "245639871", "135749862", and "123849765".
It can be proven that "123549876" is the smallest possible num that meets the conditions.
Note that "123414321" is not possible because the digit '1' is used more than once.

Example 2:

Input: pattern = "DDD"
Output: "4321"
Explanation:
Some possible values of num are "9876", "7321", and "8742".
It can be proven that "4321" is the smallest possible num that meets the conditions.


Constraints:

1 <= pattern.length <= 8
pattern consists of only the characters 'I' and 'D'.

"""

# V0
# IDEA : PUSH 1, 2, 3, ... ON A STACK AND FLUSH IT AT EVERY 'I'
#
#   to be lexicographically smallest we want to emit the smallest digits as
#   early as possible, so hand out 1, 2, 3, ... in order — but a run of 'D's
#   needs those digits DESCENDING.
#
#   a stack does exactly that : push the next digit at every position, and
#   pop everything whenever the constraint allows the values to be released
#   (at an 'I', and once at the very end). the pops come out reversed, which
#   is precisely the descending run each 'D' block needs.
#
# time = O(n), space = O(n)
class Solution(object):
    def smallestNumber(self, pattern):
        res = []
        stack = []
        for i in range(len(pattern) + 1):
            stack.append(str(i + 1))
            if i == len(pattern) or pattern[i] == 'I':
                while stack:
                    res.append(stack.pop())
        return ''.join(res)
