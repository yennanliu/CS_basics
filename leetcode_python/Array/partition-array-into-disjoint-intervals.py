# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82914423
class Solution(object):
    def partitionDisjoint(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        disjoint = 0
        v = A[0]
        max_so_far = v
        for i in range(len(A)):
            max_so_far = max(max_so_far, A[i])
            if A[i] < v:
                v = max_so_far
                disjoint = i
        return disjoint + 1

# V1'
# https://blog.csdn.net/xx_123_1_rj/article/details/82949858
class Solution:
    def partitionDisjoint(self, A):

        if not A: return 0

        leftMax, rightMax = A[0], A[0]
        res, n = 1, len(A)

        for i in range(n-1):
            if A[i] < leftMax:  
                res = i + 1  # update the boarder (smaller and bigger)
                if rightMax > leftMax:
                    leftMax = rightMax
                rightMax = A[0]  # reset : max value in the bigger group
            elif A[i] > rightMax:
                rightMax = A[i]

        return res

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def partitionDisjoint(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        B = A[:]
        for i in reversed(range(len(A)-1)):
            B[i] = min(B[i], B[i+1])
        p_max = 0
        for i in range(1, len(A)):
            p_max = max(p_max, A[i-1])
            if p_max <= B[i]:
                return i