# V1 : DEV 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/82915149
class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        cmax = 0
        for i in range(len(A) - 2):
            cmax = max(cmax, A[i])
            if cmax > A[i + 2]:
                return False
        return True

 # V2'
 class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        for i, a in enumerate(A):
            if abs(a - i) > 1:
                return False
        return True

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        return all(abs(v-i) <= 1 for i,v in enumerate(A))
