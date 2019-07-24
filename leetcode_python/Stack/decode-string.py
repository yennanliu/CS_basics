# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79332894
# IDEA : STACK
# curstring = prestring + prenum * curstring 
# ord() is a way transforming string -> integer 
# e.g. 
# c = '3' -> ord(c) - ord('0') = 3 
# c= '9'  -> ord(c) - ord('0') = 9
# we can do that via int() as well. i.e. int('9') -> 9 
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        curnum = 0
        curstring = ''
        stack = []
        for char in s:
            if char == '[':
                stack.append(curstring)
                stack.append(curnum)
                curstring = ''
                curnum = 0
            elif char == ']':
                prenum = stack.pop()
                prestring = stack.pop()
                curstring = prestring + prenum * curstring
            elif char.isdigit():
                curnum = curnum * 10 + int(char)
            else:
                curstring += char
        return curstring

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def decodeString(self, s):
        """
        :type s: str
        :rtype: str
        """
        curr, nums, strs = [], [], []
        n = 0

        for c in s:
            if c.isdigit():
                n = n * 10 + ord(c) - ord('0')
            elif c == '[':
                nums.append(n)
                n = 0
                strs.append(curr)
                curr = []
            elif c == ']':
                strs[-1].extend(curr * nums.pop())
                curr = strs.pop()
            else:
                curr.append(c)

        return "".join(strs[-1]) if strs else "".join(curr)