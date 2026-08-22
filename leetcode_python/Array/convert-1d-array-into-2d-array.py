"""

2022. Convert 1D Array Into 2D Array
Easy

You are given a 0-indexed 1-dimensional (1D) integer array original, and two integers, m and n. You are tasked with creating a 2-dimensional (2D) array with m rows and n columns using all the elements from original.

The elements from indices 0 to n - 1 (inclusive) of original should form the first row of the constructed 2D array, the elements from indices n to 2 * n - 1 (inclusive) should form the second row of the constructed 2D array, and so on.

Return an m x n 2D array constructed according to the above procedure, or an empty 2D array if it is impossible.


Example 1:

Input: original = [1,2,3,4], m = 2, n = 2
Output: [[1,2],[3,4]]
Explanation: The constructed 2D array should contain 2 rows and 2 columns.
The first group of n=2 elements in original, [1,2], becomes the first row in the constructed 2D array.
The second group of n=2 elements in original, [3,4], becomes the second row in the constructed 2D array.

Example 2:

Input: original = [1,2,3], m = 1, n = 3
Output: [[1,2,3]]
Explanation: The constructed 2D array should contain 1 row and 3 columns.
Put all three elements in original into the first row of the constructed 2D array.

Example 3:

Input: original = [1,2], m = 1, n = 1
Output: []
Explanation: There are 2 elements in original.
It is impossible to fit 2 elements in a 1x1 2D array, so return an empty 2D array.


Constraints:

1 <= original.length <= 5 * 10^4
1 <= original[i] <= 10^5
1 <= m, n <= 4 * 10^4

"""

# V0
# IDEA : SLICING (only feasible when len(original) == m * n)
#
#   the layout is purely row-major, so once the size check passes the answer
#   is just consecutive chunks of length n.
#
#   NOTE : the size check must come first — m * n can overflow the array in
#          either direction (too many or too few elements).
#
# time = O(m * n), space = O(1) beyond the output
class Solution(object):
    def construct2DArray(self, original, m, n):
        if len(original) != m * n:
            return []
        return [original[i * n:(i + 1) * n] for i in range(m)]


# V0-1
# IDEA : ONE PASS + INDEX ARITHMETIC (divmod) INTO A PRE-ALLOCATED GRID
#
#   the flat index k of an element determines its cell directly :
#       row = k // n,  col = k % n
#   so allocate the m x n grid up front and SCATTER each value into place.
#   nothing is sliced, so this also works on a one-shot stream / generator
#   where `original[a:b]` is not available at all.
#
#   NOTE : build the rows with a comprehension — [[0] * n] * m would alias
#          the same row object m times and every write would hit all rows.
#
# time = O(m * n)
# space = O(1) beyond the output
class Solution(object):
    def construct2DArray(self, original, m, n):
        if len(original) != m * n:
            return []
        grid = [[0] * n for _ in range(m)]
        for k, v in enumerate(original):
            r, c = divmod(k, n)
            grid[r][c] = v
        return grid


# V0-2
# IDEA : ONE SHARED ITERATOR — THE zip(*[it] * n) "GROUPER" IDIOM
#
#   zip pulls one item from each of its arguments per round; feeding it the
#   SAME iterator n times therefore hands back the next n items of the input
#   as one tuple. consuming the iterator is what advances the chunking, so
#   there is no arithmetic and no indexing anywhere — the rows fall out of
#   iterator exhaustion.
#
#   NOTE : the size guard still has to come first. zip stops at the shortest
#          argument, so a bad m * n would silently drop the tail instead of
#          reporting the impossible case.
#
# time = O(m * n)
# space = O(1) beyond the output
class Solution(object):
    def construct2DArray(self, original, m, n):
        if len(original) != m * n:
            return []
        it = iter(original)
        return [list(chunk) for chunk in zip(*[it] * n)]
