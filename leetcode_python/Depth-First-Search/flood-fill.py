"""

733. Flood Fill
Easy

An image is represented by an m x n integer grid image where image[i][j] represents the pixel value of the image.

You are also given three integers sr, sc, and newColor. You should perform a flood fill on the image starting from the pixel image[sr][sc].

To perform a flood fill, consider the starting pixel, plus any pixels connected 4-directionally to the starting pixel of the same color as the starting pixel, plus any pixels connected 4-directionally to those pixels (also with the same color), and so on. Replace the color of all of the aforementioned pixels with newColor.

Return the modified image after performing the flood fill.

 

Example 1:


Input: image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, newColor = 2
Output: [[2,2,2],[2,2,0],[2,0,1]]
Explanation: From the center of the image with position (sr, sc) = (1, 1) (i.e., the red pixel), all pixels connected by a path of the same color as the starting pixel (i.e., the blue pixels) are colored with the new color.
Note the bottom corner is not colored 2, because it is not 4-directionally connected to the starting pixel.
Example 2:

Input: image = [[0,0,0],[0,0,0]], sr = 0, sc = 0, newColor = 2
Output: [[2,2,2],[2,2,2]]
 

Constraints:

m == image.length
n == image[i].length
1 <= m, n <= 50
0 <= image[i][j], newColor < 216
0 <= sr < m
0 <= sc < n

"""

# V0 
# IDEA : DFS 
class Solution(object):
    def floodFill(self, image, sr, sc, newColor):

        directions = [(0, -1), (0, 1), (-1, 0), (1, 0)]

        def dfs(image, r, c, newColor, color):
            if  (0 <= r < len(image) and \
                 0 <= c < len(image[0]) and \
                    image[r][c] == color):
                for d in directions:
                    dfs(image, r+d[0], c+d[1], newColor, color)
                    image[r][c] = newColor
            else:
                return 
        color = image[sr][sc]
        if color == newColor: return image
        dfs(image, sr, sc, newColor, color)
        return image

# V1'
# https://www.jiuzhang.com/solution/flood-fill/#tag-highlight-lang-python
# IDEA : DFS 
class Solution(object):
    def floodFill(self, image, sr, sc, newColor):
        rows, cols, orig_color = len(image), len(image[0]), image[sr][sc]
        def traverse(row, col):
            if (not (0 <= row < rows and 0 <= col < cols)) or image[row][col] != orig_color:
                return
            image[row][col] = newColor
            [traverse(row + x, col + y) for (x, y) in ((0, 1), (1, 0), (0, -1), (-1, 0))]
        if orig_color != newColor:
            traverse(sr, sc)
        return image 

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79401716
# IDEA : DFS 
class Solution(object):
    def floodFill(self, image, sr, sc, newColor):
        """
        :type image: List[List[int]]
        :type sr: int
        :type sc: int
        :type newColor: int
        :rtype: List[List[int]]
        """
        SR, SC = len(image), len(image[0])
        color = image[sr][sc]
        if color == newColor: return image
        def dfs(r, c):
            if image[r][c] == color:
                image[r][c] = newColor
                if r >= 1: dfs(r - 1, c)
                if r < SR - 1: dfs(r + 1, c)
                if c >= 1: dfs(r, c - 1)
                if c < SC - 1: dfs(r, c + 1)
        dfs(sr, sc)
        return image

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79401716
# IDEA : BFS 
class Solution:
    def floodFill(self, image, sr, sc, newColor):
        """
        :type image: List[List[int]]
        :type sr: int
        :type sc: int
        :type newColor: int
        :rtype: List[List[int]]
        """
        que = collections.deque()
        que.append((sr, sc))
        start = image[sr][sc]
        if start == newColor: return image
        M, N = len(image), len(image[0])
        directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        while que:
            pos = que.popleft()
            image[pos[0]][pos[1]] = newColor
            for d in directions:
                newx, newy = pos[0] + d[0], pos[1] + d[1]
                if 0 <= newx < M and 0 <= newy < N and image[newx][newy] == start:
                    que.append((newx, newy))
        return image

# V2 
# Time:  O(m * n)
# Space: O(m * n)
class Solution(object):
    def floodFill(self, image, sr, sc, newColor):
        """
        :type image: List[List[int]]
        :type sr: int
        :type sc: int
        :type newColor: int
        :rtype: List[List[int]]
        """
        directions = [(0, -1), (0, 1), (-1, 0), (1, 0)]

        def dfs(image, r, c, newColor, color):
            if not (0 <= r < len(image) and \
                    0 <= c < len(image[0]) and \
                    image[r][c] == color):
                return

            image[r][c] = newColor
            for d in directions:
                dfs(image, r+d[0], c+d[1], newColor, color)

        color = image[sr][sc]
        if color == newColor: return image
        dfs(image, sr, sc, newColor, color)
        return image
