"""

657. Robot Return to Origin
Easy

There is a robot starting at the position (0, 0), the origin, on a 2D plane. Given a sequence of its moves, judge if this robot ends up at (0, 0) after it completes its moves.

You are given a string moves that represents the move sequence of the robot where moves[i] represents its i^th move. Valid moves are 'R' (right), 'L' (left), 'U' (up), and 'D' (down).

Return true if the robot returns to the origin after it finishes all of its moves, or false otherwise.

Note: The way that the robot is "facing" is irrelevant. 'R' will always make the robot move to the right once, 'L' will always make it move left, etc. Also, assume that the magnitude of the robot's movement is the same for each move.

Example 1:

Input: moves = "UD"
Output: true
Explanation: The robot moves up once, and then down once. All moves have the same magnitude, so it ended up at the origin where it started. Therefore, we return true.

Example 2:

Input: moves = "LL"
Output: false
Explanation: The robot moves left twice. It ends up two "moves" to the left of the origin. We return false because it is not at the origin at the end of its moves.

Constraints:

1 <= moves.length <= 2 * 10^4
moves only contains the characters 'U', 'D', 'L' and 'R'.

"""

# V0
# time = O(n)
# space = O(1)
class Solution:
    def judgeCircle(self, moves):
        directs = {'L':-1, 'R':1, 'U':1j, 'D':-1j}
        return 0 == sum(directs[move] for move in moves)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83663650
# IDEA : PY SUPPORT Complex arithmetic
# time = O(n)
# space = O(1)
class Solution:
    def judgeCircle(self, moves):
        """
        :type moves: str
        :rtype: bool
        """
        directs = {'L':-1, 'R':1, 'U':1j, 'D':-1j}
        return 0 == sum(directs[move] for move in moves)

### Test case
s=Solution()
assert s.judgeCircle('LLL')==False
assert s.judgeCircle('LR')==True
assert s.judgeCircle('LRUD')==True
assert s.judgeCircle('LRUUUUD')==False
assert s.judgeCircle('')==True

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83663650
# time = O(n)
# space = O(1)
class Solution:
    def judgeCircle(self, moves):
        """
        :type moves: str
        :rtype: bool
        """
        count = collections.Counter(moves)
        return count['U'] == count['D'] and count['L'] == count['R']

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/83663650
# time = O(n)
# space = O(1)
class Solution:
    def judgeCircle(self, moves):
        """
        :type moves: str
        :rtype: bool
        """
        u = d = l = r = 0
        for move in moves:
            if move == 'U':
                u += 1
            elif move == "D":
                d += 1
            elif move == 'L':
                l += 1
            elif move == 'R':
                r += 1
        return u == d and l == r

# V1'''
# https://blog.csdn.net/zhangpeterx/article/details/88362411
# time = O(n)
# space = O(1)
class Solution:
    def judgeCircle(self, moves: str) -> bool:
        return moves.count('L')==moves.count('R') and moves.count('U')==moves.count('D')

# V2