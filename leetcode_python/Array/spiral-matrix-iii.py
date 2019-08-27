# V0 

# V1 
# https://www.jiuzhang.com/solution/spiral-matrix-iii/#tag-highlight-lang-python
def spiralMatrixIII(self, R, C, r0, c0):
        """
        :type R: int
        :type C: int
        :type r0: int
        :type c0: int
        :rtype: List[List[int]]
        """
        
        i = r0
        j = c0
        ans = [[i, j]]

        adder = 0

        while len(ans) < R * C:

            adder += 1
            cj = j
            while j < (cj + adder):
                j += 1
                if 0 <= j < C and 0 <= i < R:
                    ans.append([i, j])

            ci = i
            while i < (ci + adder):
                i += 1
                if 0 <= j < C and 0 <= i < R:
                    ans.append([i, j])

            adder += 1
            cj = j
            while j > (cj - adder):
                j -= 1
                if 0 <= j < C and 0 <= i < R:
                    ans.append([i, j])

            ci = i
            while i > (ci - adder):
                i -= 1
                if 0 <= j < C and 0 <= i < R:
                    ans.append([i, j])
        return ans

# V1'
# https://blog.csdn.net/XX_123_1_RJ/article/details/81952905
class Solution:
    def spiralMatrixIII(self, R, C, r0, c0):
        res = [[r0, c0]]
        if R * C == 1: return res
        for k in range(1, 2*(R+C), 2):  # k = lengh of every side 
            for dr, dc, dk in ((0, 1, k), (1, 0, k), (0, -1, k+1), (-1, 0, k+1)):  # left, up, right, down (4 directions of move) 
                for _ in range(dk):
                    r0 += dr
                    c0 += dc
                    if 0 <= r0 < R and 0 <= c0 < C: # check if out of the boader 
                        res.append([r0, c0])  # add the new route 
                        if len(res) == R * C:  # len(res) == R * C, means already finish the process 
                            return res

# V2 
# Time:  O(max(m, n)^2)
# Space: O(1)
class Solution(object):
    def spiralMatrixIII(self, R, C, r0, c0):
        """
        :type R: int
        :type C: int
        :type r0: int
        :type c0: int
        :rtype: List[List[int]]
        """
        r, c = r0, c0
        result = [[r, c]]
        x, y, n, i = 0, 1, 0, 0
        while len(result) < R*C:
            r, c, i = r+x, c+y, i+1
            if 0 <= r < R and 0 <= c < C:
                result.append([r, c])
            if i == n//2+1:
                x, y, n, i = y, -x, n+1, 0
        return result