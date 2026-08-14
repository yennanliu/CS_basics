"""

2550. Count Collisions of Monkeys on a Polygon
Medium

There is a regular convex polygon with n vertices. The vertices are labeled from 0 to n - 1 in a clockwise direction, and each vertex has exactly one monkey.

Simultaneously, each monkey moves to a neighboring vertex. A collision happens if at least two monkeys reside on the same vertex after the movement or intersect on an edge.

Return the number of ways the monkeys can move so that at least one collision happens. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: n = 3
Output: 6
Explanation:
There are 8 total possible movements.
Two ways such that they collide at some point are:
- Monkey 1 moves in a clockwise direction; monkey 2 moves in an anticlockwise direction; monkey 3 moves in a clockwise direction. Monkeys 1 and 2 collide.
- Monkey 1 moves in an anticlockwise direction; monkey 2 moves in an anticlockwise direction; monkey 3 moves in a clockwise direction. Monkeys 1 and 3 collide.

Example 2:

Input: n = 4
Output: 14


Constraints:

3 <= n <= 10^9

"""

# V0
# IDEA : MATH / COMPLEMENT COUNTING + FAST MODULAR POWER
#
#   each of the n monkeys independently picks clockwise or counter-clockwise,
#   giving 2^n total movement patterns.
#
#   count the COLLISION-FREE ones instead: if any two adjacent monkeys pick
#   opposite directions they swap along the shared edge and collide, so a
#   safe pattern forces ALL monkeys to agree — either everyone goes clockwise
#   or everyone goes counter-clockwise. that is exactly 2 safe patterns
#   (and both are genuinely safe: the whole ring rotates by one).
#
#   answer = 2^n - 2 (mod 1e9+7).
#
#   NOTE : n is up to 10^9, so use modular fast power (pow(2, n, mod)), and
#          take a final % mod because pow(...) - 2 can go negative when
#          2^n % mod happens to be 0 or 1.
#
# time = O(log n), space = O(1)
class Solution(object):
    def monkeyMove(self, n):
        MOD = 10 ** 9 + 7
        return (pow(2, n, MOD) - 2) % MOD
