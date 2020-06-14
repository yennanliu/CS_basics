# V0
class Solution(object):
    def isIdealPermutation(self, A):
        for idx, value in enumerate(A):
            if abs(idx - value) > 1:
                return False
        return True

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82915149
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

### Test case : dev 

# V1'
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

# V1'''
# https://leetcode.com/problems/global-and-local-inversions/discuss/635114/Python-intuitive-approach
class Solution(object):
    def isIdealPermutation(self, A: List[int]) -> bool:
        for idx, value in enumerate(A):
            if abs(idx - value) > 1:
                return False
        return True


# V1'''
# http://bookshadow.com/weblog/2018/01/28/leetcode-global-and-local-inversions/
# JAVA

# V1''''
# https://leetcode.com/problems/global-and-local-inversions/discuss/535557/775-Global-and-Local-Inversions-Py-All-in-One-by-Talse
class Solution(object):
    def isIdealPermutation(self, A: List[int]) -> bool:
            mx = 0
            for i in range(len(A) - 2):
                mx = max(mx, A[i])
                if mx > A[i+2]: return False
            return True

# V1'''
# http://bookshadow.com/weblog/2018/01/28/leetcode-global-and-local-inversions/
class Solution(object):
    def isIdealPermutation(self, A: List[int]) -> bool:
            return all(abs(A[i] - i) <= 1 for i in range(len(A)))

# V2
# https://github.com/kamyu104/LeetCode-Solutions/blob/master/Python/global-and-local-inversions.py
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isIdealPermutation(self, A):
        """
        :type A: List[int]
        :rtype: bool
        """
        return all(abs(v-i) <= 1 for i,v in enumerate(A))