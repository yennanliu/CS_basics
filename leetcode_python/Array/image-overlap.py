"""

835. Image Overlap
Medium

You are given two images, img1 and img2, represented as binary, square matrices of size n x n. A binary matrix has only 0s and 1s as values.

We translate one image however we choose by sliding all the 1 bits left, right, up, and/or down any number of units. We then place it on top of the other image. We can then calculate the overlap by counting the number of positions that have a 1 in both images.

Note also that a translation does not include any kind of rotation. Any 1 bits that are translated outside of the matrix borders are erased.

Return the largest possible overlap.

Example 1:

https://assets.leetcode.com/uploads/2020/09/09/overlap1.jpg

Input: img1 = [[1,1,0],[0,1,0],[0,1,0]], img2 = [[0,0,0],[0,1,1],[0,0,1]]
Output: 3
Explanation: We translate img1 to right by 1 unit and down by 1 unit.
https://assets.leetcode.com/uploads/2020/09/09/overlap_step1.jpg
The number of positions that have a 1 in both images is 3 (shown in red).
https://assets.leetcode.com/uploads/2020/09/09/overlap_step2.jpg

Example 2:

Input: img1 = [[1]], img2 = [[1]]
Output: 1

Example 3:

Input: img1 = [[0]], img2 = [[0]]
Output: 0

Constraints:

n == img1.length == img1[i].length
n == img2.length == img2[i].length
1 <= n <= 30
img1[i][j] is either 0 or 1.
img2[i][j] is either 0 or 1.

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82597238
# https://leetcode.com/problems/image-overlap/discuss/130623/C++JavaPython-Straight-Forward
# time = O(n^4)
# space = O(n^2)
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
# time = O(n^4)
# space = O(n^2)
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