# V1  : DEV 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79404220
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A)
        self.res = 0
        self.slices(A, N - 1)
        return self.res
    
    def slices(self, A, end):
        if end < 2: return 0
        op = 0
        if A[end] - A[end - 1] == A[end - 1] - A[end - 2]:
            op = 1 + self.slices(A, end - 1)
            self.res += op 
        else:
            self.slices(A, end - 1)
        return op

# V2'
class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        count = 0
        addend = 0
        for i in range(len(A) - 2):
            if A[i + 1] - A[i] == A[i + 2] - A[i + 1]:
                addend += 1
                count += addend
            else:
                addend = 0
        return count

        

# V3 
# Time:  O(n)
# Space: O(1)

class Solution(object):
    def numberOfArithmeticSlices(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        res, i = 0, 0
        while i+2 < len(A):
            start = i
            while i+2 < len(A) and A[i+2] + A[i] == 2*A[i+1]:
                res += i - start + 1
                i += 1
            i += 1

        return res