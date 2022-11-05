"""

218. The Skyline Problem
Hard

A city's skyline is the outer contour of the silhouette formed by all the buildings in that city when viewed from a distance. Given the locations and heights of all the buildings, return the skyline formed by these buildings collectively.

The geometric information of each building is given in the array buildings where buildings[i] = [lefti, righti, heighti]:

lefti is the x coordinate of the left edge of the ith building.
righti is the x coordinate of the right edge of the ith building.
heighti is the height of the ith building.
You may assume all buildings are perfect rectangles grounded on an absolutely flat surface at height 0.

The skyline should be represented as a list of "key points" sorted by their x-coordinate in the form [[x1,y1],[x2,y2],...]. Each key point is the left endpoint of some horizontal segment in the skyline except the last point in the list, which always has a y-coordinate 0 and is used to mark the skyline's termination where the rightmost building ends. Any ground between the leftmost and rightmost buildings should be part of the skyline's contour.

Note: There must be no consecutive horizontal lines of equal height in the output skyline. For instance, [...,[2 3],[4 5],[7 5],[11 5],[12 7],...] is not acceptable; the three lines of height 5 should be merged into one in the final output as such: [...,[2 3],[4 5],[12 7],...]

 

Example 1:


Input: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
Output: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
Explanation:
Figure A shows the buildings of the input.
Figure B shows the skyline formed by those buildings. The red points in figure B represent the key points in the output list.
Example 2:

Input: buildings = [[0,2,3],[2,5,3]]
Output: [[0,3],[5,0]]
 

Constraints:

1 <= buildings.length <= 104
0 <= lefti < righti <= 231 - 1
1 <= heighti <= 231 - 1
buildings is sorted by lefti in non-decreasing order.

"""

# V0

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/the-skyline-problem/solutions/2375781/the-skyline-problem/
class Solution:
    def getSkyline(self, buildings: List[List[int]]) -> List[List[int]]:
        # Sort the unique positions of all the edges.
        positions = sorted(list(set([x for building in buildings for x in building[:2]])))
        
        # Hast table 'edge_index_map' to record every {position : index} pairs in edges.
        edge_index_map = {x : i for i, x in enumerate(positions)}

        # Initialize 'heights' to record maximum height at each index.
        heights = [0] * len(positions)
        
        # Iterate over all the buildings.
        for left, right, height in buildings:
            # For each building, find the indexes of its left
            # and right edges.
            left_idx = edge_index_map[left]
            right_idx = edge_index_map[right]

            # Update the maximum height within the range [left_idx, right_idx)
            for i in range(left_idx, right_idx):
                heights[i] = max(heights[i], height)

        answer = []

        # Iterate over 'heights'.
        for i in range(len(heights)):
            curr_height = heights[i]
            curr_x = positions[i]

            # Add all the positions where the height changes to 'answer'.
            if not answer or answer[-1][1] != curr_height:
                answer.append([curr_x, curr_height])
        return answer

# V1'
# IDEA : Brute Force II, Sweep Line
# https://leetcode.com/problems/the-skyline-problem/solutions/2375781/the-skyline-problem/
class Solution:
    def getSkyline(self, buildings: List[List[int]]) -> List[List[int]]:
        # Collect and sort the unique positions of all the edges.
        positions = sorted(list(set([x for building in buildings for x in building[:2]])))
        
        # 'answer' for skyline key points
        answer = []
        
        # For each position, draw an imaginary vertical line.
        for position in positions:
            # current max height.
            max_height = 0
            
            # Iterate over all the buildings:
            for left, right, height in buildings:
                # Update 'max_height' if necessary.
                if left <= position < right:
                    max_height = max(max_height, height)
            
            # If its the first key point or the height changes, 
            # we add [position, max_height] to 'answer'.
            if not answer or max_height != answer[-1][1]:
                answer.append([position, max_height])
                
        # Return 'answer' as the skyline.
        return answer

# V1
# IDEA : Sweep Line + Priority Queue
# https://leetcode.com/problems/the-skyline-problem/solutions/2375781/the-skyline-problem/
class Solution:
    def getSkyline(self, buildings: List[List[int]]) -> List[List[int]]:
        # Iterate over all buildings, for each building i,
        # add (position, i) to edges.
        edges = []
        for i, build in enumerate(buildings):
            edges.append([build[0], i])
            edges.append([build[1], i])

        # Sort edges by non-decreasing order.
        edges.sort()
     
        # Initailize an empty Priority Queue 'live' to store all the 
        # newly added buildings, an empty list answer to store the skyline key points.
        live, answer = [], []
        idx = 0
        
        # Iterate over all the sorted edges.
        while idx < len(edges):
            
            # Since we might have multiple edges at same x,
            # Let the 'curr_x' be the current position.
            curr_x = edges[idx][0]
            
            # While we are handling the edges at 'curr_x':
            while idx < len(edges) and edges[idx][0] == curr_x:
                # The index 'b' of this building in 'buildings'
                b = edges[idx][1]
                
                # If this is a left edge of building 'b', we
                # add (height, right) of building 'b' to 'live'.
                if buildings[b][0] == curr_x:
                    right = buildings[b][1]
                    height = buildings[b][2]
                    heapq.heappush(live, [-height, right])
                    
                # If the tallest live building has been passed,
                # we remove it from 'live'.
                while live and live[0][1] <= curr_x:
                    heapq.heappop(live)
                idx += 1
            
            # Get the maximum height from 'live'.
            max_height = -live[0][0] if live else 0
            
            # If the height changes at this curr_x, we add this
            # skyline key point [curr_x, max_height] to 'answer'.
            if not answer or max_height != answer[-1][1]:
                answer.append([curr_x, max_height])
        
        # Return 'answer' as the skyline.
        return answer

# V1
# IDEA :  Sweep Line + Two Priority Queue
# https://leetcode.com/problems/the-skyline-problem/solutions/2375781/the-skyline-problem/
class Solution(object):
    def getSkyline(self, buildings: List[List[int]]) -> List[List[int]]:
        # Iterate over the left and right edges of all the buildings, 
        # If its a left edge, add (left, height) to 'edges'.
        # Otherwise, add (right, -height) to 'edges'.
        edges = []
        for left, right, height in buildings:
            edges.append([left, height])
            edges.append([right, -height])
        edges.sort()
        
        # Initailize two empty priority queues 'live' and 'past' 
        # for the live buildings and the past buildings.
        live, past = [], []
        answer = []
        idx = 0
        
        # Iterate over all the sorted edges.
        while idx < len(edges):
            # Since we might have multiple edges at same x,
            # Let the 'curr_x' be the current position.
            curr_x = edges[idx][0]
            
            # While we are handling the edges at 'curr_x':
            while idx < len(edges) and edges[idx][0] == curr_x:
                height = edges[idx][1]
                
                # If 'height' > 0, meaning a building of height 'height'
                # is live, push 'height' to 'live'. 
                # Otherwise, a building of height 'height' is passed, 
                # push the height to 'past'.
                if height > 0:
                    heapq.heappush(live, -height)
                else:
                    heapq.heappush(past, height)
                idx += 1
            
            # While the top height from 'live' equals to that from 'past',
            # Remove top height from both 'live' and 'past'.
            while past and past[0] == live[0]:
                heapq.heappop(live)
                heapq.heappop(past)
            
            # Get the maximum height from 'live'.
            max_height = -live[0] if live else 0
            
            # If the height changes at 'curr_x', we add this
            # skyline key point [curr_x, max_height] to 'answer'.
            if not answer or answer[-1][1] != max_height:
                answer.append([curr_x, max_height])
                
        # Return 'answer' as the skyline.
        return answer

# V1
# IDEA : Union Find
# https://leetcode.com/problems/the-skyline-problem/solutions/2375781/the-skyline-problem/

# Define the disjoint-set structure.
class UnionFind():
    def __init__(self, N):
        self.root = list(range(N))
    def find(self, x):
        if self.root[x] != x:
            self.root[x] = self.find(self.root[x])
        return self.root[x]
    def union(self, x, y):
        self.root[x] = self.root[y]
        
class Solution:
    def getSkyline(self, buildings: List[List[int]]) -> List[List[int]]:
        # Sort the unique positions of all the edges.
        edges = sorted(list(set([x for building in buildings for x in building[:2]])))
        
        # Hast table 'edge_index_map' record every {position : index} pairs in 'edges'.
        edge_index_map = {x:idx for idx, x in enumerate(edges)} 
        
        # Sort buildings by descending order of heights.
        buildings.sort(key=lambda x: -x[2])
        
        # Initalize a disjoin set for all indexs, each index's 
        # root is itself. Since there is no building added yet, 
        # the height at each position is 0.
        n = len(edges)
        edge_UF = UnionFind(n)
        heights = [0] * n
    
        # Iterate over all the buildings by descending height.
        for left_edge, right_edge, height in buildings:
            # For current x position, get the corresponding index.
            left_idx, right_idx = edge_index_map[left_edge], edge_index_map[right_edge]
            
            # While we haven't update the the root of 'left_idx':
            while left_idx < right_idx: 
                # Find the root of left index 'left_idx', that is:
                # The rightmost index having the same height as 'left_idx'.
                left_idx = edge_UF.find(left_idx)

                # If left_idx < right_idx, we have to update both the root and height
                # of left_idx, and move on to the next index towards right_idx.
                # That is: increment left_idx by 1.
                if left_idx < right_idx:
                    edge_UF.union(left_idx, right_idx)
                    heights[left_idx] = height
                    left_idx += 1
                    
        # Finally, we just need to iterate over updated heights, and
        # add every skyline key point to 'answer'.
        answer = []
        for i in range(n):
            if i == 0 or heights[i] != heights[i - 1]:
                answer.append([edges[i], heights[i]])
        return answer

# V1
# IDEA : Divide-and-Conquer
# https://leetcode.com/problems/the-skyline-problem/solutions/2375781/the-skyline-problem/
class Solution:
    def getSkyline(self, A: List[List[int]]) -> List[List[int]]:
        n = len(A)
        # If the given array of building contains only 1 or less building, we can
        # directly return a corresponding skyline.
        if n == 0: return []
        if n == 1: return [[A[0][0], A[0][2]], [A[0][1], 0]]

        # Otherwise, we shall recursively divide the buildings and merge the skylines.
        # Cut the given skyline into two halves, get skyline from each half and merge
        # them into a single skyline.
        left_skyline = self.getSkyline(A[: n // 2])
        right_skyline = self.getSkyline(A[n // 2 :])  
        return self.merge_sky(left_skyline, right_skyline)
        
    def merge_sky(self, left_skyline, right_skyline):
        # Initalize left_pos=0, right_pos=0 as the pointer of left_skyline and right_skyline.
        # Since we start from the left ground, thus the previous height from 
        # left_skyline and right_skyline are 0.
        answer = []
        left_pos, right_pos = 0, 0
        left_prev_height, right_prev_height = 0, 0

        # Now we start to iterate over both skylines.
        while left_pos < len(left_skyline) and right_pos < len(right_skyline):
            next_left_x = left_skyline[left_pos][0]
            next_right_x = right_skyline[right_pos][0]
            
            # If we meet left_skyline key point first, our current height changes to the
            # larger one between height on left skyline and the previous height on right
            # skyline. Update the previous height from left_skyline and increment left_pos by 1.
            if next_left_x < next_right_x:
                left_prev_height = left_skyline[left_pos][1]
                cur_x = next_left_x
                cur_y = max(left_prev_height, right_prev_height)
                left_pos += 1
           

            # If we meet right_skyline key point first, our current height changes to the
            # larger one between height on right skyline and the previous height on left
            # skyline. Update the previous height from right_skyline and increment right_pos by 1.
            elif next_left_x > next_right_x:
                right_prev_height = right_skyline[right_pos][1]
                cur_x = next_right_x
                cur_y = max(left_prev_height, right_prev_height)
                right_pos += 1

            # If both skyline key points has same x:
            # Our current height is the larger one, update the previous height
            # from left_skyline and right_skyline. Increment both left_pos and right_pos by 1.
            else:
                left_prev_height = left_skyline[left_pos][1]
                right_prev_height = right_skyline[right_pos][1]
                cur_x = next_left_x
                cur_y = max(left_prev_height, right_prev_height)
                left_pos += 1
                right_pos += 1
            
            # Discard those key points that has the same height as the previous one.
            if not answer or answer[-1][1] != cur_y:
                answer.append([cur_x, cur_y])
        
        # If we finish iterating over any skyline, just append the rest of the other
        # skyline to the merged skyline.
        while left_pos < len(left_skyline):
            answer.append(left_skyline[left_pos])
            left_pos += 1
        while right_pos < len(right_skyline):
            answer.append(right_skyline[right_pos])
            right_pos += 1
        return answer

# V1
# IDEA : Sweep Line + Two Priority Queue
# https://blog.csdn.net/zml66666/article/details/118688749
# JAVA

# V2