"""

631. Design Excel Sum Formula
Hard

Design the basic function of Excel and implement the function of the sum formula.

Implement the Excel class:

- Excel(int height, char width) Initializes the object with the height and the width of
  the sheet. The sheet is an integer matrix mat of size height x width with the row index
  in the range [1, height] and the column index in the range ['A', width].
  All the values should be zero initially.
- void set(int row, char column, int val) Changes the value at mat[row][column] to be val.
- int get(int row, char column) Returns the value at mat[row][column].
- int sum(int row, char column, List<String> numbers) Sets the value at mat[row][column]
  to be the sum of cells represented by numbers and returns the value at mat[row][column].
  This sum formula should exist until this cell is overlapped by another value or another
  sum formula. numbers[i] could be on the format:
    - "ColRow" that represents a single cell.
      For example, "F7" represents the cell mat[7]['F'].
    - "ColRow1:ColRow2" that represents a range of cells. The range will always be a
      rectangle where "ColRow1" represent the position of the top-left cell, and
      "ColRow2" represents the position of the bottom-right cell.
      For example, "B3:F7" represents the cells mat[i][j] for 3 <= i <= 7 and 'B' <= j <= 'F'.

Note: You could assume that there will not be any circular sum reference.
For example, mat[1]['A'] == sum(1, "B") and mat[1]['B'] == sum(1, "A").

Example 1:

Input
["Excel", "set", "sum", "set", "get"]
[[3, "C"], [1, "A", 2], [3, "C", ["A1", "A1:B2"]], [2, "B", 2], [3, "C"]]
Output
[null, null, 4, null, 6]

Explanation
Excel excel = new Excel(3, "C");
 // construct a 3*3 2D array with all zero.
 //   A B C
 // 1 0 0 0
 // 2 0 0 0
 // 3 0 0 0
excel.set(1, "A", 2);
 // set mat[1]["A"] to be 2.
 //   A B C
 // 1 2 0 0
 // 2 0 0 0
 // 3 0 0 0
excel.sum(3, "C", ["A1", "A1:B2"]); // return 4
 // set mat[3]["C"] to be the sum of value at mat[1]["A"] and the values sum of the
 // rectangle range whose top-left cell is mat[1]["A"] and bottom-right cell is mat[2]["B"].
 //   A B C
 // 1 2 0 0
 // 2 0 0 0
 // 3 0 0 4
excel.set(2, "B", 2);
 // set mat[2]["B"] to be 2. Note mat[3]["C"] should also be changed.
 //   A B C
 // 1 2 0 0
 // 2 0 2 0
 // 3 0 0 6
excel.get(3, "C"); // return 6

Constraints:

1 <= height <= 26
'A' <= width <= 'Z'
1 <= row <= height
'A' <= column <= width
-100 <= val <= 100
1 <= numbers.length <= 5
numbers[i] has the format "ColRow" or "ColRow1:ColRow2".
At most 100 calls will be made to set, get, and sum.

"""

# V0
# IDEA : LAZY EVALUATION -- store the formula, resolve it recursively on get()
#
#   Two pieces of state per cell:
#     - mat[r][c]      : a plain literal value
#     - formula[(r,c)] : a Counter {referenced cell -> how many times it is referenced}
#                        (a cell can legitimately appear twice, e.g. ["A1", "A1:B2"])
#
#   set()  wipes any formula on that cell and writes a literal.
#   sum()  records the formula (so later edits to referenced cells propagate).
#   get()  walks the formula tree recursively. No circular reference is
#          guaranteed by the problem, so the recursion always terminates.
#
#   The sheet is at most 26 x 26 and at most 100 calls are made, so the naive
#   recursive re-evaluation is plenty fast (no topological sort needed).
#
# time = O(1) for set, O(cells referenced transitively) for get / sum
# space = O(height * width)
from collections import Counter
class Excel(object):
    def __init__(self, height, width):
        """
        :type height: int
        :type width: str
        """
        self.h = height
        self.w = ord(width) - ord('A') + 1
        # rows are 1-indexed -> allocate height + 1 rows and ignore row 0
        self.mat = [[0] * self.w for _ in range(height + 1)]
        self.formula = {}   # (row, col_idx) -> Counter of (row, col_idx)

    def set(self, row, column, val):
        """
        :type row: int
        :type column: str
        :type val: int
        :rtype: None
        """
        c = ord(column) - ord('A')
        # a literal write overrides any formula previously attached to this cell
        self.formula.pop((row, c), None)
        self.mat[row][c] = val

    def get(self, row, column):
        """
        :type row: int
        :type column: str
        :rtype: int
        """
        return self._eval(row, ord(column) - ord('A'))

    def sum(self, row, column, numbers):
        """
        :type row: int
        :type column: str
        :type numbers: List[str]
        :rtype: int
        """
        c = ord(column) - ord('A')
        refs = Counter()
        for token in numbers:
            if ':' in token:
                start, end = token.split(':')
                c1, r1 = self._parse(start)
                c2, r2 = self._parse(end)
                for rr in range(r1, r2 + 1):
                    for cc in range(c1, c2 + 1):
                        refs[(rr, cc)] += 1
            else:
                cc, rr = self._parse(token)
                refs[(rr, cc)] += 1

        self.formula[(row, c)] = refs
        return self._eval(row, c)

    def _eval(self, r, c):
        """resolve a cell: follow its formula if it has one, else read the literal"""
        if (r, c) in self.formula:
            total = 0
            for (rr, cc), times in self.formula[(r, c)].items():
                total += self._eval(rr, cc) * times
            return total
        return self.mat[r][c]

    @staticmethod
    def _parse(token):
        """ "F7" -> (col index 5, row 7) """
        return ord(token[0]) - ord('A'), int(token[1:])


# Your Excel object will be instantiated and called as such:
# obj = Excel(height, width)
# obj.set(row, column, val)
# param_2 = obj.get(row, column)
# param_3 = obj.sum(row, column, numbers)
