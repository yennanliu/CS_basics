"""

Given a string containing just the characters '(', ')', '{', '}', '[' and ']', 
determine if the input string is valid.

An input string is valid if:

Open brackets must be closed by the same type of brackets.
Open brackets must be closed in the correct order.
Note that an empty string is also considered valid.

Example 1:

Input: "()"
Output: true

Example 2:

Input: "()[]{}"
Output: true

Example 3:

Input: "(]"
Output: false

Example 4:

Input: "([)]"
Output: false

Example 5:

Input: "{[]}"
Output: true

"""

# V0
# IDEA : STACK + DICT
class Solution:
    # @return a boolean
    def isValid(self, s):
        stack, lookup = [], {"(": ")", "{": "}", "[": "]"}
        for parenthese in s:
            if parenthese in lookup:
                stack.append(parenthese)
            elif len(stack) == 0 or lookup[stack.pop()] != parenthese:
                return False
        return len(stack) == 0

# V0'
# IDEA : queue + dict
class Solution(object):
    def isValid(self, s):
        d = {'(':')', '{':'}', '[':']'}
        q = []
        if not s:
            return True
        if len(s) == 1:
            return False
        for i in s:
            if i in d:
                q.append(i)
            else:
                if not q:
                    return False
                else:
                    tmp = q.pop(-1)
                    if d[tmp] != i:
                        return False
        return True if not q else False

# V0'
# IDEA : STACK + DICT
class Solution:
    def isValid(self, s):
        lookup = {"(":")", "[":"]", "{":"}"}
        q = []
        for i in s:
            if i not in lookup and len(q) == 0:
                return False
            elif i in lookup:
                q.append(i)
            else:
                tmp = q.pop()
                if lookup[tmp] != i:
                    return False
        return True if len(q) == 0 else False

# V1
# https://blog.csdn.net/coder_orz/article/details/51697963
# IDEA : STACK
class Solution(object):
    def isValid(self, s):
        """
        :type s: str
        :rtype: bool
        """
        pars = [None]
        parmap = {')': '(', '}': '{', ']': '['}
        for c in s:
            if c in parmap and parmap[c] == pars[len(pars)-1]:
                pars.pop()
            else:
                pars.append(c)
        return len(pars) == 1

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51697963
# IDEA : STACK 
class Solution(object):
    def isValid(self, s):
        """
        :type s: str
        :rtype: bool
        """
        pars = [None]
        parmap = {')': '(', '}': '{', ']': '['}
        for c in s:
            if c in parmap:
                if parmap[c] != pars.pop():
                    return False
            else:
                pars.append(c)
        return len(pars) == 1

# V1'' 
class Solution:
    # @return a boolean
    def isValid(self, s):
        stack, lookup = [], {"(": ")", "{": "}", "[": "]"}
        for parenthese in s:
            if parenthese in lookup:
                stack.append(parenthese)
            elif len(stack) == 0 or lookup[stack.pop()] != parenthese:
                return False
        return len(stack) == 0
# if __name__ == "__main__":
#     print(Solution().isValid("()[]{}"))
#     print(Solution().isValid("()[{]}"))

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    # @return a boolean
    def isValid(self, s):
        stack, lookup = [], {"(": ")", "{": "}", "[": "]"}
        for parenthese in s:
            if parenthese in lookup:
                stack.append(parenthese)
            elif len(stack) == 0 or lookup[stack.pop()] != parenthese:
                return False
        return len(stack) == 0
