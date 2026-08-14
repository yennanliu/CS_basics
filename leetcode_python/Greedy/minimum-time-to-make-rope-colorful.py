"""

1578. Minimum Deletion Cost to Avoid Repeating Letters
Medium

Alice has n balloons arranged on a rope. You are given a 0-indexed string colors where colors[i] is the color of the ith balloon.

Alice wants the rope to be colorful. She does not want two consecutive balloons to be of the same color, so she asks Bob for help. Bob can remove some balloons from the rope to make it colorful. You are given a 0-indexed integer array neededTime where neededTime[i] is the time (in seconds) that Bob needs to remove the ith balloon from the rope.

Return the minimum time Bob needs to make the rope colorful.

Example 1:

Input: colors = "abaac", neededTime = [1,2,3,4,5]
Output: 3
Explanation: In the above image, 'a' is blue, 'b' is red, and 'c' is green.
Bob can remove the blue balloon at index 2. This takes 3 seconds.
There are no longer two consecutive balloons of the same color. Total time = 3.

Example 2:

Input: colors = "abc", neededTime = [1,2,3]
Output: 0
Explanation: The rope is already colorful. Bob does not need to remove any balloons from the rope.

Example 3:

Input: colors = "aabaa", neededTime = [1,2,3,4,1]
Output: 2
Explanation: Bob will remove the balloons at indices 0 and 4. Each balloons takes 1 second to remove.
There are no longer two consecutive balloons of the same color. Total time = 1 + 1 = 2.

Constraints:

n == colors.length == neededTime.length
1 <= n <= 10^5
1 <= neededTime[i] <= 10^4
colors contains only lowercase English letters.

"""

# V0
# IDEA : GREEDY (inside a block of equal colours keep the most expensive one)
#
#   consecutive balloons of the same colour form a block ; exactly one
#   balloon of that block may survive and it is obviously the one with
#   the LARGEST removal time.
#   -> cost of a block = sum(block) - max(block)
#   NOTE : blocks are independent, neighbouring blocks have different
#          colours by construction.
#
# time = O(n), space = O(1)
class Solution(object):
    def minCost(self, colors, neededTime):
        n = len(colors)
        res = 0
        i = 0
        while i < n:
            j = i
            total = 0
            biggest = 0
            while j < n and colors[j] == colors[i]:
                total += neededTime[j]
                if neededTime[j] > biggest:
                    biggest = neededTime[j]
                j += 1
            res += total - biggest
            i = j
        return res
