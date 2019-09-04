# V0 

# V1 
# https://www.jiuzhang.com/solution/921-minimum-add-to-make-parentheses-valid/#tag-other-lang-python
# IDEA : COUNT THE "LEFT" AND  "RIGHT" RESPECTIVELY (# ADD TO LEFT, # ADD TO RIGHT)
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
# Time:  O(n)
# Space: O(1)
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