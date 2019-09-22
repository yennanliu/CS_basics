# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82533658
class Solution:
    def loudAndRich(self, richer, quiet):
        """
        :type richer: List[List[int]]
        :type quiet: List[int]
        :rtype: List[int]
        """
        m = collections.defaultdict(list)
        for i, j in richer:
            m[j].append(i)

        res = [-1] * len(quiet)

        def dfs(i):
            if res[i] > 0: return res[i]
            res[i] = i
            for j in m[i]:
                if quiet[res[i]] > quiet[dfs(j)]:
                    res[i] = res[j]
            return res[i]

        for i in range(len(quiet)):
            dfs(i)
        return res

# V1'
# https://www.jiuzhang.com/solution/loud-and-rich/#tag-highlight-lang-python
class Solution:
    """
    @param richer: the richer array
    @param quiet: the quiet array
    @return: the answer
    """
    def loudAndRich(self, richer, quiet):
        # Write your code here
        from collections import defaultdict
        graph = defaultdict(list)
        for x, y in richer:
            graph[y].append(x)
        n = len(quiet)
        res = [-1]*n
        
        def dfs(person):
            if res[person] >= 0:
                return res[person]
            res[person] = person
            for i in graph[person]:
                if quiet[res[person]] > quiet[dfs(i)]:
                    res[person] = res[i]
            return res[person]
        
        for person in range(n):
            dfs(person)
        return res

# V2 
# Time:  O(q + r)
# Space: O(q + r)
class Solution(object):
    def loudAndRich(self, richer, quiet):
        """
        :type richer: List[List[int]]
        :type quiet: List[int]
        :rtype: List[int]
        """
        def dfs(graph, quiet, node, result):
            if result[node] is None:
                result[node] = node
                for nei in graph[node]:
                    smallest_person = dfs(graph, quiet, nei, result)
                    if quiet[smallest_person] < quiet[result[node]]:
                        result[node] = smallest_person
            return result[node]

        graph = [[] for _ in range(len(quiet))]
        for u, v in richer:
            graph[v].append(u)
        result = [None]*len(quiet)
        return map(lambda x: dfs(graph, quiet, x, result), range(len(quiet)))