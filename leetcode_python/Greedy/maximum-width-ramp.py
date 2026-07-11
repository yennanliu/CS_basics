# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/85223568
# IDEA : STACK 
# time = O(n^2)  # worst case: inner while doesn't pop, can rescan full stack per index
# space = O(n)
class Solution(object):
    def maxWidthRamp(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A)
        stack = []
        res = 0
        for i, a in enumerate(A):
            if not stack or stack[-1][1] > a:
                stack.append((i, a))
            else:
                x = len(stack) - 1
                while x >= 0 and stack[x][1] <= a:
                    res = max(res, i - stack[x][0])
                    x -= 1
        return res

# V2
# time = O(n)
# space = O(n)
class Solution(object):
    def maxWidthRamp(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        result = 0
        s = []
        for i in A:
            if not s or A[s[-1]] > A[i]:
                s.append(i)
        for j in reversed(range(len(A))):
            while s and A[s[-1]] <= A[j]:
                result = max(result, j-s.pop())
        return result