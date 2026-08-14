"""

1202. Smallest String With Swaps
Medium

You are given a string s, and an array of pairs of indices in the string pairs
where pairs[i] = [a, b] indicates 2 indices (0-indexed) of the string.

You can swap the characters at any pair of indices in the given pairs any number
of times.

Return the lexicographically smallest string that s can be changed to after
using the swaps.


Example 1:

Input: s = "dcab", pairs = [[0,3],[1,2]]
Output: "bacd"
Explaination:
Swap s[0] and s[3], s = "bcad"
Swap s[1] and s[2], s = "bacd"

Example 2:

Input: s = "dcab", pairs = [[0,3],[1,2],[0,2]]
Output: "abcd"
Explaination:
Swap s[0] and s[3], s = "bcad"
Swap s[0] and s[2], s = "acbd"
Swap s[1] and s[2], s = "abcd"

Example 3:

Input: s = "cba", pairs = [[0,1],[1,2]]
Output: "abc"
Explaination:
Swap s[0] and s[1], s = "bca"
Swap s[1] and s[2], s = "bac"
Swap s[0] and s[1], s = "abc"


Constraints:

1 <= s.length <= 10^5
0 <= pairs.length <= 10^5
0 <= pairs[i][0], pairs[i][1] < s.length
s only contains lower case English letters.

"""

# V0
# IDEA: UNION FIND (connected components)
"""

 KEY INSIGHT !!!

 -> if indices i and j are connected (directly or transitively via pairs),
    the characters at those indices can be rearranged ARBITRARILY
    within that connected component (swaps generate the full permutation group).

 -> so: group indices by component,
        sort the characters of each component,
        write them back into the component's indices (also sorted ascending).
"""
# time = O(n log n + m), n = len(s), m = len(pairs)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def smallestStringWithSwaps(self, s, pairs):
        n = len(s)
        parent = list(range(n))

        def find(x):
            # path compression
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(x, y):
            rx, ry = find(x), find(y)
            if rx != ry:
                parent[rx] = ry

        for a, b in pairs:
            union(a, b)

        # group indices by root
        groups = defaultdict(list)
        for i in range(n):
            groups[find(i)].append(i)

        res = list(s)
        for idxs in groups.values():
            # idxs is already ascending (built by an ascending loop)
            chars = sorted(res[i] for i in idxs)
            for i, ch in zip(idxs, chars):
                res[i] = ch

        return "".join(res)


# V1
# IDEA: DFS on the swap graph
# collect each connected component with DFS, then sort its chars in place
# time = O(n log n + m)
# space = O(n + m)
from collections import defaultdict
class Solution(object):
    def smallestStringWithSwaps(self, s, pairs):
        n = len(s)

        g = defaultdict(list)
        for a, b in pairs:
            g[a].append(b)
            g[b].append(a)

        visited = [False] * n
        res = list(s)

        for start in range(n):
            if visited[start]:
                continue

            # iterative DFS (recursion would overflow for n = 10^5)
            comp = []
            stack = [start]
            visited[start] = True
            while stack:
                cur = stack.pop()
                comp.append(cur)
                for nxt in g[cur]:
                    if not visited[nxt]:
                        visited[nxt] = True
                        stack.append(nxt)

            comp.sort()
            chars = sorted(res[i] for i in comp)
            for i, ch in zip(comp, chars):
                res[i] = ch

        return "".join(res)
