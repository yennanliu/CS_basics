"""

2106. Maximum Fruits Harvested After at Most K Steps
Hard

Fruits are positioned along an infinite x-axis. You are given a 2D integer array fruits where fruits[i] = [positioni, amounti] depicts amounti fruits at the position positioni. fruits is already sorted by positioni in ascending order, and each positioni is unique.

You are also given an integer startPos and an integer k. You start at the position startPos and you can walk at most k steps. In each step, you can walk one unit to the right or one unit to the left. Optionally, you may also pick fruits at the position you are at. Once you harvest fruits at a position, the fruits will disappear at that position.

Return the maximum total number of fruits you can harvest.


Example 1:

Input: fruits = [[2,8],[6,3],[8,6]], startPos = 5, k = 4
Output: 9
Explanation:
The optimal way is to:
- Move right to position 6 and harvest 3 fruits
- Move right to position 8 and harvest 6 fruits
You moved 3 steps and harvested 3 + 6 = 9 fruits in total.

Example 2:

Input: fruits = [[0,9],[4,1],[5,7],[6,2],[7,4],[10,9]], startPos = 5, k = 4
Output: 14
Explanation:
You can move at most k = 4 steps, so you cannot reach position 0 nor 10.
The optimal way is to:
- Harvest the 7 fruits at the starting position 5
- Move left to position 4 and harvest 1 fruit
- Move right to position 6 and harvest 2 fruits
- Move right to position 7 and harvest 4 fruits
You moved 1 + 3 = 4 steps and harvested 7 + 1 + 2 + 4 = 14 fruits in total.

Example 3:

Input: fruits = [[0,3],[6,4],[8,5]], startPos = 3, k = 2
Output: 0
Explanation:
You can move at most k = 2 steps and cannot reach any position with fruits.


Constraints:

1 <= fruits.length <= 10^5
fruits[i].length == 2
0 <= startPos, positioni <= 2 * 10^5
positioni-1 < positioni for any i > 0 (0-indexed)
1 <= amounti <= 10^4
0 <= k <= 2 * 10^5

"""

# V0
# IDEA : SLIDING WINDOW OVER THE FRUIT POSITIONS, COST = "GO ONE WAY, THEN TURN"
#
#   an optimal walk visits exactly one CONTIGUOUS range of positions — you
#   never gain by skipping a position you walk past. so the answer is the
#   best window [l, r] of the fruit array whose cost fits in k steps.
#
#   for a window from L = pos[l] to R = pos[r], the cheapest route is to
#   cover the shorter side first and then sweep across :
#
#       cost = (R - L) + min(|start - L|, |start - R|)
#
#   this stays correct when the whole window sits on one side of start (the
#   min term simply collapses to the near end).
#
#   cost is non-increasing as l grows, so a plain two-pointer works : extend
#   r, shrink l while the cost exceeds k, and track the running sum.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxTotalFruits(self, fruits, startPos, k):

        def cost(l, r):
            left, right = fruits[l][0], fruits[r][0]
            return (right - left) + min(abs(startPos - left), abs(startPos - right))

        res = 0
        cur = 0
        l = 0
        for r in range(len(fruits)):
            cur += fruits[r][1]
            while l <= r and cost(l, r) > k:
                cur -= fruits[l][1]
                l += 1
            if l <= r:
                res = max(res, cur)
        return res
