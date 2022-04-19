"""

# https://ttzztt.gitbooks.io/lc/content/string/next-closet-time.html

681. Next Closest Time
Medium

Given a time represented in the format "HH:MM", form the next closest time by reusing the current digits. There is no limit on how many times a digit can be reused.

You may assume the given input string is always valid. For example, "01:34", "12:09" are all valid. "1:34", "12:9" are all invalid.

 

Example 1:

Input: time = "19:34"
Output: "19:39"
Explanation: The next closest time choosing from digits 1, 9, 3, 4, is 19:39, which occurs 5 minutes later.
It is not 19:33, because this occurs 23 hours and 59 minutes later.
Example 2:

Input: time = "23:59"
Output: "22:22"
Explanation: The next closest time choosing from digits 2, 3, 5, 9, is 22:22.
It may be assumed that the returned time is next day's time since it is smaller than the input time numerically.
 

Constraints:

time.length == 5
time is a valid time in the form "HH:MM".
0 <= HH < 24
0 <= MM < 60

"""

# V0
# IDEA : brute force + check subset of set
# there is only 60 * 24 = 1440 min per day
# so we can for loop over 1440 mins and check if all digits are still in original set (and return min if there is)
from datetime import datetime, timedelta
class Solution:
    def nextClosestTime(self, time):
        def check(x):
            s = set([_ for _ in list(time) if _ != ':'])
            x_ = [_ for _ in list(x) if _ != ':']
            for _ in x_:
                if _ not in s:
                    return False
            return True

        time_ =  datetime.strptime(time, "%H:%M")
        # there is 60 * 24 = 1440 min per day
        for i in range(1, 1441):
            tmp = time_ + timedelta(minutes=i)
            tmp = tmp.strftime("%H:%M")
            #print ("i = " + str(i) + " time_ = " + str(time_) + " tmp = " + str(tmp))
            if check(tmp):
                return tmp
        return -1

# V0'
# IDEA : brute force + check subset of set 
# LOGIC : start from the given time, then "add 1 minute" in every loop, 
# if all the elements in the upddated time are also the subset of the original time set -> return True (find the answer)
# CONCEPT : subset in python
# https://stackoverflow.com/questions/16579085/how-can-i-verify-if-one-list-is-a-subset-of-another
# >>> a = [1, 3, 5]
# >>> b = [1, 3, 5, 8]
# >>> c = [3, 5, 9]
# >>> set(a) <= set(b)
# True
# >>> set(c) <= set(b)
# False

# >>> a = ['yes', 'no', 'hmm']
# >>> b = ['yes', 'no', 'hmm', 'well']
# >>> c = ['sorry', 'no', 'hmm']
# >>> 
# >>> set(a) <= set(b)
# True
# >>> set(c) <= set(b)
# False 
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
            if set(time) <= digits: # if set(time) is the "subset" of original set (which is digits)
                return time

# V1
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
                
# V1'
# http://bookshadow.com/weblog/2017/09/24/leetcode-next-closest-time/
# time = "23:59"
# time = "2359"
# stime = [2,3,5,9]
# x = 3, time[x] = 9, y = 2
# x = 2, time[x] = 5, y = 3
# x = 1, time[x] = 3, y = 5
# x = 0, time[x] = 2, y = 9 
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