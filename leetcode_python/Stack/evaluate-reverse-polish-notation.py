# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79559703
class Solution(object):
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        stack = []
        operators = ['+', '-', '*', '/']
        for token in tokens:
            if token not in operators:
                stack.append(token)
            else:
                b = stack.pop()
                a = stack.pop()
                if token == '/' and int(a) * int(b) < 0:
                    res = eval('-' + '(' + '-' + a + '/' + b + ')')
                else:
                    res = eval(a + token + b)
                stack.append(str(res))
        return int(stack.pop())

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79559703
class Solution(object):
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        stack = []
        operators = ['+', '-', '*', '/']
        for token in tokens:
            if token not in operators:
                stack.append(token)
            else:
                b = stack.pop()
                a = stack.pop()
                if token == '/':
                    res = int(operator.truediv(int(a), int(b)))
                else:
                    res = eval(a + token + b)
                stack.append(str(res))
        return int(stack.pop())

# V2
# Time:  O(n)
# Space: O(n)
import operator
class Solution(object):
    # @param tokens, a list of string
    # @return an integer
    def evalRPN(self, tokens):
        numerals, operators = [], {"+": operator.add, "-": operator.sub, "*": operator.mul, "/": operator.div}
        for token in tokens:
            if token not in operators:
                numerals.append(int(token))
            else:
                y, x = numerals.pop(), numerals.pop()
                numerals.append(int(operators[token](x * 1.0, y)))
        return numerals.pop()
