# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80476862
# IDEA : DFS 
class Solution:
    def canVisitAllRooms(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: bool
        """
        visited = [0] * len(rooms)
        self.dfs(rooms, 0, visited)
        return sum(visited) == len(rooms)
        
    def dfs(self, rooms, index, visited):
        visited[index] = 1
        for key in rooms[index]:
            if not visited[key]:
                self.dfs(rooms, key, visited)
 
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80476862
# IDEA : BFS 
# class Solution {
# public:
#     bool canVisitAllRooms(vector<vector<int>>& rooms) {
#         int N = rooms.size();
#         vector<int> visited(N);
#         queue<int> q;
#         q.push(0);
#         while (!q.empty()) {
#             int f = q.front(); q.pop();
#             if (visited[f]) continue;
#             visited[f] = 1;
#             for (int n : rooms[f]) {
#                 q.push(n);
#             }
#         }
#         int res = 0;
#         for (int v : visited) res += v;
#         return res == N;
#     }
# };

# V1'
# https://www.jiuzhang.com/solution/keys-and-rooms/#tag-highlight-lang-python
# IDEA : BFS 
import collections
class Solution:
    """
    @param rooms: a list of keys rooms[i]
    @return: can you enter every room
    """
    def canVisitAllRooms(self, rooms):
        # Write your code here
        q, seen = collections.deque([0]), {0}        
        while q:
            if len(seen) == len(rooms): return True
            k = q.popleft()            
            for v in rooms[k]:
                if v not in seen:
                    seen.add(v)
                    q.append(v)                  
        return len(seen) == len(rooms)

# V2 
# Time:  O(n!)
# Space: O(n)
class Solution(object):
    def canVisitAllRooms(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: bool
        """
        lookup = set([0])
        stack = [0]
        while stack:
            node = stack.pop()
            for nei in rooms[node]:
                if nei not in lookup:
                    lookup.add(nei)
                    if len(lookup) == len(rooms):
                        return True
                    stack.append(nei)
        return len(lookup) == len(rooms)