"""

957. Prison Cells After N Days
Medium

There are 8 prison cells in a row and each cell is either occupied or vacant.

Each day, whether the cell is occupied or vacant changes according to the following rules:

If a cell has two adjacent neighbors that are both occupied or both vacant, then the cell becomes occupied.
Otherwise, it becomes vacant.

Note that because the prison is a row, the first and the last cells in the row can't have two adjacent neighbors.

You are given an integer array cells where cells[i] == 1 if the i^th cell is occupied and cells[i] == 0 if the i^th cell is vacant, and you are given an integer n.

Return the state of the prison after n days (i.e., n such changes described above).

Example 1:

Input: cells = [0,1,0,1,1,0,0,1], n = 7
Output: [0,0,1,1,0,0,0,0]
Explanation: The following table summarizes the state of the prison on each day:
Day 0: [0, 1, 0, 1, 1, 0, 0, 1]
Day 1: [0, 1, 1, 0, 0, 0, 0, 0]
Day 2: [0, 0, 0, 0, 1, 1, 1, 0]
Day 3: [0, 1, 1, 0, 0, 1, 0, 0]
Day 4: [0, 0, 0, 0, 0, 1, 0, 0]
Day 5: [0, 1, 1, 1, 0, 1, 0, 0]
Day 6: [0, 0, 1, 0, 1, 1, 0, 0]
Day 7: [0, 0, 1, 1, 0, 0, 0, 0]

Example 2:

Input: cells = [1,0,0,1,0,0,1,0], n = 1000000000
Output: [0,0,1,1,1,1,1,0]

Constraints:

cells.length == 8
cells[i] is either 0 or 1.
1 <= n <= 10^9

"""

# V0 

# V1 
# https://www.twblogs.net/a/5c162c02bd9eee5e40bbaace/zh-cn
# IDEA : DP + MOD
# time = O(1)
# space = O(1)
class Solution(object):
    def prisonAfterNDays(self, oldcells, N):
        """
        :type cells: List[int]
        :type N: int
        :rtype: List[int]
        """
        cells = copy.deepcopy(oldcells)
        count = 0
        N %= 14
        if N == 0:
            N = 14
        while count < N:
            newCell = [0] * 8
            for i in range(1, 7):
                if cells[i - 1] == cells[i + 1]:
                    newCell[i] = 1
                else:
                    newCell[i] = 0
            cells = newCell
            count += 1
        return cells

# V2
# time = O(1)
# space = O(1)
class Solution(object):
    def prisonAfterNDays(self, cells, N):
        """
        :type cells: List[int]
        :type N: int
        :rtype: List[int]
        """
        N -= max(N-1, 0) // 14 * 14  # 14 is got from Solution2
        for i in range(N):
            cells = [0] + [cells[i-1] ^ cells[i+1] ^ 1 for i in range(1, 7)] + [0]
        return cells

# time = O(1)
# space = O(1)
class Solution2(object):
    def prisonAfterNDays(self, cells, N):
        """
        :type cells: List[int]
        :type N: int
        :rtype: List[int]
        """
        cells = tuple(cells)
        lookup = {}
        while N:
            lookup[cells] = N
            N -= 1
            cells = tuple([0] + [cells[i - 1] ^ cells[i + 1] ^ 1 for i in range(1, 7)] + [0])
            if cells in lookup:
                assert(lookup[cells] - N in (1, 7, 14))
                N %= lookup[cells] - N
                break
        while N:
            N -= 1
            cells = tuple([0] + [cells[i - 1] ^ cells[i + 1] ^ 1 for i in range(1, 7)] + [0])
        return list(cells)