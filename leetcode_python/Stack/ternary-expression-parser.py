# V0

# V1 

# V2
# time = O(n), n = len(expression)
# space = O(n)
class Solution(object):
    def parseTernary(self, expression):
        """
        :type expression: str
        :rtype: str
        """
        if not expression:
            return ""

        stack = []
        for c in expression[::-1]:
            if stack and stack[-1] == '?':
                stack.pop()  # pop '?'
                first = stack.pop()
                stack.pop()  # pop ':'
                second = stack.pop()

                if c == 'T':
                    stack.append(first)
                else:
                    stack.append(second)
            else:
                stack.append(c)
        return str(stack[-1])