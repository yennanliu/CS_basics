# Time:  O(n)
# Space: O(1)

# Initially, there is a Robot at position (0, 0). Given a sequence of its moves,
# judge if this robot makes a circle, which means it moves back to the original place.
#
# The move sequence is represented by a string. And each move is represent by a character.
# The valid robot moves are R (Right), L (Left), U (Up) and D (down).
# The output should be true or false representing whether the robot makes a circle.
#
# Example 1:
# Input: "UD"
# Output: true
# Example 2:
# Input: "LL"
# Output: false


# V1 


class Solution:
    def judgeCircle(self, moves):
        x_move = 1*(moves.count('R')) + (-1)*(moves.count('L'))
        y_move = 1*(moves.count('U')) + (-1)*(moves.count('D'))
        if (x_move == 0  and y_move == 0):
            return True 
        else:
            return False

            


# V2 
class Solution(object):
    def judgeCircle(self, moves):
        """
        :type moves: str
        :rtype: bool
        """
        v, h = 0, 0
        for move in moves:
            if move == 'U':
                v += 1
            elif move == 'D':
                v -= 1
            elif move == 'R':
                h += 1
            elif move == 'L':
                h -= 1
        return v == 0 and h == 0