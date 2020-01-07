# Time:  O(m + n)
# Space: O(1)

# Given two strings S and T, return if they are equal
# when both are typed into empty text editors. # means a backspace character.
#
# Example 1:
#
# Input: S = "ab#c", T = "ad#c"
# Output: true
# Explanation: Both S and T become "ac".
# Example 2:
#
# Input: S = "ab##", T = "c#d#"
# Output: true
# Explanation: Both S and T become "".
# Example 3:
#
# Input: S = "a##c", T = "#a#c"
# Output: true
# Explanation: Both S and T become "c".
# Example 4:
#
# Input: S = "a#c", T = "b"
# Output: false
# Explanation: S becomes "c" while T becomes "b".
#
# Note:
# - 1 <= S.length <= 200
# - 1 <= T.length <= 200
# - S and T only contain lowercase letters and '#' characters.

# V0
class Solution(object):
    def backspaceCompare(self, S, T):

        def check(x):
            ans = []
            for i in x:
                if i != "#":
                    ans.append(i)
                if ans:
                    if ans[-1] != "#" and i == "#":
                        ans.pop()
            return ans 
            
        return check(S) == check(T)

# V1
# http://bookshadow.com/weblog/2018/06/03/leetcode-backspace-string-compare/
class Solution(object):
    def backspaceCompare(self, S, T):
        """
        :type S: str
        :type T: str
        :rtype: bool
        """
        def toString(S):
            ans = []
            for c in S:
                if c == '#': ans and ans.pop()
                else: ans.append(c)
            return ''.join(ans)
        return toString(S) == toString(T)



# V1'
# http://bookshadow.com/weblog/2018/06/03/leetcode-backspace-string-compare/
class Solution(object):
    def backspaceCompare(self, S, T):
        def toString(S):
            ans = []
            #print ('S = ', S)
            for c in S:
                #print ('c = ', c )
                #print ('ans = ', ans )
                
                if c == '#': 
                    ans and ans.pop()
                else: 
                    ans.append(c)
                #print ('ans = ', ans )
            return ''.join(ans)
        ## re-run the runc compare m treated S, T 
        return toString(S) == toString(T)

# V1''
# https://www.jiuzhang.com/solution/backspace-string-compare/#tag-highlight-lang-python
class Solution(object):
    def backspaceCompare(self, S, T):
        def build(S):
            ans = []
            for c in S:
                if c != '#':
                    ans.append(c)
                elif ans:
                    ans.pop()
            return "".join(ans)
        return build(S) == build(T)

# V2
import itertools
class Solution(object):
    def backspaceCompare(self, S, T):
        """
        :type S: str
        :type T: str
        :rtype: bool
        """
        def findNextChar(S):
            skip = 0
            for i in reversed(range(len(S))):
                if S[i] == '#':
                    skip += 1
                elif skip:
                    skip -= 1
                else:
                    yield S[i]
        return all(x == y for x, y in itertools.zip_longest(findNextChar(S), findNextChar(T)))
