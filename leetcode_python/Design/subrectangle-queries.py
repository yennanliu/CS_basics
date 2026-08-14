"""

1476. Subrectangle Queries
Medium

Implement the class SubrectangleQueries which receives a rows x cols rectangle as a matrix of
integers in the constructor and supports two methods:

1. updateSubrectangle(int row1, int col1, int row2, int col2, int newValue)

- Updates all values with newValue in the subrectangle whose upper left coordinate is (row1,col1)
  and bottom right coordinate is (row2,col2).

2. getValue(int row, int col)

- Returns the current value of the coordinate (row,col) from the rectangle.


Example 1:

Input
["SubrectangleQueries","getValue","updateSubrectangle","getValue","getValue","updateSubrectangle","getValue","getValue"]
[[[[1,2,1],[4,3,4],[3,2,1],[1,1,1]]],[0,2],[0,0,3,2,5],[0,2],[3,1],[3,0,3,2,10],[3,1],[0,2]]
Output
[null,1,null,5,5,null,10,5]
Explanation
SubrectangleQueries subrectangleQueries = new SubrectangleQueries([[1,2,1],[4,3,4],[3,2,1],[1,1,1]]);
// The initial rectangle (4x3) looks like:
// 1 2 1
// 4 3 4
// 3 2 1
// 1 1 1
subrectangleQueries.getValue(0, 2); // return 1
subrectangleQueries.updateSubrectangle(0, 0, 3, 2, 5);
// After this update the rectangle looks like:
// 5 5 5
// 5 5 5
// 5 5 5
// 5 5 5
subrectangleQueries.getValue(0, 2); // return 5
subrectangleQueries.getValue(3, 1); // return 5
subrectangleQueries.updateSubrectangle(3, 0, 3, 2, 10);
// After this update the rectangle looks like:
// 5   5   5
// 5   5   5
// 5   5   5
// 10  10  10
subrectangleQueries.getValue(3, 1); // return 10
subrectangleQueries.getValue(0, 2); // return 5

Example 2:

Input
["SubrectangleQueries","getValue","updateSubrectangle","getValue","getValue","updateSubrectangle","getValue"]
[[[[1,1,1],[2,2,2],[3,3,3]]],[0,0],[0,0,2,2,100],[0,0],[2,2],[1,1,2,2,20],[2,2]]
Output
[null,1,null,100,100,null,20]


Constraints:

There will be at most 500 operations considering both methods: updateSubrectangle and getValue.
1 <= rows, cols <= 100
rows == rectangle.length
cols == rectangle[i].length
0 <= row1 <= row2 < rows
0 <= col1 <= col2 < cols
1 <= newValue, rectangle[i][j] <= 10^9
0 <= row < rows
0 <= col < cols

"""

# V0
# IDEA : BRUTE FORCE UPDATE (just overwrite the cells)
#        -> at most 500 ops and the grid is at most 100 x 100,
#           so a full sub-rectangle overwrite is cheap enough.
# time = O(rows * cols) per update, O(1) per getValue
# space = O(rows * cols)
class SubrectangleQueries(object):
    def __init__(self, rectangle):
        # NOTE !!! keep a reference (LC allows in-place mutation of the input)
        self.g = rectangle

    def updateSubrectangle(self, row1, col1, row2, col2, newValue):
        for r in range(row1, row2 + 1):
            for c in range(col1, col2 + 1):
                self.g[r][c] = newValue

    def getValue(self, row, col):
        return self.g[row][col]


# V1
# IDEA : LOG THE UPDATES, RESOLVE ON READ
#        -> O(1) update. on getValue, scan the update log BACKWARDS
#           and return the first (= newest) update that covers (row, col).
#        -> better when updates far outnumber reads.
# time = O(1) per update, O(u) per getValue, u = number of updates
# space = O(rows * cols + u)
class SubrectangleQueries2(object):
    def __init__(self, rectangle):
        self.g = rectangle
        self.ops = []

    def updateSubrectangle(self, row1, col1, row2, col2, newValue):
        self.ops.append((row1, col1, row2, col2, newValue))

    def getValue(self, row, col):
        for r1, c1, r2, c2, v in reversed(self.ops):
            if r1 <= row <= r2 and c1 <= col <= c2:
                return v
        return self.g[row][col]


# Your SubrectangleQueries object will be instantiated and called as such:
# obj = SubrectangleQueries(rectangle)
# obj.updateSubrectangle(row1,col1,row2,col2,newValue)
# param_2 = obj.getValue(row,col)
