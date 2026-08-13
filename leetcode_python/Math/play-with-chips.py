"""

1217. Minimum Cost to Move Chips to The Same Position
Easy

(this problem was originally titled "Play with Chips")

We have n chips, where the position of the ith chip is position[i].

We need to move all the chips to the same position. In one step, we can change the
position of the ith chip from position[i] to:

position[i] + 2 or position[i] - 2 with cost = 0.
position[i] + 1 or position[i] - 1 with cost = 1.

Return the minimum cost needed to move all the chips to the same position.

Example 1:

Input: position = [1,2,3]
Output: 1
Explanation: First step: Move the chip at position 3 to position 1 with cost = 0.
Second step: Move the chip at position 2 to position 1 with cost = 1.
Total cost is 1.

Example 2:

Input: position = [2,2,2,3,3]
Output: 2
Explanation: We can move the two chips at position 3 to position 2.
Each move has cost = 1. The total cost = 2.

Example 3:

Input: position = [1,1000000000]
Output: 1


Constraints:

1 <= position.length <= 100
1 <= position[i] <= 10^9

"""

# V0
# IDEA : MATH / PARITY (greedy)
#        moving by 2 is free -> every chip can reach ANY position of the same
#        parity for free. So collapse all even chips to 0 and all odd chips to 1.
#        Then the only real cost is moving the smaller group by 1 step.
# time = O(n)
# space = O(1)
class Solution(object):
    def minCostToMoveChips(self, position):
        odd = 0
        for p in position:
            odd += p % 2
        even = len(position) - odd
        return min(odd, even)
