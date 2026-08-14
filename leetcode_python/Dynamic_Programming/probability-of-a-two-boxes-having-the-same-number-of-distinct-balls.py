"""

1467. Probability of a Two Boxes Having The Same Number of Distinct Balls
Hard

Given 2n balls of k distinct colors. You will be given an integer array balls of size k where balls[i] is the number of balls of color i.

All the balls will be shuffled uniformly at random, then we will distribute the first n balls to the first box and the remaining n balls to the other box (Please read the explanation of the second example carefully).

Please note that the two boxes are considered different. For example, if we have two balls of colors a and b, and two boxes [] and (), then the distribution [a] (b) is considered different than the distribution [b] (a) (Please read the explanation of the first example carefully).

Return the probability that the two boxes have the same number of distinct balls. Answers within 10^-5 of the actual value will be accepted as correct.


Example 1:

Input: balls = [1,1]
Output: 1.00000
Explanation: Only 2 ways to divide the balls equally:
- A ball of color 1 to box 1 and a ball of color 2 to box 2
- A ball of color 2 to box 1 and a ball of color 1 to box 2
In both ways, the number of distinct colors in each box is equal. The probability is 2/2 = 1

Example 2:

Input: balls = [2,1,1]
Output: 0.66667
Explanation: We have the set of balls [1, 1, 2, 3]
This set of balls will be shuffled randomly and we may have one of the 12 distinct shuffles with equal probability (i.e. 1/12):
[1,1 / 2,3], [1,1 / 3,2], [1,2 / 1,3], [1,2 / 3,1], [1,3 / 1,2], [1,3 / 2,1], [2,1 / 1,3], [2,1 / 3,1], [2,3 / 1,1], [3,1 / 1,2], [3,1 / 2,1], [3,2 / 1,1]
After that, we add the first two balls to the first box and the second two balls to the second box.
We can see that 8 of these 12 possible random distributions have the same number of distinct colors of balls in each box.
Probability is 8/12 = 0.66667

Example 3:

Input: balls = [1,2,1,2]
Output: 0.60000
Explanation: The set of balls is [1, 2, 2, 3, 4, 4]. It is hard to display all the 180 possible random shuffles of this set but it is easy to check that 108 of them will have the same number of distinct colors in each box.
Probability = 108 / 180 = 0.6


Constraints:

1 <= balls.length <= 8
1 <= balls[i] <= 6
sum(balls) is even.

"""

# V0
# IDEA : COMBINATORICS + DFS OVER COLORS (count splits, not shuffles)
#
#   a uniformly random shuffle cut in half is the same as choosing which n
#   of the 2n positions land in box 1, so:
#     P = (weighted # of valid colour splits) / C(2n, n)
#   walk the colours one by one; putting x balls of colour i into box 1 can
#   happen in C(balls[i], x) distinguishable ways (which of that colour's
#   positions go left), so multiply that factor in.
#   track diff = (#distinct in box 1) - (#distinct in box 2): taking all of
#   a colour gives +1, taking none gives -1, a partial take gives 0.
#   a split counts iff box 1 ends exactly full (j == 0) and diff == 0.
#
# time = O(k * n * k * maxBalls), space = O(k * n * k)
from math import factorial
from functools import lru_cache
class Solution(object):
    def getProbability(self, balls):
        k = len(balls)
        n = sum(balls) // 2

        def comb(a, b):
            if b < 0 or b > a:
                return 0
            return factorial(a) // (factorial(b) * factorial(a - b))

        @lru_cache(None)
        def dfs(i, j, diff):
            # j = slots left in box 1, diff = distinct(box1) - distinct(box2)
            if j < 0:
                return 0
            if i == k:
                return 1 if j == 0 and diff == 0 else 0
            total = 0
            for x in range(balls[i] + 1):
                if x == balls[i]:
                    d = 1            # colour appears only in box 1
                elif x == 0:
                    d = -1           # colour appears only in box 2
                else:
                    d = 0            # colour appears in both
                total += comb(balls[i], x) * dfs(i + 1, j - x, diff + d)
            return total

        good = dfs(0, n, 0)
        dfs.cache_clear()
        return float(good) / comb(2 * n, n)
