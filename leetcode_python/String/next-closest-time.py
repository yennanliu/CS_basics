# V0 

# V1 
# http://bookshadow.com/weblog/2017/09/24/leetcode-next-closest-time/
class Solution(object):
    def nextClosestTime(self, time):
        """
        :type time: str
        :rtype: str
        """
        time = time[:2] + time[3:]
        isValid = lambda t: int(t[:2]) < 24 and int(t[2:]) < 60
        stime = sorted(time)
        for x in (3, 2, 1, 0):
            for y in stime:
                if y <= time[x]: continue
                ntime = time[:x] + y + (stime[0] * (3 - x))
                if isValid(ntime): return ntime[:2] + ':' + ntime[2:]
        return stime[0] * 2 + ':' + stime[0] * 2

# V1'
# https://www.jiuzhang.com/solution/next-closest-time/#tag-highlight-lang-python
from datetime import *
class Solution:
    """
    @param time: the given time
    @return: the next closest time
    """
    def nextClosestTime(self, time):
        digits = set(time)
        while True:
            time = (datetime.strptime(time, '%H:%M') + timedelta(minutes=1)).strftime('%H:%M')
            if set(time) <= digits:
                return time
                
# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def nextClosestTime(self, time):
        """
        :type time: str
        :rtype: str
        """
        h, m = time.split(":")
        curr = int(h) * 60 + int(m)
        result = None
        for i in range(curr+1, curr+1441):
            t = i % 1440
            h, m = t // 60, t % 60
            result = "%02d:%02d" % (h, m)
            if set(result) <= set(time):
                break
        return result