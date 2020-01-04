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
