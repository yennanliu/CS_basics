"""

2189. Number of Ways to Build House of Cards
Medium
(premium / locked problem)

You are given an integer n representing the number of playing cards you have. A house of cards meets the following conditions:

A house of cards consists of one or more rows of triangles and horizontal cards.
Triangles are created by leaning two cards against each other.
One card must be placed horizontally between all adjacent triangles in a row.
Any triangle on a row higher than the first must be placed on a horizontal card from the previous row.
Each triangle is placed in the leftmost available spot in the row.

Return the number of distinct house of cards you can build using all n cards.

Two houses of cards are considered distinct if there exists a row where the two houses contain a different number of cards.


Example 1:

Input: n = 16
Output: 2
Explanation: The two valid houses of cards are shown.
The third house of cards in the diagram is not valid because the rightmost triangle on the top row is not placed on top of a horizontal card.

Example 2:

Input: n = 2
Output: 1
Explanation: The one valid house of cards is shown.

Example 3:

Input: n = 4
Output: 0
Explanation: The three houses of cards in the diagram are not valid.
The first house of cards needs a horizontal card placed between the two triangles.
The second house of cards uses 5 cards.
The third house of cards uses 2 cards.


Constraints:

1 <= n <= 500

"""

# V0
# IDEA : A ROW OF k TRIANGLES COSTS 3k - 1 CARDS, AND ROW SIZES ARE DISTINCT
#
#   k triangles need 2k leaning cards plus k - 1 horizontals between them, so
#   the row costs 3k - 1.
#
#   a row of k' triangles offers only k' - 1 horizontal cards to stand on, so
#   the row above holds at most k' - 1 triangles — the row sizes strictly
#   decrease going up. that means a house is exactly a SET OF DISTINCT k
#   values whose costs sum to n (the decreasing order is then forced).
#
#   so this is a 0/1 knapsack COUNT over the item weights 2, 5, 8, 11, ...
#   iterating the capacity downward keeps each row size usable at most once.
#
# time = O(n * sqrt(n)), space = O(n)
class Solution(object):
    def houseOfCards(self, n):
        dp = [0] * (n + 1)
        dp[0] = 1
        k = 1
        while 3 * k - 1 <= n:
            cost = 3 * k - 1
            for total in range(n, cost - 1, -1):
                dp[total] += dp[total - cost]
            k += 1
        return dp[n]
