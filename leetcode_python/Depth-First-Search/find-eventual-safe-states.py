# V0 

# V1
# http://bookshadow.com/weblog/2018/03/18/leetcode-find-eventual-safe-states/
import collections
class Solution(object):
    def eventualSafeNodes(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[int]
        """
        srcs = collections.defaultdict(set)
        tgts = collections.defaultdict(set)
        for idx in range(len(graph)):
            for v in graph[idx]:
                tgts[idx].add(v)
                srcs[v].add(idx)

        degZeros = [k for k in range(len(graph)) if not tgts[k]]
        while degZeros:
            ndegZeros = []
            for t in degZeros:
                for s in srcs[t]:
                    tgts[s].remove(t)
                    if not tgts[s]: ndegZeros.append(s)
            degZeros = ndegZeros
        return [k for k in range(len(graph)) if not tgts[k]] 

# V1'
# https://www.jiuzhang.com/solution/find-eventual-safe-states/#tag-highlight-lang-python
class Solution:
    """
    @param graph: a 2D integers array
    @return: return a list of integers
    """
    def eventualSafeNodes(self, graph):
        def dfs(graph, i, visited):
            for j in graph[i]:
                if j in visited:
                    return False
                if j in ans:
                    continue
                visited.add(j)
                if not dfs(graph, j, visited):
                    return False
                visited.remove(j)
            ans.add(i)
            return True
        ans = set()
        for i in range(len(graph)):
            visited = set([i])
            dfs(graph, i, visited)
        return sorted(list(ans))

# V2 
# Time:  O(|V| + |E|)
# Space: O(|V|)
import collections
class Solution(object):
    def eventualSafeNodes(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[int]
        """
        WHITE, GRAY, BLACK = 0, 1, 2

        def dfs(graph, node, lookup):
            if lookup[node] != WHITE:
                return lookup[node] == BLACK
            lookup[node] = GRAY
            for child in graph[node]:
                if lookup[child] == BLACK:
                    continue
                if lookup[child] == GRAY or \
                   not dfs(graph, child, lookup):
                    return False
            lookup[node] = BLACK
            return True

        lookup = collections.defaultdict(int)
        return filter(lambda node: dfs(graph, node, lookup), range(len(graph)))
