"""

2335. Minimum Amount of Time to Fill Cups
Easy

You have a water dispenser that can dispense cold, warm, and hot water. Every second, you can either fill up 2 cups with different types of water, or 1 cup of any type of water.

You are given a 0-indexed integer array amount of length 3 where amount[0], amount[1], and amount[2] denote the number of cold, warm, and hot water cups you need to fill respectively. Return the minimum number of seconds needed to fill up all the cups.


Example 1:

Input: amount = [1,4,2]
Output: 4
Explanation: One way to fill up the cups is:
Second 1: Fill up a cold cup and a warm cup.
Second 2: Fill up a warm cup and a hot cup.
Second 3: Fill up a warm cup and a hot cup.
Second 4: Fill up a warm cup.
It can be proven that 4 is the minimum number of seconds needed.

Example 2:

Input: amount = [5,4,4]
Output: 7
Explanation: One way to fill up the cups is:
Second 1: Fill up a cold cup, and a hot cup.
Second 2: Fill up a cold cup, and a warm cup.
Second 3: Fill up a cold cup, and a warm cup.
Second 4: Fill up a warm cup, and a hot cup.
Second 5: Fill up a cold cup, and a hot cup.
Second 6: Fill up a cold cup, and a hot cup.
Second 7: Fill up a warm cup.

Example 3:

Input: amount = [5,0,0]
Output: 5
Explanation: Every second, we fill up a cold cup.


Constraints:

amount.length == 3
0 <= amount[i] <= 100

"""

# V0
# IDEA : TWO LOWER BOUNDS, AND THE LARGER ONE IS ALWAYS ACHIEVABLE
#
#   bound 1 : the biggest pile must be poured one cup per second at best, so
#             at least max(amount) seconds are needed.
#   bound 2 : each second fills at most 2 cups, so at least
#             ceil(total / 2) seconds.
#
#   the answer is exactly the larger of the two. when one pile dominates the
#   other two combined, bound 1 binds; otherwise the piles can be paired off
#   evenly and bound 2 binds.
#
# time = O(1), space = O(1)
class Solution(object):
    def fillCups(self, amount):
        total = sum(amount)
        return max(max(amount), (total + 1) // 2)
