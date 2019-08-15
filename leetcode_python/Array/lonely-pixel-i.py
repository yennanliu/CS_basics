# V0 

# V1 
# http://bookshadow.com/weblog/2017/03/05/leetcode-lonely-pixel-i/
class Solution(object):
    def findLonelyPixel(self, picture):
        """
        :type picture: List[List[str]]
        :rtype: int
        """
        w, h = len(picture), len(picture[0])
        rows, cols = [0] * w, [0] * h
        for x in range(w):
            for y in range(h):
                if picture[x][y] == 'B':
                    rows[x] += 1         # to lebel (x-axis) the exist of B 
                    cols[y] += 1         # to lebel (y-axis) the exist of B 
        ans = 0
        for x in range(w):
            for y in range(h):
                if picture[x][y] == 'B':
                    if rows[x] == 1:     # to check if this is a "lonely" "B", i.e. rows[x]==1 and rows[y] == 1 
                        if cols[y] == 1: # since if 2 "B" exist adjacent, then at least one of there row[x], row[y] must > 1 
                            ans += 1
        return ans

# V2 
# Time:  O(m * n)
# Space: O(m + n)
class Solution(object):
    def findLonelyPixel(self, picture):
        """
        :type picture: List[List[str]]
        :rtype: int
        """
        rows, cols = [0] * len(picture),  [0] * len(picture[0])
        for i in range(len(picture)):
            for j in range(len(picture[0])):
                if picture[i][j] == 'B':
                    rows[i] += 1
                    cols[j] += 1

        result = 0
        for i in range(len(picture)):
            if rows[i] == 1:
                for j in range(len(picture[0])):
                     result += picture[i][j] == 'B' and cols[j] == 1
        return result


class Solution2(object):
    def findLonelyPixel(self, picture):
        """
        :type picture: List[List[str]]
        :type N: int
        :rtype: int
        """
        return sum(col.count('B') == 1 == picture[col.index('B')].count('B') \
               for col in zip(*picture))