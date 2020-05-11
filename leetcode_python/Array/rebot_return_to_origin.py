# V0
class Solution:
    def judgeCircle(self, moves):
        directs = {'L':-1, 'R':1, 'U':1j, 'D':-1j}
        return 0 == sum(directs[move] for move in moves)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83663650
# IDEA : PY SUPPORT Complex arithmetic
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
class Solution:
    def judgeCircle(self, moves: str) -> bool:
        return moves.count('L')==moves.count('R') and moves.count('U')==moves.count('D')

# V2