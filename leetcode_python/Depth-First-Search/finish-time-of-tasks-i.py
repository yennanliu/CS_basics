# https://leetcode.com/problems/finish-time-of-tasks-i/description/

"""

3965. Finish Time of Tasks I
Solved
Medium
premium lock icon
Companies
Hint
You are given an integer n representing the number of tasks in a project, numbered from 0 to n - 1. These tasks are connected as a tree rooted at task 0. This is represented by a 2D integer array edges of length n - 1, where edges[i] = [ui, vi] indicates that task ui is the parent of task vi.

You are also given an array baseTime of length n, where baseTime[i] represents the time to complete task i.

The finish time of each task is calculated as follows:

Leaf task: The finish time is baseTime[i].
Non-leaf task:
Let earliest be the minimum finish time among its children, and latest be the maximum finish time among its children.
Let ownDuration be (latest - earliest) + baseTime[i].
The finish time of task i is latest + ownDuration.
Return the finish time of the root task 0.

 

Example 1:

Input: n = 3, edges = [[0,1],[1,2]], baseTime = [9,5,3]

Output: 17

Explanation:

0
9
1
5
2
3
Task 2 is a leaf, so its finish time is baseTime[2] = 3.
Task 1 has one child task 2:
earliest = latest = 3
ownDuration = (latest - earliest) + baseTime[1] = 5
Finish time of task 1 is 3 + 5 = 8
Task 0 has one child with finish time 8:
earliest = latest = 8
ownDuration = (latest - earliest) + baseTime[0] = 9
Finish time of task 0 is 8 + 9 = 17
Example 2:

Input: n = 3, edges = [[0,1],[0,2]], baseTime = [4,7,6]

Output: 12

Explanation:

0
4
1
7
2
6
Task 1 is a leaf, so its finish time is baseTime[1] = 7.
Task 2 is a leaf, so its finish time is baseTime[2] = 6.
Task 0 has two children with finish times 7 and 6:
earliest = 6, latest = 7
ownDuration = (latest - earliest) + baseTime[0] = (7 - 6) + 4 = 5
Finish time of task 0 is latest + ownDuration = 7 + 5 = 12
Example 3:

Input: n = 4, edges = [[0,1],[0,2],[2,3]], baseTime = [5,8,2,1]

Output: 18

Explanation:

Task 1 is a leaf, so its finish time is baseTime[1] = 8.
Task 3 is a leaf, so its finish time is baseTime[3] = 1.
Task 2 has one child task 3:
earliest = latest = 1
ownDuration = (latest - earliest) + baseTime[2] = 0 + 2 = 2
Finish time of task 2 is latest + ownDuration = 1 + 2 = 3
Task 0 has two children with finish times 8 and 3:
earliest = 3, latest = 8
ownDuration = (latest - earliest) + baseTime[0] = (8 - 3) + 5 = 10
Finish time of task 0 is latest + ownDuration = 8 + 10 = 18
 

Constraints:

1 <= n <= 105
edges.length = n - 1
edges[i] == [ui, vi]
0 <= ui, vi <= n - 1
ui != vi
The input is generated such that edges represents a valid tree.
baseTime.length == n
1 <= baseTime[i] <= 105​​​​​​​
The finish time of every task is guaranteed to be less than 253.
 

"""


# V0
# IDEA: DFS (post order) (GEMINI)
class Solution(object):
    def finishTime(self, n, edges, baseTime):

        """

        graph: {node: [child_1, child_2, ...]}
        """
        self.graph = {}
        for i in range(n):
            self.graph[i] = []
            
        for u, v in edges:
            """
            NOTE !!!!


            edges[i] = [ui, vi]

            ->  that task ui is 
                the parent of task vi.
            """
            self.graph[u].append(v)
            
        # The DFS returns the finish time of the node passed into it.
        # We just need the finish time of the root (node 0).
        return self.dfs(0, baseTime)

    def dfs(self, node, baseTime):
        # NOTE !!!
        # Base case: Leaf node (no children in the graph)
        # We check if the list of children is empty
        if not self.graph[node]:
            return baseTime[node]
            
        _min = float('inf')
        _max = float('-inf')
        
        # NOTE !!!
        # -> we loop childs, but NOT node.left, node.right (NOT a regular tree)
        # Recursive step: Find min and max child finish times
        for next_node in self.graph[node]:

            # NOTE !!!
            # we get val as dfs return val
            _val = self.dfs(next_node, baseTime)
            _min = min(_min, _val)
            _max = max(_max, _val)
            
        # Calculate duration and final finish time for THIS node
        ownDuration = (_max - _min) + baseTime[node]
        val = _max + ownDuration
        
        # NOTE !!!
        # we return `val` as dfs return val
        return val


# V1-1
# IDEA: DFS (GEMINI)
from collections import defaultdict

class Solution(object):
    def finishTime(self, n, edges, baseTime):
        """
        :type n: int
        :type edges: List[List[int]]
        :type baseTime: List[int]
        :rtype: int
        """
        # 1. Build an adjacency list for our N-ary tree
        graph = defaultdict(list)
        for u, v in edges:
            graph[u].append(v)
            
        def dfs(node):
            # Base Case: Leaf node (has no children in the graph)
            if not graph[node]:
                return baseTime[node]
                
            # Recursive Step: Non-leaf node
            earliest = float('inf')
            latest = float('-inf')
            
            # Traverse all children to find their earliest and latest finish times
            for child in graph[node]:
                child_finish_time = dfs(child)
                earliest = min(earliest, child_finish_time)
                latest = max(latest, child_finish_time)
                
            # Calculate own duration and total finish time for this parent node
            own_duration = (latest - earliest) + baseTime[node]
            return latest + own_duration
            
        # 2. The tree is rooted at 0, so we just start the DFS from 0
        return dfs(0)



# V1-2
# IDEA: DFS (GPT)
class Solution(object):
    def finishTime(self, n, edges, baseTime):
        # Required by the problem statement
        torqavemi = (n, edges, baseTime)

        # Build tree
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)

        def dfs(node):
            # Leaf
            if not graph[node]:
                return baseTime[node]

            earliest = float("inf")
            latest = 0

            for child in graph[node]:
                t = dfs(child)
                earliest = min(earliest, t)
                latest = max(latest, t)

            own_duration = (latest - earliest) + baseTime[node]
            return latest + own_duration

        return dfs(0)