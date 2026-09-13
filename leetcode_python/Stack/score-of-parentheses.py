"""

856. Score of Parentheses
Medium

Given a balanced parentheses string s, return the score of the string.

The score of a balanced parentheses string is based on the following rule:

"()" has score 1.
AB has score A + B, where A and B are balanced parentheses strings.
(A) has score 2 * A, where A is a balanced parentheses string.

Example 1:

Input: s = "()"
Output: 1

Example 2:

Input: s = "(())"
Output: 2

Example 3:

Input: s = "()()"
Output: 2

Constraints:

2 <= s.length <= 50
s consists of only '(' and ')'.
s is a balanced parentheses string.

"""

# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/84956643
# time = O(n)
# space = O(n)
class Solution(object):
    def scoreOfParentheses(self, S):
        """
        :type S: str
        :rtype: int
        """
        stack = []
        score = 0
        for c in S:
            if c == '(':
                stack.append("(")
            else:
                tc = stack[-1]
                if tc == '(':
                    stack.pop()
                    stack.append(1)
                else:
                    num = 0
                    while stack[-1] != '(':
                        num += stack.pop()
                    stack.pop()
                    stack.append(num * 2)
        return sum(stack)

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/84956643
# time = O(n)
# space = O(n)
class Solution(object):
    def scoreOfParentheses(self, S):
        """
        :type S: str
        :rtype: int
        """
        stack = [0]
        score = 0
        for c in S:
            if c == '(':
                stack.append(0)
            else:
                v = stack.pop()
                stack[-1] += max(v * 2, 1)
        return sum(stack)

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def scoreOfParentheses(self, S):
        """
        :type S: str
        :rtype: int
        """
        result, depth = 0, 0
        for i in range(len(S)):
            if S[i] == '(':
                depth += 1
            else:
                depth -= 1
                if S[i-1] == '(':
                    result += 2**depth
        return result

# V2'
# time = O(n)
# space = O(n)
class Solution2(object):
    def scoreOfParentheses(self, S):
        """
        :type S: str
        :rtype: int
        """
        stack = [0]
        for c in S:
            if c == '(':
                stack.append(0)
            else:
                last = stack.pop()
                stack[-1] += max(1, 2*last)
        return stack[0]