"""

851. Loud and Rich
Medium

There is a group of n people labeled from 0 to n - 1 where each person has a different amount of money and a different level of quietness.

You are given an array richer where richer[i] = [ai, bi] indicates that ai has more money than bi and an integer array quiet where quiet[i] is the quietness of the ith person. All the given data in richer are logically correct (i.e., the data will not lead you to a situation where x is richer than y and y is richer than x at the same time).

Return an integer array answer where answer[x] = y if y is the least quiet person (that is, the person y with the smallest value of quiet[y]) among all people who definitely have equal to or more money than the person x.

 

Example 1:

Input: richer = [[1,0],[2,1],[3,1],[3,7],[4,3],[5,3],[6,3]], quiet = [3,2,5,4,6,1,7,0]
Output: [5,5,2,5,4,5,6,7]
Explanation: 
answer[0] = 5.
Person 5 has more money than 3, which has more money than 1, which has more money than 0.
The only person who is quieter (has lower quiet[x]) is person 7, but it is not clear if they have more money than person 0.
answer[7] = 7.
Among all people that definitely have equal to or more money than person 7 (which could be persons 3, 4, 5, 6, or 7), the person who is the quietest (has lower quiet[x]) is person 7.
The other answers can be filled out with similar reasoning.
Example 2:

Input: richer = [], quiet = [0]
Output: [0]
 

Constraints:

n == quiet.length
1 <= n <= 500
0 <= quiet[i] < n
All the values of quiet are unique.
0 <= richer.length <= n * (n - 1) / 2
0 <= ai, bi < n
ai != bi
All the pairs of richer are unique.
The observations in richer are all logically consistent

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82533658
class Solution:
    def loudAndRich(self, richer, quiet):
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

# V1''
# IDEA : Cached Depth-First Search
# https://leetcode.com/problems/loud-and-rich/solution/
class Solution(object):
    def loudAndRich(self, richer, quiet):
        N = len(quiet)
        graph = [[] for _ in xrange(N)]
        for u, v in richer:
            graph[v].append(u)

        answer = [None] * N
        def dfs(node):
            #Want least quiet person in this subtree
            if answer[node] is None:
                answer[node] = node
                for child in graph[node]:
                    cand = dfs(child)
                    if quiet[cand] < quiet[answer[node]]:
                        answer[node] = cand
            return answer[node]

        return map(dfs, range(N))

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