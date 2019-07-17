# V0 

# V1 
# https://blog.csdn.net/NXHYD/article/details/72326279
class Solution(object):
    def findRadius(self, houses, heaters):
        """
        :type houses: List[int]
        :type heaters: List[int]
        :rtype: int
        """
        heaters.sort()
        ans = 0
        for house in houses:
            radius = 0x7fffffff
            le = bisect.bisect_right(heaters, house)
            if le > 0:
                radius = min(radius, house - heaters[le -1])
            ge = bisect.bisect_left(heaters, house)
            if ge < len(heaters):
                radius = min(radius, heaters[ge] - house)
            ans = max(ans, radius)
        return ans

# V1' 
# https://blog.csdn.net/NXHYD/article/details/72326279
import math
class Solution(object):
    def findRadius(self, houses, heaters):
        """
        :type houses: List[int]
        :type heaters: List[int]
        :rtype: int
        """
        j = 0
        i = 0
        heaters = sorted(heaters) + [float('Inf')]
        for each in sorted(houses):
            while( each >= sum(heaters[i:i+2])/2. ):
                # print(heaters[i]),
                i+=1
            j = max(j , abs(heaters[i] - each) )
        return(j)

# V1'' 
# https://blog.csdn.net/NXHYD/article/details/72326279
class Solution(object):
    def findRadius(self, houses, heaters):
        """
        :type houses: List[int]
        :type heaters: List[int]
        :rtype: int
        """
        heaters.sort()
        heaters = [float('-inf')]+heaters+[float('inf')]
        r = i = 0 
        for x in sorted(houses):
            while x > heaters[i+1]:
                i += 1
            dis = min (x - heaters[i], heaters[i+1]- x)
            r = max(r, dis)
        return r

# V2 
# Time:  O((m + n) * logn), m is the number of the houses, n is the number of the heaters.
# Space: O(1)
import bisect
class Solution(object):
    def findRadius(self, houses, heaters):
        """
        :type houses: List[int]
        :type heaters: List[int]
        :rtype: int
        """
        heaters.sort()
        min_radius = 0
        for house in houses:
        	equal_or_larger = bisect.bisect_left(heaters, house)
        	curr_radius = float("inf")
        	if equal_or_larger != len(heaters):
        	    curr_radius = heaters[equal_or_larger] - house
        	if equal_or_larger != 0:
        	    smaller = equal_or_larger-1
        	    curr_radius = min(curr_radius, house - heaters[smaller])
        	min_radius = max(min_radius, curr_radius)
        return min_radius
