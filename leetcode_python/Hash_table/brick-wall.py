"""

554. Brick Wall
Medium

There is a rectangular brick wall in front of you with n rows of bricks. The ith row has some number of bricks each of the same height (i.e., one unit) but they can be of different widths. The total width of each row is the same.

Draw a vertical line from the top to the bottom and cross the least bricks. If your line goes through the edge of a brick, then the brick is not considered as crossed. You cannot draw a line just along one of the two vertical edges of the wall, in which case the line will obviously cross no bricks.

Given the 2D array wall that contains the information about the wall, return the minimum number of crossed bricks after drawing such a vertical line.

 

Example 1:


Input: wall = [[1,2,2,1],[3,1,2],[1,3,2],[2,4],[3,1,2],[1,3,1,1]]
Output: 2
Example 2:

Input: wall = [[1],[1],[1]]
Output: 3
 

Constraints:

n == wall.length
1 <= n <= 104
1 <= wall[i].length <= 104
1 <= sum(wall[i].length) <= 2 * 104
sum(wall[i]) is the same for each row i.
1 <= wall[i][j] <= 231 - 1

"""

# V0
# IDEA : HASH TABLE + COUNTER UPDATE (looping every element in the list and cumsum and get the max count)
### DEMO : counter.update()
# In [87]: _counter = collections.Counter()

# In [88]: _counter
# Out[88]: Counter()

# In [89]: _counter.update([1])

# In [90]: _counter
# Out[90]: Counter({1: 1})

# In [91]: _counter.update([1])

# In [92]: _counter
# Out[92]: Counter({1: 2})

# In [93]: _counter.update([2])

# In [94]: _counter
# Out[94]: Counter({1: 2, 2: 1})
import collections
class Solution(object):
    def leastBricks(self, wall):
        _counter = collections.Counter()
        count = 0
        # go through every sub-wall in wall
        for w in wall:
            cum_sum = 0
            # go through every element in sub-wall
            for i in range(len(w) - 1):
                cum_sum += w[i]
                ### NOTE we can update collections.Counter() via below
                _counter.update([cum_sum])
                count = max(count, _counter[cum_sum])
        return len(wall) - count

# V0'
# IDEA : HASH TABLE + COUNTER UPDATE (looping every element in the list and cumsum and get the max count)
class Solution(object):
    def leastBricks(self, wall):
        left_counter = collections.Counter()
        count = 0
        for row in wall:
            left = 0
            for i in range(len(row) - 1):
                left += row[i]
                left_counter.update([left])
                count = max(count, left_counter[left])
        return len(wall) - count

# V1 
# http://bookshadow.com/weblog/2017/04/09/leetcode-brick-wall/
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        rims = collections.Counter()
        for bricks in wall:
            cnt = 0
            for b in bricks:
                if cnt: rims[cnt] += 1
                cnt += b
        return len(wall) - max(rims.values() or [0])

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80526298
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        left_counter = collections.Counter()
        count = 0
        for row in wall:
            left = 0
            for i in range(len(row) - 1):
                left += row[i]
                left_counter.update([left])
                count = max(count, left_counter[left])
        return len(wall) - count

# V1''
# IDEA : HASHMAP
# https://leetcode.com/problems/brick-wall/solution/
# JAVA
# public class Solution {
#     public int leastBricks(List < List < Integer >> wall) {
#         HashMap < Integer, Integer > map = new HashMap < > ();
#         for (List < Integer > row: wall) {
#             int sum = 0;
#             for (int i = 0; i < row.size() - 1; i++) {
#                 sum += row.get(i);
#                 if (map.containsKey(sum))
#                     map.put(sum, map.get(sum) + 1);
#                 else
#                     map.put(sum, 1);
#             }
#         }
#         int res = wall.size();
#         for (int key: map.keySet())
#             res = Math.min(res, wall.size() - map.get(key));
#         return res;
#     }
# }

# V2 
# Time:  O(n), n is the total number of the bricks
# Space: O(m), m is the total number different widths
import collections
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        widths = collections.defaultdict(int)
        result = len(wall)
        for row in wall:
            width = 0
            for i in range(len(row)-1):
                width += row[i]
                widths[width] += 1
                result = min(result, len(wall) - widths[width])
        return result