"""

2558. Take Gifts From the Richest Pile
Easy

You are given an integer array gifts denoting the number of gifts in various piles. Every second, you do the following:

- Choose the pile with the maximum number of gifts.
- If there is more than one pile with the maximum number of gifts, choose any.
- Reduce the number of gifts in the pile to the floor of the square root of the original number of gifts in the pile.

Return the number of gifts remaining after k seconds.


Example 1:

Input: gifts = [25,64,9,4,100], k = 4
Output: 29
Explanation:
The gifts are taken in the following way:
- In the first second, the last pile is chosen and 10 gifts are left behind.
- Then the second pile is chosen and 8 gifts are left behind.
- After that the first pile is chosen and 5 gifts are left behind.
- Finally, the last pile is chosen again and 3 gifts are left behind.
The final remaining gifts are [5,8,9,4,3], so the total number of gifts remaining is 29.

Example 2:

Input: gifts = [1,1,1,1], k = 4
Output: 4
Explanation:
In this case, regardless which pile you choose, you have to leave behind 1 gift in each pile.
That is, you can't take any pile with you.
So, the total gifts remaining are 4.


Constraints:

1 <= gifts.length <= 10^3
1 <= gifts[i] <= 10^9
1 <= k <= 10^3

"""

# V0
# IDEA : MAX HEAP (SIMULATION)
#
#   pure simulation: k times, pop the biggest pile, push back floor(sqrt(x)).
#   python's heapq is a MIN heap, so we store negated values and negate back
#   on read; heapreplace = pop + push in a single O(log n) sift.
#
#   NOTE : use math.isqrt (exact integer sqrt) rather than int(math.sqrt(x)) —
#          gifts[i] can reach 10^9 and float rounding could hand back a value
#          one off around perfect squares.
#   NOTE : a pile of 1 stays at 1 forever, so the loop is safe to run all k
#          rounds even after everything has bottomed out.
#
# time = O(n + k log n), space = O(n)
import heapq
import math


class Solution(object):
    def pickGifts(self, gifts, k):
        h = [-x for x in gifts]
        heapq.heapify(h)
        for _ in range(k):
            top = -h[0]
            heapq.heapreplace(h, -int(math.isqrt(top)))
        return -sum(h)
