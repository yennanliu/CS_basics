"""

3174. Clear Digits
Easy

You are given a string s.

Your task is to remove all digits by doing this operation repeatedly:

Delete the first digit and the closest non-digit character to its left.

Return the resulting string after removing all digits.


Example 1:

Input: s = "abc"
Output: "abc"
Explanation:
There is no digit in the string.

Example 2:

Input: s = "cb34"
Output: ""
Explanation:
First, we apply the operation on s[2], and s becomes "c4".
Then we apply the operation on s[1], and s becomes "".


Constraints:

1 <= s.length <= 100
s consists only of lowercase English letters and digits.
The input is generated such that it is possible to delete all digits.

"""

# V0
# IDEA : A STACK — A DIGIT ALWAYS CANCELS WHATEVER IS ON TOP
#
#   "the closest non-digit to its left" is exactly the most recently kept
#   character, because every earlier digit has already consumed its own
#   partner. so push letters and pop on each digit.
#
#   the input is guaranteed solvable, so the stack is never empty at a pop.
#
# time = O(n), space = O(n)
class Solution(object):
    def clearDigits(self, s):
        stack = []
        for ch in s:
            if ch.isdigit():
                stack.pop()
            else:
                stack.append(ch)
        return ''.join(stack)
