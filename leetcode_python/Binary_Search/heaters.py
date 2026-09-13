"""

475. Heaters
Medium

Winter is coming! During the contest, your first job is to design a standard heater with a fixed warm radius to warm all the houses.

Every house can be warmed, as long as the house is within the heater's warm radius range.

Given the positions of houses and heaters on a horizontal line, return the minimum radius standard of heaters so that those heaters could cover all houses.

Notice that all the heaters follow your radius standard, and the warm radius will be the same.

Example 1:

Input: houses = [1,2,3], heaters = [2]
Output: 1
Explanation: The only heater was placed in the position 2, and if we use the radius 1 standard, then all the houses can be warmed.

Example 2:

Input: houses = [1,2,3,4], heaters = [1,4]
Output: 1
Explanation: The two heaters were placed at positions 1 and 4. We need to use a radius 1 standard, then all the houses can be warmed.

Example 3:

Input: houses = [1,5], heaters = [2]
Output: 3

Constraints:

1 <= houses.length, heaters.length <= 3 * 10^4
1 <= houses[i], heaters[i] <= 10^9

"""

# V0 

# V1
# https://blog.csdn.net/NXHYD/article/details/72326279
# time = O((m + n) log n), m = len(houses), n = len(heaters)
# space = O(1)
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
# time = O(m log m + n log n), m = len(houses), n = len(heaters)
# space = O(n)
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
# time = O(m log m + n log n), m = len(houses), n = len(heaters)
# space = O(n)
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
# time = O((m + n) * log n), m is the number of the houses, n is the number of the heaters.
# space = O(1)
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
