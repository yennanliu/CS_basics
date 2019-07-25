# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80525152
# IDEA : ZIP + MAP
# ZIP:
# >>> a = [1,2,3]
# >>> b = [4,5,6]
# >>> c = [4,5,6,7,8]
# >>> zipped = zip(a,b)     
# [(1, 4), (2, 5), (3, 6)]
# >>> zip(a,c)              
# [(1, 4), (2, 5), (3, 6)]
# >>> zip(*zipped)          
# [(1, 2, 3), (4, 5, 6)]

# MAP:
# In [10]: def f(x):
#     ...:     return x+1
#     ...: 
# In [11]: list(map(f,x))
# Out[11]: [2, 3, 4]
# In [12]: x
# Out[12]: [1, 2, 3]
# In [13]: def f(x):
#     ...:     return x+1
#     ...: 
# In [14]: list(map(f,x))
# Out[14]: [2, 3, 4]
class Solution(object):
    def findMinDifference(self, timePoints):
        """
        :type timePoints: List[str]
        :rtype: int
        """
        def convert(time):
            return int(time[:2]) * 60 + int(time[3:])
        timePoints = map(convert, timePoints)
        timePoints.sort()
        return min((y - x) % (24 * 60)  for x, y in zip(timePoints, timePoints[1:] + timePoints[:1]))


# V1' 
# http://bookshadow.com/weblog/2017/03/12/leetcode-minimum-time-difference/
# IDEA : SORT  + MAP 
class Solution(object):
    def findMinDifference(self, timePoints):
        """
        :type timePoints: List[str]
        :rtype: int
        """
        tp = sorted(map(int, p.split(':')) for p in timePoints)
        tp += [[tp[0][0] + 24, tp[0][1]]]
        return min((tp[x+1][0] - tp[x][0]) * 60 + tp[x+1][1] - tp[x][1] \
                   for x in range(len(tp) - 1))

# V2 
# Time:  O(nlogn)
# Space: O(n)
class Solution(object):
    def findMinDifference(self, timePoints):
        """
        :type timePoints: List[str]
        :rtype: int
        """
        minutes = map(lambda x: int(x[:2]) * 60 + int(x[3:]), timePoints)
        minutes.sort()
        return min((y - x) % (24 * 60)  \
                   for x, y in zip(minutes, minutes[1:] + minutes[:1]))