"""

841. Keys and Rooms
Medium

There are n rooms labeled from 0 to n - 1 and all the rooms are locked except for room 0. Your goal is to visit all the rooms. However, you cannot enter a locked room without having its key.

When you visit a room, you may find a set of distinct keys in it. Each key has a number on it, denoting which room it unlocks, and you can take all of them with you to unlock the other rooms.

Given an array rooms where rooms[i] is the set of keys that you can obtain if you visited room i, return true if you can visit all the rooms, or false otherwise.

Example 1:

Input: rooms = [[1],[2],[3],[]]
Output: true
Explanation:
We visit room 0 and pick up key 1.
We then visit room 1 and pick up key 2.
We then visit room 2 and pick up key 3.
We then visit room 3.
Since we were able to visit every room, we return true.

Example 2:

Input: rooms = [[1,3],[3,0,1],[2],[0]]
Output: false
Explanation: We can not enter room number 2 since the only key that unlocks it is in that room.

Constraints:

n == rooms.length
2 <= n <= 1000
0 <= rooms[i].length <= 1000
1 <= sum(rooms[i].length) <= 3000
0 <= rooms[i][j] < n
All the values of rooms[i] are unique.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80476862
# IDEA : DFS
# time = O(V + E), V = len(rooms), E = total keys
# space = O(V)
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

# V1''
# https://www.jiuzhang.com/solution/keys-and-rooms/#tag-highlight-lang-python
# IDEA : BFS
# time = O(V + E), V = len(rooms), E = total keys
# space = O(V)
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
# time = O(V + E), V = len(rooms), E = total keys
# space = O(V)
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