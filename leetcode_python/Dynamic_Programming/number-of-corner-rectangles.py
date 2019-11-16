# V0

# V1
# http://bookshadow.com/weblog/2017/12/17/leetcode-number-of-corner-rectangles/
# IDEA : DP
# JAVA
# class Solution {
#     public int countCornerRectangles(int[][] grid) {
#         int m = grid.length, n = grid[0].length;
#         int ans = 0;
#         for (int x = 0; x < m; x++) {
#             for (int y = x + 1; y < m; y++) {
#                 int cnt = 0;
#                 for (int z = 0; z < n; z++) {
#                     if (grid[x][z] == 1 && grid[y][z] == 1) {
#                         cnt++;
#                     }
#                 }
#                 ans += cnt * (cnt - 1) / 2;
#             }
#         }
#         return ans;
#     }
# }

# V1'
# https://www.jiuzhang.com/solution/number-of-corner-rectangles/#tag-highlight-lang-python
class Solution(object):
    def countCornerRectangles(self, grid):
        rows = [[c for c, val in enumerate(row) if val]
                for row in grid]
        N = sum(sum(row) for row in grid)
        SQRTN = int(N**.5)

        ans = 0
        count = collections.Counter()
        for r, row in enumerate(rows):
            if len(row) >= SQRTN:
                target = set(row)
                for r2, row2 in enumerate(rows):
                    if r2 <= r and len(row2) >= SQRTN:
                        continue
                    found = sum(1 for c2 in row2 if c2 in target)
                    ans += found * (found - 1) / 2
            else:
                for pair in itertools.combinations(row, 2):
                    ans += count[pair]
                    count[pair] += 1
        return ans

# V2
# Time:  O(n * m^2), n is the number of rows with 1s, m is the number of cols with 1s
# Space: O(n * m)
class Solution(object):
    def countCornerRectangles(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        rows = [[c for c, val in enumerate(row) if val]
                for row in grid]
        result = 0
        for i in range(len(rows)):
            lookup = set(rows[i])
            for j in range(i):
                count = sum(1 for c in rows[j] if c in lookup)
                result += count*(count-1)/2
        return result
