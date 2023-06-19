"""

844. Backspace String Compare
Easy

Given two strings s and t, return true if they are equal when both are typed into empty text editors. '#' means a backspace character.

Note that after backspacing an empty text, the text will continue empty.

 

Example 1:

Input: s = "ab#c", t = "ad#c"
Output: true
Explanation: Both s and t become "ac".
Example 2:

Input: s = "ab##", t = "c#d#"
Output: true
Explanation: Both s and t become "".
Example 3:

Input: s = "a#c", t = "b"
Output: false
Explanation: s becomes "c" while t becomes "b".
 

Constraints:

1 <= s.length, t.length <= 200
s and t only contain lowercase letters and '#' characters.
 

Follow up: Can you solve it in O(n) time and O(1) space?

"""

# V0
class Solution(object):
    def backspaceCompare(self, S, T):

        def check(x):
            ans = []
            for i in x:
                if i != "#":
                    ans.append(i)
                # both `if ans:` and `if len(ans) > 0` work
                #if ans:
                if len(ans) > 0:
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