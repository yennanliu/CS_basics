"""

815. Bus Routes
Hard

You are given an array routes representing bus routes where routes[i] is a bus route that the ith bus repeats forever.

For example, if routes[0] = [1, 5, 7], this means that the 0th bus travels in the sequence 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... forever.
You will start at the bus stop source (You are not on any bus initially), and you want to go to the bus stop target. You can travel between bus stops by buses only.

Return the least number of buses you must take to travel from source to target. Return -1 if it is not possible.

 

Example 1:

Input: routes = [[1,2,7],[3,6,7]], source = 1, target = 6
Output: 2
Explanation: The best strategy is take the first bus to the bus stop 7, then take the second bus to the bus stop 6.
Example 2:

Input: routes = [[7,12],[4,5,15],[6],[15,19],[9,12,13]], source = 15, target = 12
Output: -1
 

Constraints:

1 <= routes.length <= 500.
1 <= routes[i].length <= 105
All the values of routes[i] are unique.
sum(routes[i].length) <= 105
0 <= routes[i][j] < 106
0 <= source, target < 106


"""

# V0
# IDEA : BFS + GRAPH
class Solution(object):
    def numBusesToDestination(self, routes, S, T):
        # edge case:
        if S == T:
            return 0
        to_routes = collections.defaultdict(set)
        for i, route in enumerate(routes):
            for j in route:
                to_routes[j].add(i)
        bfs = [(S, 0)]
        seen = set([S])
        for stop, bus in bfs:
            if stop == T:
            	return bus
            for i in to_routes[stop]:
                for j in routes[i]:
                    if j not in seen:
                        bfs.append((j, bus + 1))
                        seen.add(j)
                routes[i] = []  # seen route
        return -1

# V1
# http://zxi.mytechroad.com/blog/graph/leetcode-815-bus-routes/
# https://www.youtube.com/watch?v=vEcm5farBls
# https://blog.csdn.net/weixin_44617992/article/details/112388066
# C++
# class Solution {
# public:
#   int numBusesToDestination(vector<vector<int>>& routes, int S, int T) {
#     if (S == T) return 0;
#    
#     unordered_map<int, vector<int>> m;
#     for (int i = 0; i < routes.size(); ++i)
#       for (const int stop : routes[i])
#         m[stop].push_back(i);
#    
#     vector<int> visited(routes.size(), 0);
#     queue<int> q;
#     q.push(S);
#     int buses = 0;
#   
#     while (!q.empty()) {
#       int size = q.size();      
#       ++buses;
#       while (size--) {
#         int curr = q.front(); q.pop();        
#         for (const int bus : m[curr]) {
#           if (visited[bus]) continue;          
#           visited[bus] = 1;
#           for (int stop : routes[bus]) {
#             if (stop == T) return buses;            
#             q.push(stop);
#           }
#         }        
#       }      
#     }
#     return -1;
#   }
# };

# V1'
# IDEA : BFS
# https://leetcode.com/problems/bus-routes/discuss/122771/C%2B%2BJavaPython-BFS-Solution
# IDEA :
# The first part loop on routes and record stop to routes mapping in to_route.
# The second part is general bfs. Take a stop from queue and find all connected route.
# The hashset seen record all visited stops and we won't check a stop for twice.
# We can also use a hashset to record all visited routes, or just clear a route after visit.
class Solution(object):
    def numBusesToDestination(self, routes, S, T):
        to_routes = collections.defaultdict(set)
        for i, route in enumerate(routes):
            for j in route:
                to_routes[j].add(i)
        bfs = [(S, 0)]
        seen = set([S])
        for stop, bus in bfs:
            if stop == T: return bus
            for i in to_routes[stop]:
                for j in routes[i]:
                    if j not in seen:
                        bfs.append((j, bus + 1))
                        seen.add(j)
                routes[i] = []  # seen route
        return -1

# V1''
# IDEA : BFS
# https://leetcode.com/problems/bus-routes/discuss/151289/Python-BFS-With-Explanation
# Reference: https://leetcode.com/problems/bus-routes/discuss/122712/Simple-Java-Solution-using-BFS
from collections import deque
class Solution:
    # This is a very good BFS problem.
    # In BFS, we need to traverse all positions in each level firstly, and then go to the next level.
    # Our task is to figure out:
    # 1. What is the level in this problem?
    # 2. What is the position we want in this problem?
    # 3. How to traverse all positions in a level?
    # 
    # For this problem:
    # 1. The level is each time to take bus.
    # 2. The position is all of the stops you can reach for taking one time of bus.
    # 3. Using a queue to record all of the stops can be arrived for each time you take buses.
    def numBusesToDestination(self, routes, S, T):
        """
        :type routes: List[List[int]]
        :type S: int
        :type T: int
        :rtype: int
        """
        # You already at the terminal, so you needn't take any bus.
        if S == T: return 0
        
        # You need to record all the buses you can take at each stop so that you can find out all
        # of the stops you can reach when you take one time of bus.
        # the key is stop and the value is all of the buses you can take at this stop.
        stopBoard = {} 
        for bus, stops in enumerate(routes):
            for stop in stops:
                if stop not in stopBoard:
                    stopBoard[stop] = [bus]
                else:
                    stopBoard[stop].append(bus)
        
        # The queue is to record all of the stops you can reach when you take one time of bus.
        queue = deque([S])
        # Using visited to record the buses that have been taken before, because you needn't to take them again.
        visited = set()

        res = 0
        while queue:
            # take one time of bus.
            res += 1
            # In order to traverse all of the stops you can reach for this time, you have to traverse
            # all of the stops you can reach in last time.
            pre_num_stops = len(queue)
            for _ in range(pre_num_stops):
                curStop = queue.popleft()
                # Each stop you can take at least one bus, you need to traverse all of the buses at this stop
                # in order to get all of the stops can be reach at this time.
                for bus in stopBoard[curStop]:
                    # if the bus you have taken before, you needn't take it again.
                    if bus in visited: continue
                    visited.add(bus)
                    for stop in routes[bus]:
                        if stop == T: return res
                        queue.append(stop)
        return -1

# V1'''
# IDEA : GRAPH + BFS
# https://leetcode.com/problems/bus-routes/discuss/269514/Python-Graph-BFS
# We can view each bus route as a node. If two routes share at least one stop, then there is an edge between them. Based on that, we can build an adjacent-list graph g.
# Then to get the minimal number of bus routes to go from S to T, we can use BFS. The source node is any node containing stop S and destination node is any node containing stop T. The distance between source nodes and destination nodes is the number of bus routes. The distance starts from 1. (If S and T is on at same bus routes, we return 1).
# A corner case is that S == T, we should return 0.
class Solution(object):
    def numBusesToDestination(self, routes, S, T):
        if S == T: return 0
        routes, n = [set(r) for r in routes], len(routes)
        g = [set() for _ in range(n)]
        for i in range(n):
            for j in range(i):
                if set(routes[i]) & set(routes[j]): 
                    g[i].add(j), g[j].add(i)
        seen, dst = set(i for i,r in enumerate(routes) if S in r), set(i for i,r in enumerate(routes) if T in r)
        q = [(x, 1) for x in seen]
        for x, d in q:
            if x in dst: return d
            for y in g[x]:
                if y not in seen: seen.add(y), q.append((y, d+1))
        return -1

# V1'''''
# IDEA : BFS
# https://leetcode.com/problems/bus-routes/solution/
class Solution(object):
    def numBusesToDestination(self, routes, S, T):
        if S == T: return 0
        routes = map(set, routes)
        graph = collections.defaultdict(set)
        for i, r1 in enumerate(routes):
            for j in range(i+1, len(routes)):
                r2 = routes[j]
                if any(r in r2 for r in r1):
                    graph[i].add(j)
                    graph[j].add(i)

        seen, targets = set(), set()
        for node, route in enumerate(routes):
            if S in route: seen.add(node)
            if T in route: targets.add(node)

        queue = [(node, 1) for node in seen]
        for node, depth in queue:
            if node in targets: return depth
            for nei in graph[node]:
                if nei not in seen:
                    seen.add(nei)
                    queue.append((nei, depth+1))
        return -1

# V2         