# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/84956643
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
# Time:  O(n)
# Space: O(1)
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
# Time:  O(n)
# Space: O(h)
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