"""

1914. Cyclically Rotating a Grid
Medium

You are given an m x n integer matrix grid​​​, where m and n are both even integers, and an integer k.

The matrix is composed of several layers, which is shown in the below image, where each color is its own layer:



A cyclic rotation of the matrix is done by cyclically rotating each layer in the matrix. To cyclically rotate a layer once, each element in the layer will take the place of the adjacent element in the counter-clockwise direction. An example rotation is shown below:


Return the matrix after applying k cyclic rotations to it.

 

Example 1:


Input: grid = [[40,10],[30,20]], k = 1
Output: [[10,20],[40,30]]
Explanation: The figures above represent the grid at every state.
Example 2:

  
Input: grid = [[1,2,3,4],[5,6,7,8],[9,10,11,12],[13,14,15,16]], k = 2
Output: [[3,4,8,12],[2,11,10,16],[1,7,6,15],[5,9,13,14]]
Explanation: The figures above represent the grid at every state.
 

Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 50
Both m and n are even integers.
1 <= grid[i][j] <= 5000
1 <= k <= 109

"""

# V0

# V1
# https://www.jianshu.com/p/b0b3391af7b3
class Solution:
    def rotateGrid(self, grid, k):
        m = len(grid)
        n = len(grid[0])
        result = [[0] * n for _ in range(m)]
        for i in range(min(m // 2, n // 2)):
            circle = []
            # Left column
            circle += [(j, i) for j in range(i, m-i)]
            # Bottom row
            circle += [(m-1-i, j) for j in range(i+1, n-i)]
            # Right column
            circle += [(j, n-1-i) for j in range(m-2-i, i-1, -1)]
            # Top row
            circle += [(i, j) for j in range(n-2-i, i, -1)]
            for index, (x, y) in enumerate(circle):
                target_x, target_y = circle[(index+k) % len(circle)]
                result[target_x][target_y] = grid[x][y]
        return result

# V1'
# https://blog.csdn.net/Quincuntial/article/details/119783736

# V1''
# https://leetcode.com/problems/cyclically-rotating-a-grid/discuss/1299584/Python-Easy-to-understand-matrix-rotation
import math
class Solution:
    def rotateGrid(self, grid, k):
        
        def recurData(arr, i, j, m, n):
            start = arr[i][n-1]
            # If i or j lies outside the matrix
            if (i >= m or j >= n):
                return

            # Print First Row
            for p in range(n-1, i, -1):
                #print(start)
                #print(arr[i][p])
                temp = arr[i][p]
                arr[i][p] = start
                start = temp
                #print(arr[i][p], end=" ")

            # Print First Column, if Last and
            # First Column are not same
            if ((n - 1) != j):
                for p in range(i, m-1):
                    temp = arr[p][j]
                    arr[p][j] = start
                    start = temp
            # Print Last Row, if Last and
            # First Row are not same
            if ((m - 1) != i):
                for p in range(j, n-1):
                    temp = arr[m - 1][p]
                    arr[m - 1][p] = start
                    start = temp
                    
            # Print Last Column
            for p in range(m-1, i, -1):
                temp = arr[p][n - 1]
                arr[p][n - 1] = start
                start = temp

            arr[i][n-1] = start
            
            #r = k % (2*(m-1)+2*(n-1))
            #recurData(arr, i + 1, j + 1, m - 1, n - 1)

        #recurData(grid, 0, 0, len(grid), len(grid[0]))
        x, y = len(grid), len(grid[0])
        level = math.ceil(min(x, y)/2)
        for l in range(int(level)):
            m, n = x-2*l, y-2*l
            r = k % (2*m+2*n-4)
            #print(r)
            for i in range(r):
                recurData(grid, l, l, x-l, y-l)
        
        return grid

# V1'''
# https://leetcode.com/problems/cyclically-rotating-a-grid/discuss/1316844/Python-or-Faster-Than-96-or-With-Comments
class Solution:
    def assign(self, temp, rows, cols, i, j, arr, topL, topR, bottomR, bottomL):
        ix = 0
        # top row
        while j < topR[1]:
            temp[i][j] = arr[ix]
            ix += 1
            j += 1

        # last column
        while i < bottomR[0]:
            temp[i][j] = arr[ix]
            ix += 1
            i += 1

        # last row
        while j > bottomL[1]:
            temp[i][j] = arr[ix]
            ix += 1
            j -= 1

        # first column
        while i > topR[0]:
            temp[i][j] = arr[ix]
            ix += 1
            i -= 1

        
    
    def rotateGrid(self, grid, k):
        rows, cols, i, j = len(grid), len(grid[0]), 0, 0
		# Marking the 4 points, which will act as boundaries
        topLeft, topRight, bottomRight, bottomLeft = [0,0],[0,cols-1],[rows-1, cols-1],[rows-1, 0]
        temp = [[-1 for _ in range(cols)] for __ in range(rows) ] 
        while topLeft[0] < rows//2 and topLeft[0] < cols//2:
            arr = []
            # top row
            while j < topRight[1]:
                arr.append(grid[i][j])
                j += 1

            # last column
            while i < bottomRight[0]:
                arr.append(grid[i][j])
                i += 1
                
            # last row
            while j > bottomLeft[1]:
                arr.append(grid[i][j])
                j -= 1
            
            # first column
            while i > topRight[0]:
                arr.append(grid[i][j])
                i -= 1

            n = len(arr)
            arr = arr[k % n:] + arr[:k % n] # Taking modulus value
            
            
            self.assign(temp, rows, cols, i, j, arr,topLeft, topRight, bottomRight, bottomLeft )
            
            i += 1
            j += 1
            topLeft[0] += 1
            topLeft[1] += 1
            topRight[0] += 1
            topRight[1] -= 1
            bottomRight[0] -= 1
            bottomRight[1] -= 1
            bottomLeft[0] -= 1
            bottomLeft[1] += 1
                 
        return temp

# V1''''
# https://leetcode.com/problems/cyclically-rotating-a-grid/discuss/1359971/This-was-a-tough-question!-Horrible-python-solution-that-works
class Solution(object):
    def plus(self, num):
        return num + 1
    
    def minus(self, num):
        return num - 1
    
    def nothing(self, num):
        return num
    
    def get_all_for_layer(self, layer=0):
        try:
            cycle = []
            x, y = layer, layer
            currentPosition = self.grid[x][y]
            order = [(self.plus, self.nothing), (self.nothing, self.plus), (self.minus, self.nothing), (self.nothing, self.minus)]
            while (x, y) not in cycle:
                try:
                    if (x, y) in self.visited or x < 0 or y < 0:
                        x = max(0, x)
                        y = max(0, y)
                        raise Exception("AYYY")
                    self.grid[order[0][0](x)][order[0][1](y)]
                    if (x, y) not in self.visited and len(cycle) == 0 or ([x, y] != cycle[0] and [x, y] != cycle[-1]):
                        cycle.append([x, y])
                    if (order[0][0](x), order[0][1](y)) in self.visited:
                        raise Exception("AYYY")

                    x, y = order[0][0](x), order[0][1](y)
                except Exception as exp:
                    order.pop(0)
                if len(order) == 0:
                    break
            return cycle
        except:
            return []
        
    def new_values_for_layer(self, grid, layer, amount_rotated):
        values = self.get_all_for_layer(layer)
        og_mapping = [{'prev': x} for x in values]
        if len(values) == 0:
            return values
        for i in range(amount_rotated % len(values)):
            x = values.pop(-1)
            values.insert(0, x)
        for i, val in enumerate(values):
            og_mapping[i]['new'] = val
            v = og_mapping[i]
            self.visited.add(tuple(v['new']))
            prev_x, prev_y = v['prev']
            new_x, new_y = v['new']
            self.og_grid[prev_x][prev_y] = self.grid[new_x][new_y]
        return og_mapping
        
    def rotateGrid(self, grid, k):
        self.visited = set()
        self.grid = grid
        self.og_grid = [list(x) for x in grid]
        vals = []
        gridLayers = max(len(grid), len(grid[0])) / 2
        for layer in xrange(gridLayers):
            self.new_values_for_layer(grid, layer, k)
        return self.og_grid

 # V1'''''
 # https://leetcode.com/problems/cyclically-rotating-a-grid/discuss/1379013/simple-python-solution-beat-97.9
 class Solution:
    def rotateGrid(self, grid: List[List[int]], k: int) -> List[List[int]]:
        m, n = len(grid), len(grid[0])
        
        def get_list(layer, mm, nn):
            res = []
            for i in range(nn):
                res.append(grid[layer][layer + i])
            for i in range(1, mm - 1):
                res.append(grid[layer + i][layer + nn - 1])
            for i in range(nn - 1, -1, -1):
                res.append(grid[layer + mm - 1][layer + i])
            for i in range(mm - 2, 0, -1):
                res.append(grid[layer + i][layer])
            return res
            
        def set_list(li, layer, mm, nn):
            idx = 0
            for i in range(nn):
                grid[layer][layer + i] = li[idx]
                idx += 1
            for i in range(1, mm - 1):
                grid[layer + i][layer + nn - 1] = li[idx]
                idx += 1
            for i in range(nn - 1, -1, -1):
                grid[layer + mm - 1][layer + i] = li[idx]
                idx += 1
            for i in range(mm - 2, 0, -1):
                grid[layer + i][layer] = li[idx]
                idx += 1
            
        
        def helper(layer, step):
            mm, nn = m - 2 * layer, n - 2 * layer
            li = get_list(layer, mm, nn)
            li = li[step:] + li[:step]
            set_list(li, layer, mm, nn)
            
        for i in range(min(m, n) // 2):
            helper(i, k % (2 * (m + n - 4 * i - 2)))
            
        return grid

# V2