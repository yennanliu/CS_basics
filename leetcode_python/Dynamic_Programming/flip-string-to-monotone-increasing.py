# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83247054
class Solution(object):
    def minFlipsMonoIncr(self, S):
        """
        :type S: str
        :rtype: int
        """
        N = len(S)
        P = [0] # how many ones
        res = float('inf')
        for s in S:
            P.append(P[-1] + int(s))
        return min(P[i] + (N - P[-1]) - (i - P[i]) for i in range(len(P)))
        
# V1'
# https://www.jiuzhang.com/solution/flip-string-to-monotone-increasing/#tag-highlight-lang-python
class Solution:
    """
    @param S: a string
    @return: the minimum number
    """
    def minFlipsMonoIncr(self, S):
        # Write your code here.
        m, n = 0, 0
        for s in S:
            m += int(s)
            n = min(m, n + 1 - int(s))
        return n

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def minFlipsMonoIncr(self, S):
        """
        :type S: str
        :rtype: int
        """
        flip0, flip1 = 0, 0
        for c in S:
            flip0 += int(c == '1')
            flip1 = min(flip0, flip1 + int(c == '0'))
        return flip1