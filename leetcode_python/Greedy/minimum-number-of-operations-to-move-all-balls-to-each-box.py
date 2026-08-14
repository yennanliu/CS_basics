"""

1769. Minimum Number of Operations to Move All Balls to Each Box
Medium

You have n boxes. You are given a binary string boxes of length n, where boxes[i] is '0' if the i^th box is empty, and '1' if it contains one ball.

In one operation, you can move one ball from a box to an adjacent box. Box i is adjacent to box j if abs(i - j) == 1. Note that after doing so, there may be more than one ball in some boxes.

Return an array answer of size n, where answer[i] is the minimum number of operations needed to move all the balls to the i^th box.

Each answer[i] is calculated considering the initial state of the boxes.

Example 1:

Input: boxes = "110"
Output: [1,1,3]
Explanation: The answer for each box is as follows:
1) First box: you will have to move one ball from the second box to the first box in one operation.
2) Second box: you will have to move one ball from the first box to the second box in one operation.
3) Third box: you will have to move one ball from the first box to the third box in two operations, and move one ball from the second box to the third box in one operation.

Example 2:

Input: boxes = "001011"
Output: [11,8,5,4,3,4]

Constraints:

n == boxes.length
1 <= n <= 2000
boxes[i] is either '0' or '1'.

"""

# V0
# IDEA : PREFIX SUM IN BOTH DIRECTIONS (cost from the left + cost from the right)
#
#   answer[i] = (cost of pulling every ball on the left of i to i)
#             + (cost of pulling every ball on the right of i to i)
#   sweeping left to right, if `cnt` balls sit strictly left of i then moving
#   from i - 1 to i costs `cnt` extra operations:
#       left[i] = left[i - 1] + cnt   (cnt updated with boxes[i - 1])
#   the right side is the mirror image.
#
# time = O(n), space = O(n)
class Solution(object):
    def minOperations(self, boxes):
        n = len(boxes)
        left = [0] * n
        right = [0] * n

        cnt = 0
        for i in range(1, n):
            if boxes[i - 1] == "1":
                cnt += 1
            left[i] = left[i - 1] + cnt

        cnt = 0
        for i in range(n - 2, -1, -1):
            if boxes[i + 1] == "1":
                cnt += 1
            right[i] = right[i + 1] + cnt

        return [left[i] + right[i] for i in range(n)]
