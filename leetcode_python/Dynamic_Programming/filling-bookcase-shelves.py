"""

1105. Filling Bookcase Shelves
Medium

You are given an array books where books[i] = [thickness_i, height_i] indicates
the thickness and height of the i-th book. You are also given an integer shelfWidth.

We want to place these books in order onto bookcase shelves that have a total
width shelfWidth.

We choose some of the books to place on this shelf such that the sum of their
thickness is less than or equal to shelfWidth, then build another level of the
shelf of the bookcase so that the total height of the bookcase has increased by
the maximum height of the books we just put down. We repeat this process until
there are no more books to place.

Note that at each step of the above process, the order of the books we place is
the same order as the given sequence of books.

- For example, if we have an ordered list of 5 books, we might place the first
  and second book onto the first shelf, the third book on the second shelf, and
  the fourth and fifth book on the last shelf.

Return the minimum possible height that the total bookshelf can be after placing
shelves in this manner.


Example 1:

Input: books = [[1,1],[2,3],[2,3],[1,1],[1,1],[1,1],[1,2]], shelfWidth = 4
Output: 6
Explanation:
The sum of the heights of the 3 shelves is 1 + 3 + 2 = 6.
Notice that book number 2 does not have to be on the first shelf.

Example 2:

Input: books = [[1,3],[2,4],[3,2]], shelfWidth = 6
Output: 4


Constraints:

1 <= books.length <= 1000
1 <= thickness_i <= shelfWidth <= 1000
1 <= height_i <= 1000

"""

# V0
# IDEA: 1D DP (partition the ordered list into consecutive shelves)
"""

 DP def:
    - dp[i] = min total height after placing the FIRST i books

 DP eq:
    - dp[i] = min over j (dp[j - 1] + max height of books[j-1 .. i-1])
      where books j..i all fit on ONE shelf (sum thickness <= shelfWidth)

 init:
    - dp[0] = 0
"""
# time = O(n^2)
# space = O(n)
class Solution(object):
    def minHeightShelves(self, books, shelfWidth):
        n = len(books)
        dp = [0] * (n + 1)

        for i in range(1, n + 1):
            w, h = books[i - 1]
            # case 1 : book i alone starts a new shelf
            dp[i] = dp[i - 1] + h

            # case 2 : extend the last shelf leftwards to book j
            for j in range(i - 1, 0, -1):
                w += books[j - 1][0]
                # NOTE !!! stop as soon as the shelf overflows
                if w > shelfWidth:
                    break
                h = max(h, books[j - 1][1])
                dp[i] = min(dp[i], dp[j - 1] + h)

        return dp[n]
