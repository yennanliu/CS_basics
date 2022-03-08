"""

2021. Brightest Position on Street
Medium

A perfectly straight street is represented by a number line. The street has street lamp(s) on it and is represented by a 2D integer array lights. Each lights[i] = [positioni, rangei] indicates that there is a street lamp at position positioni that lights up the area from [positioni - rangei, positioni + rangei] (inclusive).

The brightness of a position p is defined as the number of street lamp that light up the position p.

Given lights, return the brightest position on the street. If there are multiple brightest positions, return the smallest one.

 

Example 1:


Input: lights = [[-3,2],[1,2],[3,3]]
Output: -1
Explanation:
The first street lamp lights up the area from [(-3) - 2, (-3) + 2] = [-5, -1].
The second street lamp lights up the area from [1 - 2, 1 + 2] = [-1, 3].
The third street lamp lights up the area from [3 - 3, 3 + 3] = [0, 6].

Position -1 has a brightness of 2, illuminated by the first and second street light.
Positions 0, 1, 2, and 3 have a brightness of 2, illuminated by the second and third street light.
Out of all these positions, -1 is the smallest, so return it.
Example 2:

Input: lights = [[1,0],[0,1]]
Output: 1
Explanation:
The first street lamp lights up the area from [1 - 0, 1 + 0] = [1, 1].
The second street lamp lights up the area from [0 - 1, 0 + 1] = [-1, 1].

Position 1 has a brightness of 2, illuminated by the first and second street light.
Return 1 because it is the brightest position on the street.
Example 3:

Input: lights = [[1,2]]
Output: -1
Explanation:
The first street lamp lights up the area from [1 - 2, 1 + 2] = [-1, 3].

Positions -1, 0, 1, 2, and 3 have a brightness of 1, illuminated by the first street light.
Out of all these positions, -1 is the smallest, so return it.
 

Constraints:

1 <= lights.length <= 105
lights[i].length == 2
-108 <= positioni <= 108
0 <= rangei <= 108

"""

# V0

# V1
# IDEA : LC 253 MEETING ROOM II
# https://leetcode.com/problems/brightest-position-on-street/discuss/1494005/Python%3A-Basically-meeting-room-II
# IDEA :
# So, the only difference in this problem in comparison to meeting room II is that we have to convert our input into intervals, which is straightforward and basically suggested to use by the first example. So, here is my code and here is meeting rooms II https://leetcode.com/problems/meeting-rooms-ii/
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        intervals, heap, res, best = [], [], 0, 0
        for x, y in lights: intervals.append([x-y, x+y])
        intervals.sort()

        for left, right in intervals:            
            while heap and heap[0] < left: heappop(heap)
            heappush(heap, right)
            if len(heap) > best:
                best = len(heap)
                res = left
        return res

# V1'
# IDEA : diff array + sorting
# https://github.com/doocs/leetcode/blob/main/solution/2000-2099/2021.Brightest%20Position%20on%20Street/README.md
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        d = defaultdict(int)
        for p, r in lights:
            d[p - r] += 1
            d[p + r + 1] -= 1
        s = mx = ans = 0
        for k in sorted(d):
            s += d[k]
            if s > mx:
                mx = s
                ans = k
        return ans

# V1''
# IDEA : heapq
# https://leetcode.com/problems/brightest-position-on-street/discuss/1502419/python-solution
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:        
        lightRange = sorted([(light[0] - light[1], light[0] + light[1]) for light in lights], key = lambda x: (x[0], x[1]))
        brightPostion = []
        curMax = 0
        res = 0
        for s, e in lightRange:
            if brightPostion and s > brightPostion[0]:
                heappop(brightPostion)
            heappush(brightPostion, e)
            if len(brightPostion) > curMax:
                curMax = len(brightPostion)
                res = s
        return res

# V1'''
# IDEA : SCAN LINE
# https://leetcode.com/problems/brightest-position-on-street/discuss/1657355/Python-Sweep-Line
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        # light range array
        light_r = []
        for p,r in lights:
            light_r.append((p-r,'start'))
            light_r.append((p+r+1,'end'))
        light_r.sort(key = lambda x:x[0])
        # focus on the boundary of light range 
        
        bright = collections.defaultdict(int)
        power = 0
        for l in light_r:
            if 'start' in l:
                power += 1
            else:
                power -= 1
            bright[l[0]] = power
                
        list_bright = list(bright.values())
        list_position = list(bright.keys())
        
        max_bright = max(list_bright)
        max_bright_index = list_bright.index(max_bright)
        
        return list_position[max_bright_index]

# V1''''
# https://leetcode.com/problems/brightest-position-on-street/discuss/1494223/Python-3-or-Sweep-Line-Sorting-or-Explanation
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        d = collections.defaultdict(int)
        for i, dis in lights:
            d[i-dis] += 1                             # index at left-end starts covering
            d[i+dis+1] -= 1                           # next index of right-end stops covering
        cur, max_idx, max_val = 0, -1, -sys.maxsize
        for idx, val in sorted(d.items()):            # sort by key would be sufficient
            cur += val
            if cur > max_val:                         # count maximum brightness
                max_val, max_idx = cur, idx
        return max_idx

# V1'''''
# https://leetcode.com/problems/brightest-position-on-street/discuss/1509561/Python-O(nlogn)-Solution
class Solution:
    def brightestPosition(self, lights: List[List[int]]) -> int:
        starts = sorted([pos-dis for pos, dis in lights])
        ends = sorted([pos+dis for pos, dis in lights])
        endInd, maxInd = 0, 0
        intCount, maxInt = 0, 0
        for st in starts:
            while st > ends[endInd]:
                intCount -= 1
                endInd += 1
            intCount += 1
            if intCount > maxInt:
                maxInt = intCount
                maxInd = st
        return maxInd

# V1'''''''
# https://blog.csdn.net/sinat_30403031/article/details/121528384

# V2