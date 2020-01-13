# V0 
# IDEA : DFS
class Solution:
    """
    @param N:  sum of the set
    @param dislikes: dislikes peoples
    @return:  if it is possible to split everyone into two groups in this way
    """
    def possibleBipartition(self, N, dislikes):
        # Write your code here.
        visited = [0 for i in range(N)];
        adj = [[] for i in range(N)];
        for dis in dislikes:
            adj[dis[0]-1].append(dis[1]-1);
            adj[dis[1]-1].append(dis[0]-1);
        
        for i in range(0,N):
            if visited[i] == 0:
                visited[i] = 1;
                if not self.dfs(i, visited, adj):
                    return False;
        return True;
    def dfs(self,cur,visited,adj):
        for j in adj[cur]:
            if visited[j] == 0:
                visited[j] = -visited[cur];
                if not self.dfs(j, visited, adj):
                    return False;
            elif visited[j] == visited[cur]:
                return False
        return True
        
# V1
# https://www.jiuzhang.com/solution/possible-bipartition/#tag-highlight-lang-python
# IDEA : DFS
class Solution:
    """
    @param N:  sum of the set
    @param dislikes: dislikes peoples
    @return:  if it is possible to split everyone into two groups in this way
    """
    def possibleBipartition(self, N, dislikes):
        # Write your code here.
        visited = [0 for i in range(N)];
        adj = [[] for i in range(N)];
        for dis in dislikes:
            adj[dis[0]-1].append(dis[1]-1);
            adj[dis[1]-1].append(dis[0]-1);
        
        for i in range(0,N):
            if visited[i] == 0:
                visited[i] = 1;
                if not self.DFS(i, visited, adj):
                    return False;
        return True;
    def DFS(self,cur,visited,adj):
        for j in adj[cur]:
            if visited[j] == 0:
                visited[j] = -visited[cur];
                if not self.DFS(j, visited, adj):
                    return False;
            elif visited[j] == visited[cur]:
                return False;
        return True

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82827177
# https://www.youtube.com/watch?v=VlZiMD7Iby4
# IDEA : BFS 
import collections
class Solution(object):
    def possibleBipartition(self, N, dislikes):
        """
        :type N: int
        :type dislikes: List[List[int]]
        :rtype: bool
        """
        graph = collections.defaultdict(list)
        for dislike in dislikes:
            graph[dislike[0] - 1].append(dislike[1] - 1)
            graph[dislike[1] - 1].append(dislike[0] - 1)
        color = [0] * N
        for i in range(N):
            if color[i] != 0: continue
            bfs = collections.deque()
            bfs.append(i)
            color[i] = 1
            while bfs:
                cur = bfs.popleft()
                for e in graph[cur]:
                    if color[e] != 0:
                        if color[cur] == color[e]:
                            return False
                    else:
                        color[e] = -color[cur]
                        bfs.append(e)
        return True

# V2 
# Time:  O(|V| + |E|)
# Space: O(|V| + |E|)
import collections
class Solution(object):
    def possibleBipartition(self, N, dislikes):
        """
        :type N: int
        :type dislikes: List[List[int]]
        :rtype: bool
        """
        adj = [[] for _ in range(N)]
        for u, v in dislikes:
            adj[u-1].append(v-1)
            adj[v-1].append(u-1)

        color = [0]*N
        color[0] = 1
        q = collections.deque([0])
        while q:
            cur = q.popleft()
            for nei in adj[cur]:
                if color[nei] == color[cur]:
                    return False
                elif color[nei] == -color[cur]:
                    continue
                color[nei] = -color[cur]
                q.append(nei)
        return True
