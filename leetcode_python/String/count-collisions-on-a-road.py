"""

2211. Count Collisions on a Road
Medium

There are n cars on an infinitely long road. The cars are numbered from 0 to n - 1 from left to right and each car is present at a unique point.

You are given a 0-indexed string directions of length n. directions[i] can be either 'L', 'R', or 'S' denoting whether the ith car is moving towards the left, towards the right, or staying at its current point respectively. Each moving car has the same speed.

The number of collisions can be calculated as follows:

When two cars moving in opposite directions collide with each other, the number of collisions increases by 2.
When a moving car collides with a stationary car, the number of collisions increases by 1.

After a collision, the cars involved can no longer move and will stay at the point where they collided. Other than that, cars cannot change their state or direction.

Return the total number of collisions that will happen on the road.


Example 1:

Input: directions = "RLRSLL"
Output: 5
Explanation:
The collisions that will happen on the road are:
- Cars 0 and 1 will collide with each other. Since they are moving in opposite directions, the number of collisions becomes 0 + 2 = 2.
- Cars 2 and 3 will collide with each other. Since car 3 is stationary, the number of collisions becomes 2 + 1 = 3.
- Cars 3 and 4 will collide with each other. Since car 3 is stationary, the number of collisions becomes 3 + 1 = 4.
- Cars 4 and 5 will collide with each other. After car 4 collides with car 3, it will stay at the point of collision and get hit by car 5. The number of collisions becomes 4 + 1 = 5.
Thus, the total number of collisions that will happen on the road is 5.

Example 2:

Input: directions = "LLRR"
Output: 0
Explanation:
No cars will collide with each other. Thus, the total number of collisions that will happen on the road is 0.


Constraints:

1 <= directions.length <= 10^5
directions[i] is either 'L', 'R', or 'S'.

"""


# V0
class Solution(object):
    def countCollisions(self, directions):
        """
        :type directions: str
        :rtype: int
        """
        pass


# V0-1
# IDEA: 2 continous var (l, r) and problem undetstanding (gpt)
class Solution(object):
    def countCollisions(self, directions):
        """
        :type directions: str
        :rtype: int
        """
        n = len(directions)

        # Remove cars that escape without collision
        left = 0
        right = n - 1

        while left < n and directions[left] == "L":
            left += 1

        while right >= left and directions[right] == "R":
            right -= 1

        # All moving cars in the remaining range will collide
        cnt = 0
        for i in range(left, right + 1):
            if directions[i] != "S":
                cnt += 1

        return cnt


# V0-2
# IDEA: PROBLEM UNDETSTANDING (gemini)
"""

這題有一個非常巧妙的物理規律，完全不需要複雜的堆疊：

    1.  左側開頭連續的 'L'：它們會一直向左開出道路，永遠不會發生碰撞，貢獻碰撞數為 0。

    2. 右側結尾連續的 'R'：它們會一直向右開出道路，永遠不會發生碰撞，貢獻碰撞數為 0。

    3. 去除這兩部分之後的中間區段：在剩下的車輛中，任何不是 'S' 的車（即 'R' 或 'L'）
       最終都一定會因為碰撞而停下來。因此，中間區段裡所有非 'S' 的車輛數量，就是總碰撞次數！

"""
class Solution(object):

  def countCollisions(self, directions):
    """:type directions: str

    :rtype: int
    """
    # 1. 移除左側所有向左開走的 'L'
    # 2. 移除右側所有向右開走的 'R'
    trimmed = directions.lstrip("L").rstrip("R")

    # 3. 剩下的字串中，任何不是 'S' 的車（即 'R' 或 'L'）最終都一定會碰撞
    return sum(1 for c in trimmed if c != "S")


# V0-3
# IDEA : ONLY THE CARS THAT ESCAPE NEVER COLLIDE — STRIP THEM AND COUNT (claude)
#
#   a car drives away forever only if it is a leading 'L' (nothing to its
#   left can ever reach it) or a trailing 'R' (nothing to its right).
#   every OTHER moving car is eventually stopped by something, and each
#   stop adds exactly 1 to the total — a head-on "R L" is two moving cars
#   stopping, which is exactly the +2 the problem charges.
#
#   e.g. "RLRSLL" -> nothing to strip -> moving cars R,L,R,L,L -> 5
#
# time = O(n), space = O(1)
class Solution(object):
    def countCollisions(self, directions):
        """
        :type directions: str
        :rtype: int
        """
        n = len(directions)

        # skip the leading 'L' cars : they drive off to the left forever
        left = 0
        while left < n and directions[left] == 'L':
            left += 1

        # skip the trailing 'R' cars : they drive off to the right forever
        right = n - 1
        while right >= 0 and directions[right] == 'R':
            right -= 1

        # NOTE !!! every moving car left in [left, right] stops exactly once
        collisions = 0
        for i in range(left, right + 1):
            if directions[i] != 'S':
                collisions += 1

        return collisions


# V1
# IDEA : STACK SIMULATION, LEFT TO RIGHT (claude)
#
#   keep the cars that are still "unresolved" on a stack. only a run of
#   'R' cars can be waiting for something to hit; an 'L' either escapes
#   (nothing stopped to its left) or crashes into whatever is there.
#
#   stopped = True once any car to the left has become stationary — after
#   that, every incoming 'L' hits a wall and adds 1.
#
#   longer than V0, but it is the version you reach by simulating, and it
#   shows WHY V0's counting rule holds.
#
# time = O(n), space = O(n)
class Solution2(object):
    def countCollisions(self, directions):
        """
        :type directions: str
        :rtype: int
        """
        collisions = 0
        pending_right = 0   # 'R' cars still moving, waiting to hit something
        stopped = False     # is there a stationary car/wreck to the left?

        for car in directions:
            if car == 'R':
                pending_right += 1
            elif car == 'S':
                # every waiting 'R' crashes into this stationary car
                collisions += pending_right
                pending_right = 0
                stopped = True
            else:  # car == 'L'
                if pending_right > 0:
                    # head-on with the nearest 'R' (+2), then the other
                    # waiting 'R' cars pile into the wreck (+1 each)
                    collisions += 2 + (pending_right - 1)
                    pending_right = 0
                    stopped = True
                elif stopped:
                    # runs into the wreck on its left
                    collisions += 1
                # else : nothing to its left, it escapes

        # any 'R' still pending escapes to the right
        return collisions
