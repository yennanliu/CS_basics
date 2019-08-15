# V0 

# V1 
# http://bookshadow.com/weblog/2017/03/05/leetcode-lonely-pixel-ii/
class Solution(object):
    def findBlackPixel(self, picture, N):
        """
        :type picture: List[List[str]]
        :type N: int
        :rtype: int
        """
        w, h = len(picture), len(picture[0])
        rows, cols = [0] * w, [0] * h
        for x in range(w):
            for y in range(h):
                if picture[x][y] == 'B':
                    rows[x] += 1
                    cols[y] += 1

        sdict = collections.defaultdict(int)
        for idx, row in enumerate(picture):
            sdict[''.join(row)] += 1

        ans = 0
        for x in range(w):
            row = ''.join(picture[x])
            if sdict[row] != N:
                continue
            for y in range(h):
                if picture[x][y] == 'B':
                    if rows[x] == N:
                        if cols[y] == N:
                            ans += 1
        return ans

# V2 
# Time:  O(m * n)
# Space: O(m * n)
import collections
class Solution(object):
    def findBlackPixel(self, picture, N):
        """
        :type picture: List[List[str]]
        :type N: int
        :rtype: int
        """
        rows, cols = [0] * len(picture),  [0] * len(picture[0])
        lookup = collections.defaultdict(int)
        for i in range(len(picture)):
            for j in range(len(picture[0])):
                if picture[i][j] == 'B':
                    rows[i] += 1
                    cols[j] += 1
            lookup[tuple(picture[i])] += 1

        result = 0
        for i in range(len(picture)):
            if rows[i] == N and lookup[tuple(picture[i])] == N:
                for j in range(len(picture[0])):
                     result += picture[i][j] == 'B' and cols[j] == N
        return result


class Solution2(object):
    def findBlackPixel(self, picture, N):
        """
        :type picture: List[List[str]]
        :type N: int
        :rtype: int
        """
        lookup = collections.Counter(map(tuple, picture))
        cols = [col.count('B') for col in zip(*picture)]
        return sum(N * zip(row, cols).count(('B', N)) \
                   for row, cnt in lookup.iteritems() \
                   if cnt == N == row.count('B'))