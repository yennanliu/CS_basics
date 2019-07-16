# V0 

# V1 
# http://bookshadow.com/weblog/2018/02/18/leetcode-is-graph-bipartite/
class Solution(object):
    def isBipartite(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: bool
        """
        edges = collections.defaultdict(set)
        for idx, points in enumerate(graph):
            for p in points: edges[idx].add(p)
        colors = {}
        def dfs(v, color):
            ocolor = 1 - color
            for p in edges[v]:
                if p not in colors:
                    colors[p] = ocolor
                    if not dfs(p, ocolor):
                        return False
                elif colors[p] != ocolor:
                    return False
            return True
        for k in edges:
            if k in colors: continue
            colors[k] = 0
            if not dfs(k, 0): return False
        return True

# V2 
# Time:  O(|V| + |E|)
# Space: O(|V|)
class Solution(object):
    def isBipartite(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: bool
        """
        color = {}
        for node in range(len(graph)):
            if node in color:
                continue
            stack = [node]
            color[node] = 0
            while stack:
                curr = stack.pop()
                for neighbor in graph[curr]:
                    if neighbor not in color:
                        stack.append(neighbor)
                        color[neighbor] = color[curr] ^ 1
                    elif color[neighbor] == color[curr]:
                        return False
        return True