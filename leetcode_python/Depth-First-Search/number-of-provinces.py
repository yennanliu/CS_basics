"""

547. Number of Provinces
Medium

There are n cities. Some of them are connected, while some are not. If city a is connected directly with city b, and city b is connected directly with city c, then city a is connected indirectly with city c.

A province is a group of directly or indirectly connected cities and no other cities outside of the group.

You are given an n x n matrix isConnected where isConnected[i][j] = 1 if the ith city and the jth city are directly connected, and isConnected[i][j] = 0 otherwise.

Return the total number of provinces.

 

Example 1:


Input: isConnected = [[1,1,0],[1,1,0],[0,0,1]]
Output: 2
Example 2:


Input: isConnected = [[1,0,0],[0,1,0],[0,0,1]]
Output: 3
 

Constraints:

1 <= n <= 200
n == isConnected.length
n == isConnected[i].length
isConnected[i][j] is 1 or 0.
isConnected[i][i] == 1
isConnected[i][j] == isConnected[j][i]

"""

# V0
# BFS : TODO : fix this
# class Solution(object):
#     def findCircleNum(self, isConnected):
#         # edge
#         if not isConnected:
#             return 0
#         d = defaultdict(set)
#         l = len(isConnected)
#         w = len(isConnected[0])
#         for i in range(l):
#             for j in range(w):
#                 if isConnected[i][j] == 1:
#                     d[i+1].add(j+1)
#                     d[j+1].add(i+1)
#         print (">>> d = " + str(d))
#         cnt = 0
#         q = [x+1 for x in range(l)]
#         visited = set()
#         while q:
#             for i in range(len(q)):
#                 tmp = q.pop(0)
#                 visited.add(tmp)
#                 if tmp not in visited:
#                     for x in d[tmp]:
#                         q.append(x)
#                         visited.add(x)
#
#             cnt += 1
#         return cnt

# V1
# IDEA : DFS ITERATIVE
# https://leetcode.com/problems/number-of-provinces/discuss/204048/Python-solution
class Solution(object):
    def findCircleNum(self, M):
        res = 0
        n = len(M)
        seen = [0]*n
        for i in range(n):
            if seen[i] == 0:
                res += 1
                seen[i] = 1
                stack = [i]
                while stack:
                    u = stack.pop()
                    for j in range(n):
                        if M[u][j] == 1 and seen[j] == 0:
                            seen[j] = 1
                            stack.append(j)
        return res

# V1' : DFS RECURSIVE
# https://leetcode.com/problems/number-of-provinces/discuss/204048/Python-solution
class Solution(object):
    def findCircleNum(self, M):
        def dfs(i):
            seen[i] = 1
            for j in range(n):
                if M[i][j] == 1 and seen[j] == 0:
                    dfs(j)
            
        n = len(M)
        res = 0
        seen = [0]*n
        for i in range(n):
            if seen[i] == 0:
                dfs(i)
                res += 1
        return res

# V1''
# IDEA : BFS
# https://leetcode.com/problems/number-of-provinces/discuss/741509/Python
class Solution(object):
    def findCircleNum(self, M):
        friendships = {i : set() for i in range(len(M[0]))}
        
        for i in range(len(M)):
            for j in range(len(M)):
                if M[i][j] == 1:
                    friendships[i].add(j)
                    friendships[j].add(i)
        
        answer = 0
        seen = set()
    
        for key in friendships:
            if key in seen:
                continue
                
            stack = [key]
            flag = False
            
            while stack:
                key = stack.pop()
                
                for val in friendships[key]:
                    if val in seen:
                        continue
                        
                    flag = True    
                    seen.add(val)
                    stack.append(val)
            
            if flag:
                answer += 1
            
        return answer

# V1''''
# https://leetcode.com/problems/number-of-provinces/discuss/269818/Union-Find-Python-solution
# IDEA : UNION FIND + ROUTE COMPRESSION
class UnionFind(object):
    def __init__(self, n):
        self.n = n
        self.parent = [-1]*n
        for i in range(n):
            self.parent[i] = i
    
    def find(self, i):
	#Path Compression
        if self.parent[i] != i:
            self.parent[i] = self.find(self.parent[i])
        return self.parent[i]
    
    def union(self, x, y):
        xroot = self.find(x)
        yroot = self.find(y)
        if xroot != yroot:
            self.parent[yroot]= xroot
    
    def get_count(self):
        total = set()
        print(self.parent)
        for i in range(self.n):
            total.add(self.find(i))
        print("total", total)
        return len(total)


class Solution:
    def findCircleNum(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        #Union-Find Solution
        n = len(M[0])
        uf = UnionFind(n)
        
        for r in range(len(M)):
            for c in range(len(M[0])):
                if M[r][c] == 1:
                    uf.union(r,c)
        
        return uf.get_count()

# V1'''''
# IDEA : BFS
# https://leetcode.com/problems/number-of-provinces/discuss/1364979/Python-or-BFS
class Solution(object):
    def findCircleNum(self, isConnected):
            dic = collections.defaultdict(list)

            for idx, clist in enumerate(isConnected):
                for i,c in enumerate(clist):
                    if c ==1:
                        dic[idx+1].append(i+1)
            res = 0
            seen = set()
            queue = deque()
            for d in dic:
                if d not in seen:
                    queue.append(d)
                    seen.add(d)
                    while queue:
                        val = queue.popleft()
                        for x in dic[val]:
                            if x not in seen:
                                queue.append(x)
                                seen.add(x)
                    res+=1

            return res

# V1''''''
# IDEA : DFS
# https://leetcode.com/problems/number-of-provinces/discuss/101349/Python-Simple-Explanation
class Solution(object):
    def findCircleNum(self, A):
        N = len(A)
        seen = set()
        def dfs(node):
            for nei, adj in enumerate(A[node]):
                if adj and nei not in seen:
                    seen.add(nei)
                    dfs(nei)

        ans = 0
        for i in range(N):
            if i not in seen:
                dfs(i)
                ans += 1
        return ans

# V1''''
# IDEA : DFS
# JAVA
# public class Solution {
#     public void dfs(int[][] M, int[] visited, int i) {
#         for (int j = 0; j < M.length; j++) {
#             if (M[i][j] == 1 && visited[j] == 0) {
#                 visited[j] = 1;
#                 dfs(M, visited, j);
#             }
#         }
#     }
#     public int findCircleNum(int[][] M) {
#         int[] visited = new int[M.length];
#         int count = 0;
#         for (int i = 0; i < M.length; i++) {
#             if (visited[i] == 0) {
#                 dfs(M, visited, i);
#                 count++;
#             }
#         }
#         return count;
#     }
# }


# V1''''
# IDEA : BFS
# JAVA
# public class Solution {
#     public int findCircleNum(int[][] M) {
#         int[] visited = new int[M.length];
#         int count = 0;
#         Queue < Integer > queue = new LinkedList < > ();
#         for (int i = 0; i < M.length; i++) {
#             if (visited[i] == 0) {
#                 queue.add(i);
#                 while (!queue.isEmpty()) {
#                     int s = queue.remove();
#                     visited[s] = 1;
#                     for (int j = 0; j < M.length; j++) {
#                         if (M[s][j] == 1 && visited[j] == 0)
#                             queue.add(j);
#                     }
#                 }
#                 count++;
#             }
#         }
#         return count;
#     }
# }

# V1''''
# IDEA : UNION FIND
# JAVA
# public class Solution {
#     int find(int parent[], int i) {
#         if (parent[i] == -1)
#             return i;
#         return find(parent, parent[i]);
#     }
#
#     void union(int parent[], int x, int y) {
#         int xset = find(parent, x);
#         int yset = find(parent, y);
#         if (xset != yset)
#             parent[xset] = yset;
#     }
#     public int findCircleNum(int[][] M) {
#         int[] parent = new int[M.length];
#         Arrays.fill(parent, -1);
#         for (int i = 0; i < M.length; i++) {
#             for (int j = 0; j < M.length; j++) {
#                 if (M[i][j] == 1 && i != j) {
#                     union(parent, i, j);
#                 }
#             }
#         }
#         int count = 0;
#         for (int i = 0; i < parent.length; i++) {
#             if (parent[i] == -1)
#                 count++;
#         }
#         return count;
#     }
# }

# V2