"""

1324. Print Words Vertically
Medium

Given a string s. Return all the words vertically in the same order in which they appear in s.

Words are returned as a list of strings, complete with spaces when is necessary.
(Trailing spaces are not allowed).

Each word would be put on only one column and that in one column there will be only one word.


Example 1:

Input: s = "HOW ARE YOU"

Output: ["HAY","ORO","WEU"]

Explanation: Each word is printed vertically.

 "HAY"

 "ORO"

 "WEU"

Example 2:

Input: s = "TO BE OR NOT TO BE"

Output: ["TBONTB","OEROOE","   T"]

Explanation: Trailing spaces is not allowed.

"TBONTB"

"OEROOE"

"   T"

Example 3:

Input: s = "CONTEST IS COMING"

Output: ["CIC","OSO","N M","T I","E N","S G","T"]


Constraints:

1 <= s.length <= 200
s contains only upper case English letters.
It's guaranteed that there is only one space between 2 words.

"""

# V0
# IDEA : MATRIX TRANSPOSE with padding, then right-strip
#
#   row j of the output reads the j-th character of every word, left to right.
#   words shorter than j contribute a space (the column is "empty" there).
#
#   the number of output rows equals the longest word's length.
#   NOTE : interior spaces MUST stay (they align the columns) while trailing
#          spaces must go -> build the row then pop from the tail only.
#
# time = O(n * m), space = O(n * m)   n = #words, m = longest word length
class Solution(object):
    def printVertically(self, s):
        words = s.split()
        width = max(len(w) for w in words)

        res = []
        for j in range(width):
            row = []
            for w in words:
                row.append(w[j] if j < len(w) else ' ')
            while row and row[-1] == ' ':
                row.pop()
            res.append(''.join(row))
        return res
