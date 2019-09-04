# V0
class Solution:
    def sortArrayByParityII(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        A.sort(key = lambda x : x % 2)
        N = len(A)
        res = []
        for i in range(N // 2):
            res.append(A[i])
            res.append(A[N - 1 - i])
        return res
        
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83045735
class Solution(object):
    def sortArrayByParityII(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        odd = [x for x in A if x % 2 == 1]
        even = [x for x in A if x % 2 == 0]
        res = []
        iseven = True
        while odd or even:
            if iseven:
                res.append(even.pop())
            else:
                res.append(odd.pop())
            iseven = not iseven
        return res

# V1'  
# https://blog.csdn.net/fuxuemingzhu/article/details/83045735
class Solution:
    def sortArrayByParityII(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        A.sort(key = lambda x : x % 2)
        N = len(A)
        res = []
        for i in range(N // 2):
            res.append(A[i])
            res.append(A[N - 1 - i])
        return res

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83045735
class Solution:
    def sortArrayByParityII(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        N = len(A)
        res = [0] * N
        even, odd = 0, 1
        for a in A:
            if a % 2 == 1:
                res[odd] = a
                odd += 2
            else:
                res[even] = a
                even += 2
        return res

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def sortArrayByParityII(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        j = 1
        for i in range(0, len(A), 2):
            if A[i] % 2:
                while A[j] % 2:
                    j += 2
                A[i], A[j] = A[j], A[i]
        return A