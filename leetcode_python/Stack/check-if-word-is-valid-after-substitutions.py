"""

1003. Check If Word Is Valid After Substitutions
Medium

Given a string s, determine if it is valid.

A string s is valid if, starting with an empty string t = "", you can transform t into s after performing the following operation any number of times:

Insert string "abc" into any position in t. More formally, t becomes tleft + "abc" + tright, where t == tleft + tright. Note that tleft and tright may be empty.

Return true if s is a valid string, otherwise, return false.


Example 1:

Input: s = "aabcbc"
Output: true
Explanation:
"" -> "abc" -> "aabcbc"
Thus, "aabcbc" is valid.

Example 2:

Input: s = "abcabcababcc"
Output: true
Explanation:
"" -> "abc" -> "abcabc" -> "abcabcabc" -> "abcabcababcc"
Thus, "abcabcababcc" is valid.

Example 3:

Input: s = "abccba"
Output: false
Explanation: It is impossible to get "abccba" using the operation.


Constraints:

1 <= s.length <= 2 * 10^4
s consists of letters 'a', 'b', and 'c'

"""

# V0
# IDEA : STACK
#
#   reverse the process : instead of INSERTING "abc",
#   keep REMOVING "abc" and see if we end up with an empty string.
#
#   push chars on a stack; whenever we push a 'c',
#   the 2 chars right below it must be 'a', 'b' -> pop all 3.
#   valid <=> the stack is empty at the end.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def isValid(self, s):
        stack = []
        for ch in s:
            if ch == 'c':
                # need "ab" right below -> stack top is 'b', then 'a'
                if len(stack) < 2 or stack[-1] != 'b' or stack[-2] != 'a':
                    return False
                stack.pop()
                stack.pop()
            else:
                stack.append(ch)
        return not stack


# V1
# IDEA : REPEATED STRING REPLACE
# (simple to read, but O(n^2) since every replace rebuilds the string)
# time = O(n^2)
# space = O(n)
class Solution(object):
    def isValid(self, s):
        while 'abc' in s:
            s = s.replace('abc', '')
        return s == ''
