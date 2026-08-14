"""

1812. Determine Color of a Chessboard Square
Easy

You are given coordinates, a string that represents the coordinates of a square of the chessboard. Below is a chessboard for your reference.

Return true if the square is white, and false if the square is black.

The coordinate will always represent a valid chessboard square. The coordinate will always have the letter first, and the number second.


Example 1:

Input: coordinates = "a1"
Output: false
Explanation: From the chessboard above, the square with coordinates "a1" is black, so return false.

Example 2:

Input: coordinates = "h3"
Output: true
Explanation: From the chessboard above, the square with coordinates "h3" is white, so return true.

Example 3:

Input: coordinates = "c7"
Output: false


Constraints:

coordinates.length == 2
'a' <= coordinates[0] <= 'h'
'1' <= coordinates[1] <= '8'

"""

# V0
# IDEA : PARITY (a chessboard colours by (file + rank) % 2)
#
#   "a1" is black. moving one file OR one rank flips the colour, so the colour
#   is decided purely by the parity of (file index + rank index).
#
#   NOTE : we can add the raw ord() values instead of normalising to 0-based
#          indices -- shifting both by a constant does not change the parity
#          relation, we only need to fix which parity means white.
#          ord('a') + ord('1') = 97 + 49 = 146 -> even -> black,
#          therefore ODD sum == white.
#
# time = O(1), space = O(1)
class Solution(object):
    def squareIsWhite(self, coordinates):
        return (ord(coordinates[0]) + ord(coordinates[1])) % 2 == 1
