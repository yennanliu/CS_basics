"""

789. Escape The Ghosts
Medium

You are playing a simplified PAC-MAN game on an infinite 2-D grid. You start at the point [0, 0], and you are given a destination point target = [x_target, y_target] that you are trying to get to. There are several ghosts on the map with their starting positions given as a 2D array ghosts, where ghosts[i] = [x_i, y_i] represents the starting position of the i^th ghost. All inputs are integral coordinates.

Each turn, you and all the ghosts may independently choose to either move 1 unit in any of the four cardinal directions: north, east, south, or west, or stay still. All actions happen simultaneously.

You escape if and only if you can reach the target before any ghost reaches you. If you reach any square (including the target) at the same time as a ghost, it does not count as an escape.

Return true if it is possible to escape regardless of how the ghosts move, otherwise return false.

Example 1:

Input: ghosts = [[1,0],[0,3]], target = [0,1]
Output: true
Explanation: You can reach the destination (0, 1) after 1 turn, while the ghosts located at (1, 0) and (0, 3) cannot catch up with you.

Example 2:

Input: ghosts = [[1,0]], target = [2,0]
Output: false
Explanation: You need to reach the destination (2, 0), but the ghost at (1, 0) lies between you and the destination.

Example 3:

Input: ghosts = [[2,0]], target = [1,0]
Output: false
Explanation: The ghost can reach the target at the same time as you.

Constraints:

1 <= ghosts.length <= 100
ghosts[i].length == 2
-10^4 <= x_i, y_i <= 10^4
There can be multiple ghosts in the same location.
target.length == 2
-10^4 <= x_target, y_target <= 10^4

"""

# V0

# V1 : dev 

# V2
# https://blog.csdn.net/fuxuemingzhu/article/details/80480462
# time = O(n)  # n = len(ghosts)
# space = O(1)
class Solution:
    def escapeGhosts(self, ghosts, target):
        """
        :type ghosts: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        mht = sum(map(abs, target))
        tx, ty = target
        return not any(abs(gx - tx) + abs(gy - ty) <= mht for gx, gy in ghosts)

# V2'
# time = O(n)  # n = len(ghosts)
# space = O(1)
class Solution:
    def escapeGhosts(self, ghosts, target):
        """
        :type ghosts: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        mht = sum( [ abs(i) for i in target ] )
        tx, ty = target
        for gx, gy in ghosts:
            if abs(gx - tx) + abs(gy - ty) <= mht:
                return False 
        return True 


# V3
# time = O(n)  # n = len(ghosts)
# space = O(1)
class Solution(object):
    def escapeGhosts(self, ghosts, target):
        """
        :type ghosts: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        total = abs(target[0])+abs(target[1])
        return all(total < abs(target[0]-i)+abs(target[1]-j) for i, j in ghosts)
