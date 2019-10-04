# V0 

# V1 
# https://blog.csdn.net/danspace1/article/details/87854254
# http://bookshadow.com/weblog/2016/11/13/leetcode-4sum-ii/
class Solution(object):
    def fourSumCount(self, A, B, C, D):
        """
        :type A: List[int]
        :type B: List[int]
        :type C: List[int]
        :type D: List[int]
        :rtype: int
        """
        ans = 0
        cnt = collections.defaultdict(int)
        for a in A:
            for b in B:
                cnt[a + b] += 1
        for c in C:
            for d in D:
                ans += cnt[-(c + d)]
        return ans

# V1'
# https://www.jiuzhang.com/solution/4sum-ii/#tag-highlight-lang-python
class Solution:
    """
    @param A: a list
    @param B: a list
    @param C: a list
    @param D: a list
    @return: how many tuples (i, j, k, l) there are such that A[i] + B[j] + C[k] + D[l] is zero
    """
    def fourSumCount(self, A, B, C, D):
        # Write your code here
        map = {}
        ans = 0
        for i in range(len(A)):
            for j in range(len(B)):
                sum = A[i] + B[j]
                if sum in map:
                    map[sum] += 1
                else:
                    map[sum] = 1
        for i in range(len(C)):
            for j in range(len(D)):
                sum = -(C[i] + D[j])
                if sum in map:
                    ans += map[sum]
        return ans
        
# V2 
# Time:  O(n^2)
# Space: O(n^2)
import collections
class Solution(object):
    def fourSumCount(self, A, B, C, D):
        """
        :type A: List[int]
        :type B: List[int]
        :type C: List[int]
        :type D: List[int]
        :rtype: int
        """
        A_B_sum = collections.Counter(a+b for a in A for b in B)
        return sum(A_B_sum[-c-d] for c in C for d in D)