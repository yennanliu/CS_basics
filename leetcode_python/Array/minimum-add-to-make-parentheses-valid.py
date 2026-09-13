"""

921. Minimum Add to Make Parentheses Valid
Medium

A parentheses string is valid if and only if:

It is the empty string,
It can be written as AB (A concatenated with B), where A and B are valid strings, or
It can be written as (A), where A is a valid string.

You are given a parentheses string s. In one move, you can insert a parenthesis at any position of the string.

For example, if s = "()))", you can insert an opening parenthesis to be "(()))" or a closing parenthesis to be "())))".

Return the minimum number of moves required to make s valid.

Example 1:

Input: s = "())"
Output: 1

Example 2:

Input: s = "((("
Output: 3

Constraints:

1 <= s.length <= 1000
s[i] is either '(' or ')'.

"""

# V0 

# V1 
# https://www.jiuzhang.com/solution/921-minimum-add-to-make-parentheses-valid/#tag-other-lang-python
# IDEA : COUNT THE "LEFT" AND  "RIGHT" RESPECTIVELY (# ADD TO LEFT, # ADD TO RIGHT)
# time = O(n)
# space = O(1)
class Solution(object):
    def minAddToMakeValid(self, s):
        """
        :type S: str
        :rtype: int
        """
        # left is length of stack
        left = right = 0
        
        for char in s:
            if char == '(':
                left += 1
            else:
                if left:
                    left -= 1
                else:
                    right += 1
        return left + right

# V1'
# https://www.jiuzhang.com/solution/minimum-add-to-make-parentheses-valid/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
class Solution:
    """
    @param S: the given string
    @return: the minimum number of parentheses we must add
    """
    def minAddToMakeValid(self, S):
        # Write your code here
        left = right = 0
        for i in S:
            if right == 0 and i == ')': 
                
                left += 1
            else: 
                
                right += 1 if i == '(' else -1
        return left + right

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def minAddToMakeValid(self, S):
        """
        :type S: str
        :rtype: int
        """
        add, bal, = 0, 0
        for c in S:
            bal += 1 if c == '(' else -1
            if bal == -1:
                add += 1
                bal += 1
        return add + bal