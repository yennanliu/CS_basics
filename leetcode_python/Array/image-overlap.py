# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82597238
# https://leetcode.com/problems/image-overlap/discuss/130623/C++JavaPython-Straight-Forward
import collections
class Solution:
    def largestOverlap(self, A, B):
        """
        :type A: List[List[int]]
        :type B: List[List[int]]
        :rtype: int
        """
        N = len(A)
        LA = [(xi, yi) for xi in range(N) for yi in range(N) if A[xi][yi]]
        LB = [(xi, yi) for xi in range(N) for yi in range(N) if B[xi][yi]]
        d = collections.Counter([(x1 - x2, y1 - y2) for (x1, y1) in LA for (x2, y2) in LB])
        return max(d.values() or [0])
         
# V2 
# Time:  O(n^4)
# Space: O(n^2)
class Solution(object):
    def largestOverlap(self, A, B):
        """
        :type A: List[List[int]]
        :type B: List[List[int]]
        :rtype: int
        """
        count = [0] * (2*len(A)-1)**2
        for i, row in enumerate(A):
            for j, v in enumerate(row):
                if not v:
                    continue
                for i2, row2 in enumerate(B):
                    for j2, v2 in enumerate(row2):
                        if not v2:
                            continue
                        count[(len(A)-1+i-i2)*(2*len(A)-1) +
                              len(A)-1+j-j2] += 1
        return max(count)